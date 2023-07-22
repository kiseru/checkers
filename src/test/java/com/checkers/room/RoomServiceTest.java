package com.checkers.room;

import com.checkers.room.impl.RoomServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    private final RoomService underTest = new RoomServiceImpl();

    private Map<Integer, Room> rooms;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        rooms = (Map<Integer, Room>) ReflectionTestUtils.getField(underTest, "rooms");
    }

    @Test
    void testFindOrCreateRoomWhileRoomDoesNotExist() {
        // when
        var actual = underTest.findOrCreateRoomById(1);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(1);
        assertThat(rooms.containsKey(1)).isTrue();
    }

    @Test
    void testFindOrCreateRoomWhileRoomExists() {
        // given
        var room = mock(Room.class);
        rooms.put(1, room);

        // when
        var actual = underTest.findOrCreateRoomById(1);

        // then
        assertThat(actual).isSameAs(room);
    }
}