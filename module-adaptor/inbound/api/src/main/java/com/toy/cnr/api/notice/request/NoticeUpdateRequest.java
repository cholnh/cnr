package com.toy.cnr.api.notice.request;

import com.toy.cnr.domain.notice.NoticeUpdateCommand;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Notice 수정 요청")
public record NoticeUpdateRequest(
    @Schema(description = "title")
    String title,

    @Schema(description = "content")
    String content,

    @Schema(description = "authorId")
    Long authorId,

    @Schema(description = "hit")
    Long hit
) {
    public NoticeUpdateCommand toCommand() {
        return new NoticeUpdateCommand(this.title, this.content, this.authorId, this.hit);
    }
}
