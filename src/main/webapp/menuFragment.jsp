<%@ page import="ilya.messenger.entity.repository.instances.UserInstance" %>
<%@ page import="ilya.messenger.entity.domains.User" %>
<% String userSigned = (String) request.getSession().getAttribute("user"); %>

<nav class="navbar navbar-inverse-custom">
  <div class="container-fluid-custom">
    <a href="/">Мессенджер</a>

    <nav>
        <a href="/chats">Чаты</a>
        <% if (request.getSession().getAttribute("user") != null) { %>
          <a href="/users/<%= request.getSession().getAttribute("user")%>">Профиль</a>
          <a href="/logout.jsp">Выход</a>
        <% } else { %>
          <a href="/login">Авторизация</a>
        <% } %>
        <% if (userSigned != null && UserInstance.getInstance().getUser(userSigned).isAdmin()) { %>
          <a href="/admin">Администратор</a>
        <% } %>
    </nav>
</nav>
