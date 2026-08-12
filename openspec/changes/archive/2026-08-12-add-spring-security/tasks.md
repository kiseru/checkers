## 1. Зависимости и конфигурация

- [x] 1.1 Добавить зависимость `spring-boot-starter-security` в [`build.gradle.kts`](build.gradle.kts)

## 2. Конфигурация безопасности

- [x] 2.1 Создать класс `SecurityConfig` с `SecurityFilterChain`: включить `httpBasic()`, отключить `csrf`, разрешить доступ ко всем запросам (`permitAll`), кроме `/room/{roomId}/board`, для которого требуется `authenticated()`
- [x] 2.2 Создать реализацию `UserDetailsService`, которая ищет пользователя по логину в `UserRepository` и возвращает `UserDetails` (или бросает `UsernameNotFoundException`), и зарегистрировать её в `AuthenticationManager`
- [x] 2.3 Настроить `PasswordEncoder` (`NoOpPasswordEncoder`), так как пароль в приложении не используется

## 3. Рефакторинг контроллера

- [x] 3.1 Убрать `@RequestHeader(HttpHeaders.AUTHORIZATION)` и параметр `authorization` из [`RoomController.getRoomBoard()`](src/main/kotlin/ru/kiseru/checkers/controller/RoomController.kt:152)
- [x] 3.2 Убрать ручную проверку пользователя через `userRepository.findUserByName` в `getRoomBoard()`
- [x] 3.3 Удалить метод `extractLoginFromAuthorization` из [`RoomController.kt`](src/main/kotlin/ru/kiseru/checkers/controller/RoomController.kt)
- [x] 3.4 Убрать неиспользуемые импорты (`HttpHeaders`, `Base64`, `UserRepository`) из [`RoomController.kt`](src/main/kotlin/ru/kiseru/checkers/controller/RoomController.kt)

## 4. Проверка и запуск

- [x] 4.1 Собрать проект (`./gradlew build`), убедиться, что приложение запускается с Spring Security
- [x] 4.2 Проверить, что `/room/{roomId}/board` возвращает `401` без заголовка и работает с валидным `Basic` заголовком, а остальные endpoint'ы доступны без аутентификации