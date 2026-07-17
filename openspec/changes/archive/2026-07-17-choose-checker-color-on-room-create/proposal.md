## Why

Currently, when a user creates or joins a room, they are always assigned white checkers. The user who creates the room has no way to choose their preferred color, forcing them to play as white even if they prefer black. This limits player choice and makes the game less flexible.

## What Changes

- Add a color selection option (White/Black) to the room creation form
- Pass the selected color through the room creation flow
- Assign the room creator to the chosen color instead of always defaulting to white
- The second player who joins the room automatically gets the opposite color
- Update the backend to accept and handle the color parameter during room creation/joining

## Capabilities

### New Capabilities
- `color-selection-on-room-create`: Allow the user to choose their checker color (white or black) when creating or joining a room

### Modified Capabilities

<!-- No existing specs to modify -->

## Impact

- **Frontend**: Room creation page (`room/create.html`) — add a color selector (radio buttons or dropdown)
- **Controller**: `RoomController` — accept a `color` parameter in the POST `/room/create` handler
- **Service**: `RoomService` — `findOrCreateRoomById` or `makeTurn` logic needs to handle explicit color assignment instead of auto-assigning white to the first player
- **Session/Flow**: The selected color needs to be passed through the redirect to `/game` so the `GameController` can assign the correct color