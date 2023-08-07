package ru.kiseru.checkers.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.kiseru.checkers.exception.CannotEatException;
import ru.kiseru.checkers.exception.CannotMoveException;
import ru.kiseru.checkers.exception.CellIsBusyException;
import ru.kiseru.checkers.exception.CellIsEmptyException;
import ru.kiseru.checkers.exception.CellNotFoundException;
import ru.kiseru.checkers.user.User;
import ru.kiseru.checkers.utils.Color;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class BoardTest {

    private static final int BOARD_SIZE = 8;

    private Board underTest;

    @BeforeEach
    void setUp() {
        underTest = new Board();
    }

    @Test
    void testInitializing() {
        // then
        var board = (Cell[][]) ReflectionTestUtils.getField(underTest, "board");

        assertThat(board).isNotNull();
        assertThat(board.length).isEqualTo(BOARD_SIZE);
        for (Cell[] row : board) {
            assertThat(row.length).isEqualTo(BOARD_SIZE);
        }

        for (Cell[] row : board) {
            for (Cell cell : row) {
                assertThat(cell).isNotNull();
            }
        }
    }

    @ParameterizedTest
    @MethodSource("testWhitePieceInitializationSource")
    void testWhitePieceInitialization(int row, int column) {
        // given
        var board = (Cell[][]) ReflectionTestUtils.getField(underTest, "board");
        var cell = board[row - 1][column - 1];
        var piece = cell.getPiece();

        assertThat(piece).isNotNull();
        assertThat(piece.getColor()).isEqualTo(Color.WHITE);
    }

    private static Stream<Arguments> testWhitePieceInitializationSource() {
        return Stream.of(
                Arguments.of(1, 1),
                Arguments.of(1, 3),
                Arguments.of(1, 5),
                Arguments.of(1, 7),
                Arguments.of(2, 2),
                Arguments.of(2, 4),
                Arguments.of(2, 6),
                Arguments.of(2, 8),
                Arguments.of(3, 1),
                Arguments.of(3, 3),
                Arguments.of(3, 5),
                Arguments.of(3, 7)
        );
    }

    @ParameterizedTest
    @MethodSource("testBlackPieceInitializationSource")
    void testBlackPieceInitialization(int row, int column) {
        // given
        var board = (Cell[][]) ReflectionTestUtils.getField(underTest, "board");
        var cell = board[row - 1][column - 1];
        var piece = cell.getPiece();

        assertThat(piece).isNotNull();
        assertThat(piece.getColor()).isEqualTo(Color.BLACK);
    }

    private static Stream<Arguments> testBlackPieceInitializationSource() {
        return Stream.of(
                Arguments.of(6, 2),
                Arguments.of(6, 4),
                Arguments.of(6, 6),
                Arguments.of(6, 8),
                Arguments.of(7, 1),
                Arguments.of(7, 3),
                Arguments.of(7, 5),
                Arguments.of(7, 7),
                Arguments.of(8, 2),
                Arguments.of(8, 4),
                Arguments.of(8, 6),
                Arguments.of(8, 8)
        );
    }

    @Test
    void testIsGamingWhileThereIsPiecesOnBoard() {
        // when
        var actual = underTest.isGaming();

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void testIsGamingWhileThereIsOnlyWhitePiecesOnBoard() {
        // given
        ReflectionTestUtils.setField(underTest, "blackPieces", 0);

        // when
        var actual = underTest.isGaming();

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void testIsGamingWhileThereIsOnlyBlackPiecesObBoard() {
        // given
        ReflectionTestUtils.setField(underTest, "whitePieces", 0);

        // when
        var actual = underTest.isGaming();

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void testEatWhileSourceCellIsEmpty() {
        // given
        var sourceCell = mock(Cell.class);

        var destinationCell = mock(Cell.class);

        // when & then
        assertThatExceptionOfType(CellIsEmptyException.class)
                .isThrownBy(() -> underTest.eat(sourceCell, destinationCell));
    }

    @Test
    void testEatWhileDestinationCellIsBusy() {
        // given
        var sourceCell = mock(Cell.class);
        given(sourceCell.getPiece()).willReturn(mock(Piece.class));

        var destinationCell = mock(Cell.class);
        given(destinationCell.getPiece()).willReturn(mock(Piece.class));

        // when & then
        assertThatExceptionOfType(CellIsBusyException.class)
                .isThrownBy(() -> underTest.eat(sourceCell, destinationCell));
    }

    @Test
    void testEatWhileCannotEat() {
        // given
        var destinationCell = mock(Cell.class);

        var piece = mock(Piece.class);
        given(piece.isAbleToEatTo(eq(destinationCell))).willReturn(Boolean.FALSE);

        var sourceCell = mock(Cell.class);
        given(sourceCell.getPiece()).willReturn(piece);

        // when & then
        assertThatExceptionOfType(CannotEatException.class)
                .isThrownBy(() -> underTest.eat(sourceCell, destinationCell));
    }

    @Test
    void testEatWhileCanEat() {
        // given
        var destinationCell = mock(Cell.class);

        var piece = mock(Piece.class);
        given(piece.isAbleToEatTo(eq(destinationCell))).willReturn(Boolean.TRUE);

        var sourceCell = mock(Cell.class);
        given(sourceCell.getPiece()).willReturn(piece);

        // when
        underTest.eat(sourceCell, destinationCell);

        // then
        then(piece).should(times(1)).eat(eq(destinationCell));
    }

    @Test
    void testMoveWhileSourceCellIsEmpty() {
        // given
        var destinationCell = mock(Cell.class);

        var sourceCell = mock(Cell.class);

        // when & then
        assertThatExceptionOfType(CellIsEmptyException.class)
                .isThrownBy(() -> underTest.move(sourceCell, destinationCell));
    }

    @Test
    void testMoveWhileDestinationCellIsBusy() {
        // given
        var destinationCell = mock(Cell.class);
        given(destinationCell.getPiece()).willReturn(mock(Piece.class));

        var sourceCell = mock(Cell.class);
        given(sourceCell.getPiece()).willReturn(mock(Piece.class));

        // when & then
        assertThatExceptionOfType(CellIsBusyException.class)
                .isThrownBy(() -> underTest.move(sourceCell, destinationCell));
    }

    @Test
    void testMoveWhileCannotMove() {
        // given
        var destinationCell = mock(Cell.class);

        var piece = mock(Piece.class);
        given(piece.isAbleToMoveTo(eq(destinationCell))).willReturn(Boolean.FALSE);

        var sourceCell = mock(Cell.class);
        given(sourceCell.getPiece()).willReturn(piece);

        // when & then
        assertThatExceptionOfType(CannotMoveException.class)
                .isThrownBy(() -> underTest.move(sourceCell, destinationCell));
    }

    @Test
    void testMoveWhileCanMove() {
        // given
        var destinationCell = mock(Cell.class);

        var piece = mock(Piece.class);
        given(piece.isAbleToMoveTo(eq(destinationCell))).willReturn(Boolean.TRUE);

        var sourceCell = mock(Cell.class);
        given(sourceCell.getPiece()).willReturn(piece);

        // when
        underTest.move(sourceCell, destinationCell);

        // then
        then(piece).should(times(1)).move(eq(destinationCell));
    }

    @ParameterizedTest
    @MethodSource("testDecrementPieceCountSource")
    void testDecrementWhitePieceCount(int initialCount, int expectedCount) {
        // given
        ReflectionTestUtils.setField(underTest, "whitePieces", initialCount);

        // when
        underTest.decrementWhitePieceCount();

        // then
        var actual = ReflectionTestUtils.getField(underTest, "whitePieces");
        assertThat(actual).isEqualTo(expectedCount);
    }

    @ParameterizedTest
    @MethodSource("testDecrementPieceCountSource")
    void testDecrementBlackPieceCount(int initialCount, int expectedCount) {
        // given
        ReflectionTestUtils.setField(underTest, "blackPieces", initialCount);

        // when
        underTest.decrementBlackPieceCount();

        // then
        var actual = ReflectionTestUtils.getField(underTest, "blackPieces");
        assertThat(actual).isEqualTo(expectedCount);
    }

    private static Arguments[] testDecrementPieceCountSource() {
        return new Arguments[] {
                Arguments.of(12, 11),
                Arguments.of(0, 0),
        };
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void testAnalyzeWhileCannotEat(boolean isCanEat) {
        // given
        var user = mock(User.class);
        given(user.getColor()).willReturn(Color.WHITE);
        willCallRealMethod().given(user).setCanEat(anyBoolean());

        var whiteCell = mock(Cell.class);

        var emptyCell = mock(Cell.class);
        given(emptyCell.getPiece()).willReturn(null);

        var playerPiece = mock(Piece.class);
        given(playerPiece.getColor()).willReturn(Color.WHITE);
        given(playerPiece.isCanEat()).willReturn(isCanEat);

        var cellWithPlayerPiece = mock(Cell.class);
        given(cellWithPlayerPiece.getPiece()).willReturn(playerPiece);

        var board = mock(Board.class);
        var matrix = new Cell[8][8];
        for (Cell[] cells : matrix) {
            Arrays.fill(cells, emptyCell);
        }

        matrix[0][1] = whiteCell;
        matrix[0][2] = cellWithPlayerPiece;
        ReflectionTestUtils.setField(board, "board", matrix);
        willCallRealMethod().given(board).analyze(eq(user));

        // when
        board.analyze(user);

        // then
        var actual = ReflectionTestUtils.getField(user, "isCanEat");
        assertThat(actual).isEqualTo(isCanEat);
    }

    @ParameterizedTest
    @MethodSource("testGetCellWhileCellExistsSource")
    void testGetCellWhileCellExists(int row, int column) {
        // given
        var board = new Board();

        // when
        var actual = board.getCell(row, column);

        // then
        assertThat(actual).isNotNull();
    }

    private static Arguments[] testGetCellWhileCellExistsSource() {
        return new Arguments[] {
                Arguments.of(1, 5),
                Arguments.of(5, 1),
                Arguments.of(8, 5),
                Arguments.of(5, 8)
        };
    }

    @ParameterizedTest
    @MethodSource("testGetCellWhileCellDoesNotExistSource")
    void testGetCellWhileCellDoesNotExist(int row, int column) {
        // given
        var board = new Board();

        // when & then
        assertThatExceptionOfType(CellNotFoundException.class)
                .isThrownBy(() -> board.getCell(row, column));

    }

    private static Arguments[] testGetCellWhileCellDoesNotExistSource() {
        return new Arguments[] {
                Arguments.of(0, 1),
                Arguments.of(1, 0),
                Arguments.of(9, 1),
                Arguments.of(1, 9)
        };
    }
}
