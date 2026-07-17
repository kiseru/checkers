## ADDED Requirements

### Requirement: Client receives board updates via long polling
The system SHALL provide a long polling endpoint that allows clients to receive board updates in real-time when an opponent makes a move.

#### Scenario: Client polls with current version and receives update after opponent's move
- **WHEN** client sends GET request to `/room/{roomId}/board?version={currentVersion}`
- **AND** opponent makes a valid move
- **THEN** the endpoint SHALL return HTTP 200 with the updated board state including the new version number and piece positions

#### Scenario: Client polls with outdated version and receives update immediately
- **WHEN** client sends GET request to `/room/{roomId}/board?version={staleVersion}`
- **AND** the board version has already changed since that version
- **THEN** the endpoint SHALL return HTTP 200 immediately with the current board state

#### Scenario: Client polls and no move occurs within timeout period
- **WHEN** client sends GET request to `/room/{roomId}/board?version={currentVersion}`
- **AND** no move occurs within the timeout period
- **THEN** the endpoint SHALL return HTTP 408 (Request Timeout) after the timeout
- **AND** the server SHALL clean up resources associated with the timed-out request

#### Scenario: Client polls for non-existent room
- **WHEN** client sends GET request to `/room/{roomId}/board`
- **AND** the room does not exist
- **THEN** the endpoint SHALL return HTTP 404 (Not Found)

### Requirement: Board state is serialized correctly as JSON
The system SHALL serialize board state to JSON with a properly ordered list of pieces.

#### Scenario: Board with pieces is serialized
- **WHEN** the server serializes a board with pieces
- **THEN** the JSON response SHALL contain a `version` field (integer)
- **AND** a `pieces` field (array of objects)
- **AND** each piece object SHALL contain `cell` (string), `color` (string), and `type` (string) fields
- **AND** the pieces array SHALL be eagerly evaluated and fully materialized before serialization

### Requirement: Frontend handles long polling responses correctly
The client-side JavaScript SHALL correctly handle all long polling response scenarios.

#### Scenario: Successful response with board data
- **WHEN** the long polling endpoint returns HTTP 200 with board data
- **THEN** the client SHALL update the board display with the new piece positions
- **AND** the client SHALL initiate a new long poll with the updated version number

#### Scenario: Timeout response
- **WHEN** the long polling endpoint returns HTTP 408 (Request Timeout)
- **THEN** the client SHALL immediately initiate a new long poll with the same version number

#### Scenario: Server error response
- **WHEN** the long polling endpoint returns a non-200, non-408 status code
- **THEN** the client SHALL wait 1 second and then retry the long poll with the same version number

#### Scenario: Game finished during polling
- **WHEN** the client detects that one side has no pieces remaining
- **THEN** the client SHALL redirect to the game page to trigger finish detection