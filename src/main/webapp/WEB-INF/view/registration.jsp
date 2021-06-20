<!DOCTYPE html>
<html>
<title>Регистрация</title>
<%@ include file = "/upperFragment.jsp" %>
<body>
  <script>
   $(document).ready(function(){
      $("#info").on({
        click: function(){
          var alertM = $($(document).find('small'));
          if(alertM.css("display") == "block"){
            alertM.css("display", "none");
            return
          }
          alertM.css("display", "block");
          }
      });
    });
  </script>
  
  <%@ include file = "/menuFragment.jsp" %>

  <div class="containerc">
    <h1 class="">Создать аккаунт</h1>
    <br>
    
    <% if (request.getAttribute("error") != null) { %>
      <h2 style="color:red"><%= request.getAttribute("error") %></h2>
    <% } %>

    <form class="form-group-custom" action="/registration" method="POST">
      <label class="form-control-label" for="username">Логин</label>
      <input pattern="\S{3,}" required title="Введите логин"
      class="form-control" type="text" name="username" id="username" placeholder="Логин"required>
      <br>
      <label class="form-control-label" for="email">Email</label>
      <div class="input-group mb-2 mr-sm-2 mb-sm-0">
        <input class="form-control" type="email" name="email" placeholder="test@gmail.com">
      </div>
      <br>
      <label class="form-control-label" for="password">Пароль</label>
      <input pattern="\S{3,}" required title="Введите пароль"
      class="form-control" type="password" name="password" id="password" placeholder="Пароль" required>
      <br>
      <button type="submit" class="btn">Создать аккаунт</button>
    </form>
  </div>
</body>
</html>
