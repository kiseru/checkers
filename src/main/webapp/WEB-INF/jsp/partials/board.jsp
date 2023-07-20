<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%@ page import="com.checkers.board.Man" %>
<%@ page import="com.checkers.board.King" %>
<%@ page import="com.checkers.utils.Color" %>

<c:set var="boardLength" value="${fn:length(board)}"/>
<c:forEach var="i" begin="0" end="${boardLength}" step="1">
    <div class="row">
        <c:forEach var="cell" items="${board[boardLength - i - 1]}">
            <c:set var="color" value="${cell.getColor()}"/>

            <c:if test="${color == Color.BLACK}">
                <div id="${cell}" class="cell black-cell">
                    <c:if test="${!cell.isEmpty()}">
                        <c:set var="piece" value="${cell.getPiece()}"/>

                        <c:if test="${piece.getColor() == Color.BLACK}">
                            <c:if test="${piece.isMan()}">
                                <div id="${cell}" class="piece black-man"></div>
                            </c:if>

                            <c:if test="${piece.isKing()}">
                                <div id="${cell}" class="piece black-king"></div>
                            </c:if>
                        </c:if>

                        <c:if test="${piece.getColor() == Color.WHITE}">
                            <c:if test="${piece.isMan()}">
                                <div id="${cell}" class="piece white-man"></div>
                            </c:if>

                            <c:if test="${piece.isKing()}">
                                <div id="${cell}" class="piece white-king"></div>
                            </c:if>
                        </c:if>
                    </c:if>
                </div>
            </c:if>

            <c:if test="${color == Color.WHITE}">
                <div class="cell white-cell"></div>
            </c:if>
        </c:forEach>
    </div>
</c:forEach>
