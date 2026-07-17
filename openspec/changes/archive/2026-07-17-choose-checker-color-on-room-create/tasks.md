## 1. Frontend: Add color selector to room creation page

- [x] 1.1 Add radio button group for color selection (White/Black) to `room/create.html` with White as default
- [x] 1.2 Ensure the color value is submitted with the form as a `color` parameter

## 2. Backend: Accept color parameter in room creation endpoint

- [x] 2.1 Add `@RequestParam("color") color: String?` parameter to `RoomController.findRoom()` method
- [x] 2.2 Parse the color string to `Color` enum (default to `Color.WHITE` if null or invalid)
- [x] 2.3 Store the selected color in the HTTP session as `color` attribute

## 3. Backend: Assign player to selected color in game flow

- [x] 3.1 Modify `GameController.getGamePage()` to read the `color` session attribute
- [x] 3.2 Before calling `roomService.makeTurn()`, call `roomService.addPlayer(currentRoom, currentUser, selectedColor)` if the user hasn't been assigned a color yet
- [x] 3.3 Modify `RoomServiceImpl.makeTurn()` to skip auto-assignment of WHITE when the player is already assigned (remove lines 48-49 that auto-assign white to the first caller)

## 4. Verify and test

- [x] 4.1 Verify that creating a room with White selected assigns the creator as white player
- [x] 4.2 Verify that creating a room with Black selected assigns the creator as black player
- [x] 4.3 Verify that the second player joining gets the opposite color
- [x] 4.4 Verify backward compatibility — requests without `color` parameter default to White