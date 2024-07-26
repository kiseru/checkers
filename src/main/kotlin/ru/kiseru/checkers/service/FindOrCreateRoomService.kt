package ru.kiseru.checkers.service

import ru.kiseru.checkers.model.Room

interface FindOrCreateRoomService {

    /**
     * Находит комнату по ее идентификатору или создает новую если комнаты с таким идентификатором нет.
     *
     * @param roomId идентификатор комнаты
     * @return комната
     */
    fun findOrCreateRoomById(roomId: Int): Room
}
