# module-adaptor:inbound:api

**REST API 인바운드 어댑터** 모듈입니다. HTTP 요청을 받아 내부 비즈니스 로직을 호출하는 컨트롤러와 유즈케이스를 제공합니다.

## 역할

- REST API 엔드포인트 정의 (`@RestController`)
- HTTP Request → 도메인 Command 변환
- 도메인 결과 → HTTP Response 변환
- 유즈케이스(UseCase) 오케스트레이션

## 구조

```
api/
├── build.gradle
└── src/main/java/com/toy/cnr/api/
    ├── common/
    │   └── error/
    │       ├── ApiError.java             ← API 에러 sealed 타입
    │       ├── ApiErrorResponse.java     ← 에러 응답 JSON 구조
    │       └── GlobalExceptionHandler.java ← 전역 예외 처리기
    └── foo/
        ├── FooApi.java                   ← REST Controller
        ├── request/
        │   ├── FooCreateRequest.java     ← 생성 요청 DTO
        │   └── FooUpdateRequest.java     ← 수정 요청 DTO
        ├── response/
        │   └── FooResponse.java          ← 응답 DTO
        └── usecase/
            └── FooUseCase.java           ← 유즈케이스 컴포넌트
```

## 의존성

```groovy
dependencies {
    implementation project(':module-core:application')  // 서비스 호출
    implementation project(':module-core:domain')       // Command, 도메인 모델
    implementation 'org.springframework.boot:spring-boot-starter-web'
}
```

## 주요 클래스

### FooApi.java (REST Controller)

```java
@RestController
@RequestMapping("/v1/foo")
public class FooApi {
    private final FooUseCase fooUseCase;

    @GetMapping          → findAll()
    @GetMapping("/{id}") → findById(id)
    @PostMapping         → create(request)
    @PutMapping("/{id}") → update(id, request)
    @DeleteMapping("/{id}") → delete(id)
}
```

### FooUseCase.java (유즈케이스)

```java
@Component
public class FooUseCase {
    private final FooQueryService fooQueryService;

    // CommandResult를 switch 패턴 매칭으로 처리
    public ResponseEntity<?> findFooWithBusinessLogic(Long id) {
        return switch (fooQueryService.findById(id)) {
            case CommandResult.Success(var foo, var msg) -> {
                var convertedFoo = fooQueryService.doBusinessLogic(foo);
                yield ResponseEntity.ok(FooResponse.from(convertedFoo));
            }
            case CommandResult.ValidationError(var errors) ->
                ResponseEntity.badRequest().body(
                    ApiErrorResponse.from(new ApiError.BadRequest("Validation failed", errors))
                );
            case CommandResult.BusinessError(var reason) ->
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiErrorResponse.from(new ApiError.NotFound("Foo", reason))
                );
        };
    }
}
```

- `FooApi`와 `FooQueryService` 사이의 중간 계층
- `CommandResult` sealed 타입을 switch 패턴 매칭으로 처리하여 `ResponseEntity` 생성
- 성공 시 `FooResponse`, 실패 시 `ApiErrorResponse`로 일관된 응답 제공

### ApiError (sealed 에러 타입)

```java
public sealed interface ApiError
    permits ApiError.NotFound, ApiError.BadRequest, ApiError.InternalError {

    record NotFound(String resource, String message) implements ApiError {}
    record BadRequest(String message, List<String> details) implements ApiError {}
    record InternalError(String message) implements ApiError {}
}
```

### GlobalExceptionHandler (전역 예외 처리기)

`@RestControllerAdvice`로 처리되지 않은 예외를 `ApiError` sealed 타입으로 변환하여 일관된 에러 응답을 보장합니다.

### Request/Response 클래스

```java
// Request → Command 변환 메서드 포함
public record FooCreateRequest(String name, String description) {
    public FooCreateCommand toCommand() { ... }
}

// Domain → Response 변환 정적 팩토리 메서드 포함
public record FooResponse(Long id, String name, String description) {
    public static FooResponse from(Foo foo) { ... }
}
```

## API 엔드포인트

| HTTP 메서드 | 경로 | 설명 |
|---|---|---|
| GET | `/v1/foo` | 모든 Foo 조회 |
| GET | `/v1/foo/{id}` | ID로 Foo 조회 (비즈니스 로직 적용) |
| POST | `/v1/foo` | 새 Foo 생성 |
| PUT | `/v1/foo/{id}` | Foo 수정 |
| DELETE | `/v1/foo/{id}` | Foo 삭제 |

## 새로운 API 추가 예시

`Bar` 도메인의 API를 추가하는 경우:

```
api/src/main/java/com/toy/cnr/api/
└── bar/
    ├── BarApi.java
    ├── request/
    │   └── BarCreateRequest.java
    ├── response/
    │   └── BarResponse.java
    └── usecase/
        └── BarUseCase.java
```

```java
@RestController
@RequestMapping("/v1/bar")
public class BarApi {
    private final BarUseCase barUseCase;

    public BarApi(BarUseCase barUseCase) {
        this.barUseCase = barUseCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BarResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(barUseCase.findById(id));
    }
}
```
