package com.toy.cnr.domain.notice;

public record NoticeUpdateCommand(
    String title,
    String content,
    Long authorId,
    Long hit
) {
}
