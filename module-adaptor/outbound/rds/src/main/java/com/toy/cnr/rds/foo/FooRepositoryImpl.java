package com.toy.cnr.rds.foo;

import com.toy.cnr.port.common.RepositoryResult;
import com.toy.cnr.port.foo.FooRepository;
import com.toy.cnr.port.foo.model.FooCreateDto;
import com.toy.cnr.port.foo.model.FooDto;
import com.toy.cnr.port.foo.model.FooUpdateDto;
import com.toy.cnr.rds.foo.entity.FooEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FooRepositoryImpl implements FooRepository {

    private final FooJpaRepository fooJpaRepository;

    public FooRepositoryImpl(FooJpaRepository fooJpaRepository) {
        this.fooJpaRepository = fooJpaRepository;
    }

    @Override
    public RepositoryResult<List<FooDto>> findAll() {
        try {
            var list = fooJpaRepository.findAll().stream()
                .map(FooEntity::toDto)
                .toList();
            return new RepositoryResult.Found<>(list);
        } catch (Exception e) {
            return new RepositoryResult.Error<>(e);
        }
    }

    @Override
    public RepositoryResult<FooDto> findById(Long id) {
        try {
            return fooJpaRepository.findById(id)
                .map(entity -> (RepositoryResult<FooDto>) new RepositoryResult.Found<>(entity.toDto()))
                .orElse(new RepositoryResult.NotFound<>(
                    "Foo not found with id: " + id
                ));
        } catch (Exception e) {
            return new RepositoryResult.Error<>(e);
        }
    }

    @Override
    public RepositoryResult<FooDto> save(FooCreateDto dto) {
        try {
            var entity = FooEntity.create(dto);
            var saved = fooJpaRepository.save(entity);
            return new RepositoryResult.Found<>(saved.toDto());
        } catch (Exception e) {
            return new RepositoryResult.Error<>(e);
        }
    }

    @Override
    public RepositoryResult<FooDto> update(Long id, FooUpdateDto dto) {
        try {
            return fooJpaRepository.findById(id)
                .map(entity -> {
                    entity.update(dto);
                    var saved = fooJpaRepository.save(entity);
                    return (RepositoryResult<FooDto>) new RepositoryResult.Found<>(saved.toDto());
                })
                .orElse(new RepositoryResult.NotFound<>(
                    "Foo not found with id: " + id
                ));
        } catch (Exception e) {
            return new RepositoryResult.Error<>(e);
        }
    }

    @Override
    public RepositoryResult<Void> deleteById(Long id) {
        try {
            fooJpaRepository.deleteById(id);
            return new RepositoryResult.Found<>(null);
        } catch (Exception e) {
            return new RepositoryResult.Error<>(e);
        }
    }
}
