package ru.kiseru.checkers.operational

import ru.kiseru.checkers.domain.room.Room

interface FindOrCreateRoomUseCase {

    /**
     * Находит комнату по ее идентификатору или создает новую если комнаты с таким идентификатором нет.
     *
     * @param roomId идентификатор комнаты
     * @return комната
     */
    fun findOrCreateRoomById(roomId: Int): Room
}
