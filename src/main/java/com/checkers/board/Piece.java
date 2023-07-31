package com.checkers.board;

import com.checkers.utils.Color;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Piece {

    protected final Color color;

    protected boolean canEat;

    protected boolean canMove;

    protected Cell cell;

    public Piece(Color color) {
        this.color = color;
    }

    public abstract boolean isAbleToMoveTo(Cell to);

    public abstract boolean isAbleToEatTo(Cell to);

    public abstract void analyzeAbilityOfMove();

    public abstract void analyzeAbilityOfEat();

    public abstract void move(Cell to);

    public abstract void eat(Cell to);

    public abstract boolean isMan();

    public abstract boolean isKing();
}
