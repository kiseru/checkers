## 1. Backend: пользователь по имени

- [x] 1.1 Добавить метод `findUserByName(name: String): User?` в интерфейс [`UserRepository`](src/main/kotlin/ru/kiseru/checkers/repository/UserRepository.kt)
- [x] 1.2 Реализовать `findUserByName` в [`UserRepositoryImpl`](src/main/kotlin/ru/kiseru/checkers/repository/impl/UserRepositoryImpl.kt) (поиск по `name` в `userStorage`)

## 2. Backend: обработка Authorization в контроллере

- [x] 2.1 В [`RoomController.getRoomBoard()`](src/main/kotlin/ru/kiseru/checkers/controller/RoomController.kt:149) добавить параметр заголовка `Authorization` (через `@RequestHeader`)
- [x] 2.2 Извлечь и декодировать логин из заголовка `Basic <base64(login)>`
- [x] 2.3 Проверить существование пользователя через `userRepository.findUserByName`; при отсутствии/невалидном заголовке вернуть `401 Unauthorized` через `ResponseStatusException`

## 3. Frontend: заголовок Authorization

- [x] 3.1 В [`subscribe()`](src/main/resources/static/js/script.js:70) добавлять заголовок `Authorization: Basic <base64(login)>` к fetch-запросу `/room/{roomId}/board`
- [x] 3.2 Корректно кодировать логин в base64 с поддержкой UTF-8 (например, через `unescape(encodeURIComponent(...))` перед `btoa`)

## 4. Тесты

- [x] 4.1 Добавить тесты для `UserRepositoryImpl.findUserByName`