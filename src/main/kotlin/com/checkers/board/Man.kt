package com.checkers.board

import com.checkers.exception.CannotEatException
import com.checkers.exception.CannotMoveException
import com.checkers.exception.MustEatException
import com.checkers.utils.Color
import com.checkers.utils.isCoordinateExists
import com.checkers.utils.isCoordinatesExists

class Man(color: Color) : Piece(color) {

    override fun analyzeAbilityOfMove() {
        val nextRow = getNextRow()
        if (!isCoordinateExists(nextRow)) {
            return
        }

        val board = cell.board
        val column = cell.column
        isCanMove = sequenceOf(column - 1, column + 1)
            .filter { isCoordinateExists(it) }
            .map { board.getCell(nextRow, it) }
            .any { isAbleToMoveTo(it) }
    }

    private fun getNextRow(): Int =
        if (color == Color.WHITE) {
            cell.row + 1
        } else {
            cell.row - 1
        }

    override fun analyzeAbilityOfEat() {
        val column = cell.column
        val row = cell.row
        val board = cell.board
        isCanEat = sequenceOf(row - 2, row + 2)
            .filter { isCoordinateExists(it) }
            .flatMap { nextRow ->
                sequenceOf(column - 2, column + 2)
                    .filter { isCoordinateExists(it) }
                    .map { board.getCell(nextRow, it) }
            }
            .any { isAbleToEatTo(it) }
    }

    override fun isAbleToMoveTo(destinationCell: Cell): Boolean {
        if (destinationCell.piece != null || isEnemyNear() || cell.diff(destinationCell) != 1) {
            return false
        }

        val nextRow = getNextRow()
        if (!isCoordinateExists(nextRow)) {
            return false
        }

        val column = cell.column
        val board = cell.board
        return sequenceOf(column - 1, column + 1)
            .filter { isCoordinateExists(it) }
            .map { board.getCell(nextRow, it) }
            .any { it == destinationCell }
    }

    private fun isEnemyNear(): Boolean =
        sequenceOf(
            cell.row + 1 to cell.column + 1,
            cell.row + 1 to cell.column - 1,
            cell.row - 1 to cell.column - 1,
            cell.row - 1 to cell.column + 1,
        )
            .filter { isCoordinatesExists(it.first, it.second) }
            .map { cell.board.getCell(it.first, it.second) }
            .any { hasEnemyIn(it) }

    private fun hasEnemyIn(cell: Cell): Boolean =
        cell.piece?.let { it.color != color } ?: false

    override fun isAbleToEatTo(destinationCell: Cell): Boolean {
        if (cell.diff(destinationCell) != 2 || destinationCell.piece != null) {
            return false
        }

        return cell.between(destinationCell, cell.board)
            .piece?.let { it.color != color } ?: false
    }

    override fun move(destinationCell: Cell) {
        if (isCanEat) {
            throw MustEatException()
        }

        if (!isCanMove) {
            throw CannotMoveException(cell, destinationCell)
        }

        if (!isAbleToMoveTo(destinationCell)) {
            throw CannotMoveException(cell, destinationCell)
        }

        cell.piece = null
        updatePiece(destinationCell)
    }

    override fun eat(destinationCell: Cell) {
        if (!isCanEat || !isAbleToEatTo(destinationCell)) {
            throw CannotEatException(cell, destinationCell)
        }

        cell.piece = null
        cell.between(destinationCell, cell.board).piece = null
        updatePiece(destinationCell)
    }

    private fun updatePiece(cell: Cell) =
        if (color == Color.WHITE && cell.row == 8 || color == Color.BLACK && cell.row == 1) {
            cell.piece = createKing()
        } else {
            cell.piece = this
        }

    private fun createKing(): Piece =
        King(color)

    override fun isMan(): Boolean =
        true

    override fun isKing(): Boolean =
        false
}