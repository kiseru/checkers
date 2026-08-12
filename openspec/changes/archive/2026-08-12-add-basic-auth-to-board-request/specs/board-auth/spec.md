## ADDED Requirements

### Requirement: Board request authenticates the user via Basic Auth
The system SHALL require the `Authorization` header in the format `Basic <base64(login)>` when the client sends a request to `/room/{roomId}/board`. The system SHALL decode the header, extract the username, and verify that a user with that name exists in the user repository. If the header is missing, malformed, or the user does not exist, the system SHALL respond with `401 Unauthorized`.

#### Scenario: Valid Authorization header is accepted
- **WHEN** the client sends a request to `/room/{roomId}/board` with an `Authorization` header containing `Basic <base64(existingLogin)>`
- **THEN** the system decodes the header, finds the user, and returns the board data as usual

#### Scenario: Missing Authorization header is rejected
- **WHEN** the client sends a request to `/room/{roomId}/board` without an `Authorization` header
- **THEN** the system responds with `401 Unauthorized`

#### Scenario: Unknown user in Authorization header is rejected
- **WHEN** the client sends a request to `/room/{roomId}/board` with an `Authorization` header containing a `base64` value of a login that does not exist in the user repository
- **THEN** the system responds with `401 Unauthorized`

#### Scenario: Malformed Authorization header is rejected
- **WHEN** the client sends a request to `/room/{roomId}/board` with an `Authorization` header that does not follow the `Basic <base64>` format
- **THEN** the system responds with `401 Unauthorized`

### Requirement: Client attaches Authorization header to board request
The client-side script SHALL add an `Authorization` header with value `Basic <base64(login)>` when fetching `/room/{roomId}/board` for the board long-polling.

#### Scenario: Board fetch includes Authorization header
- **WHEN** the client calls `subscribe` and fetches `/room/{roomId}/board`
- **THEN** the request includes an `Authorization` header with `Basic <base64(login)>` using the current logged-in user's name