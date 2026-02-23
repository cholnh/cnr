package com.toy.cnr.rds.notice.entity;

import com.toy.cnr.port.notice.model.NoticeCreateDto;
import com.toy.cnr.port.notice.model.NoticeDto;
import com.toy.cnr.port.notice.model.NoticeUpdateDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notice")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "author_id", nullable = false)
    private Long authorId;

    @Column(name = "hit", nullable = false)
    private Long hit;

    private NoticeEntity(Long id, String title, String content, Long authorId, Long hit) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.hit = hit;
    }

    public static NoticeEntity create(NoticeCreateDto from) {
        return new NoticeEntity(null, from.title(), from.content(), from.authorId(), from.hit());
    }

    public void update(NoticeUpdateDto from) {
        this.title = from.title();
        this.content = from.content();
        this.authorId = from.authorId();
        this.hit = from.hit();
    }

    public NoticeDto toDto() {
        return new NoticeDto(id, title, content, authorId, hit);
    }
}
