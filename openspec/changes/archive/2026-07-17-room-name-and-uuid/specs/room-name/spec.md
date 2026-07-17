## ADDED Requirements

### Requirement: Room name
The system SHALL allow users to specify a name for the room when creating it. The name is a free-form text string.

#### Scenario: User creates a room with a name
- **WHEN** user fills in the room name field and submits the create room form
- **THEN** a new room is created with the specified name and a randomly generated UUID

#### Scenario: User creates a room with an empty name
- **WHEN** user submits the create room form with an empty name field
- **THEN** the system SHALL reject the request and show a validation error

#### Scenario: Room name is displayed in the room list
- **WHEN** user views the available rooms page
- **THEN** each room SHALL display its name instead of its numeric ID

### Requirement: Room UUID
The system SHALL use UUID as the room identifier instead of an integer. The UUID SHALL be generated automatically by the server when the room is created.

#### Scenario: Room is created with a UUID
- **WHEN** a new room is created
- **THEN** the room SHALL have a unique UUID assigned automatically

#### Scenario: Room is accessed by UUID
- **WHEN** a user joins a room from the room list
- **THEN** the system SHALL use the room's UUID to look up the room

#### Scenario: Board polling uses UUID
- **WHEN** the game page polls for board updates
- **THEN** the request SHALL use the room's UUID as the identifier