<%--
  Created by IntelliJ IDEA.
  User: root
  Date: 1/3/16
  Time: 5:48 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Client</title>
  </head>
  <body>
    <form action="server" method="post">
      <input type="hidden" name="command" value="login"/>
      <input type="submit" value="Login"/>
    </form>
  </body>
</html>
