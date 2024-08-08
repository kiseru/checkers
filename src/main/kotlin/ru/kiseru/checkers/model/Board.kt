package ru.kiseru.checkers.model

import ru.kiseru.checkers.exception.CellException
import ru.kiseru.checkers.exception.CellIsBusyException
import ru.kiseru.checkers.exception.CellNotFoundException
import ru.kiseru.checkers.exception.ConvertCellException
import ru.kiseru.checkers.exception.PieceException
import ru.kiseru.checkers.utils.getCellCaption
import ru.kiseru.checkers.utils.isCoordinatesExists
import java.util.UUID

class Board(val id: UUID) {

    val board: Array<Array<Piece?>> = Array(SIZE_OF_BOARD) { arrayOfNulls(SIZE_OF_BOARD) }

    var version = 1
        private set

    init {
        initWhitePieces()
        initBlackPieces()
        analyzeAbilities()
    }

    private fun initWhitePieces() =
        (0..2).asSequence()
            .flatMap { row ->
                (0..7).asSequence()
                    .map { column -> row to column }
            }
            .filter { (row, column) -> (row + column) % 2 == 0 }
            .forEach { (row, column) -> board[row][column] = Piece(Color.WHITE, ManStrategy) }

    private fun initBlackPieces() =
        (5..7).asSequence()
            .flatMap { row ->
                (0..7).asSequence()
                    .map { column -> row to column }
            }
            .filter { (row, column) -> (row + column) % 2 == 0 }
            .forEach { (row, column) -> board[row][column] = Piece(Color.BLACK, ManStrategy) }

    fun pieces(): Sequence<Piece> =
        piecesCoordinates()
            .mapNotNull { (row, column) -> board[row - 1][column - 1] }

    fun piecesCoordinates(): Sequence<Pair<Int, Int>> =
        sequence {
            for (row in board.indices) {
                for (column in board.indices) {
                    if (board[row][column] != null) {
                        yield(row + 1 to column + 1)
                    }
                }
            }
        }

    fun makeTurn(userColor: Color, from: String, to: String): Boolean {
        val source = convertCell(from)
        val destination = convertCell(to)
        val piece = getUserPiece(source, userColor)
        checkForPiece(destination)
        val isCanEat = makeTurn(userColor, piece, source, destination)
        updateVersion()
        return isCanEat
    }

    private fun convertCell(cell: String): Pair<Int, Int> {
        if (cell.length != 2) {
            throw ConvertCellException("Can't convert '$cell' to cell")
        }

        val column = convertColumn(cell[0])
        val row = convertRow(cell[1])
        return row to column
    }

    private fun convertColumn(columnName: Char): Int =
        if (columnName < 'a' || columnName > 'h') {
            throw ConvertCellException("Column '$columnName' doesn't exists")
        } else {
            columnName.code - 'a'.code + 1
        }

    private fun convertRow(rowName: Char): Int =
        if (rowName < '1' || rowName > '8') {
            throw ConvertCellException("Row '$rowName' doesn't exists")
        } else {
            rowName.code - '1'.code + 1
        }

    private fun getUserPiece(source: Pair<Int, Int>, userColor: Color): Piece {
        val piece = getPiece(source) ?: throw CellException("Cell '${getCellCaption(source)}' is empty")
        checkPieceOwner(userColor, piece)
        return piece
    }

    private fun checkPieceOwner(userColor: Color, piece: Piece) {
        if (piece.color != userColor) {
            throw PieceException("The piece does not belong to the $userColor user")
        }
    }

    private fun checkForPiece(destination: Pair<Int, Int>) {
        if (getPiece(destination) != null) {
            throw CellIsBusyException(destination.first, destination.second)
        }
    }

    private fun makeTurn(userColor: Color, piece: Piece, source: Pair<Int, Int>, destination: Pair<Int, Int>): Boolean {
        val isCanEat = analyze(userColor)
        return if (isCanEat) {
            piece.pieceStrategy.eat(this, piece, source, destination)
            analyze(userColor)
        } else {
            piece.pieceStrategy.move(this, piece, source, destination)
            false
        }
    }

    fun getPiece(coordinates: Pair<Int, Int>): Piece? =
        if (isCoordinatesExists(coordinates)) {
            board[coordinates.first - 1][coordinates.second - 1]
        } else {
            throw CellNotFoundException(coordinates.first, coordinates.second)
        }

    private fun updateVersion() {
        version++
    }

    fun analyze(userColor: Color): Boolean {
        analyzeAbilities()

        return pieces()
            .any { it.color == userColor && it.isCanEat }
    }

    private fun analyzeAbilities() =
        piecesCoordinates()
            .forEach {
                val piece = board[it.first - 1][it.second - 1]!!
                piece.isCanMove = piece.pieceStrategy.analyzeAbilityOfMove(this, piece, it)
                piece.isCanEat = piece.pieceStrategy.analyzeAbilityOfEat(this, piece, it)
            }

    companion object {
        private const val SIZE_OF_BOARD = 8
    }
}
