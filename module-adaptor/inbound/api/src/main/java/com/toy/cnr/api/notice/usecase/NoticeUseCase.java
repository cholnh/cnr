package com.toy.cnr.api.notice.usecase;

import com.toy.cnr.api.notice.request.NoticeCreateRequest;
import com.toy.cnr.api.notice.request.NoticeUpdateRequest;
import com.toy.cnr.api.notice.response.NoticeResponse;
import com.toy.cnr.application.notice.service.NoticeQueryService;
import com.toy.cnr.domain.common.CommandResult;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NoticeUseCase {

    private final NoticeQueryService noticeQueryService;

    public NoticeUseCase(NoticeQueryService noticeQueryService) {
        this.noticeQueryService = noticeQueryService;
    }

    public CommandResult<List<NoticeResponse>> findAll() {
        return noticeQueryService.findAll()
            .map(list -> list.stream().map(NoticeResponse::from).toList());
    }

    public CommandResult<NoticeResponse> findById(Long id) {
        return noticeQueryService.findById(id)
            .map(NoticeResponse::from);
    }

    public CommandResult<NoticeResponse> create(NoticeCreateRequest request) {
        return noticeQueryService.create(request.toCommand())
            .map(NoticeResponse::from);
    }

    public CommandResult<NoticeResponse> update(Long id, NoticeUpdateRequest request) {
        return noticeQueryService.update(id, request.toCommand())
            .map(NoticeResponse::from);
    }

    public CommandResult<Void> delete(Long id) {
        return noticeQueryService.delete(id);
    }
}
