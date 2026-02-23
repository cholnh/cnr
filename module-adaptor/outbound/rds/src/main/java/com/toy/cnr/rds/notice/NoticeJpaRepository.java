package com.toy.cnr.rds.notice;

import com.toy.cnr.rds.notice.entity.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeJpaRepository extends JpaRepository<NoticeEntity, Long> {
}
