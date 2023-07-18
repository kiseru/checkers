<%@ page import="com.checkers.board.Piece" %>
<%@ page import="com.checkers.board.Man" %>
<%@ page import="com.checkers.board.Board" %>
<%@ page import="com.checkers.utils.Color" %>
<%@ page import="com.checkers.board.Cell" %>
<% Room boardRoom = (Room)request.getAttribute("room"); %>
<% Board board = boardRoom.getBoard(); %>
<% for (int row = 8; row > 0; row--) { %>
<div class="row">
    <% for (int col = 1; col <= 8; col++) { %>
        <% Color color = null; %>
        <% try {
            color = board.getCell(row, col).getColor();
        } catch (Exception e) {}%>
        <% if (color == Color.BLACK) { %>
            <div id="<%= board.getCell(row, col) %>" class="cell black-cell">
                <%
                    String rang = null;
                    Cell cell = board.getCell(row, col);
                    Piece piece = cell.getPiece();
                    if (piece != null) {
                        if (piece.getColor() == Color.BLACK) {
                            if (piece instanceof Man) {
                                rang = "black-man";
                            } else {
                                rang = "black-king";
                            }
                        } else {
                            if (piece instanceof Man) {
                                rang = "white-man";
                            } else {
                                rang = "white-king";
                            }
                        }
                    }
                %>
                <div id="<%= cell %>" class="piece <%= rang %>"></div>
            </div>
        <% } else if (color == Color.WHITE) { %>
            <div class="cell white-cell"></div>
        <% } else { %>
            <div class="cell">null</div>
        <% } %>
    <% } %>
</div>
<% } %>
