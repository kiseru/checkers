package ru.kiseru.checkers.room

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.eq
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import ru.kiseru.checkers.domain.room.Room
import ru.kiseru.checkers.domain.room.RoomRepository
import ru.kiseru.checkers.room.impl.RoomServiceImpl

@ExtendWith(MockitoExtension::class)
class RoomServiceTest {

    @InjectMocks
    lateinit var underTest: RoomServiceImpl

    @Mock
    lateinit var roomRepository: RoomRepository

    @Test
    fun `findOrCreateRoom test when room doesn't exist`() {
        // when
        val actual = underTest.findOrCreateRoomById(1)

        // then
        assertThat(actual).isNotNull()
        assertThat(actual.id).isEqualTo(1)
    }

    @Test
    fun `findOrCreateRoom test when room exists`() {
        // given
        val room = mock<Room>()
        
        given { roomRepository.findRoom(eq(1)) }
            .willReturn(room)

        // when
        val actual = underTest.findOrCreateRoomById(1)

        // then
        assertThat(actual).isSameAs(room)
    }
}
