package com.toy.cnr.application.notice.mapper;

import com.toy.cnr.domain.notice.Notice;
import com.toy.cnr.domain.notice.NoticeCreateCommand;
import com.toy.cnr.domain.notice.NoticeUpdateCommand;
import com.toy.cnr.port.notice.model.NoticeCreateDto;
import com.toy.cnr.port.notice.model.NoticeDto;
import com.toy.cnr.port.notice.model.NoticeUpdateDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class NoticeMapper {

    public static Notice toDomain(NoticeDto dto) {
        return new Notice(
            dto.id(),
            dto.title(), dto.content(), dto.authorId(), dto.hit()
        );
    }

    public static NoticeCreateDto toExternal(NoticeCreateCommand command) {
        return new NoticeCreateDto(
            command.title(), command.content(), command.authorId(), command.hit()
        );
    }

    public static NoticeUpdateDto toExternal(NoticeUpdateCommand command) {
        return new NoticeUpdateDto(
            command.title(), command.content(), command.authorId(), command.hit()
        );
    }
}
