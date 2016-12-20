<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="partials/head.jsp"%>
</head>
<body>
    <%@include file="partials/header.jsp"%>

    <div class="panel panel-default" style="margin-top:175px">
        <div class="panel-body">
            <form method="post" action="find_room">
                <div class="form-group">
                    <label for="log-in">Login</label>
                    <input type="text" name="login" class="form-control" id="log-in" placeholder="Your login">
                </div>
                <div class="row">
                    <a href="/" class="btn btn-success btn-lg">Back</a>
                    <input class=" btn btn-success btn-lg" type="submit" value="Log in">
                </div>
            </form>
        </div>
    </div>
</body>
</html>
