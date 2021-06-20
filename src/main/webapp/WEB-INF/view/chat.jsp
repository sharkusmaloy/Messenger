<%@ page import="java.util.List" %>
<%@ page import="ilya.messenger.entity.domains.Chat" %>
<%@ page import="ilya.messenger.entity.domains.User" %>
<%@ page import="ilya.messenger.entity.domains.Sender" %>
<%@ page import="ilya.messenger.entity.repository.instances.UserInstance" %>
<%
Chat chat = (Chat) request.getAttribute("chat");
List<Sender> senders = (List<Sender>) request.getAttribute("senders");
List<User> users = (List<User>) request.getAttribute("users");
%>

<!DOCTYPE html>
<html>
<head>
  <script src="http://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <title><%= chat.getTitle() %></title>
  <%@ include file = "/upperFragment.jsp" %>

  <style>
    #chat {
        background-color: white;
        height: 300px;
        overflow-y: scroll;
        margin-bottom: 15px;
        padding: 15px;
        border: 4px solid #502828;
    }
    .listMessages {
      font-size:20px;
    }

    #author{
        color: #864949;
    }
    #author:hover{
            color: #b98181;
        }
  </style>

  <script>
    function scrollChat() {
      var chatDiv = document.getElementById('chat');
      chatDiv.scrollTop = chatDiv.scrollHeight;
    };

    <% if (request.getSession().getAttribute("user") != null) { %>
       var authorLogin = "<%= request.getSession().getAttribute("user")%>";
            $(document).ready(function(){
              $("li.listMessages > span").on({
                mouseenter: function(){
                  $(this).css("cursor", "default");
                  var author = $($(this).siblings()).find('a').text();
                  if(author == authorLogin) {
                    $(this).css("cursor", "not-allowed");
                    $(this).css("color", "red");
                    $(this).css("text-decoration", "line-through");
                  }
                },
                mouseleave: function(){
                  var author = $($(this).siblings()).find('a').text();
                  if (author == authorLogin) {
                    $(this).css("color", "#444");
                    $(this).css("text-decoration", "none");
                  }
                },
                click: function(){
                  var author = $($(this).siblings()).find('a').text();
                  if (author == authorLogin) {
                    if(confirm("Вы уверены, что хотите удалить это сообщение?")){
                      var listMessage = $(this).parent(".listMessages");
                      $(listMessage).fadeOut("slow");
                      $.post("", {
                            deletedMessageId: ($(listMessage).attr("value"))
                      });
                    }
                  }
                }
              });
            });
    <% } %>
  </script>
</head>
<body onload="scrollChat()">
  <%@ include file = "/menuFragment.jsp" %>

  <div class="container">
    <h1>Чат: <%= chat.getTitle() %></h1>
    <hr/>
    <% if (chat.isPrivate()) { %>
      <%@ include file = "/add.jsp" %>
    <% } %>
    <div>
      <div id="chat">
      <ul>
        <% for (Sender sender : senders) {
          String author = UserInstance.getInstance()
              .getUser(sender.getAuthorId()).getName(); %>
          <li class="listMessages" value="<%=sender.getId()%>"><strong><a id="author" href="/users/<%= author%>"><%= author %></a>:</strong> <span class="messageOutput" ><%= sender.getContent() %></%></span></li>
        <% } %>
      </ul>
    </div>

    <% if (request.getSession().getAttribute("user") != null) { %>

      <form class="form-group" action="/chat/<%= chat.getTitle() %>" method="POST">
        <input class="form-control" id="input" type="text" style="margin-bottom: 3%; font-size: 20px; border: 4px solid #502828;height: 60px;" name="messageInput" required>
        <button type="submit" class="btn btn-default">Отправить</button>
      </form>
    <% } else { %>
      <p><a href="/login">Авторизируйтесь</a>, для пользования приложением.</p>
    <% } %>
      </div>
    <hr/>
  </div>
</body>
</html>
