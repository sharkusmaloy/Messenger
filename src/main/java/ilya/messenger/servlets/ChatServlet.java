package ilya.messenger.servlets;

import ilya.messenger.entity.domains.Chat;
import ilya.messenger.entity.domains.Sender;
import ilya.messenger.entity.domains.User;
import ilya.messenger.entity.repository.instances.ChatInstance;
import ilya.messenger.entity.repository.instances.MessageInstance;
import ilya.messenger.entity.repository.instances.UserInstance;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class ChatServlet extends HttpServlet {

  private ChatInstance chatInstance;

  private MessageInstance messageInstance;

  private UserInstance userInstance;

  @Override
  public void init() throws ServletException {
    super.init();
    setChatStore(ChatInstance.getInstance());
    setMessageInstance(MessageInstance.getInstance());
    setUserInstance(UserInstance.getInstance());
  }

  void setChatStore(ChatInstance chatInstance) {
    this.chatInstance = chatInstance;
  }

  void setMessageInstance(MessageInstance messageInstance) {
    this.messageInstance = messageInstance;
  }

  void setUserInstance(UserInstance userInstance) {
    this.userInstance = userInstance;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String requestUrl = request.getRequestURI();
    String chatTitle = requestUrl.substring("/chat/".length());
    chatTitle = chatTitle.toLowerCase();

    Chat chat = chatInstance.getChatsWithTitle(chatTitle);
    if (chat == null) {
      response.sendRedirect("/chats");
      return;
    }
    if (chat.isPrivate() && request.getSession().getAttribute("user") == null) {
      response.sendRedirect("/chats");
      return;
    }

    UUID chatId = chat.getId();

    List<Sender> senders = messageInstance.getMessagesInChats(chatId);
    List<User> users = userInstance.getUsers();

    if(chat.isPrivate()){
      List<User> excludedUsers = new ArrayList<>();
      String existingUsers = chat.getUsers();
      for(User u: users){
        if(!existingUsers.contains(u.getId().toString())){
          excludedUsers.add(u);
        }
      }
      request.setAttribute("excludedUsers", excludedUsers);
    }

    request.setAttribute("users", users);
    request.setAttribute("chat", chat);
    request.setAttribute("senders", senders);
    request.getRequestDispatcher("/WEB-INF/view/chat.jsp").forward(request, response);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    String username = (String) request.getSession().getAttribute("user");
    
    if (username == null) {
      response.sendRedirect("/login");
      return;
    }
    username = username.toLowerCase();

    User user = userInstance.getUser(username);
    if (user == null) {
      response.sendRedirect("/login");
      return;
    }

    String requestUrl = request.getRequestURI();
    String chatTitle = requestUrl.substring("/chat/".length());

    Chat chat = chatInstance.getChatsWithTitle(chatTitle);
    if (chat == null) {
      response.sendRedirect("/chats");
      return;
    }

    String messageContent = request.getParameter("messageInput");
    String deletedMessageId = request.getParameter("deletedMessageId");
    String userNameToAdd = request.getParameter("nameToBeAdded");

    if (userNameToAdd != null) {
      userNameToAdd = Jsoup.clean(userNameToAdd, Whitelist.none());
      chat.addUser(userInstance.getUser(userNameToAdd).getId());
      chatInstance.updateChats(chat);
    }

    if (messageContent != null) {
      messageContent = Jsoup.clean(messageContent, Whitelist.none());
      boolean isPrivate = (chat.isPrivate() ? true : false);
      Sender sender =
          new Sender(
              UUID.randomUUID(),
              chat.getId(),
              user.getId(),
              isPrivate,
              messageContent,
              Instant.now());
      messageInstance.addMessage(sender);
    }

    if (deletedMessageId != null) {
      deletedMessageId = Jsoup.clean(deletedMessageId, Whitelist.none());
      Sender senderToDelete = messageInstance.getMessageById(UUID.fromString(deletedMessageId));
      messageInstance.deleteMessage(senderToDelete);
    }
    response.sendRedirect("/chat/" + chatTitle);
  }
}
