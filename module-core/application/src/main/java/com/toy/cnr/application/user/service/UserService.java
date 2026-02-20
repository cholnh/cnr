package com.toy.cnr.application.user.service;

import com.toy.cnr.domain.common.CommandResult;
import com.toy.cnr.domain.user.User;
import com.toy.cnr.port.common.RepositoryResult;
import com.toy.cnr.port.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public CommandResult<User> findByEmail(String email) {
        return switch (userRepository.findByEmail(email)) {
            case RepositoryResult.Found(var dto) -> new CommandResult.Success<>(
                new User(dto.id(), dto.email(), dto.password()), null
            );
            case RepositoryResult.NotFound(var msg) -> new CommandResult.BusinessError<>(msg);
            case RepositoryResult.Error(var t) -> new CommandResult.BusinessError<>(t.getMessage());
        };
    }

    public void updateLastLoginAt(String email, LocalDateTime lastLoginAt) {
        userRepository.updateLastLoginAt(email, lastLoginAt);
    }
}
