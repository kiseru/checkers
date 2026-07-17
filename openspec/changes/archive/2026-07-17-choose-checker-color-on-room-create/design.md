## Context

Currently, when a user creates or joins a room, the `RoomServiceImpl.makeTurn()` method auto-assigns the first player to WHITE (lines 48-49). The room creation page (`room/create.html`) only has a room ID input field with no color selection. The `RoomController.findRoom()` POST handler only accepts `roomId` as a parameter.

The flow is:
1. User logs in → redirected to `/room/create` (GET)
2. User enters room ID → POST `/room/create` with `roomId` → stored in session → redirect to `/game`
3. `GameController.getGamePage()` → `roomService.findOrCreateRoomById(roomId)` → creates room if not exists
4. `roomService.makeTurn()` auto-assigns the caller to WHITE if no white player exists

The change needs to let the user pick their color at step 2 and have it properly assigned at step 4.

## Goals / Non-Goals

**Goals:**
- Allow the user to select white or black checkers when creating/joining a room
- Pass the selected color through the room creation flow to the game page
- Assign the room creator to the chosen color
- The second player who joins automatically gets the opposite color
- Maintain backward compatibility (existing rooms continue to work)

**Non-Goals:**
- Changing the color after the room is created (mid-game color switching)
- Spectator mode or multi-player (more than 2 players)
- Color selection for the second player (they automatically get the opposite color)
- UI redesign beyond adding a color selector

## Decisions

### Decision 1: Pass color as a request parameter in POST `/room/create`
- **Option A**: Add `color` as a `@RequestParam` in `RoomController.findRoom()`, store it in the HTTP session alongside `roomId`
- **Option B**: Pass color as a query parameter in the redirect URL to `/game`
- **Option C**: Store color in a separate session attribute
- **Chosen**: **Option A + C** — Accept `color` as a request parameter and store it in the session as `color`. This keeps the URL clean (no query params in redirect) and makes the color available to `GameController` via session attribute. The session is the natural place for per-user game state.

### Decision 2: Assign color in `GameController` instead of `RoomServiceImpl.makeTurn()`
- **Option A**: Modify `makeTurn()` to accept an optional color parameter
- **Option B**: Assign color in `GameController` before calling `makeTurn()`, using the session-stored color
- **Chosen**: **Option B** — The `makeTurn()` method currently handles both player assignment AND move execution, which is a mixed responsibility. By assigning color in `GameController` before calling `makeTurn()`, we keep the service method focused on move logic and avoid overloading it with room-joining concerns. We'll extract a dedicated `joinRoom()` or `assignPlayer()` call that `GameController` invokes before `makeTurn()`.

### Decision 3: Frontend color selector
- **Option A**: Radio buttons (White / Black)
- **Option B**: Dropdown select
- **Option C**: Two buttons ("Play as White" / "Play as Black")
- **Chosen**: **Option A** — Radio buttons are the most intuitive for a binary choice. They clearly show both options and require one click to select. The default selection will be White to maintain backward compatibility.

## Risks / Trade-offs

- **[Risk] Session-based color could be stale**: If a user opens multiple tabs with different colors, the last submitted color wins. → **Mitigation**: This is an edge case; the game only allows one active room per session anyway.
- **[Risk] Backward compatibility**: Existing bookmarks or direct POST requests without `color` parameter. → **Mitigation**: Make `color` optional with a default of `WHITE`.
- **[Trade-off] Color assignment in controller vs service**: Moving color assignment to the controller adds some logic to the web layer but keeps the service cleaner. The service's `makeTurn()` already has mixed concerns (player assignment + move execution), so this is a step toward separation.