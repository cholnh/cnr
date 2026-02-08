package com.toy.cnr.api.foo.request;

import com.toy.cnr.domain.foo.FooCreateCommand;

public record FooCreateRequest(
    String name,
    String description
) {
    public FooCreateCommand toCommand() {
        return new FooCreateCommand(this.name, this.description);
    }
}
