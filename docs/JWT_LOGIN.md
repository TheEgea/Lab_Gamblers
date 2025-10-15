# JWT Login Implementation

This document describes the JWT-based authentication system implemented in this application.

## Overview

The application now supports JWT-based login with the following features:
- User authentication via username/password
- JWT token issuance upon successful login
- Token-based protection for `/api/**` endpoints
- Layered architecture (domain, application, infrastructure, web)

## Architecture

### Layers

1. **Domain Layer** (`domain/`)
   - `User` - Domain model for users
   - `UserAuthPort` - Port for user authentication operations
   - `TokenService` - Port for JWT token operations
   - Value objects: `Username`, `HashedPassword`, `RawPassword`, `UserId`, `Role`

2. **Application Layer** (`application/`)
   - `LoginService` - Handles login business logic

3. **Infrastructure Layer** (`infrastructure/`)
   - **Persistence**:
     - `UserEntity` - JPA entity for users
     - `UserJpaRepository` - Spring Data repository
     - `UserDataLoader` - Seeds test users in dev mode
   - **Auth**:
     - `UserAuthAdapter` - Implements `UserAuthPort`
     - `JwtTokenService` - Implements `TokenService`

4. **Web Layer** (`web/`)
   - `LoginController` - REST endpoint for login
   - `LoginRequest`/`LoginResponse` - DTOs

## Database Schema

### Users Table
```sql
CREATE TABLE users (
    id UUID PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL
);

CREATE TABLE user_roles (
    user_id UUID REFERENCES users(id),
    role VARCHAR(50) NOT NULL
);
```

## API Endpoints

### POST /auth/login
Authenticates a user and returns a JWT token.

**Request:**
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9..."
}
```

**Error Response (401 Unauthorized):**
No body, just HTTP 401 status.

### Using the Token

Include the token in the `Authorization` header for protected endpoints:

```bash
curl -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..." \
  http://localhost:8080/api/protected-resource
```

## Test Users

In development mode (`dev` profile), the following test users are automatically created:

| Username | Password   | Roles       |
|----------|------------|-------------|
| admin    | admin123   | ADMIN, USER |
| user     | user123    | USER        |

## Configuration

### JWT Secret Key

The JWT secret key is configured in `application.properties`:

```properties
application.security.jwt.secret-key=${ENV_PROTUBE_JWT_SECRET:change_me_to_a_long_random_secret_key_at_least_64_chars____________________________________________________}
```

**For production:** Set the `ENV_PROTUBE_JWT_SECRET` environment variable with a strong secret (at least 64 characters for HS512).

### Database

**Development (H2):**
```properties
spring.datasource.url=jdbc:h2:mem:mydb
spring.jpa.hibernate.ddl-auto=update
```

**Production (PostgreSQL):**
Uncomment and configure in `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/${ENV_PROTUBE_DB}
spring.datasource.username=${ENV_PROTUBE_DB_USER}
spring.datasource.password=${ENV_PROTUBE_DB_PWD}
```

## Security Configuration

- `/auth/login` - Public (no authentication required)
- `/media/**` - Public (no authentication required)
- `/api/**` - Protected (requires JWT token)
- All other endpoints - Protected by default

## Testing

### Integration Tests

Run tests with:
```bash
mvn test
```

The test suite includes:
- `LoginControllerTest` - Tests login endpoint with valid/invalid credentials
- Existing application tests continue to work

### Manual Testing

Start the application:
```bash
mvn spring-boot:run
```

Test login:
```bash
# Successful login
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Failed login
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"wrong"}'
```

Test protected endpoint:
```bash
# Get token
TOKEN=$(curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' | jq -r '.token')

# Use token
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/some-endpoint
```

## JWT Token Details

- **Algorithm:** HS512
- **Expiration:** 24 hours from issuance
- **Claims:**
  - `sub` - Username
  - `iat` - Issued at timestamp
  - `exp` - Expiration timestamp
  - `roles` - Array of user roles

## Migration to PostgreSQL

To use PostgreSQL instead of H2:

1. Update `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/protube
   spring.datasource.username=protube
   spring.datasource.password=protube
   ```

2. Ensure PostgreSQL is running with the database created:
   ```sql
   CREATE DATABASE protube;
   ```

3. Tables will be created automatically via `spring.jpa.hibernate.ddl-auto=update`

## Next Steps

Potential enhancements:
- [ ] User registration endpoint
- [ ] Password reset functionality
- [ ] Token refresh mechanism
- [ ] Role-based access control on endpoints
- [ ] User profile management
- [ ] Remember me functionality
