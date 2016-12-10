<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="partials/head.jsp"%>
</head>
<body>
    <%@include file="partials/header.jsp"%>
    <div class="panel panel-default col-md-4 col-md-offset-4">
        <div class="panel-body">
            <form method="post" action="game">
                <div class="form-group">
                    <label for="log-in">Login</label>
                    <input type="text" name="login" class="form-control" id="log-in" placeholder="Your login">
                </div>
                <input class=" btn btn-info btn-lg" type="submit" value="Log in">
            </form>
        </div>
    </div>
</body>
</html>
