## Why

Сейчас аутентификация запроса к доске `/room/{roomId}/board` реализована вручную внутри контроллера ([`RoomController.getRoomBoard()`](src/main/kotlin/ru/kiseru/checkers/controller/RoomController.kt:152)). Это дублирование логики, которое сложно поддерживать и тестировать. Требуется перевести защиту endpoint'а на стандартный механизм Spring Security с применением Basic Auth на уровне HTTP-фильтра, оставив все остальные endpoint'ы открытыми.

## What Changes

- Добавить зависимость `spring-boot-starter-security` в [`build.gradle.kts`](build.gradle.kts).
- Создать конфигурацию Spring Security (`SecurityConfig`) с `SecurityFilterChain`, которая:
  - включает Basic Auth;
  - разрешает доступ ко **всем** endpoint'ам, **кроме** `/room/{roomId}/board`, который требует аутентификации;
  - для запросов без валидных учётных данных возвращает `401 Unauthorized`.
- Реализовать `UserDetailsService` на основе существующего `UserRepository`, чтобы Spring Security проверял учётные данные через пользовательский репозиторий.
- Убрать ручную проверку `Authorization`-заголовка и метод `extractLoginFromAuthorization` из [`RoomController.getRoomBoard()`](src/main/kotlin/ru/kiseru/checkers/controller/RoomController.kt:152), передав эту ответственность Spring Security.
- Обновить клиентский код (Basic Auth заголовок) при необходимости для совместимости со Spring Security.

## Capabilities

### New Capabilities
- `spring-security`: защита endpoint'а `/room/{roomId}/board` через Spring Security (Basic Auth), при этом все остальные endpoint'ы остаются доступными без аутентификации.

### Modified Capabilities
- `board-auth`: существующее требование аутентификации запроса доски через Basic Auth сохраняется, но реализация переносится с ручной проверки в контроллере на стандартный механизм Spring Security.

## Impact

- **Dependencies**: добавление `spring-boot-starter-security` в `build.gradle.kts`.
- **Code**: новый класс конфигурации безопасности, новая реализация `UserDetailsService`; изменение [`RoomController.kt`](src/main/kotlin/ru/kiseru/checkers/controller/RoomController.kt) (удаление ручной аутентификации).
- **Tests**: обновление `RoomControllerTest` и, при необходимости, добавление тестов на конфигурацию безопасности.
- **API**: поведение `/room/{roomId}/board` (401 при отсутствии/неверных учётных данных) сохраняется.