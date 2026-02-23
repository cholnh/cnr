package com.toy.cnr.port.notice.model;

public record NoticeUpdateDto(
    String title,
    String content,
    Long authorId,
    Long hit
) {
}
