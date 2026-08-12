## Why

Эндпоинт `/room/{roomId}/board` (long-polling для получения актуального состояния доски) в настоящее время не проверяет аутентификацию пользователя — любой клиент, знающий `roomId`, может подписаться на доску комнаты. Необходимо, чтобы клиент передавал идентификатор пользователя, и сервер проверял его наличие.

## What Changes

- Клиентский скрипт [`script.js`](src/main/resources/static/js/script.js) при запросе к `/room/{roomId}/board` добавляет заголовок `Authorization` со схемой Basic Auth, кодируя логин пользователя в `base64` (формат `Basic <base64(login)>`).
- Серверный контроллер [`RoomController.getRoomBoard()`](src/main/kotlin/ru/kiseru/checkers/controller/RoomController.kt:149) начинает читать заголовок `Authorization`, декодировать его и проверять, что указанный пользователь существует.

## Capabilities

### New Capabilities
- `board-auth`: Требование к эндпоинту `/room/{roomId}/board` принимать и проверять заголовок `Authorization` с Basic Auth при отправке запроса на получение доски.

### Modified Capabilities
<!-- No existing specs are affected. -->

## Impact

- Фронтенд: [`script.js`](src/main/resources/static/js/script.js) — функция `subscribe`, добавляющая заголовок `Authorization`.
- Бэкенд: [`RoomController.getRoomBoard()`](src/main/kotlin/ru/kiseru/checkers/controller/RoomController.kt:149) — чтение и валидация заголовка `Authorization`.
- Возможная модификация шаблона [`game.html`](src/main/resources/templates/game.html), если значение логина передаётся в скрипт.
- Тесты: покрытие обработки заголовка `Authorization` в контроллере.