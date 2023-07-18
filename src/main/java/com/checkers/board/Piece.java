package com.checkers.board;

import com.checkers.exceptions.CheckersException;
import com.checkers.utils.Color;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public abstract class Piece {

    protected final Color color;

    protected boolean canEat;

    protected boolean canMove;

    protected Cell cell;

    public abstract boolean isAbleToMoveTo(Cell to);

    public abstract boolean isAbleToEatTo(Cell to);

    public abstract void analyzeAbilityOfMove();

    public abstract void analyzeAbilityOfEat();

    public abstract void move(Cell to);

    public abstract void eat(Cell to);
}
