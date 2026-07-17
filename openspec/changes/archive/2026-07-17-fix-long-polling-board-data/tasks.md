## 1. Fix `BoardDto` serialization

- [x] 1.1 Change `BoardDto.pieces` type from `Sequence<PieceDto>` to `List<PieceDto>`
- [x] 1.2 Update `RoomController.getRoomBoard` to materialize the pieces sequence to a list with `.toList()`

## 2. Fix `DeferredResult` timeout to interrupt executor thread

- [x] 2.1 Store `Future<?>` reference from `executor.submit()` in `RoomController.getRoomBoard`
- [x] 2.2 Call `future.cancel(true)` in the `onTimeout` callback to interrupt the waiting thread
- [x] 2.3 Ensure `InterruptedException` is properly caught and handled in the executor task

## 3. Fix frontend error handling

- [x] 3.1 Change `if (e.message = "Game finished")` to `if (e.message === "Game finished")` in `script.js`

## 4. Fix thread safety between move and poll paths

- [x] 4.1 Move the board lookup inside a `synchronized(room)` block in `RoomController.getRoomBoard` to ensure consistent board reference

## 5. Verify the fix

- [x] 5.1 Build and run the application
- [ ] 5.2 Open two browser windows, join the same room, and verify that moves made by one player appear in real-time on the other player's board
- [ ] 5.3 Verify that timeout responses (408) are handled correctly and the client retries
- [ ] 5.4 Verify that the game finish detection still works correctly