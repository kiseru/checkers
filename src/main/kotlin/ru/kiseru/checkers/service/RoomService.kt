package ru.kiseru.checkers.service

import ru.kiseru.checkers.model.Color
import ru.kiseru.checkers.model.Room
import ru.kiseru.checkers.model.User

interface RoomService {

    /**
     * Находит комнату по ее идентификатору или создает новую если комнаты с таким идентификатором нет.
     *
     * @param roomId идентификатор комнаты
     * @return комната
     */
    fun findOrCreateRoomById(roomId: Int): Room

    /**
     * Добавляет игрока в комнату.
     *
     * @param room комната
     * @param user пользователь
     * @param color цвет шашек
     */
    fun addPlayer(room: Room, user: User, color: Color)

    /**
     * Возвращает победителя комнаты.
     *
     * @param room комната
     * @return победитель партии
     */
    fun getTurnOwner(room: Room): User?

    /**
     * Двигает шашку из [from] в [to].
     *
     * @param room комната
     * @param user пользователь
     * @param from местоположение пешки, которую необходимо передвинуть
     * @param to местополжение, куда необходимо передвинуть пешку
     */
    fun makeTurn(room: Room, user: User, from: String, to: String)
}
