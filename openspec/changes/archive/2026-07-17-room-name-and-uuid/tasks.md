## 1. Model — Room

- [x] 1.1 Изменить `Room.id` с `Int` на `UUID`
- [x] 1.2 Добавить поле `Room.name: String`

## 2. Repository

- [x] 2.1 Изменить `RoomRepository.findRoom(roomId: Int)` на `findRoom(roomId: UUID)`
- [x] 2.2 Изменить `RoomRepositoryImpl.roomStorage` с `MutableMap<Int, Room>` на `MutableMap<UUID, Room>`

## 3. Service

- [x] 3.1 Изменить `RoomService.findOrCreateRoomById(roomId: Int)` на `createRoom(name: String): Room` — всегда создаёт новую комнату с UUID
- [x] 3.2 Убрать `roomLocks` из `RoomServiceImpl` (блокировка по Int-ключу больше не нужна)
- [x] 3.3 Обновить `RoomServiceImpl.createRoom()` — генерировать UUID, принимать name

## 4. Controller

- [x] 4.1 Изменить `POST /room/create` — принимать `@RequestParam("name") String name` вместо `@RequestParam("roomId") Int`
- [x] 4.2 Изменить `POST /{roomId}/join` — `roomId` с `Int` на `UUID`
- [x] 4.3 Изменить `GET /{roomId}/board` — `roomId` с `Int` на `UUID`
- [x] 4.4 Обновить `session.setAttribute("roomId", ...)` — сохранять UUID

## 5. Templates (Thymeleaf)

- [x] 5.1 Изменить `create.html` — убрать поле `ID комнаты`, добавить поле `Название комнаты` (name)
- [x] 5.2 Изменить `room/index.html` — отображать `room.name` вместо `room.id` в таблице

## 6. Tests

- [x] 6.1 Обновить `RoomServiceImplTest` — использовать `Room(UUID, Board)` вместо `Room(Int, Board)`
- [x] 6.2 Обновить тесты `findOrCreateRoomById` под новую логику `createRoom`