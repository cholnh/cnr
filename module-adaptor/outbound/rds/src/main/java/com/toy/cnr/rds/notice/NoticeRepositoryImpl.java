package com.toy.cnr.rds.notice;

import com.toy.cnr.port.common.RepositoryResult;
import com.toy.cnr.port.notice.NoticeRepository;
import com.toy.cnr.port.notice.model.NoticeCreateDto;
import com.toy.cnr.port.notice.model.NoticeDto;
import com.toy.cnr.port.notice.model.NoticeUpdateDto;
import com.toy.cnr.rds.notice.entity.NoticeEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NoticeRepositoryImpl implements NoticeRepository {

    private final NoticeJpaRepository noticeJpaRepository;

    public NoticeRepositoryImpl(NoticeJpaRepository noticeJpaRepository) {
        this.noticeJpaRepository = noticeJpaRepository;
    }

    @Override
    public RepositoryResult<List<NoticeDto>> findAll() {
        return RepositoryResult.wrap(() -> {
            var list = noticeJpaRepository.findAll().stream()
                .map(NoticeEntity::toDto)
                .toList();
            return new RepositoryResult.Found<>(list);
        });
    }

    @Override
    public RepositoryResult<NoticeDto> findById(Long id) {
        return RepositoryResult.ofOptional(
            () -> noticeJpaRepository.findById(id).map(NoticeEntity::toDto),
            "Notice not found with id: " + id
        );
    }

    @Override
    public RepositoryResult<NoticeDto> save(NoticeCreateDto dto) {
        return RepositoryResult.wrap(() -> {
            var saved = noticeJpaRepository.save(NoticeEntity.create(dto));
            return new RepositoryResult.Found<>(saved.toDto());
        });
    }

    @Override
    public RepositoryResult<NoticeDto> update(Long id, NoticeUpdateDto dto) {
        return RepositoryResult.ofOptional(
            () -> noticeJpaRepository.findById(id).map(entity -> {
                entity.update(dto);
                return noticeJpaRepository.save(entity).toDto();
            }),
            "Notice not found with id: " + id
        );
    }

    @Override
    public RepositoryResult<Void> deleteById(Long id) {
        return RepositoryResult.wrap(() -> {
            noticeJpaRepository.deleteById(id);
            return new RepositoryResult.Found<>(null);
        });
    }
}
