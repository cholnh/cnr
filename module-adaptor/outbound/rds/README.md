# module-adaptor:outbound:rds

**RDS 아웃바운드 어댑터** 모듈입니다. `module-core:port`에 정의된 Repository 인터페이스를 관계형 데이터베이스를 사용하여 구현합니다.

## 역할

- Port Repository 인터페이스의 구현체 제공
- JPA Entity 정의 및 관리
- 데이터베이스 접근 로직 (JPA, QueryDSL)

## 구조

```
rdS/
├── build.gradle
└── src/main/java/com/toy/cnr/rds/
    └── foo/
        ├── entity/
        │   └── FooEntity.java           ← JPA 엔티티
        └── FooRepositoryImpl.java       ← Repository 구현체
```

## 의존성

```groovy
dependencies {
    implementation project(':module-core:port')

    implementation 'com.querydsl:querydsl-jpa:5.0.0:jakarta'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-jdbc'
    runtimeOnly 'org.mariadb.jdbc:mariadb-java-client'
    runtimeOnly 'com.mysql:mysql-connector-j'
    runtimeOnly 'com.h2database:h2'
}
```

## 주요 클래스

### FooEntity.java (JPA 엔티티)

```java
public class FooEntity {
    private final Long id;
    private final String name;
    private final String description;

    // 정적 팩토리 메서드
    public static FooEntity create(FooCreateDto from) { ... }
    public static FooEntity update(Long id, FooUpdateDto from) { ... }

    // Port DTO로 변환
    public FooDto toDto() { ... }
}
```

- Port DTO(`FooCreateDto`, `FooUpdateDto`)로부터 엔티티 생성
- 엔티티에서 Port DTO(`FooDto`)로 변환하는 메서드 제공
- 현재는 인메모리 구현이며, 실제 JPA 어노테이션 추가 시 DB 연동 가능

### FooRepositoryImpl.java (Repository 구현체)

```java
@Repository
public class FooRepositoryImpl implements FooRepository {
    // 현재 ConcurrentHashMap 기반 인메모리 구현
    // 향후 JpaRepository 또는 QueryDSL로 교체 가능

    public List<FooDto> findAll() { ... }
    public Optional<FooDto> findById(Long id) { ... }
    public FooDto save(FooCreateDto dto) { ... }
    public Optional<FooDto> update(Long id, FooUpdateDto dto) { ... }
    public void deleteById(Long id) { ... }
}
```

- `FooRepository` 포트 인터페이스를 구현
- `@Repository`로 Spring Bean 등록
- 현재는 하드코딩된 초기 데이터 3건 포함 (개발/테스트 용도)

## 데이터 변환 흐름

```
[Port DTO] → FooEntity.create(dto) → [엔티티 저장]
[엔티티 조회] → entity.toDto() → [Port DTO 반환]
```

## JPA 엔티티로 전환 예시

현재 인메모리 구현을 실제 JPA로 전환하려면:

```java
import jakarta.persistence.*;

@Entity
@Table(name = "foo")
public class FooEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    // ... 기존 메서드 유지
}
```

## 새로운 도메인의 RDS 구현 추가 예시

`Bar` 도메인의 RDS 어댑터를 추가하는 경우:

```
rds/src/main/java/com/toy/cnr/rds/
└── bar/
    ├── entity/
    │   └── BarEntity.java
    └── BarRepositoryImpl.java
```
