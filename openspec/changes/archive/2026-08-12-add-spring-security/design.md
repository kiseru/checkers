## Context

Проект — Spring Boot приложение «Шашки» на Kotlin. Сейчас аутентификация запроса к доске `/room/{roomId}/board` реализована вручную внутри [`RoomController.getRoomBoard()`](src/main/kotlin/ru/kiseru/checkers/controller/RoomController.kt:152): контроллер вручную декодирует `Authorization` заголовок через `extractLoginFromAuthorization` и проверяет существование пользователя через `UserRepository`. Остальные endpoint'ы не защищены. Требуется перенести защиту этого endpoint'а на стандартный механизм Spring Security (Basic Auth), оставив остальные endpoint'ы открытыми.

## Goals / Non-Goals

**Goals:**
- Подключить `spring-boot-starter-security`.
- Защитить только `/room/{roomId}/board` через Basic Auth; все остальные endpoint'ы остаются доступными без аутентификации.
- Перенести проверку учётных данных (login из `Basic` заголовка) с ручной логики в контроллере на `UserDetailsService`, основанный на `UserRepository`.
- Убрать ручную проверку `Authorization` из [`RoomController.getRoomBoard()`](src/main/kotlin/ru/kiseru/checkers/controller/RoomController.kt:152) и метод `extractLoginFromAuthorization`.

**Non-Goals:**
- Не добавлять полноценную систему паролей и хеширование (приложение не имеет паролей; аутентификация — только по login).
- Не защищать другие endpoint'ы.
- Не внедрять сессии безопасности, CSRF-защиту для форм и другие расширенные функции Spring Security, не требуемые задачей.
- Не писать тесты к контроллерам (тесты к контроллерам не пишутся в этом проекте).

## Decisions

**Решение 1: Базовая конфигурация `SecurityFilterChain` с `requestMatchers` для `/room/{roomId}/board`.**
Создать класс конфигурации (например, `SecurityConfig`), в котором определяется `SecurityFilterChain`. Цепочка: `httpBasic()` для Basic Auth, `csrf` отключён (приложение использует long-polling и не опирается на CSRF-токены), и правило доступа:
- `/room/{roomId}/board` → `authenticated()`
- все остальные запросы → `permitAll()`

При неудачной аутентификации Spring Security автоматически вернёт `401 Unauthorized`, что соответствует текущему поведению.

*Альтернатива:* `requestMatchers("/**").permitAll()` + точечная защита. Выбрано явное `authenticated()` для `/room/{roomId}/board`, поскольку это единственный защищаемый endpoint.

**Решение 2: `UserDetailsService` на основе `UserRepository`.**
Реализовать `UserDetailsService`, который по имени (login) ищет пользователя в `UserRepository` и возвращает `org.springframework.security.core.userdetails.User` (без пароля или с пустым/фиксированным паролем). Так как приложение аутентифицирует только по логину, пароль не проверяется строго; важно, что `loadUserByUsername` возвращает пользователя, если login существует, иначе выбрасывает `UsernameNotFoundException`.

*Альтернатива:* реализовать собственный `AuthenticationProvider`. `UserDetailsService` проще и является стандартным способом интеграции Spring Security с собственным хранилищем пользователей.

**Решение 3: Передача `UserDetailsService` через `AuthenticationManager` / конфигурацию.**
`AuthenticationManager` использует предоставленный `UserDetailsService` и `PasswordEncoder`. Так как пароль не используется, допустим `NoOpPasswordEncoder` (или пустой). Это позволяет Basic Auth фильтру проверять учётные данные против `UserRepository`.

**Решение 4: Обновление `RoomController`.**
Убрать `@RequestHeader(HttpHeaders.AUTHORIZATION)` из `getRoomBoard()`, метод `extractLoginFromAuthorization` и связанные проверки `userRepository.findUserByName`. Spring Security гарантирует, что запрос уже аутентифицирован. (При необходимости логирование имени пользователя можно получить через `Authentication`.)

## Risks / Trade-offs

- **Включение Spring Security может перехватывать статические ресурсы и все страницы** → В конфигурации все запросы, кроме `/room/{roomId}/board`, получают `permitAll()`, включая статику и Thymeleaf-страницы; требуется явно разрешить их.
- **`NoOpPasswordEncoder` снижает безопасность** → В приложении нет паролей, аутентификация только по логину; это допустимо для текущей модели. При введении паролей потребуется `BCryptPasswordEncoder`.
- **Отключение CSRF** → Приложение не использует токены CSRF (long-polling + формальные GET/POST), поэтому отключение приемлемо; риск низкий.
- **Изменение формата заголовка для клиента** → Клиентский скрипт уже отправляет `Basic <base64(login)>`; формат совместим со Spring Security.

## Migration Plan

1. Добавить зависимость `spring-boot-starter-security` в `build.gradle.kts`.
2. Создать `SecurityConfig` и `UserDetailsService`.
3. Обновить `RoomController`: убрать ручную аутентификацию.
4. Проверить клиентский код и полное поведение приложения.

## Open Questions

- Нужно ли логировать имя пользователя в `getRoomBoard()` после переноса аутентификации? (Предполагается — да, через `Authentication`.)