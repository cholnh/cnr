package com.toy.cnr.api.foo.response;

import com.toy.cnr.domain.foo.Foo;

public record FooResponse(
    Long id,
    String name,
    String description
) {
    public static FooResponse from(Foo foo) {
        return new FooResponse(
            foo.id(),
            foo.name(),
            foo.description()
        );
    }
}
