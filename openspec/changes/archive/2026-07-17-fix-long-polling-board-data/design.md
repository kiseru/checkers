## Context

The game uses long polling to deliver real-time board updates to clients. The current implementation has several issues:

1. **`BoardDto.pieces` uses `Sequence<PieceDto>`** — Kotlin `Sequence` is lazily evaluated. Jackson may not serialize it correctly, potentially returning empty or incomplete piece arrays.

2. **`DeferredResult` timeout leaves executor threads blocked** — When the 15-second timeout fires, the `DeferredResult` is completed with an error, but the executor thread remains blocked on `condition.await()`. This wastes thread pool resources and can cause stale threads to attempt setting results on already-completed `DeferredResult` instances.

3. **Frontend `=` instead of `===` in error handling** — The `catch` block uses `if (e.message = "Game finished")` which is an assignment, not a comparison. This causes ALL errors to redirect to `/game`, breaking the retry logic for timeouts and transient errors.

4. **No proper synchronization between move and poll paths** — `RoomServiceImpl.makeTurn` synchronizes on `room`, but the long polling path in `RoomController` reads the board and submits the wait task outside any synchronization. While the `ReentrantLock` in `BoardServiceImpl` provides some protection, the board reference could theoretically be stale.

## Goals / Non-Goals

**Goals:**
- Fix long polling so clients reliably receive board updates when opponents make moves
- Fix `BoardDto` to use `List` instead of `Sequence` for proper JSON serialization
- Fix `DeferredResult` timeout to properly interrupt waiting executor threads
- Fix frontend error handling (`=` → `===`)
- Ensure thread safety between move and poll paths

**Non-Goals:**
- Changing the overall architecture from long polling to WebSockets or SSE
- Adding authentication/authorization to the polling endpoint
- Performance optimization beyond fixing the existing mechanism

## Decisions

### Decision 1: Change `BoardDto.pieces` from `Sequence` to `List`

**Why:** `Sequence` in Kotlin is lazily evaluated. Jackson serializes collections by iterating over them, but `Sequence` doesn't implement `Collection` or `Iterable` in a way Jackson recognizes by default. This can result in empty arrays or serialization errors. Using `List` ensures eager evaluation and reliable serialization.

**Alternatives considered:**
- Keep `Sequence` and add a custom Jackson serializer — unnecessary complexity for no benefit
- Use `Array` — `List` is more idiomatic in Kotlin

### Decision 2: Interrupt executor thread on `DeferredResult` timeout

**Why:** When the `DeferredResult` times out, the executor thread is still blocked on `condition.await()`. By interrupting the thread, we allow it to wake up, catch `InterruptedException`, and exit cleanly. This frees the thread for other requests.

**How:** Store a reference to the `Future<?>` returned by `executor.submit()`, and call `future.cancel(true)` in the `onTimeout` callback. This interrupts the thread, causing `condition.await()` to throw `InterruptedException`.

**Alternatives considered:**
- Use `condition.await(timeout, unit)` with the same 15s timeout — would require tracking remaining time and doesn't integrate with `DeferredResult`'s timeout mechanism
- Ignore the issue — wastes thread pool resources and can cause stale result sets

### Decision 3: Fix frontend `=` to `===`

**Why:** The assignment operator `=` instead of strict equality `===` causes the catch block to always redirect to `/game` on any error, breaking the retry logic for timeouts and transient errors.

### Decision 4: Read board reference inside `synchronized(room)` block

**Why:** To ensure consistency between the board reference used for polling and the board being modified by moves, the board should be read while holding the same lock that `RoomServiceImpl.makeTurn` uses.

**How:** Move the `boardSearchByRoomIdService.find(roomId)` call inside a `synchronized(room)` block, or use the room service to provide a consistent board reference.

## Risks / Trade-offs

- **Thread interruption on timeout** → The `future.cancel(true)` interrupts the thread. If the thread is in `condition.await()`, it throws `InterruptedException` which is already caught. However, if the thread is between the wait and the `result.setResult()` call, the interrupt flag could cause issues. Mitigation: clear the interrupt flag after catching `InterruptedException`.

- **`synchronized(room)` in the polling path** → Adding synchronization to the polling path could cause contention if many clients poll the same room simultaneously. Mitigation: the synchronized block is very short (just reading the board reference), and the actual wait happens outside the synchronized block.

- **`List` vs `Sequence` memory** → `List` materializes all pieces in memory, while `Sequence` is lazy. For a checkers board with at most 24 pieces, this is negligible.