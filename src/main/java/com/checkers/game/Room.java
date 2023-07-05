package com.checkers.game;

import com.checkers.board.CheckerBoard;
import com.checkers.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
@Setter
public class Room {

    @Getter
    private final int id;

    @Getter
    private final CheckerBoard board;

    private User firstPlayer;

    private User secondPlayer;

    private User turn;
}
