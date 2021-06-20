package ilya.messenger.servlets;

import ilya.messenger.entity.domains.Sender;
import ilya.messenger.entity.domains.User;
import ilya.messenger.entity.repository.instances.ChatInstance;
import ilya.messenger.entity.repository.instances.MessageInstance;
import ilya.messenger.entity.repository.instances.UserInstance;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminServlet extends HttpServlet {

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

    String username = (String) request.getSession().getAttribute("user");
    if (username == null) {
      response.sendRedirect("/login");
      return;
    }

    User user = userInstance.getUser(username);
    if (user == null) {
      response.sendRedirect("/login");
      return;
    }

    if (!user.isAdmin()) {
      request.setAttribute("error", "Sorry, you are not an admin.");
      request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
      return;
    }

    request.setAttribute("totalUsers", userInstance.getUsers().size());
    request.setAttribute("totalChats", chatInstance.getAllChats().size());
    request.setAttribute("totalMessages", messageInstance.getTotalMessages());
    request.setAttribute("mostActive", getMostActiveUser());
    request.setAttribute("newestUser", getNewestUser());
    request.setAttribute("wordiestUser", getWordiestUser());
    request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    response.sendRedirect("/admin");
  }

  public String getMostActiveUser() {
    if (userInstance.getUsers().isEmpty()) {
      return "No Users.";
    }
    User currentMostActiveUser = userInstance.getUsers().get(0);
    int currentMostMessages = 0;
    for (User user : userInstance.getUsers()) {
      int messageCount = messageInstance.getMessagesByUser(user.getId()).size();
      if (messageCount > currentMostMessages) {
        currentMostMessages = messageCount;
        currentMostActiveUser = user;
      }
    }
    return currentMostActiveUser.getName();
  }

  public String getNewestUser() {
    if (userInstance.getUsers().isEmpty()) {
      return "No Users.";
    }
    return userInstance.getUsers().get(userInstance.getUsers().size() - 1).getName();
  }

  public String getWordiestUser() {
    if (userInstance.getUsers().isEmpty()) {
      return "No Users.";
    }
    User currentWordiestUser = userInstance.getUsers().get(0);
    int currentWordiest = 0;
    for (User user : userInstance.getUsers()) {
      int charCount = 0;
      List<Sender> senderList = messageInstance.getMessagesByUser(user.getId());
      for (Sender sender : senderList) {
        String characters = sender.getContent().replaceAll("\\s+", "");
        charCount += characters.length();
      }
      if (charCount > currentWordiest) {
        currentWordiestUser = user;
        currentWordiest = charCount;
      }
    }
    return currentWordiestUser.getName();
  }
}