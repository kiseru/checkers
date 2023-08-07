package ru.kiseru.checkers.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.kiseru.checkers.board.Board;
import ru.kiseru.checkers.board.Cell;
import ru.kiseru.checkers.board.Piece;
import ru.kiseru.checkers.exception.CellException;
import ru.kiseru.checkers.exception.ConvertCellException;
import ru.kiseru.checkers.exception.PieceException;
import ru.kiseru.checkers.utils.Color;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class UserTest {

    private static final String NAME = "Some cool name";

    @Mock
    private Board board;

    @InjectMocks
    private User underTest = new User(NAME, Color.WHITE);

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(underTest, "color", Color.WHITE);
    }

    @ParameterizedTest
    @ValueSource(strings = { "a", "aaa", "`2", "i9", "a0", "a9" })
    void testMakeTurnWhileSourceCellNameIsNotValid(String from) {
        // when & then
        assertThatExceptionOfType(ConvertCellException.class)
                .isThrownBy(() -> underTest.makeTurn(from, "a2"));
    }

    @Test
    void testMakeTurnWhileSourceCellHasNotPiece() {
        // given
        var emptyCell = mock(Cell.class);

        given(board.getCell(eq(2), eq(2))).willReturn(emptyCell);

        // when & then
        assertThatExceptionOfType(CellException.class)
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
        assertThatExceptionOfType(PieceException.class)
                .isThrownBy(() -> underTest.makeTurn("b2", "c3"));
    }

    @ParameterizedTest
    @ValueSource(strings = { "a", "aaa", "`2", "i9", "a0", "a9" })
    void testMakeTurnWhileDestinationCellNameIsNotValid(String to) {
        // given
        var piece = mock(Piece.class);
        given(piece.getColor()).willReturn(Color.WHITE);

        var sourceCell = mock(Cell.class);
        given(sourceCell.getPiece()).willReturn(piece);

        given(board.getCell(eq(2), eq(2))).willReturn(sourceCell);

        // when & then
        assertThatExceptionOfType(ConvertCellException.class)
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
        given(destinationCell.getPiece()).willReturn(mock(Piece.class));

        given(board.getCell(eq(2), eq(2))).willReturn(sourceCell);
        given(board.getCell(eq(3), eq(3))).willReturn(destinationCell);

        // when & then
        assertThatExceptionOfType(CellException.class)
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

        given(board.getCell(eq(2), eq(2))).willReturn(sourceCell);
        given(board.getCell(eq(3), eq(3))).willReturn(destinationCell);

        underTest.setCanEat(true);

        // when & then
        assertThatNoException().isThrownBy(() -> underTest.makeTurn("b2", "c3"));
    }

    @Test
    void testMakeTurnWhileCannotEat() {
        // given
        var piece = mock(Piece.class);
        given(piece.getColor()).willReturn(Color.WHITE);

        var sourceCell = mock(Cell.class);
        given(sourceCell.getPiece()).willReturn(piece);

        var destinationCell = mock(Cell.class);

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