package com.toy.cnr.port.user;

import com.toy.cnr.port.common.RepositoryResult;
import com.toy.cnr.port.user.model.UserDto;

import java.time.LocalDateTime;

public interface UserRepository {
    RepositoryResult<UserDto> findByEmail(String email);
    RepositoryResult<Void> updateLastLoginAt(String email, LocalDateTime lastLoginAt);
}
