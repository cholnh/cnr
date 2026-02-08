package com.toy.cnr.api.foo.usecase;

import com.toy.cnr.api.foo.request.FooCreateRequest;
import com.toy.cnr.api.foo.request.FooUpdateRequest;
import com.toy.cnr.api.foo.response.FooResponse;
import com.toy.cnr.api.common.error.ApiError;
import com.toy.cnr.api.common.error.ApiErrorResponse;
import com.toy.cnr.application.foo.service.FooQueryService;
import com.toy.cnr.domain.common.CommandResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 테스트 도메인 유즈케이스
 * : Foo 관련 비즈니스 로직을 정의합니다.
 * <p>
 * {@link CommandResult} sealed 타입을 switch 패턴 매칭으로 처리하여
 * 성공/실패에 따른 적절한 {@link ResponseEntity}를 생성합니다.
 */
@Component
public class FooUseCase {

    private final FooQueryService fooQueryService;

    public FooUseCase(FooQueryService fooQueryService) {
        this.fooQueryService = fooQueryService;
    }

    /**
     * UseCase: 모든 Foo 엔티티를 조회합니다.
     */
    public List<FooResponse> findAll() {
        var result = fooQueryService.findAll();
        return result.stream()
            .map(FooResponse::from)
            .toList();
    }

    /**
     * UseCase: ID로 Foo 엔티티를 조회하고 비즈니스 로직을 적용합니다.
     * CommandResult를 switch 패턴 매칭으로 처리합니다.
     */
    public ResponseEntity<?> findFooWithBusinessLogic(Long id) {
        return switch (fooQueryService.findById(id)) {
            case CommandResult.Success(var foo, var msg) -> {
                var convertedFoo = fooQueryService.doBusinessLogic(foo);
                yield ResponseEntity.ok(FooResponse.from(convertedFoo));
            }
            case CommandResult.ValidationError(var errors) ->
                ResponseEntity.badRequest().body(
                    ApiErrorResponse.from(
                        new ApiError.BadRequest("Validation failed", errors)
                    )
                );
            case CommandResult.BusinessError(var reason) ->
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiErrorResponse.from(
                        new ApiError.NotFound("Foo", reason)
                    )
                );
        };
    }

    /**
     * UseCase: 새로운 Foo 엔티티를 생성합니다.
     * CommandResult를 switch 패턴 매칭으로 처리합니다.
     */
    public ResponseEntity<?> create(FooCreateRequest request) {
        return switch (fooQueryService.create(request.toCommand())) {
            case CommandResult.Success(var foo, var msg) ->
                ResponseEntity.ok(FooResponse.from(foo));
            case CommandResult.ValidationError(var errors) ->
                ResponseEntity.badRequest().body(
                    ApiErrorResponse.from(
                        new ApiError.BadRequest("Validation failed", errors)
                    )
                );
            case CommandResult.BusinessError(var reason) ->
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiErrorResponse.from(
                        new ApiError.InternalError(reason)
                    )
                );
        };
    }

    /**
     * UseCase: 기존 Foo 엔티티를 업데이트합니다.
     * CommandResult를 switch 패턴 매칭으로 처리합니다.
     */
    public ResponseEntity<?> update(Long id, FooUpdateRequest request) {
        return switch (fooQueryService.update(id, request.toCommand())) {
            case CommandResult.Success(var foo, var msg) ->
                ResponseEntity.ok(FooResponse.from(foo));
            case CommandResult.ValidationError(var errors) ->
                ResponseEntity.badRequest().body(
                    ApiErrorResponse.from(
                        new ApiError.BadRequest("Validation failed", errors)
                    )
                );
            case CommandResult.BusinessError(var reason) ->
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiErrorResponse.from(
                        new ApiError.NotFound("Foo", reason)
                    )
                );
        };
    }

    /**
     * UseCase: ID로 Foo 엔티티를 삭제합니다.
     */
    public void delete(Long id) {
        fooQueryService.delete(id);
    }
}
