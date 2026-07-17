## ADDED Requirements

### Requirement: User can view list of rooms with available slots
The system SHALL display a page at `/rooms` listing all rooms where at least one player slot (white or black) is available. Each room SHALL show its ID and the occupied/free slots. The page SHALL be accessible only to authenticated users (with `uid` in session).

#### Scenario: Authenticated user views room list
- **WHEN** an authenticated user navigates to `/rooms`
- **THEN** the system displays a page with a list of rooms that have at least one free slot
- **AND** each room entry shows the room ID and which slots are free

#### Scenario: Unauthenticated user tries to view room list
- **WHEN** an unauthenticated user navigates to `/rooms`
- **THEN** the system redirects to `/login`

#### Scenario: No rooms with available slots exist
- **WHEN** an authenticated user navigates to `/rooms` and no rooms have free slots
- **THEN** the system displays an empty list with a message "No available rooms"

### Requirement: User can join a room from the list
The system SHALL provide a "Join" button for each room in the list. When clicked, the system SHALL add the user to the available slot and redirect to the game page. The color SHALL be assigned automatically based on which slot is free.

#### Scenario: User joins a room with free white slot
- **WHEN** an authenticated user clicks "Join" on a room where only the white slot is free
- **THEN** the system assigns the user the WHITE color
- **AND** redirects the user to `/game`

#### Scenario: User joins a room with free black slot
- **WHEN** an authenticated user clicks "Join" on a room where only the black slot is free
- **THEN** the system assigns the user the BLACK color
- **AND** redirects the user to `/game`

#### Scenario: User joins a room with both slots free
- **WHEN** an authenticated user clicks "Join" on a room where both slots are free
- **THEN** the system assigns the user the WHITE color (default)
- **AND** redirects the user to `/game`

#### Scenario: User tries to join a room that became full
- **WHEN** an authenticated user clicks "Join" on a room that has become full between page load and the join request
- **THEN** the system returns an error message indicating the room is no longer available

### Requirement: User can navigate to create room page
The system SHALL display a "Create Game" button above the room list. When clicked, the system SHALL redirect the user to `/room/create`.

#### Scenario: User clicks Create Game button
- **WHEN** an authenticated user clicks "Create Game" button on the `/rooms` page
- **THEN** the system redirects the user to `/room/create`