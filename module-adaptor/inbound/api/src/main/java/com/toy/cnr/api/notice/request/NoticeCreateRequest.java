package com.toy.cnr.api.notice.request;

import com.toy.cnr.domain.notice.NoticeCreateCommand;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Notice 생성 요청")
public record NoticeCreateRequest(
    @Schema(description = "title")
    String title,

    @Schema(description = "content")
    String content,

    @Schema(description = "authorId")
    Long authorId,

    @Schema(description = "hit")
    Long hit
) {
    public NoticeCreateCommand toCommand() {
        return new NoticeCreateCommand(this.title, this.content, this.authorId, this.hit);
    }
}
