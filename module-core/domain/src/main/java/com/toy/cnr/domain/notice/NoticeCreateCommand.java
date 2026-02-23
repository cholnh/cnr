package com.toy.cnr.domain.notice;

public record NoticeCreateCommand(
    String title,
    String content,
    Long authorId,
    Long hit
) {
}
