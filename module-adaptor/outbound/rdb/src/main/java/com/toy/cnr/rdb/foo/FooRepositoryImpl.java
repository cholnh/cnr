package com.toy.cnr.rdb.foo;

import com.toy.cnr.port.common.RepositoryResult;
import com.toy.cnr.port.foo.FooRepository;
import com.toy.cnr.port.foo.model.FooCreateDto;
import com.toy.cnr.port.foo.model.FooDto;
import com.toy.cnr.port.foo.model.FooUpdateDto;
import com.toy.cnr.rdb.foo.entity.FooEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class FooRepositoryImpl implements FooRepository {

    private final ConcurrentHashMap<Long, FooEntity> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public FooRepositoryImpl() {
        // 초기 하드코딩 데이터
        storage.put(1L, new FooEntity(1L, "Foo 1", "첫 번째 Foo"));
        storage.put(2L, new FooEntity(2L, "Foo 2", "두 번째 Foo"));
        storage.put(3L, new FooEntity(3L, "Foo 3", "세 번째 Foo"));
        idGenerator.set(4);
    }

    @Override
    public List<FooDto> findAll() {
        return storage.values().stream()
            .map(FooEntity::toDto)
            .toList();
    }

    @Override
    public RepositoryResult<FooDto> findById(Long id) {
        try {
            var entity = storage.get(id);
            if (entity == null) {
                return new RepositoryResult.NotFound<>(
                    "Foo not found with id: " + id
                );
            }
            return new RepositoryResult.Found<>(entity.toDto());
        } catch (Exception e) {
            return new RepositoryResult.Error<>(e);
        }
    }

    @Override
    public FooDto save(FooCreateDto dto) {
        var id = idGenerator.getAndIncrement();
        var entity = FooEntity.create(dto).withId(id);
        storage.put(id, entity);
        return entity.toDto();
    }

    @Override
    public RepositoryResult<FooDto> update(Long id, FooUpdateDto dto) {
        if (!storage.containsKey(id)) {
            return new RepositoryResult.NotFound<>(
                "Foo not found with id: " + id
            );
        }
        var entity = FooEntity.update(id, dto);
        storage.put(id, entity);
        return new RepositoryResult.Found<>(entity.toDto());
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }
}
