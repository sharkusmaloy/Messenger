<%@ page import="java.util.List" %>
<%@ page import="ilya.messenger.entity.domains.Chat" %>
<%@ page import="ilya.messenger.entity.domains.Sender" %>
<%@ page import="ilya.messenger.entity.domains.Action" %>
<%@ page import="ilya.messenger.entity.repository.instances.UserInstance" %>
<%@ page import="ilya.messenger.entity.repository.instances.ChatInstance" %>
<%@ page import="ilya.messenger.entity.repository.instances.MessageInstance" %>
<%@ page import="ilya.messenger.entity.util.Util" %>
<%@ page import="java.util.UUID" %>

<!DOCTYPE html>
<html>
  <head>
    <title>Администрация</title>
    <%@ include file = "/upperFragment.jsp" %>
    <style>
      #backContainer {
        background-color: white;
        height: 500px;
      }
      .texts {
        font-size:20px;
      }
      #left{
        float:left; 
        margin-right:auto; 
        margin-left:auto;
        border-right-style: double;
        border-right-width: 10px;
        border-right-color: #1a718c;
        padding-right: 150px;"
      }
    </style>

  </head>
<body>
  <%@ include file = "/menuFragment.jsp" %>
  <div class="container-fluid">
    <div style= "text-align: center; color: #1b708b;">
      <h1>Администрация</h1>
      <p>
        Это администраторская панель сайта, здесь можно смотреть статистику и управлять сайтом.
      </p>
    </div>
    <div style="display:flex" id="backContainer">
      <div id ="left">
        <h1>Статистика сайта</h1>
        <h3>Статистика по всем активностям: </h3>
        <ul>
          <li class="texts"><b>Пользователей: </b><%= request.getAttribute("totalUsers")%></li>
          <li class="texts"><b>Переписок: </b><%= request.getAttribute("totalChats")%></li>
          <li class="texts"><b>Сообщений: </b><%= request.getAttribute("totalMessages")%></li>
          <li class="texts"><b>Наиболее активный пользователь: </b><%= request.getAttribute("mostActive")%></li>
          <li class="texts"><b>Новый пользователь: </b><%= request.getAttribute("newestUser")%></li>
          <li class="texts"><b>Красноречивейший пользователь: </b><%= request.getAttribute("wordiestUser")%></li>
        </ul>
      </div>
      <div style="float:right; margin-left:10px;margin-right:auto;">

      </div>
    </div>
  </div>
</body>
</html>
