# OAuth2 구현 진행 상황

## ✅ 전체 완료 (빌드 성공)

### Phase 1 — Domain ✅
- `User.java`: `password` 제거, `name` + `createdAt` 추가
- `UserAuthOAuth.java`: 신규 생성

### Phase 2 — Port ✅
- `UserDto.java`: `password` 제거, `name` + `createdAt` 추가
- `UserRepository.java`: `save(UserCreateDto)` 메서드 추가
- `UserCreateDto.java`: 신규 생성
- `UserAuthLocalDto.java`: 신규 생성
- `UserAuthOAuthDto.java`: 신규 생성
- `UserAuthOAuthCreateDto.java`: 신규 생성
- `UserAuthLocalRepository.java`: 신규 생성
- `UserAuthOAuthRepository.java`: 신규 생성
- `OAuthProviderRepository.java`: 신규 생성
- `OAuthUserInfoDto.java`: 신규 생성

### Phase 3 — Application ✅
- `UserService.java`: `findOrCreateByOAuth()` 추가, `findByEmail()` 반환값 수정
- `UserAuthLocalService.java`: 신규 생성
- `OAuthUserService.java`: 신규 생성

### Phase 4 — RDS ✅
- `UserEntity.java`: 테이블명 `user_entity` → `users`, `password` 제거, `name` + `createdAt` 추가
- `UserRepositoryImpl.java`: `save()` 구현 추가
- `UserAuthLocalEntity.java`: 신규 생성
- `UserAuthOAuthEntity.java`: 신규 생성
- `UserAuthLocalJpaRepository.java`: 신규 생성
- `UserAuthOAuthJpaRepository.java`: 신규 생성
- `UserAuthLocalRepositoryImpl.java`: 신규 생성
- `UserAuthOAuthRepositoryImpl.java`: 신규 생성

### Phase 5 — External ✅
- `module-adaptor/outbound/external/kakao-oauth-client/` 서브모듈 신규 생성
- `build.gradle`: Spring Boot + Web 의존성 (`bootJar disabled`)
- `KakaoOAuthProviderRepositoryImpl.java`: RestClient로 카카오 토큰/유저정보 호출
- `application-kakao-oauth.yml`: 카카오 API URL + 클라이언트 설정
- `settings.gradle`: `kakao-oauth-client` 서브모듈 등록

### Phase 6 — Security ✅
- `OAuthToken.java`: `UnAuthentication` 상속, provider/code 보관
- `OAuthUserLoaderService.java`: port interface 신규 생성
- `OAuthAuthenticationConverter.java`: JSON body `{provider, code}` 파싱
- `OAuthAuthenticationProvider.java`: OAuthToken → BearerAuthenticationToken 발급
- `OAuthAuthenticationFilter.java`: `UserAuthenticationFilter` 상속, `/v1/auth/oauth` path 처리
- `UserAuthenticationFilter.java`: `pathMatcher` 접근제어자 `private` → `protected` 변경
- `SecurityConfiguration.java`: `oAuthAuthenticationFilter` 필터 체인에 추가
- `application-security.yml`: `security.oauth` 설정 및 `/v1/auth/oauth` permitMatcher 추가

### Phase 7 — API ✅
- `SecurityUserLoaderServiceAdaptor.java`: `UserAuthLocalService` 추가 주입 (password hash 분리 조회)
- `OAuthUserLoaderServiceAdaptor.java`: 신규 생성 (`OAuthUserService` 호출)

### Phase 8 — Bootstrap ✅
- `build.gradle`: `kakao-oauth-client` 의존성 추가
- `application.yml`: `application-kakao-oauth.yml` import 추가

---

## 다음 단계 (직접 실행 필요)

```bash
# 1. DB 재시작 (테이블 구조 변경으로 필수)
docker compose -f docker/docker-compose.yml down -v && docker compose -f docker/docker-compose.yml up -d

# 2. 앱 실행
./gradlew :module-bootstrap:bootRun

# 3. 로컬 로그인 확인 (기존 동작)
POST /v1/auth/token  email=... password=...

# 4. OAuth 로그인 확인
POST /v1/auth/oauth
Content-Type: application/json
{"provider": "kakao", "code": "<실제 카카오 code>"}
```

## 환경변수 설정 필요
- `KAKAO_CLIENT_ID`: 카카오 앱 클라이언트 ID
- `KAKAO_REDIRECT_URI`: 카카오 앱 등록 redirect URI
