package ru.kiseru.checkers.board

import ru.kiseru.checkers.utils.Color

abstract class Piece(val color: Color) {
    
    var isCanEat = false
    
    var isCanMove = false
    
    lateinit var cell: Cell

    abstract fun analyzeAbilityOfMove()

    abstract fun move(destinationCell: Cell)

    abstract fun isAbleToMoveTo(destinationCell: Cell): Boolean

    abstract fun analyzeAbilityOfEat()

    abstract fun eat(destinationCell: Cell)

    abstract fun isAbleToEatTo(destinationCell: Cell): Boolean

    abstract fun isMan(): Boolean

    abstract fun isKing(): Boolean
}