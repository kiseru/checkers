package ru.kiseru.checkers.web.controllers

import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.BDDMockito.eq
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ru.kiseru.checkers.domain.board.Board
import ru.kiseru.checkers.domain.room.Room
import ru.kiseru.checkers.operational.RoomSearchService

@WebMvcTest(RoomController::class)
class RoomControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var roomSearchService: RoomSearchService

    @Test
    fun `test room search when available`() {
        // given
        val room = Room(1, Board())
        given(roomSearchService.find(anyInt()))
            .willReturn(room)

        // when & then
        mockMvc.perform(get("/room/{roomId}/piece", 1))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    }

    @Test
    fun `test room search when unavailable`() {
        // given
        given(roomSearchService.find(eq(1)))
            .willReturn(null)

        // when & then
        mockMvc.perform(get("/room/{roomId}/piece", 1))
            .andExpect(status().isNotFound)
    }
}
