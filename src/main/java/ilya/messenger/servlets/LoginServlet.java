package ilya.messenger.servlets;

import ilya.messenger.entity.domains.User;
import ilya.messenger.entity.repository.instances.UserInstance;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;

public class LoginServlet extends HttpServlet {

  private UserInstance userInstance;

  @Override
  public void init() throws ServletException {
    super.init();
    setUserInstance(UserInstance.getInstance());
  }

  void setUserInstance(UserInstance userInstance) {
    this.userInstance = userInstance;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String username = request.getParameter("username");
    username = username.toLowerCase();
    String password = request.getParameter("password");

    if (!userInstance.isUserRegistered(username)) {
      request.setAttribute("error", "Логин не найден");
      request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
      return;
    }

    User user = userInstance.getUser(username);

    if(user.getPassword().substring(10).equals(password)) {
      request.setAttribute("isReset", "true");
      request.setAttribute("username", username);
      request.getRequestDispatcher("/WEB-INF/view/reset.jsp").forward(request, response);
      return;
    }

    if (!BCrypt.checkpw(password, user.getPassword())) {
      request.setAttribute("error", "Некорректный пароль");
      request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
      return;
    }

    request.getSession().setAttribute("user", username);
    if (user.isAdmin()) {
      response.sendRedirect("/admin");
      return;
    }
    response.sendRedirect("/chats");
  }
}
