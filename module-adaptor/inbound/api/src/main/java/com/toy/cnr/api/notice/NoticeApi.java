package com.toy.cnr.api.notice;

import com.toy.cnr.api.common.util.ResponseMapper;
import com.toy.cnr.api.notice.request.NoticeCreateRequest;
import com.toy.cnr.api.notice.request.NoticeUpdateRequest;
import com.toy.cnr.api.notice.response.NoticeResponse;
import com.toy.cnr.api.notice.usecase.NoticeUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Notice", description = "Notice 도메인 CRUD API")
@RestController
@RequestMapping("/v1/notice")
public class NoticeApi {

    private final NoticeUseCase noticeUseCase;

    public NoticeApi(NoticeUseCase noticeUseCase) {
        this.noticeUseCase = noticeUseCase;
    }

    @Operation(summary = "전체 Notice 조회")
    @GetMapping
    public ResponseEntity<List<NoticeResponse>> findAll() {
        return ResponseMapper.toResponseEntity(noticeUseCase.findAll());
    }

    @Operation(summary = "Notice 단건 조회")
    @GetMapping("/{id}")
    public ResponseEntity<NoticeResponse> findById(
        @Parameter(description = "Notice ID") @PathVariable Long id
    ) {
        return ResponseMapper.toResponseEntity(noticeUseCase.findById(id));
    }

    @Operation(summary = "Notice 생성")
    @PostMapping
    public ResponseEntity<NoticeResponse> create(@RequestBody NoticeCreateRequest request) {
        return ResponseMapper.toResponseEntity(noticeUseCase.create(request));
    }

    @Operation(summary = "Notice 수정")
    @PutMapping("/{id}")
    public ResponseEntity<NoticeResponse> update(
        @Parameter(description = "Notice ID") @PathVariable Long id,
        @RequestBody NoticeUpdateRequest request
    ) {
        return ResponseMapper.toResponseEntity(noticeUseCase.update(id, request));
    }

    @Operation(summary = "Notice 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @Parameter(description = "Notice ID") @PathVariable Long id
    ) {
        return ResponseMapper.toNoContentResponse(noticeUseCase.delete(id));
    }
}
