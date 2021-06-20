<%@ page import="java.util.List" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.regex.Pattern" %>
<%@ page import="java.util.regex.Matcher" %>
<%@ page import="java.time.Instant" %>
<%@ page import="ilya.messenger.entity.domains.User" %>
<%@ page import="java.util.UUID" %>
<%@ page import="ilya.messenger.entity.domains.Sender" %>
<%@ page import="ilya.messenger.entity.domains.Chat" %>
<%@ page import="ilya.messenger.entity.repository.instances.UserInstance" %>
<%@ page import="ilya.messenger.entity.repository.instances.MessageInstance" %>

<%
User activeUser = (User) request.getAttribute("user");
String profileOwner = (String) request.getAttribute("profileOwner");
List<Sender> messagesByUser = (List<Sender>) request.getAttribute("messagesByUser");
List<User> users = (List<User>) request.getAttribute("users");
%>

<!DOCTYPE html>
<html>
<title>Профиль</title>
<%@ include file = "/upperFragment.jsp" %>
<body>
  <style>
    #chat {
      background-color: white;
      height: 200px;
      overflow-y: scroll;
      width: 45%;
    }
    .texts {
      font-size:20px;
    }
    textarea {
      -webkit-box-sizing: border-box;
      -moz-box-sizing: border-box;
      box-sizing: border-box;
      width: 100%;
    }
  </style>

  <script>
    function scrollChat() {
      var chatDiv = document.getElementById('chat');
      chatDiv.scrollTop = chatDiv.scrollHeight;
    };
  </script>

  <%@ include file = "/menuFragment.jsp" %>

  <div class="container">
    <% if (request.getAttribute("error") != null) { %>
      <h2 style="color:red"><%= request.getAttribute("error") %></h2>
    <% } %>

    <% if (request.getSession().getAttribute("user") != null) { %>

      <h1><%=profileOwner%> - Профиль пользователя</h1>
      <hr/>
      <strong><%=profileOwner%></strong><br>
      <p><%=activeUser.getBio()%></p>

      <% if (request.getSession().getAttribute("user").equals(profileOwner)) { %>
        <form action="/users/<%=request.getSession().getAttribute("user") %>" method="POST">
          <div class="form-group">
            <label class="form-control-label">Изменить биографию</label>
            <textarea pattern="[\S]+(\s[\S]+)*" required title="Без пробелов"
            class="form-control"rows="5" cols="75" name="About Me" required></textarea>
          </div>
          <button type="submit" class="btn">Указать</button>
        </form>
        <hr/>
      <% } %>
     <% } %>
<hr/>
</div>
</body>
</html>