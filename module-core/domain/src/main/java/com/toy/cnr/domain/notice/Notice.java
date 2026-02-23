package com.toy.cnr.domain.notice;

public record Notice(
    Long id,
    String title,
    String content,
    Long authorId,
    Long hit
) {
}
