<%@ page import="com.checkers.user.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="partials/head.jsp"%>
</head>
<body>
    <%@include file="partials/header.jsp"%>

    <div class="container-fluid">
        <div class="row">
            <div class="col-md-8">
                <%@include file="partials/board.jsp"%>
            </div>

            <%
                String turn = "";
                if (request.getAttribute("turn") != null) {
                    turn = ((User) request.getAttribute("turn")).getName();
                }

                String firstPlayer = "";
                if (request.getAttribute("firstPlayer") != null) {
                    firstPlayer = ((User)request.getAttribute("firstPlayer")).getName();
                }

                String secondPlayer = "";
                if (request.getAttribute("secondPlayer") != null) {
                    secondPlayer = ((User)request.getAttribute("secondPlayer")).getName();
                }
            %>

            <div class="col-md-4">
                <div class="panel panel-default">
                    <div class="panel-body">
                        <p>Login: ${param.login}</p>
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

                <div class="panel panel-default">
                    <div class="panel-body">
                        <form method="post" action="game">
                            <input type="hidden" name="login" value="${param.login}">

                            <div class="form-group">
                                <label for="from1">From</label>
                                <input type="text" class="form-control" name="from" id="from1" hidden>
                            </div>

                            <div class="form-group">
                                <label for="to1">To</label>
                                <input type="text" class="form-control" name="to" id="to1">
                            </div>

                            <% if (request.getParameter("login").equals(turn)) { %>
                                <input type="submit" class="btn btn-success btn-lg" value="Make turn">
                            <% } else { %>
                                <input type="submit" class="btn btn-success btn-lg" value="Make turn" disabled>
                            <% } %>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
