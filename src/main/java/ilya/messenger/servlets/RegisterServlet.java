package ilya.messenger.servlets;

import ilya.messenger.entity.domains.User;
import ilya.messenger.entity.repository.instances.UserInstance;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegisterServlet extends HttpServlet {

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
    request.getRequestDispatcher("/WEB-INF/view/registration.jsp").forward(request, response);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    String username = request.getParameter("username");
    username = username.toLowerCase();
    String email = request.getParameter("email");

    if (!username.matches("[\\w*\\s*]*")) {
      request.setAttribute("error", "Введите имя на латинице (Допускаются цифры)");
      request.getRequestDispatcher("/WEB-INF/view/registration.jsp").forward(request, response);
      return;
    }

    if (userInstance.isUserRegistered(username)) {
      request.setAttribute("error", "Это имя уже занято");
      request.getRequestDispatcher("/WEB-INF/view/registration.jsp").forward(request, response);
      return;
    }

    String password = request.getParameter("password");

    userInstance.addUser(username, password, false);

    if (email != null) {
      User user = userInstance.getUser(username);
      user.setEmail(email);
      userInstance.updateUser(user);
    }
    response.sendRedirect("/login");
  }
}
