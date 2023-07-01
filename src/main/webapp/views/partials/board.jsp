<%@ page import="com.checkers.board.Piece" %>
<%@ page import="com.checkers.board.Man" %>
<%@ page import="com.checkers.board.CheckerBoard" %>
<%@ page import="com.checkers.utils.Colour" %>
<%@ page import="com.checkers.board.Cell" %>
<% Room boardRoom = (Room)request.getAttribute("room"); %>
<% CheckerBoard board = boardRoom.getBoard(); %>
<% for (int row = CheckerBoard.SIZE_OF_BOARD; row > 0; row--) { %>
<div class="row">
    <% for (int col = 1; col <= CheckerBoard.SIZE_OF_BOARD; col++) { %>
        <% Colour colour = null; %>
        <% try {
            colour = board.getCell(row, col).getColour();
        } catch (Exception e) {}%>
        <% if (colour == Colour.BLACK) { %>
            <div id="<%= board.getCell(row, col) %>" class="cell black-cell">
                <%
                    String rang = null;
                    Cell cell = board.getCell(row, col);
                    Piece piece = cell.getPiece();
                    if (piece != null) {
                        if (piece.getColour() == Colour.BLACK) {
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
        <% } else if (colour == Colour.WHITE) { %>
            <div class="cell white-cell"></div>
        <% } else { %>
            <div class="cell">null</div>
        <% } %>
    <% } %>
</div>
<% } %>
