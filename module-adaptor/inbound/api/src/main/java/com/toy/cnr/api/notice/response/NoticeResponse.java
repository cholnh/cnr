package com.toy.cnr.api.notice.response;

import com.toy.cnr.domain.notice.Notice;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Notice 응답")
public record NoticeResponse(
    @Schema(description = "Notice ID")
    Long id,

    @Schema(description = "title")
    String title,

    @Schema(description = "content")
    String content,

    @Schema(description = "authorId")
    Long authorId,

    @Schema(description = "hit")
    Long hit
) {
    public static NoticeResponse from(Notice notice) {
        return new NoticeResponse(
            notice.id(),
            notice.title(),
            notice.content(),
            notice.authorId(),
            notice.hit()
        );
    }
}
