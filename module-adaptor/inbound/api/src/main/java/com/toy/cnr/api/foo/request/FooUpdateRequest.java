package com.toy.cnr.api.foo.request;

import com.toy.cnr.domain.foo.FooUpdateCommand;

public record FooUpdateRequest(
    String name,
    String description
) {
    public FooUpdateCommand toCommand() {
        return new FooUpdateCommand(this.name, this.description);
    }
}
