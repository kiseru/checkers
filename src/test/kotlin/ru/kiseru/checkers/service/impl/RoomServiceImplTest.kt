package ru.kiseru.checkers.service.impl

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.eq
import org.mockito.kotlin.given
import ru.kiseru.checkers.model.Board
import ru.kiseru.checkers.model.Color
import ru.kiseru.checkers.model.Room
import ru.kiseru.checkers.model.User
import ru.kiseru.checkers.repository.RoomRepository
import ru.kiseru.checkers.service.BoardService
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class RoomServiceImplTest {

    @InjectMocks
    private lateinit var underTest: RoomServiceImpl

    @Mock
    private lateinit var roomRepository: RoomRepository

    @Mock
    private lateinit var boardService: BoardService

    @ParameterizedTest
    @EnumSource(Color::class)
    fun `addPlayerTurn test`(color: Color) {
        // given
        val board = Board(UUID.randomUUID())
        val room = Room(1, board)
        val user = User("name")

        // when
        underTest.addPlayer(room, user, color)

        // then
        assertThat(user.color).isEqualTo(color)
        when (color) {
            Color.WHITE -> {
                assertThat(room.whitePlayer).isSameAs(user)
                assertThat(room.blackPlayer).isNull()
            }

            Color.BLACK -> {
                assertThat(room.blackPlayer).isSameAs(user)
                assertThat(room.whitePlayer).isNull()
            }
        }
    }

    @ParameterizedTest
    @EnumSource(Color::class)
    fun `getTurnOwner test`(color: Color) {
        // given
        val board = Board(UUID.randomUUID())
        val whitePlayer = User("white player")
        val blackPlayer = User("black player")

        val room = Room(1, board)
        room.whitePlayer = whitePlayer
        room.blackPlayer = blackPlayer
        room.turn = color

        // when
        val actual = underTest.getTurnOwner(room)

        // then
        val expected = when (color) {
            Color.WHITE -> whitePlayer
            Color.BLACK -> blackPlayer
        }
        assertThat(actual).isSameAs(expected)
    }

    @Test
    fun `makeTurn test when white player is not assigned`() {
        // given
        val board = Board(UUID.randomUUID())
        val room = Room(1, board)
        val user = User("name")
        user.id = UUID.randomUUID()

        // when
        underTest.makeTurn(room, user, "a1", "b1")

        // then
        assertThat(room.whitePlayer).isSameAs(user)
        assertThat(room.blackPlayer).isNull()
        assertThat(room.turn).isEqualTo(Color.WHITE)
    }

    @Test
    fun `makeTurn test when black player is not assigned for assigned user`() {
        // given
        val board = Board(UUID.randomUUID())
        val user = User("name")
        user.id = UUID.randomUUID()

        val room = Room(1, board)
        room.whitePlayer = user

        // when
        underTest.makeTurn(room, user, "a1", "b1")

        // then
        assertThat(room.whitePlayer).isSameAs(user)
        assertThat(room.blackPlayer).isNull()
        assertThat(room.turn).isEqualTo(Color.WHITE)
    }

    @Test
    fun `makeTurn test when black player is not assigned for new user`() {
        // given
        val board = Board(UUID.randomUUID())

        val whitePlayer = User("name")
        whitePlayer.id = UUID.randomUUID()

        val blackPlayer = User("black player")
        blackPlayer.id = UUID.randomUUID()

        val room = Room(1, board)
        room.whitePlayer = whitePlayer

        // when
        underTest.makeTurn(room, blackPlayer, "a1", "b1")

        // then
        assertThat(room.whitePlayer).isSameAs(whitePlayer)
        assertThat(room.blackPlayer).isSameAs(blackPlayer)
        assertThat(room.turn).isEqualTo(Color.WHITE)
    }

    @Test
    fun `makeTurn test when the turn is not user's`() {
        // given
        val board = Board(UUID.randomUUID())

        val whitePlayer = User("name")
        whitePlayer.id = UUID.randomUUID()

        val blackPlayer = User("black player")
        blackPlayer.id = UUID.randomUUID()

        val room = Room(1, board)
        room.whitePlayer = whitePlayer
        room.blackPlayer = blackPlayer

        // when
        underTest.makeTurn(room, blackPlayer, "a1", "b1")

        // then
        assertThat(room.whitePlayer).isSameAs(whitePlayer)
        assertThat(room.blackPlayer).isSameAs(blackPlayer)
        assertThat(room.turn).isEqualTo(Color.WHITE)
    }

    @Test
    fun `makeTurn test when source cell is unknown`() {
        // given
        val board = Board(UUID.randomUUID())

        val whitePlayer = User("name")
        whitePlayer.id = UUID.randomUUID()

        val blackPlayer = User("black player")
        blackPlayer.id = UUID.randomUUID()

        val room = Room(1, board)
        room.whitePlayer = whitePlayer
        room.blackPlayer = blackPlayer

        // when
        underTest.makeTurn(room, whitePlayer, null, "b1")

        // then
        assertThat(room.whitePlayer).isSameAs(whitePlayer)
        assertThat(room.blackPlayer).isSameAs(blackPlayer)
        assertThat(room.turn).isEqualTo(Color.WHITE)
    }

    @Test
    fun `makeTurn test when destination cell is unknown`() {
        // given
        val board = Board(UUID.randomUUID())

        val whitePlayer = User("name")
        whitePlayer.id = UUID.randomUUID()

        val blackPlayer = User("black player")
        blackPlayer.id = UUID.randomUUID()

        val room = Room(1, board)
        room.whitePlayer = whitePlayer
        room.blackPlayer = blackPlayer

        // when
        underTest.makeTurn(room, whitePlayer, "a1", null)

        // then
        assertThat(room.whitePlayer).isSameAs(whitePlayer)
        assertThat(room.blackPlayer).isSameAs(blackPlayer)
        assertThat(room.turn).isEqualTo(Color.WHITE)
    }

    @ParameterizedTest
    @EnumSource(Color::class)
    fun `makeTurn test without eating`(color: Color) {
        // given
        val board = Board(UUID.randomUUID())

        val whitePlayer = User("white player")
        whitePlayer.id = UUID.randomUUID()
        whitePlayer.color = Color.WHITE

        val blackPlayer = User("black player")
        blackPlayer.id = UUID.randomUUID()
        blackPlayer.color = Color.BLACK

        val room = Room(1, board)
        room.whitePlayer = whitePlayer
        room.blackPlayer = blackPlayer
        room.turn = color

        val sourceCell = "a1"
        val destinationCell = "b2"
        given(boardService.makeTurn(eq(board), eq(color), eq(sourceCell), eq(destinationCell)))
            .willReturn(false)

        // when
        val player = when (color) {
            Color.WHITE -> whitePlayer
            Color.BLACK -> blackPlayer
        }
        underTest.makeTurn(room, player, sourceCell, destinationCell)

        // then
        assertThat(room.whitePlayer).isSameAs(whitePlayer)
        assertThat(room.blackPlayer).isSameAs(blackPlayer)
        val expectedTurn = when (color) {
            Color.WHITE -> Color.BLACK
            Color.BLACK -> Color.WHITE
        }
        assertThat(room.turn).isEqualTo(expectedTurn)
    }

    @ParameterizedTest
    @EnumSource(Color::class)
    fun `makeTurn test with eating and after that user can eat again`(color: Color) {
        // given
        val board = Board(UUID.randomUUID())

        val whitePlayer = User("white player")
        whitePlayer.id = UUID.randomUUID()
        whitePlayer.color = Color.WHITE

        val blackPlayer = User("black player")
        blackPlayer.id = UUID.randomUUID()
        blackPlayer.color = Color.BLACK

        val room = Room(1, board)
        room.whitePlayer = whitePlayer
        room.blackPlayer = blackPlayer
        room.turn = color

        val sourceCell = "a1"
        val destinationCell = "b2"
        given(boardService.makeTurn(eq(board), eq(color), eq(sourceCell), eq(destinationCell)))
            .willReturn(true)

        // when
        val player = when (color) {
            Color.WHITE -> whitePlayer
            Color.BLACK -> blackPlayer
        }
        underTest.makeTurn(room, player, sourceCell, destinationCell)

        // then
        assertThat(room.whitePlayer).isSameAs(whitePlayer)
        assertThat(room.blackPlayer).isSameAs(blackPlayer)
        assertThat(room.turn).isEqualTo(color)
    }

    @Test
    fun `findOrCreateRoomById test when room exists`() {
        // given
        val board = Board(UUID.randomUUID())

        val roomId = 1
        val room = Room(roomId, board)

        given { roomRepository.findRoom(roomId) }
            .willReturn(room)

        // when
        val actual = underTest.findOrCreateRoomById(roomId)

        // then
        assertThat(actual).isSameAs(room)
    }

    @Test
    fun `findOrCreateRoomById test when room doesn't exist`() {
        // given
        val roomId = 1

        given { roomRepository.findRoom(roomId) }
            .willReturn(null)

        // when
        val actual = underTest.findOrCreateRoomById(roomId)

        // then
        assertThat(actual.id).isSameAs(roomId)
    }
}
