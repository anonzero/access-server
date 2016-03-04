<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Client</title>
    </head>
    <body>
    <form action="server" method="post">
        <input type="text" name="command" title="Command"/>
        <input type="submit" value="Execute"/>
    </form>
    <p>${print}</p>
    </body>
</html>
