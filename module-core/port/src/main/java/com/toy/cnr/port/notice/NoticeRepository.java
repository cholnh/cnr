package com.toy.cnr.port.notice;

import com.toy.cnr.port.common.RepositoryResult;
import com.toy.cnr.port.notice.model.NoticeCreateDto;
import com.toy.cnr.port.notice.model.NoticeDto;
import com.toy.cnr.port.notice.model.NoticeUpdateDto;

import java.util.List;

public interface NoticeRepository {
    RepositoryResult<List<NoticeDto>> findAll();
    RepositoryResult<NoticeDto> findById(Long id);
    RepositoryResult<NoticeDto> save(NoticeCreateDto dto);
    RepositoryResult<NoticeDto> update(Long id, NoticeUpdateDto dto);
    RepositoryResult<Void> deleteById(Long id);
}
