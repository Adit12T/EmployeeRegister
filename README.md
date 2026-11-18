# Employee Register

[![CI](https://github.com/Adit12T/EmployeeRegister/actions/workflows/ci.yml/badge.svg)](https://github.com/Adit12T/EmployeeRegister/actions/workflows/ci.yml)
[![CodeQL](https://github.com/Adit12T/EmployeeRegister/actions/workflows/codeql.yml/badge.svg)](https://github.com/Adit12T/EmployeeRegister/actions/workflows/codeql.yml)

A REST API for registering users and managing employee onboarding records, built with
**Java 21, Spring Boot 3.5, Spring Security and MongoDB**.

I first wrote this in 2018 on Spring Boot 2.1. Version 2 is a rewrite that keeps the same two
features, user registration and employee onboarding, and fixes how the original handled
passwords, validation, concurrency and cross-origin access.

## What changed from version 1

| Area | Version 1 (2018) | Version 2 |
|---|---|---|
| Passwords | Stored in plain text, returned by the API | BCrypt hash only; never returned |
| Access | Every endpoint open | Registration is public; everything else needs a registered user's email and password |
| CORS | Every origin allowed, with credentials | Only origins listed in `CORS_ALLOWED_ORIGINS` |
| Validation | None | Bean Validation with field-level error messages (RFC 9457 problem details) |
| Concurrent edits | Last write wins silently | Optimistic locking with `ETag` / `If-Match`; a stale update gets `409` |
| Duplicates | Allowed | Unique email indexes, `409 Conflict` |
| Lists | Unbounded | Paged and sortable, filter by first name and status |
| Audit | None | `createdAt`, `updatedAt`, `updatedBy` on every record |
| Config | Database password in the repository | Read from `MONGODB_URI` |
| Docs | Springfox Swagger 2 | OpenAPI 3 with Swagger UI |
| Tests | One empty context test | Integration tests against a real MongoDB (Testcontainers) |

## API

| Method | Path | Auth | Purpose |
|---|---|---|---|
| `POST` | `/api/v1/users` | public | Register a user |
| `GET` | `/api/v1/users?firstName=` | user | List users |
| `POST` | `/api/v1/employees` | user | Onboard an employee |
| `GET` | `/api/v1/employees?firstName=&status=&page=&size=&sort=` | user | Search employees |
| `GET` | `/api/v1/employees/{id}` | user | Get one employee (returns an `ETag`) |
| `PUT` | `/api/v1/employees/{id}` | user | Update; send the `ETag` in `If-Match` |
| `DELETE` | `/api/v1/employees/{id}` | user | Delete |

Interactive docs: `http://localhost:8080/swagger-ui.html`.

## Run it

With Docker:

```bash
docker compose up --build
```

Without Docker (needs MongoDB on `localhost:27017` and JDK 21):

```bash
mvn spring-boot:run
```

Try it:

```bash
curl -X POST localhost:8080/api/v1/users -H 'Content-Type: application/json' -d '{
  "firstName":"Jane","lastName":"Doe","email":"jane@example.com",
  "password":"correct-horse-battery","confirmPassword":"correct-horse-battery"}'

curl -u jane@example.com:correct-horse-battery -X POST localhost:8080/api/v1/employees \
  -H 'Content-Type: application/json' -d '{
  "firstName":"Sam","lastName":"Lee","primaryEmail":"sam@example.com",
  "jobTitle":"Software Engineer","startDate":"2026-01-05"}'
```

## Configuration

| Variable | Default | Meaning |
|---|---|---|
| `MONGODB_URI` | `mongodb://localhost:27017/employee_register` | Database connection |
| `CORS_ALLOWED_ORIGINS` | empty (no cross-origin access) | Comma-separated browser origins allowed to call the API |

## Tests

```bash
mvn verify
```

Needs Docker for the MongoDB test container. The tests cover password hashing, validation errors,
duplicate detection, authentication, the full employee life cycle, optimistic locking and CORS.

## License

Copyright © 2026 Aditya Tripathi. All rights reserved. The code is published for viewing only; see
[LICENSE](LICENSE). Security reports: see [SECURITY.md](SECURITY.md).
