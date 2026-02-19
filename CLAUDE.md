# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build
./gradlew build
./gradlew :module-bootstrap:bootJar

# Run
./gradlew :module-bootstrap:bootRun

# Test
./gradlew test
./gradlew :module-core:test   # single module

# Docker (local infra)
docker compose -f docker/docker-compose.yml up -d
docker compose -f docker/docker-compose.yml down -v
```

Swagger UI: `http://localhost:8080/swagger-ui/index.html`

## Architecture

Hexagonal Architecture (Ports & Adapters) multi-module Gradle project.

**Dependency direction (one-way):**
```
module-adaptor → module-core:port → module-core:application → module-core:domain
```

`module-core:domain` has **zero** framework dependencies — pure Java.

**Module roles:**
- `module-bootstrap` — Spring Boot entry point, application config
- `module-core/domain` — Domain models and business rules (pure Java)
- `module-core/application` — Application services, orchestration, mappers
- `module-core/port` — Repository/service interface contracts and DTOs
- `module-adaptor/inbound/api` — REST controllers (`*Api`) and use case orchestrators (`*UseCase`)
- `module-adaptor/outbound/rds` — JPA entities and `*RepositoryImpl`
- `module-adaptor/outbound/cache` — Redis configuration
- `module-adaptor/outbound/external` — OpenFeign clients

**Error handling pattern — sealed types:**
- `CommandResult<T>`: `Success` / `ValidationError` / `BusinessError` (domain layer)
- `RepositoryResult<T>`: `Found` / `NotFound` / `Error` (port layer)
- `ApiError`: `NotFound` / `BadRequest` / `InternalError` (API layer)

## Spring Profiles

- `local` — PostgreSQL on localhost:5432, `ddl-auto=update`, debug logging
- `prod` — Supabase PostgreSQL, Aiven Valkey, `ddl-auto=validate`

## Code Style (from `code-convention.md`)

- 4-space indentation, max 120 characters per line
- When parameters exceed 120 chars, each parameter goes on its own line with closing `)` on a separate line
- Opening `{` stays on the same line; annotations each get their own line
- Long method chains: each `.method()` call on its own line
- Constructor injection only (no field injection)
- Use `var` for local variable type inference where type is obvious
