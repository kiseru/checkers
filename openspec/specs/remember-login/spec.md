# remember-login

## Purpose

Хранение последнего введённого логина пользователя в браузере и автоматическая подстановка его в поле ввода на странице входа.

## Requirements

### Requirement: Last login is remembered in the browser
The system SHALL save the last entered username to browser `localStorage` under the key `lastLogin` when the user submits the login form on the `/login` page. The system SHALL pre-fill the username input with the saved value when the `/login` page loads, if a value exists.

#### Scenario: Successful form submission saves the username
- **WHEN** the user submits the login form on `/login` with a non-empty username
- **THEN** the system saves that username to `localStorage` under the key `lastLogin`

#### Scenario: Saved username is pre-filled on page load
- **WHEN** the user opens `/login` and a value exists in `localStorage` under the key `lastLogin`
- **THEN** the system pre-fills the username input with that value

#### Scenario: No saved username leaves the input empty
- **WHEN** the user opens `/login` and no value exists in `localStorage` under the key `lastLogin`
- **THEN** the system leaves the username input empty

#### Scenario: localStorage is unavailable
- **WHEN** `localStorage` is unavailable (e.g., disabled or blocked in private mode) and the user opens or submits the `/login` page
- **THEN** the system does not fail and the login flow continues to work as before