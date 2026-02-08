package com.toy.cnr.application.foo.service;

import com.toy.cnr.application.foo.mapper.FooMapper;
import com.toy.cnr.domain.common.CommandResult;
import com.toy.cnr.domain.foo.Foo;
import com.toy.cnr.domain.foo.FooCreateCommand;
import com.toy.cnr.domain.foo.FooUpdateCommand;
import com.toy.cnr.port.common.RepositoryResult;
import com.toy.cnr.port.foo.FooRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FooQueryService {

    private static final int FOO_BUSINESS_LOGIC_NUMBER = 9999; // for example

    private final FooRepository fooRepository;

    public FooQueryService(FooRepository fooRepository) {
        this.fooRepository = fooRepository;
    }

    @Cacheable(cacheNames = {"fooCache"}, key = "'allFoos'")
    public List<Foo> findAll() {
        var externalFooList = fooRepository.findAll();
        return externalFooList.stream()
            .map(FooMapper::toDomain)
            .toList();
    }

    @Cacheable(cacheNames = {"fooCache"}, key = "#id")
    public CommandResult<Foo> findById(Long id) {
        return switch (fooRepository.findById(id)) {
            case RepositoryResult.Found(var data) ->
                new CommandResult.Success<>(FooMapper.toDomain(data), null);
            case RepositoryResult.NotFound(var msg) ->
                new CommandResult.BusinessError<>(msg);
            case RepositoryResult.Error(var e) ->
                new CommandResult.BusinessError<>(e.getMessage());
        };
    }

    public Foo doBusinessLogic(Foo foo) {
        return foo.fooBusinessLogic(FOO_BUSINESS_LOGIC_NUMBER);
    }

    public CommandResult<Foo> create(FooCreateCommand command) {
        var externalRequest = FooMapper.toExternal(command);
        var externalFoo = fooRepository.save(externalRequest);
        return new CommandResult.Success<>(
            FooMapper.toDomain(externalFoo),
            "Foo created successfully"
        );
    }

    public CommandResult<Foo> update(Long id, FooUpdateCommand command) {
        var externalRequest = FooMapper.toExternal(command);
        return switch (fooRepository.update(id, externalRequest)) {
            case RepositoryResult.Found(var data) ->
                new CommandResult.Success<>(FooMapper.toDomain(data), null);
            case RepositoryResult.NotFound(var msg) ->
                new CommandResult.BusinessError<>(msg);
            case RepositoryResult.Error(var e) ->
                new CommandResult.BusinessError<>(e.getMessage());
        };
    }

    public void delete(Long id) {
        fooRepository.deleteById(id);
    }
}
