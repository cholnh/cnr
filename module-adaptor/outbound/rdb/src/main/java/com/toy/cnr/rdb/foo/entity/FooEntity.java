package com.toy.cnr.rdb.foo.entity;

import com.toy.cnr.port.foo.model.FooCreateDto;
import com.toy.cnr.port.foo.model.FooDto;
import com.toy.cnr.port.foo.model.FooUpdateDto;

public class FooEntity {

    private final Long id;
    private final String name;
    private final String description;

    public FooEntity(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public static FooEntity create(FooCreateDto from) {
        return new FooEntity(
            0L, // ID will be set when saving to the database
            from.name(),
            from.description()
        );
    }

    public static FooEntity update(Long id, FooUpdateDto from) {
        return new FooEntity(
            id,
            from.name(),
            from.description()
        );
    }

    public FooEntity withId(Long newId) {
        return new FooEntity(newId, this.name, this.description);
    }

    public FooDto toDto() {
        return new FooDto(id, name, description);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
