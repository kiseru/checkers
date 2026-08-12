## MODIFIED Requirements

### Requirement: Board request authenticates the user via Basic Auth
The system SHALL protect the endpoint `/room/{roomId}/board` through Spring Security using the `Authorization` header in the format `Basic <base64(login)>`. The security layer SHALL decode the header, extract the username, and verify that a user with that name exists in the user repository. If the header is missing, malformed, or the user does not exist, the system SHALL respond with `401 Unauthorized`. The manual authentication logic in the controller is replaced by the standard Spring Security filter chain.

#### Scenario: Valid Authorization header is accepted
- **WHEN** the client sends a request to `/room/{roomId}/board` with an `Authorization` header containing `Basic <base64(existingLogin)>`
- **THEN** the security layer authenticates the user via the user repository and the board data is returned

#### Scenario: Missing Authorization header is rejected
- **WHEN** the client sends a request to `/room/{roomId}/board` without an `Authorization` header
- **THEN** the system responds with `401 Unauthorized`

#### Scenario: Unknown user in Authorization header is rejected
- **WHEN** the client sends a request to `/room/{roomId}/board` with an `Authorization` header containing a `base64` value of a login that does not exist in the user repository
- **THEN** the system responds with `401 Unauthorized`

#### Scenario: Malformed Authorization header is rejected
- **WHEN** the client sends a request to `/room/{roomId}/board` with an `Authorization` header that does not follow the `Basic <base64>` format
- **THEN** the system responds with `401 Unauthorized`