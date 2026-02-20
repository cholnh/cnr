package com.toy.cnr.domain.user;

public record User(
    Long id,
    String email,
    String password
) {
}
