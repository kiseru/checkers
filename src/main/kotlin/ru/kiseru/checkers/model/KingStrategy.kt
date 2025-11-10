package ru.kiseru.checkers.model

import ru.kiseru.checkers.utils.isCoordinatesExists
import kotlin.math.abs

object KingStrategy : PieceStrategy {

    override val type: PieceType = PieceType.KING

    override fun analyzeAbilityOfMove(board: Board, piece: Piece, source: Pair<Int, Int>): Boolean {
        if (!piece.isCanMove) {
            return false
        }

        val maxDistance = minOf(
            8 - source.first,
            source.first - 1,
            8 - source.second,
            source.second - 1,
        )

        if (maxDistance == 0) {
            return false
        }

        return (1..maxDistance).asSequence()
            .flatMap { sequenceOf(it to it, it to -it, -it to -it, -it to it) }
            .map { source.first + it.first to source.second + it.second }
            .filter { (row, col) -> isCoordinatesExists(row, col) }
            .any { isAbleToMoveTo(board, source, it) }
    }

    override fun move(board: Board, piece: Piece, source: Pair<Int, Int>, destination: Pair<Int, Int>) {
        if (piece.isCanEat) {
            throw IllegalStateException(
                "Cannot move: piece must eat instead. " +
                        "Source=$source, Destination=$destination, PieceColor=${piece.color}"
            )
        }

        if (!piece.isCanMove) {
            throw IllegalStateException(
                "Piece is not allowed to move. Source=$source, Destination=$destination, PieceColor=${piece.color}"
            )
        }

        if (!isAbleToMoveTo(board, source, destination)) {
            throw IllegalStateException(
                "Invalid move path: cannot move from $source to $destination. PieceColor=${piece.color}"
            )
        }

        validateBoardCoordinates(source)
        validateBoardCoordinates(destination)

        board.board[source.first - 1][source.second - 1] = null
        board.board[destination.first - 1][destination.second - 1] = piece
    }

    private fun isAbleToMoveTo(board: Board, source: Pair<Int, Int>, destination: Pair<Int, Int>): Boolean {
        if (diff(source, destination) == -1) {
            return false
        }

        if (board.getPiece(destination) != null) {
            return false
        }

        if (isOnOtherDiagonal(source, destination)) {
            return false
        }

        val deltaCol = destination.second - source.second
        val deltaRow = destination.first - source.first

        val colSign = Integer.signum(deltaCol)
        val rowSign = Integer.signum(deltaRow)

        var step = 1

        while (true) {
            val currentRow = source.first + rowSign * step
            val currentCol = source.second + colSign * step

            if (currentRow == destination.first && currentCol == destination.second) {
                return true
            }

            if (board.getPiece(currentRow to currentCol) != null) {
                return false
            }

            step++

            if (!isCoordinatesExists(currentRow, currentCol)) {
                return false
            }
        }
    }

    override fun analyzeAbilityOfEat(
        board: Board,
        piece: Piece,
        source: Pair<Int, Int>
    ): Boolean {
        return (2..7).asSequence()
            .flatMap { sequenceOf(it to it, it to -it, -it to -it, -it to it) }
            .map { source.first + it.first to source.second + it.second }
            .filter { (row, col) -> isCoordinatesExists(row, col) }
            .any { destination -> isAbleToEatTo(board, piece, source, destination) }
    }

    override fun eat(
        board: Board,
        piece: Piece,
        source: Pair<Int, Int>,
        destination: Pair<Int, Int>
    ) {
        if (!piece.isCanEat) {
            throw IllegalStateException(
                "Piece cannot eat: isCanEat=false. Source=$source, Destination=$destination, Color=${piece.color}"
            )
        }

        if (!isAbleToEatTo(board, piece, source, destination)) {
            throw IllegalStateException(
                "Invalid eat move: cannot eat from $source to $destination. Color=${piece.color}"
            )
        }

        val sacrificedPieceLocation = getSacrificedPieceLocation(board, piece, source, destination)

        validateBoardCoordinates(sacrificedPieceLocation)
        validateBoardCoordinates(source)
        validateBoardCoordinates(destination)

        board.board[sacrificedPieceLocation.first - 1][sacrificedPieceLocation.second - 1] = null
        board.board[source.first - 1][source.second - 1] = null
        board.board[destination.first - 1][destination.second - 1] = piece
    }

    private fun validateBoardCoordinates(position: Pair<Int, Int>) {
        require(position.first in 1..8 && position.second in 1..8) {
            "Invalid board coordinates: $position. Board size: ${8}x${8}"
        }
    }

    private fun isAbleToEatTo(
        board: Board,
        piece: Piece,
        source: Pair<Int, Int>,
        destination: Pair<Int, Int>
    ): Boolean {
        if (board.getPiece(destination) != null) {
            return false
        }

        if (isOnOtherDiagonal(source, destination)) {
            return false
        }

        if (diff(source, destination) < 2) {
            return false
        }

        val deltaCol = destination.second - source.second
        val deltaRow = destination.first - source.first

        val colSign = Integer.signum(deltaCol)
        val rowSign = Integer.signum(deltaRow)

        var enemyPieceCount = 0
        var step = 1

        while (true) {
            val currentRow = source.first + rowSign * step
            val currentCol = source.second + colSign * step

            if (currentRow == destination.first && currentCol == destination.second) {
                return enemyPieceCount == 1
            }

            val pieceOnPath = board.getPiece(currentRow to currentCol)
            if (pieceOnPath == null) {
                step++
                continue
            }

            if (pieceOnPath.color == piece.color) {
                return false
            }

            enemyPieceCount++
            step++

            if (enemyPieceCount > 1) {
                return false
            }
        }
    }

    private fun isOnOtherDiagonal(source: Pair<Int, Int>, destination: Pair<Int, Int>): Boolean {
        val deltaCol = destination.second - source.second
        val deltaRow = destination.first - source.first

        if (deltaCol == 0 && deltaRow == 0) {
            return false
        }

        if (deltaRow == 0) {
            return false
        }

        val slope = deltaCol.toFloat() / deltaRow
        return abs(slope) != 1f
    }

    private fun diff(source: Pair<Int, Int>, destination: Pair<Int, Int>): Int {
        val colDiff = abs(source.second - destination.second)
        val rowDiff = abs(source.first - destination.first)
        return if (colDiff == rowDiff) colDiff else -1
    }

    private fun getSacrificedPieceLocation(
        board: Board,
        piece: Piece,
        source: Pair<Int, Int>,
        destination: Pair<Int, Int>
    ): Pair<Int, Int> {
        validateBoardCoordinates(source)
        validateBoardCoordinates(destination)

        val rowStep = Integer.signum(destination.first - source.first)
        val colStep = Integer.signum(destination.second - source.second)

        if (rowStep == 0 || colStep == 0 || abs(rowStep) != abs(colStep)) {
            throw IllegalStateException("Movement is not diagonal: source=$source, destination=$destination")
        }

        var currentRow = source.first + rowStep
        var currentCol = source.second + colStep
        while (currentRow != destination.first || currentCol != destination.second) {
            val anotherPiece = board.getPiece(currentRow to currentCol)
            if (anotherPiece == null) {
                currentRow += rowStep
                currentCol += colStep
                continue
            }

            if (anotherPiece.color == piece.color) {
                throw IllegalStateException(
                    "Cannot eat: friendly piece at ($currentRow, $currentCol). Source=$source, Destination=$destination"
                )
            }

            return currentRow to currentCol
        }

        throw IllegalStateException("No piece to eat on the path to destination. Source=$source, Destination=$destination")
    }
}
