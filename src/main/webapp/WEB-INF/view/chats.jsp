<%@ page import="java.util.List" %>
<%@ page import="ilya.messenger.entity.domains.Chat" %>
<%
List<Chat> chats = (List<Chat>) request.getAttribute("chats");
%>
<!DOCTYPE html>
<html>
<title>Чаты</title>
<%@ include file = "/upperFragment.jsp" %>
<body>
  <%@ include file = "/menuFragment.jsp" %>

  <div class="container">

    <% if (request.getAttribute("error") != null) { %>
      <h2 style="color:red"><%= request.getAttribute("error") %></h2>
    <% } %>

    <% if (request.getSession().getAttribute("user") != null) { %>
      <h1>Новый чат</h1>
      <form class="form-group" action="/chats" method="POST">
        <label class="form-control-label">Заголовок:</label>
        <input pattern="\S{3,}" required title="Введите название на латинице"
        class="form-control"type="text" name="chatTitle" required>
        <div class="form-check">
          <label class="form-check-label" for="checkF">
          Приватный
          </label>
          <input class="form-check-input" type="checkbox" value="true" id="checkF" name="isPrivate">
        </div>
        <button type="submit" class="btn">Добавить</button>
      </form>

      <hr/>
    <% } %>

    <h1>Публичные чаты</h1>

    <% if (chats == null || chats.isEmpty()) { %>
      <p>Нет созданных чатов.</p>
    <% } else { %>
      <ul class="mdl-list">
        <% for (Chat chat : chats) { %>
            <% if (!chat.isPrivate()) { %>
              <li><a href="/chat/<%= chat.getTitle() %>">
                  <%= chat.getTitle() %></a></li>
            <% } else if(request.getSession().getAttribute("user") != null &&
                        chat.check(request.getSession().getAttribute("user").toString())) { %>
                   <li><a href="/chat/<%= chat.getTitle() %>">&#x1F512;<%= chat.getTitle() %></a></li>
            <% } %>
        <% } %>
      </ul>
    <% } %>
    <hr/>

  </div>
</body>
</html>
