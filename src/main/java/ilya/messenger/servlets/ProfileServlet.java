package ilya.messenger.servlets;

import ilya.messenger.entity.domains.Sender;
import ilya.messenger.entity.domains.User;
import ilya.messenger.entity.repository.instances.MessageInstance;
import ilya.messenger.entity.repository.instances.UserInstance;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class ProfileServlet extends HttpServlet {

  private UserInstance userInstance;

  private MessageInstance messageInstance;

  @Override
  public void init() throws ServletException {
    super.init();
    setUserInstance(UserInstance.getInstance());
    setMessageInstance(MessageInstance.getInstance());
  }

  void setUserInstance(UserInstance userInstance) {
    this.userInstance = userInstance;
  }

  void setMessageInstance(MessageInstance messageInstance) {
    this.messageInstance = messageInstance;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    String requestUrl = request.getRequestURI();
    String profileOwner = requestUrl.substring("/users/".length());
    profileOwner = profileOwner.toLowerCase();
    
    User user = userInstance.getUser(profileOwner);
    if (user == null) {
      response.sendRedirect("/login");
      return;
    }

    UUID userID = user.getId();
    if (userID == null) {
      response.sendRedirect("/login");
      return;
    }

    List<Sender> messagesByUser = messageInstance.getMessagesByUser(userID);
    List<User> users = userInstance.getUsers();

    request.setAttribute("users", users);
    request.setAttribute("messagesByUser", messagesByUser);
    request.setAttribute("profileOwner", profileOwner);
    request.setAttribute("user", user);
    request.getRequestDispatcher("/WEB-INF/view/profile.jsp").forward(request, response);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
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

    String aboutMeContent = request.getParameter("About Me");

    if (aboutMeContent != null) {
      String cleanedAboutMeContent = Jsoup.clean(aboutMeContent, Whitelist.none());
      user.setBio(cleanedAboutMeContent);
    }

    userInstance.updateUser(user);
    response.sendRedirect("/users/" + username);
  }
}
