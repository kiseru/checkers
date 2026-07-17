## Why

Long polling for board data (`GET /room/{roomId}/board`) does not work reliably. Clients either never receive board updates or receive them after significant delays, making the game unplayable in real-time.

## What Changes

- Fix the long polling endpoint to correctly wait for board version changes and return updated board data in a timely manner
- Fix the `DeferredResult` timeout handling to properly clean up resources and avoid thread leaks
- Fix the frontend error handling in the `subscribe` function (incorrect `=` instead of `===` comparison)
- Fix `BoardDto` to use a properly serializable collection type instead of `Sequence`
- Ensure proper thread safety between the move-making path and the long-polling path

## Capabilities

### New Capabilities

- `board-long-polling`: Reliable long polling mechanism for receiving real-time board updates when opponents make moves

### Modified Capabilities

<!-- No existing specs are being modified -->

## Impact

- `src/main/kotlin/ru/kiseru/checkers/controller/RoomController.kt` — Fix long polling endpoint logic
- `src/main/kotlin/ru/kiseru/checkers/controller/dto/BoardDto.kt` — Change `Sequence` to `List` for proper JSON serialization
- `src/main/kotlin/ru/kiseru/checkers/service/impl/BoardServiceImpl.kt` — Fix thread safety and resource cleanup
- `src/main/resources/static/js/script.js` — Fix frontend error handling