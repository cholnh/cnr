package com.toy.cnr.rds.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * JPA 관련 Bean 설정.
 * <p>
 * - JPA Repository 스캔 범위를 rds 패키지로 한정
 * - 트랜잭션 관리 활성화
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.toy.cnr.rds")
@EnableTransactionManagement
public class JpaConfig {
}
