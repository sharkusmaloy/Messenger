package ilya.messenger.servlets;

import ilya.messenger.entity.domains.Chat;
import ilya.messenger.entity.domains.User;
import ilya.messenger.entity.repository.instances.ChatInstance;
import ilya.messenger.entity.repository.instances.UserInstance;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ChatsServlet extends HttpServlet {

  private UserInstance userInstance;

  private ChatInstance chatInstance;

  @Override
  public void init() throws ServletException {
    super.init();
    setUserInstance(UserInstance.getInstance());
    setChatInstance(ChatInstance.getInstance());
  }

  void setUserInstance(UserInstance userInstance) {
    this.userInstance = userInstance;
  }

  public ChatInstance getChatInstance() {
    return chatInstance;
  }

  public void setChatInstance(ChatInstance chatInstance) {
    this.chatInstance = chatInstance;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    List<Chat> chats = chatInstance.getAllChats();
    request.setAttribute("chats", chats);
    request.getRequestDispatcher("/WEB-INF/view/chats.jsp").forward(request, response);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    String username = (String) request.getSession().getAttribute("user");

    if (username == null) {
      response.sendRedirect("/chats");
      return;
    }
    username = username.toLowerCase();

    User user = userInstance.getUser(username);
    if (user == null) {
      response.sendRedirect("/chats");
      return;
    }

    String chatTitle = request.getParameter("chatTitle");
    chatTitle = chatTitle.toLowerCase();

    if (!chatTitle.matches("[\\w*]*")) {
      request.setAttribute("error", "Ошибка при вводе названия чата");
      request.getRequestDispatcher("/WEB-INF/view/chats.jsp").forward(request, response);
      return;
    }

    if (chatInstance.isTitleTaken(chatTitle)) {
      response.sendRedirect("/chat/" + chatTitle);
      return;
    }

    boolean isPrivate = Boolean.valueOf(request.getParameter("isPrivate"));
    Chat chat =
        new Chat(UUID.randomUUID(), user.getId(), chatTitle, Instant.now(), (isPrivate?true:false));
    if(isPrivate) {
      chat.addUser(user.getId());
    }
    chatInstance.addChats(chat);
    response.sendRedirect("/chat/" + chatTitle);
  }
}
