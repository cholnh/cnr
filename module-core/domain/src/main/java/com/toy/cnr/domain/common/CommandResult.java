package com.toy.cnr.domain.common;

import java.util.List;

/**
 * 서비스/애플리케이션 계층의 명령 실행 결과를 표현하는 sealed 인터페이스.
 * <p>
 * 예외를 던지는 대신 타입으로 성공/실패를 표현하여,
 * 호출자가 switch 패턴 매칭으로 모든 케이스를 명시적으로 처리하도록 강제합니다.
 * <p>
 * 사용 예시:
 * <pre>{@code
 * CommandResult<Foo> result = fooQueryService.findById(id);
 *
 * return switch (result) {
 *     case CommandResult.Success(var data, var msg) -> ResponseEntity.ok(data);
 *     case CommandResult.ValidationError(var errors) -> ResponseEntity.badRequest().body(errors);
 *     case CommandResult.BusinessError(var reason) -> ResponseEntity.status(404).body(reason);
 * };
 * }</pre>
 *
 * @param <T> 성공 시 반환할 데이터 타입
 */
public sealed interface CommandResult<T>
    permits CommandResult.Success, CommandResult.ValidationError, CommandResult.BusinessError {

    /**
     * 명령이 성공적으로 수행된 경우.
     *
     * @param data    결과 데이터
     * @param message 성공 메시지 (선택적, null 가능)
     */
    record Success<T>(T data, String message) implements CommandResult<T> {}

    /**
     * 입력 데이터 검증에 실패한 경우.
     *
     * @param errors 검증 오류 목록
     */
    record ValidationError<T>(List<String> errors) implements CommandResult<T> {}

    /**
     * 비즈니스 규칙 위반 또는 리소스를 찾지 못한 경우.
     *
     * @param reason 실패 사유
     */
    record BusinessError<T>(String reason) implements CommandResult<T> {}
}
