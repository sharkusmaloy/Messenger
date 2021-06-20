<%@ page import="ilya.messenger.entity.domains.User" %>
<%@ page import="java.util.List" %>
<style>
.dropbtn {
background: #864949;
    width: 100%;
  color: white;
  padding: 16px;
  font-size: 16px;
  cursor: pointer;
}

#myInput {
    width: 100%;
  border-box: box-sizing;
  background-position: 14px 12px;
  background-repeat: no-repeat;
  font-size: 16px;
  padding: 14px 20px 12px 45px;
  border: none;
  border-bottom: 1px solid #ddd;
}

#myInput:focus {outline: 3px solid #ddd;}

.dropdown {
    width: 100%;
  position: relative;
  display: inline-block;
}

.dropdown-content {
    width: 100%;
  height: 200px;
  display: none;
  position: absolute;
  background-color: #f6f6f6;
  min-width: 230px;
  overflow-y: scroll;
  border: 1px solid #ddd;
  z-index: 1;
}

.dropdown-content a {
  color: black;
  padding: 12px 16px;
  text-decoration: none;
  display: block;
}
.dropdown a:hover {background-color: #ddd;}
  
</style>

<script>
  $(document).ready(function(){
    $(".user").on({
      click: function(){
        var name = $(this).text(); 
        if(confirm("Добавить пользователя " + name + " к данному приватному чату?")){
           $.post("", {nameToBeAdded: name });
        }
        $(this).fadeOut("fast");
        var menu = $(document.getElementById("myDropdown"));
        menu.fadeOut('slow');
      }
    });
    $(".dropbtn").on({
      click: function(){
        var search = document.getElementById("myInput");
        var menu = $(document.getElementById("myDropdown"));
        if(menu.css("display") == "block"){
            menu.fadeOut('fast');
            return;
        }
        menu.css("display", "block")
        search.focus(); 
      }, 
      mouseover: function(){
        $(this).css("background-color", "#502828")
      }, 
      mouseleave: function(){
        $(this).css("background-color", "#864949")
      }
    });
     
    $(".dropdown").on({
      mouseleave: function(){
        var menu = $(document.getElementById("myDropdown"));
        setTimeout(function(){menu.fadeOut('slow');}, 1000);
      }
    });  
     $("#myDropdown").on({
       mouseenter: function(){
       $(this).css("display", "block")
      }
    });  
  });

  function filterFunction() {
    var input, filter, ul, li, a, i;
    input = document.getElementById("myInput");
    filter = input.value.toUpperCase();
    div = document.getElementById("myDropdown");
    a = div.getElementsByTagName("a");
    for (i = 0; i < a.length; i++) {
        if (a[i].innerHTML.toUpperCase().indexOf(filter) > -1) {
            a[i].style.display = "";
        } else {
            a[i].style.display = "none";
        }
    }
  }
</script>

<div class="dropdown" style="margin-bottom: 3%;">
<button class="dropbtn">Добавить участника</button>
  <div id="myDropdown" class="dropdown-content">
    <input type="text" placeholder="Поиск.." id="myInput" onkeyup="filterFunction()">
    <% List<User> excludedUsers = (List<User>) request.getAttribute("excludedUsers"); %>
    <% for (User user: excludedUsers){
        String name = user.getName();
    %>
		    <a class="user"><%=name%></a>
	  <% } %>
  </div>
</div>