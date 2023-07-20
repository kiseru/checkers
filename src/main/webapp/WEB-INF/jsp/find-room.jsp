<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@ include file="partials/head.jsp" %>
</head>
<body>
    <%@ include file="partials/header.jsp" %>

    <div class="panel panel-default" style="margin-top: 175px;">
        <div class="panel-body">
            <form method="post" action="/find-room">
                <div class="form-group">
                    <label for="log-in">Room ID</label>
                    <input type="number" name="roomId" class="form-control" id="log-in" placeholder="Room ID">
                </div>
                <div class="row">
                    <a href="/login" class="btn btn-success btn-lg">Back</a>
                    <input class="btn btn-success btn-lg" type="submit" value="Find">
                </div>
            </form>

        </div>
    </div>
</body>
</html>
