package com.checkers.board;

import com.checkers.utils.Colour;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.mockito.Mockito.mock;

class CellTest {

    @Test
    void testCreatingBlackCell() {
        var row = 0;
        var col = 0;
        var board = mock(CheckerBoard.class);
        var cell = new Cell(row, col, board);

        assertThat(cell.getCol()).isEqualTo(col);
        assertThat(cell.getRow()).isEqualTo(row);
        assertThat(cell.getBoard()).isEqualTo(board);
        assertThat(cell.getColour()).isEqualTo(Colour.BLACK);
        assertThat(cell.getPiece()).isNull();
    }

    @Test
    void testCreatingWhiteCell() {
        var row = 0;
        var col = 1;
        var board = mock(CheckerBoard.class);
        var cell = new Cell(row, col, board);

        assertThat(cell.getCol()).isEqualTo(col);
        assertThat(cell.getRow()).isEqualTo(row);
        assertThat(cell.getBoard()).isEqualTo(board);
        assertThat(cell.getColour()).isEqualTo(Colour.WHITE);
        assertThat(cell.getPiece()).isNull();
    }

    @Test
    void testSettingPieceWhileItIsNull() {
        var row = 0;
        var col = 0;
        var board = mock(CheckerBoard.class);
        var cell = new Cell(row, col, board);

        assertThatNoException()
                .isThrownBy(() -> cell.setPiece(null));
        assertThat(cell.getPiece()).isNull();
    }

    @Test
    void testSettingPieceWhileItIsNotNull() {
        var row = 0;
        var col = 0;
        var board = mock(CheckerBoard.class);
        var cell = new Cell(row, col, board);
        var piece = mock(Piece.class);

        cell.setPiece(piece);

        assertThat(cell.getPiece()).isEqualTo(piece);
    }
}
