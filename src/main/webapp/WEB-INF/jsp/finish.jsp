<%@ page import="com.checkers.game.Room" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@ include file="partials/head.jsp" %>
</head>
<body>
    <%@ include file="partials/header.jsp" %>

    <div class="panel panel-default">
        <div class="panel-body">
            <h2><%= (String)(request.getAttribute("winner")) %> win!</h2>
            <a href="/" class="btn btn-success btn-lg">Play again</a>
        </div>
    </div>


</body>
</html>
