package com.checkers.user;

import com.checkers.board.Board;
import com.checkers.board.Cell;
import com.checkers.board.Piece;
import com.checkers.exceptions.CellDoesNotExistException;
import com.checkers.exceptions.CheckersException;
import com.checkers.exceptions.EmptyCellNotFoundException;
import com.checkers.exceptions.PieceNotFoundException;
import com.checkers.exceptions.YourPieceNotFoundException;
import com.checkers.utils.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class UserTest {

    @InjectMocks
    private User underTest;

    @Mock
    private Board board;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(underTest, "color", Color.WHITE);
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "aaa", "`2", "i9", "a0", "a9"})
    void testMakeTurnWhileSourceCellNameIsNotValid(String from) {
        // when & then
        assertThatExceptionOfType(CellDoesNotExistException.class)
                .isThrownBy(() -> underTest.makeTurn(from, "a2"));
    }

    @Test
    void testMakeTurnWhileSourceCellHasNotPiece() {
        // given
        var emptyCell = mock(Cell.class);
        given(emptyCell.getPiece()).willReturn(null);

        given(board.getCell(eq(2), eq(2))).willReturn(emptyCell);

        // when & then
        assertThatExceptionOfType(PieceNotFoundException.class)
                .isThrownBy(() -> underTest.makeTurn("b2", "c3"));
    }

    @Test
    void testMakeTurnWhileSourceCellHasNotPlayerPiece() {
        // given
        var piece = mock(Piece.class);
        given(piece.getColor()).willReturn(Color.BLACK);

        var sourceCell = mock(Cell.class);
        given(sourceCell.getPiece()).willReturn(piece);

        given(board.getCell(eq(2), eq(2))).willReturn(sourceCell);

        // when & then
        assertThatExceptionOfType(YourPieceNotFoundException.class)
                .isThrownBy(() -> underTest.makeTurn("b2", "c3"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "aaa", "`2", "i9", "a0", "a9"})
    void testMakeTurnWhileDestinationCellNameIsNotValid(String to) {
        // given
        var piece = mock(Piece.class);
        given(piece.getColor()).willReturn(Color.WHITE);

        var sourceCell = mock(Cell.class);
        given(sourceCell.getPiece()).willReturn(piece);

        given(board.getCell(eq(2), eq(2))).willReturn(sourceCell);

        // when & then
        assertThatExceptionOfType(CellDoesNotExistException.class)
                .isThrownBy(() -> underTest.makeTurn("b2", to));
    }

    @Test
    void testMakeTurnWhileDestinationCellHasPiece() {
        // given
        var piece = mock(Piece.class);
        given(piece.getColor()).willReturn(Color.WHITE);

        var sourceCell = mock(Cell.class);
        given(sourceCell.getPiece()).willReturn(piece);

        var destinationCell = mock(Cell.class);
        given(destinationCell.getPiece()).willReturn(piece);

        given(board.getCell(eq(2), eq(2))).willReturn(sourceCell);
        given(board.getCell(eq(3), eq(3))).willReturn(destinationCell);

        // when & then
        assertThatExceptionOfType(EmptyCellNotFoundException.class)
                .isThrownBy(() -> underTest.makeTurn("b2", "c3"));
    }

    @Test
    void testMakeTurnWhileCanEat() {
        // given
        var piece = mock(Piece.class);
        given(piece.getColor()).willReturn(Color.WHITE);

        var sourceCell = mock(Cell.class);
        given(sourceCell.getPiece()).willReturn(piece);

        var destinationCell = mock(Cell.class);
        given(destinationCell.getPiece()).willReturn(null);

        given(board.getCell(eq(2), eq(2))).willReturn(sourceCell);
        given(board.getCell(eq(3), eq(3))).willReturn(destinationCell);

        underTest.setCanEat(true);

        // when & then
        assertThatNoException().isThrownBy(() -> underTest.makeTurn("b2", "c3"));
    }

    @Test
    void testMakeTurnWhileCannotEat() throws CheckersException {
        // given
        var piece = mock(Piece.class);
        given(piece.getColor()).willReturn(Color.WHITE);

        var sourceCell = mock(Cell.class);
        given(sourceCell.getPiece()).willReturn(piece);

        var destinationCell = mock(Cell.class);
        given(destinationCell.getPiece()).willReturn(null);

        given(board.getCell(eq(2), eq(2))).willReturn(sourceCell);
        given(board.getCell(eq(3), eq(3))).willReturn(destinationCell);

        underTest.setCanEat(false);

        // when
        underTest.makeTurn("b2", "c3");

        // then
        var actual = underTest.isCanEat();
        assertThat(actual).isFalse();
    }

    @Test
    void testToString() {
        // given
        ReflectionTestUtils.setField(underTest, "name", "Some cool name");

        // when
        var actual = underTest.toString();

        // then
        assertThat(actual).isEqualTo("Some cool name");
    }
}