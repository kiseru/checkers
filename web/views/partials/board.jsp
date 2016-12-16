<%@ page import="com.checkers.board.Piece" %>
<%@ page import="com.checkers.board.Man" %>
<%@ page import="com.checkers.board.CheckerBoard" %>
<%@ page import="com.checkers.utils.Colour" %>
<% CheckerBoard board = (CheckerBoard)request.getAttribute("board"); %>
<% for (int row = CheckerBoard.SIZE_OF_BOARD; row > 0; row--) { %>
<div class="row">
    <% for (int col = 1; col <= CheckerBoard.SIZE_OF_BOARD; col++) { %>
    <% Colour colour = null; %>
    <% try {
        colour = board.getCell(row, col).getColour();
    } catch (Exception e) {}%>
    <% if (colour == Colour.BLACK) { %>
    <div class="cell black-cell">
        <% Piece piece = board.getCell(row, col).getPiece(); %>
        <% if (piece != null) { %>
            <% if (piece.getColour() == Colour.BLACK) { %>
                <% if (piece instanceof Man) { %>
                    <div class="piece black-man"></div>
                <% } else { %>
                    <div class="piece black-king"></div>
                <% } %>
            <% } else { %>
                <% if (piece instanceof Man) { %>
                    <div class="piece white-man"></div>
                <% } else { %>
                    <div class="piece white-king"></div>
                <% } %>
            <% } %>
        <% } %>
    </div>
    <% } else if (colour == Colour.WHITE) { %>
    <div class="cell white-cell"></div>
    <% } else { %>
    <div class="cell">null</div>
    <% } %>
    <% } %>
</div>
<% } %>
