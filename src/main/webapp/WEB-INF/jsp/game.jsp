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
                String login = (String) request.getAttribute("login");
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
                        <p>Login: <span id="login">${login}</span></p>
                        <p>Turn: <%= turn %></p>
                        <p>Room ID: <span id="id">${room.getId()}</span></p>
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

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script
        src="https://code.jquery.com/jquery-1.12.4.min.js"
        integrity="sha384-nvAa0+6Qg9clwYCGGPpDQLVpLNn0fRaROjHqs13t4Ggj3Ez50XnGQqc/r8MhnRDZ"
        crossorigin="anonymous"></script>

    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script
        src="https://cdn.jsdelivr.net/npm/bootstrap@3.4.1/dist/js/bootstrap.min.js"
        integrity="sha384-aJ21OjlMXNL5UyIl/XNwTMqvzeRMZH2w8c5cRVpzpU8Y5bApTppSuUkhZXN0VxHd"
        crossorigin="anonymous"></script>

    <script src="js/script.js"></script>
</body>
</html>
