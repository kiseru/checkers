<%@ page import="com.checkers.user.User" %>
<%@ page import="com.checkers.game.Room" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="partials/head.jsp"%>
    <script src="js/autoreload.js"></script>
</head>
<body>
    <%@include file="partials/header.jsp"%>

    <div class="container-fluid">
        <div class="row">
            <div class="col-md-8">
                <%@include file="partials/board.jsp"%>
            </div>

            <%
                Room room = (Room) request.getAttribute("room");

                String turn = room.getTurn().toString();

                String firstPlayer = room.getFirstPlayer().toString();

                String secondPlayer = null;
                if (room.getSecondPlayer() != null) {
                    secondPlayer = room.getSecondPlayer().toString();
                }
            %>

            <div class="col-md-4">
                <div class="panel panel-default">
                    <div class="panel-body">
                        <p>Login: <span id="login">${param.login}</span></p>
                        <p>Turn: <%= turn %></p>
                    </div>
                </div>

                <div class="panel panel-default">
                    <div class="panel-body">
                        <p>White: <%= firstPlayer %></p>
                        <p>Black: <%= secondPlayer %></p>
                        <p>Message: <%= request.getAttribute("Message") %></p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
