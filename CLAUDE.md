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

## Code Style (from `(rules) code-convention.md`)

- 4-space indentation, max 120 characters per line
- When parameters exceed 120 chars, each parameter goes on its own line with closing `)` on a separate line
- Opening `{` stays on the same line; annotations each get their own line
- Long method chains: each `.method()` call on its own line
- Constructor injection only (no field injection)
- Use `var` for local variable type inference where type is obvious

## Package & Naming Conventions

Base package: `com.toy.cnr`

| Module | Package | Key class patterns |
|--------|---------|-------------------|
| `module-core:domain` | `.domain.<name>` | `<Name>` (record), `<Name>CreateCommand`, `<Name>UpdateCommand` |
| `module-core:application` | `.application.<name>` | `<Name>QueryService` (`@Service`), `<Name>Mapper` (`@UtilityClass`) |
| `module-core:port` | `.port.<name>` | `<Name>Repository` (interface), `<Name>Dto`, `<Name>CreateDto`, `<Name>UpdateDto` |
| `module-adaptor:inbound:api` | `.api.<name>` | `<Name>Api` (`@RestController`), `<Name>UseCase` (`@Component`), `<Name>CreateRequest`, `<Name>Response` |
| `module-adaptor:outbound:rds` | `.rds.<name>` | `<Name>Entity`, `<Name>RepositoryImpl` (`@Repository`) |
| `module-adaptor:outbound:external` | `.external.<name>` | `<Name>FeignClient`, `<Name>ExternalRepository` |

**Responsibility split (api layer):**
- `UseCase` — business orchestration, returns `CommandResult<*Response>` (no HTTP concerns)
- `*Api` — HTTP concerns only, converts `CommandResult` → `ResponseEntity` via switch pattern matching

## Module README Reference

Read the relevant README when working on that layer (on-demand, not auto-loaded):

| Task | README |
|------|--------|
| Domain model / business logic | `module-core/domain/README.md` |
| Service layer | `module-core/application/README.md` |
| Port interfaces / DTOs | `module-core/port/README.md` |
| REST API + UseCase | `module-adaptor/inbound/api/README.md` |
| DB (JPA) implementation | `module-adaptor/outbound/rds/README.md` |
| External API client (Feign) | `module-adaptor/outbound/external/README.md` |
| Batch jobs | `module-adaptor/inbound/batch/README.md` |
| Event listeners | `module-adaptor/inbound/event/README.md` |
| Bootstrap / app config | `module-bootstrap/README.md` |
