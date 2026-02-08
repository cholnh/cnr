package com.toy.cnr.port.common;

/**
 * Repository 계층의 결과를 표현하는 sealed 인터페이스.
 * <p>
 * 예외를 던지는 대신 타입으로 성공/실패를 표현하여,
 * 컴파일 타임에 모든 케이스를 처리하도록 강제합니다.
 * <p>
 * 사용 예시:
 * <pre>{@code
 * RepositoryResult<FooDto> result = fooRepository.findById(id);
 *
 * return switch (result) {
 *     case RepositoryResult.Found(var data) -> handleSuccess(data);
 *     case RepositoryResult.NotFound(var msg) -> handleNotFound(msg);
 *     case RepositoryResult.Error(var msg) -> handleError(msg);
 * };
 * }</pre>
 *
 * @param <T> 조회/저장 결과 데이터 타입
 */
public sealed interface RepositoryResult<T>
    permits RepositoryResult.Found, RepositoryResult.NotFound, RepositoryResult.Error {

    /**
     * 데이터를 성공적으로 찾은 경우.
     */
    record Found<T>(T data) implements RepositoryResult<T> {}

    /**
     * 요청한 데이터를 찾지 못한 경우.
     */
    record NotFound<T>(String message) implements RepositoryResult<T> {}

    /**
     * 저장소 접근 중 오류가 발생한 경우.
     */
    record Error<T>(Throwable t) implements RepositoryResult<T> {}
}
