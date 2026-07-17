## 1. Backend: Repository Layer

- [x] 1.1 Add `findRoomsWithAvailableSlot()` method to `RoomRepository` interface
- [x] 1.2 Implement `findRoomsWithAvailableSlot()` in `RoomRepositoryImpl` — filter rooms where `whitePlayer == null || blackPlayer == null`

## 2. Backend: Service Layer

- [x] 2.1 Add `getAvailableRooms()` method to `RoomService` interface
- [x] 2.2 Implement `getAvailableRooms()` in `RoomServiceImpl` — delegate to `roomRepository.findRoomsWithAvailableSlot()`

## 3. Backend: Controller Layer

- [x] 3.1 Add `GET /rooms` endpoint in `RoomController` — returns `room/index` view with list of available rooms
- [x] 3.2 Add `POST /rooms/{roomId}/join` endpoint in `RoomController` — assigns free color automatically and redirects to `/game`

## 4. Frontend: Templates

- [x] 4.1 Create `/templates/room/index.html` — page with "Create Game" button and list of rooms with "Join" buttons
- [x] 4.2 Update navigation: add link to `/rooms` from relevant pages (e.g., `/login` or `/`)

## 5. Verification

- [x] 5.1 Build and run the application — verify no compilation errors
- [x] 5.2 Manual test: create a room, verify it appears in the list
- [x] 5.3 Manual test: join a room from the list, verify redirect to game
- [x] 5.4 Manual test: verify "Create Game" button redirects to `/room/create`