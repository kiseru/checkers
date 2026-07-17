## ADDED Requirements

### Requirement: User can select checker color when creating a room
The room creation page SHALL provide a color selector allowing the user to choose between WHITE and BLACK checkers before joining a room.

#### Scenario: User selects white color
- **WHEN** the user opens the room creation page
- **THEN** the page SHALL display radio buttons or similar UI for selecting "White" or "Black" checkers
- **AND** "White" SHALL be the default selected option

#### Scenario: User selects black color
- **WHEN** the user selects "Black" on the room creation form
- **AND** submits the form with a room ID
- **THEN** the system SHALL assign the user to the black player slot in the room

### Requirement: System assigns the room creator to the selected color
When a user creates or joins a room, the system SHALL assign them to the color they selected on the room creation form.

#### Scenario: First player joins as white
- **WHEN** a user creates a room with color WHITE
- **AND** no other player has joined yet
- **THEN** the user SHALL be assigned as the white player
- **AND** the next player to join SHALL be assigned as the black player

#### Scenario: First player joins as black
- **WHEN** a user creates a room with color BLACK
- **AND** no other player has joined yet
- **THEN** the user SHALL be assigned as the black player
- **AND** the next player to join SHALL be assigned as the white player

### Requirement: Second player automatically gets the opposite color
When a second player joins a room, the system SHALL automatically assign them the color opposite to the first player's color.

#### Scenario: Second player joins after white creator
- **WHEN** the first player joined as WHITE
- **AND** a second player joins the same room
- **THEN** the second player SHALL be automatically assigned as BLACK

#### Scenario: Second player joins after black creator
- **WHEN** the first player joined as BLACK
- **AND** a second player joins the same room
- **THEN** the second player SHALL be automatically assigned as WHITE

### Requirement: Color parameter is optional with default WHITE
The system SHALL accept room creation requests without an explicit color parameter and default to WHITE for backward compatibility.

#### Scenario: No color specified
- **WHEN** a user submits the room creation form without selecting a color
- **THEN** the system SHALL default to assigning the user as WHITE