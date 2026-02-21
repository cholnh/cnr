package com.toy.cnr.application.user.service;

import com.toy.cnr.domain.common.CommandResult;
import com.toy.cnr.domain.user.User;
import com.toy.cnr.port.common.RepositoryResult;
import com.toy.cnr.port.user.UserAuthOAuthRepository;
import com.toy.cnr.port.user.UserRepository;
import com.toy.cnr.port.user.model.UserAuthOAuthCreateDto;
import com.toy.cnr.port.user.model.UserCreateDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserAuthOAuthRepository userAuthOAuthRepository;

    public UserService(UserRepository userRepository, UserAuthOAuthRepository userAuthOAuthRepository) {
        this.userRepository = userRepository;
        this.userAuthOAuthRepository = userAuthOAuthRepository;
    }

    public CommandResult<User> findByEmail(String email) {
        return switch (userRepository.findByEmail(email)) {
            case RepositoryResult.Found(var dto) -> new CommandResult.Success<>(
                new User(dto.id(), dto.email(), dto.name(), dto.createdAt()), null
            );
            case RepositoryResult.NotFound(var msg) -> new CommandResult.BusinessError<>(msg);
            case RepositoryResult.Error(var t) -> new CommandResult.BusinessError<>(t.getMessage());
        };
    }

    public CommandResult<User> findOrCreateByOAuth(
        String provider,
        String oauthId,
        String email,
        String name,
        String accessToken
    ) {
        return switch (userAuthOAuthRepository.findByProviderAndOauthId(provider, oauthId)) {
            case RepositoryResult.Found(var oauthDto) -> switch (userRepository.findByEmail(email)) {
                case RepositoryResult.Found(var userDto) -> new CommandResult.Success<>(
                    new User(userDto.id(), userDto.email(), userDto.name(), userDto.createdAt()), null
                );
                case RepositoryResult.NotFound(var msg) -> new CommandResult.BusinessError<>(msg);
                case RepositoryResult.Error(var t) -> new CommandResult.BusinessError<>(t.getMessage());
            };
            case RepositoryResult.NotFound(var ignored) -> createUserByOAuth(provider, oauthId, email, name, accessToken);
            case RepositoryResult.Error(var t) -> new CommandResult.BusinessError<>(t.getMessage());
        };
    }

    private CommandResult<User> createUserByOAuth(
        String provider,
        String oauthId,
        String email,
        String name,
        String accessToken
    ) {
        return switch (userRepository.save(new UserCreateDto(email, name))) {
            case RepositoryResult.Found(var userDto) -> {
                userAuthOAuthRepository.upsert(
                    new UserAuthOAuthCreateDto(userDto.id(), provider, oauthId, accessToken)
                );
                yield new CommandResult.Success<>(
                    new User(userDto.id(), userDto.email(), userDto.name(), userDto.createdAt()), null
                );
            }
            case RepositoryResult.NotFound(var msg) -> new CommandResult.BusinessError<>(msg);
            case RepositoryResult.Error(var t) -> new CommandResult.BusinessError<>(t.getMessage());
        };
    }

    public void updateLastLoginAt(String email, LocalDateTime lastLoginAt) {
        userRepository.updateLastLoginAt(email, lastLoginAt);
    }
}
