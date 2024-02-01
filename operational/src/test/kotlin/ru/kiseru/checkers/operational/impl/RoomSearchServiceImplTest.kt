package ru.kiseru.checkers.operational.impl

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.eq
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import ru.kiseru.checkers.domain.board.Board
import ru.kiseru.checkers.domain.room.Room
import ru.kiseru.checkers.domain.room.RoomRepository

@ExtendWith(MockitoExtension::class)
class RoomSearchServiceImplTest {

    @InjectMocks
    lateinit var underTest: RoomSearchServiceImpl

    @Mock
    lateinit var roomRepository: RoomRepository

    @Test
    fun `test room search when available`() {
        // given
        val room = Room(1, Board())
        given(roomRepository.findRoom(eq(1)))
            .willReturn(room)

        // when
        val actual = underTest.find(1)

        // then
        assertThat(actual).isNotNull
        assertThat(actual?.id).isEqualTo(1)
    }

    @Test
    fun `test room search when unavailable`() {
        // given
        given(roomRepository.findRoom(eq(1)))
            .willReturn(null)

        // when
        val actual = underTest.find(1)

        // then
        assertThat(actual).isNull()
    }
}
