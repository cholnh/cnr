package com.toy.cnr.application.notice.service;

import com.toy.cnr.application.common.ResultMapper;
import com.toy.cnr.application.notice.mapper.NoticeMapper;
import com.toy.cnr.domain.common.CommandResult;
import com.toy.cnr.domain.notice.Notice;
import com.toy.cnr.domain.notice.NoticeCreateCommand;
import com.toy.cnr.domain.notice.NoticeUpdateCommand;
import com.toy.cnr.port.notice.NoticeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeQueryService {

    private final NoticeRepository noticeRepository;

    public NoticeQueryService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    public CommandResult<List<Notice>> findAll() {
        return ResultMapper.toCommandResult(
            noticeRepository.findAll()
                .map(list -> list.stream().map(NoticeMapper::toDomain).toList())
        );
    }

    public CommandResult<Notice> findById(Long id) {
        return ResultMapper.toCommandResult(noticeRepository.findById(id))
            .map(NoticeMapper::toDomain);
    }

    public CommandResult<Notice> create(NoticeCreateCommand command) {
        return ResultMapper.toCommandResult(noticeRepository.save(NoticeMapper.toExternal(command)))
            .map(NoticeMapper::toDomain);
    }

    public CommandResult<Notice> update(Long id, NoticeUpdateCommand command) {
        return ResultMapper.toCommandResult(noticeRepository.update(id, NoticeMapper.toExternal(command)))
            .map(NoticeMapper::toDomain);
    }

    public CommandResult<Void> delete(Long id) {
        return ResultMapper.toCommandResult(noticeRepository.deleteById(id));
    }
}
