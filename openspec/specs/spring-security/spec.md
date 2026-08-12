## Purpose

Описывает требования к защите доски комнаты `/room/{roomId}/board` с использованием Spring Security и Basic Auth.

## Requirements

### Requirement: Board endpoint is protected via Spring Security
The system SHALL protect the endpoint `/room/{roomId}/board` using Spring Security's Basic Auth mechanism. All other endpoints (for example `/`, `/login`, `/room`, `/room/create`, `/game`) SHALL remain accessible without authentication. When a request to `/room/{roomId}/board` is made without valid credentials, the system SHALL respond with `401 Unauthorized`.

#### Scenario: Board endpoint accessible with valid Basic Auth credentials
- **WHEN** the client sends a request to `/room/{roomId}/board` with a valid Basic Auth header
- **THEN** the request passes the security filter and the board data is returned

#### Scenario: Board endpoint rejected without credentials
- **WHEN** the client sends a request to `/room/{roomId}/board` without an `Authorization` header
- **THEN** the system responds with `401 Unauthorized`

#### Scenario: Board endpoint rejected with invalid credentials
- **WHEN** the client sends a request to `/room/{roomId}/board` with a Basic Auth header containing credentials of a user that does not exist in the user repository
- **THEN** the system responds with `401 Unauthorized`

#### Scenario: Other endpoints accessible without authentication
- **WHEN** the client sends a request to any endpoint other than `/room/{roomId}/board` without authentication
- **THEN** the request is processed without requiring authentication

### Requirement: Basic Auth credentials are validated against the user repository
The system SHALL validate the username supplied via Basic Auth against the existing `UserRepository`. A request SHALL be authenticated only when a user with the given login exists in the user repository.

#### Scenario: Existing user is authenticated
- **WHEN** the client sends a request with Basic Auth credentials containing the login of an existing user
- **THEN** the user is authenticated and the request proceeds

#### Scenario: Unknown user is not authenticated
- **WHEN** the client sends a request with Basic Auth credentials containing a login that does not exist in the user repository
- **THEN** the user is not authenticated and the system responds with `401 Unauthorized`