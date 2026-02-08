package com.toy.cnr.port.common;

import java.util.function.Function;

/**
 * Repository 계층의 결과를 표현하는 sealed 인터페이스.
 * <p>
 * 예외를 던지는 대신 타입으로 성공/실패를 표현하여,
 * 컴파일 타임에 모든 케이스를 처리하도록 강제합니다.
 * <p>
 * 사용 예시:
 * <pre>{@code
 * // switch 패턴 매칭
 * return switch (result) {
 *     case RepositoryResult.Found(var data) -> handleSuccess(data);
 *     case RepositoryResult.NotFound(var msg) -> handleNotFound(msg);
 *     case RepositoryResult.Error(var t) -> handleError(t);
 * };
 *
 * // map으로 성공 데이터 변환 (실패는 자동 전파)
 * RepositoryResult<Foo> result = repository.findById(id).map(FooMapper::toDomain);
 * }</pre>
 *
 * @param <T> 조회/저장 결과 데이터 타입
 */
public sealed interface RepositoryResult<T>
    permits RepositoryResult.Found, RepositoryResult.NotFound, RepositoryResult.Error {

    /**
     * 성공 데이터를 변환합니다. 실패 케이스는 타입만 변경되어 그대로 전파됩니다.
     *
     * @param mapper 성공 데이터 변환 함수
     * @param <R>    변환된 타입
     * @return 변환된 RepositoryResult
     */
    default <R> RepositoryResult<R> map(Function<T, R> mapper) {
        return switch (this) {
            case Found(var data) -> new Found<>(mapper.apply(data));
            case NotFound(var msg) -> new NotFound<>(msg);
            case Error(var t) -> new Error<>(t);
        };
    }

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
