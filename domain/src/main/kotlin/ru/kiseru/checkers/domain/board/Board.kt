package ru.kiseru.checkers.domain.board

import ru.kiseru.checkers.domain.exception.CellException
import ru.kiseru.checkers.domain.exception.CellIsBusyException
import ru.kiseru.checkers.domain.exception.CellNotFoundException
import ru.kiseru.checkers.domain.exception.ConvertCellException
import ru.kiseru.checkers.domain.exception.PieceException
import ru.kiseru.checkers.domain.utils.Color
import ru.kiseru.checkers.domain.utils.getCellCaption
import ru.kiseru.checkers.domain.utils.isCoordinatesExists
import java.util.*

class Board(val id: UUID) {

    val board: Array<Array<Piece?>> = Array(SIZE_OF_BOARD) { arrayOfNulls(SIZE_OF_BOARD) }

    var version = 1
        private set

    private var whitePieces = 12

    private var blackPieces = 12

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
            .filter { (it.first + it.second) % 2 == 0 }
            .forEach { board[it.first][it.second] = Man(Color.WHITE) }

    private fun initBlackPieces() =
        (5..7).asSequence()
            .flatMap { row ->
                (0..7).asSequence()
                    .map { column -> row to column }
            }
            .filter { (it.first + it.second) % 2 == 0 }
            .forEach { board[it.first][it.second] = Man(Color.BLACK) }

    fun pieces(): Sequence<Piece> =
        piecesCoordinates()
            .mapNotNull { board[it.first - 1][it.second - 1] }

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
            piece.eat(this, source, destination)
            analyze(userColor)
        } else {
            piece.move(this, source, destination)
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
                piece.analyzeAbilityOfMove(this, it)
                piece.analyzeAbilityOfEat(this, it)
            }

    fun isGaming(): Boolean =
        whitePieces != 0 && blackPieces != 0

    fun decrementWhitePieceCount() {
        if (whitePieces > 0) {
            whitePieces--
        }
    }

    fun decrementBlackPieceCount() {
        if (blackPieces > 0) {
            blackPieces--
        }
    }

    companion object {
        private const val SIZE_OF_BOARD = 8
    }
}
