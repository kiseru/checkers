package ru.kiseru.checkers.service

import ru.kiseru.checkers.model.Color
import ru.kiseru.checkers.model.Room
import ru.kiseru.checkers.model.User

interface RoomService {

    /**
     * Возвращает список комнат, в которых есть свободное место для игрока.
     *
     * @return список комнат со свободными местами
     */
    fun getAvailableRooms(): List<Room>

    /**
     * Создаёт новую комнату с указанным названием и автоматически сгенерированным UUID.
     *
     * @param name название комнаты
     * @return созданная комната
     */
    fun createRoom(name: String): Room

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
    fun makeTurn(room: Room, user: User, from: String?, to: String?)
}
