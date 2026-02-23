package com.toy.cnr.port.notice.model;

public record NoticeCreateDto(
    String title,
    String content,
    Long authorId,
    Long hit
) {
}
