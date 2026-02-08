package com.toy.cnr.rds.foo;

import com.toy.cnr.port.common.RepositoryResult;
import com.toy.cnr.port.foo.FooRepository;
import com.toy.cnr.port.foo.model.FooCreateDto;
import com.toy.cnr.port.foo.model.FooDto;
import com.toy.cnr.port.foo.model.FooUpdateDto;
import com.toy.cnr.rds.foo.entity.FooEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class FooRepositoryImpl implements FooRepository {

    private final FooJpaRepository fooJpaRepository;

    public FooRepositoryImpl(FooJpaRepository fooJpaRepository) {
        this.fooJpaRepository = fooJpaRepository;
    }

    @Override
    public List<FooDto> findAll() {
        return fooJpaRepository.findAll().stream()
            .map(FooEntity::toDto)
            .toList();
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
    public FooDto save(FooCreateDto dto) {
        var entity = FooEntity.create(dto);
        var saved = fooJpaRepository.save(entity);
        return saved.toDto();
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
    public void deleteById(Long id) {
        fooJpaRepository.deleteById(id);
    }
}
