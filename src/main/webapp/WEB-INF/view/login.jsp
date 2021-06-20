<!DOCTYPE html>
<html>
<title>Авторизация</title>
<%@ include file = "/upperFragment.jsp" %>
<body>
  <%@ include file = "/menuFragment.jsp" %>

  <div class="containerc">
    <h1>Авторизация</h1>

    <% if (request.getAttribute("error") != null) { %>
      <h2 style="color:red"><%= request.getAttribute("error") %></h2>
    <% } %>
    <% if (request.getAttribute("sent") != null) { %>
      <h2 style="color:green"><%= request.getAttribute("sent") %></h2>
    <% } %>

    <form class="form-group-custom" action="/login" method="POST">
      <label class="form-control-label" for="username">Логин</label>
      <input class="form-control-custom" type="text" name="username" id="username" placeholder="Логин" required>
      </br>
      <label class="form-control-label" for="password">Пароль</label>
      <input class="form-control-custom" type="password" name="password" id="password" placeholder="Пароль" required>
      </br>
      <button type="submit" class="btn">Вход</button>
    </form>
    <p>Если у вас нет аккаунта - <a href="/registration">Регистрация</a></p>
  </div>
</body>
</html>
