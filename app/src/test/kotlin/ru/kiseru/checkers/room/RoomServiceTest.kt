package ru.kiseru.checkers.room

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.mock
import org.springframework.test.util.ReflectionTestUtils
import ru.kiseru.checkers.domain.room.Room
import ru.kiseru.checkers.room.impl.RoomServiceImpl

@ExtendWith(MockitoExtension::class)
class RoomServiceTest {

    private val underTest = RoomServiceImpl()

    private lateinit var rooms: MutableMap<Int, Room>

    @BeforeEach
    fun setUp() {
        rooms = ReflectionTestUtils.getField(underTest, "rooms") as MutableMap<Int, Room>
    }

    @Test
    fun `findOrCreateRoom test when room doesn't exist`() {
        // when
        val actual = underTest.findOrCreateRoomById(1)

        // then
        assertThat(actual).isNotNull()
        assertThat(actual.id).isEqualTo(1)
        assertThat(rooms.containsKey(1)).isTrue()
    }

    @Test
    fun `findOrCreateRoom test when room exists`() {
        // given
        val room = mock<Room>()
        rooms[1] = room

        // when
        val actual = underTest.findOrCreateRoomById(1)

        // then
        assertThat(actual).isSameAs(room)
    }
}
