package ru.kiseru.checkers.domain.board

import ru.kiseru.checkers.domain.utils.Color

abstract class Piece(val color: Color) {
    
    var isCanEat = false
    
    var isCanMove = false
    
    lateinit var cell: Cell

    abstract val type: PieceType

    abstract fun analyzeAbilityOfMove()

    abstract fun move(destinationCell: Cell)

    abstract fun isAbleToMoveTo(destinationCell: Cell): Boolean

    abstract fun analyzeAbilityOfEat()

    abstract fun eat(destinationCell: Cell)

    abstract fun isAbleToEatTo(destinationCell: Cell): Boolean

    abstract fun isMan(): Boolean

    abstract fun isKing(): Boolean

    abstract fun getCssClass(): String
}
