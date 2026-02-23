package com.toy.cnr.port.notice.model;

public record NoticeDto(
    Long id,
    String title,
    String content,
    Long authorId,
    Long hit
) {
}
