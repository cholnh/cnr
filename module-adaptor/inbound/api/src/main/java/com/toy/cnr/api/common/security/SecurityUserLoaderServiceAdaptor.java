package com.toy.cnr.api.common.security;

import com.toy.cnr.application.user.service.UserService;
import com.toy.cnr.domain.common.CommandResult;
import com.toy.cnr.domain.user.User;
import com.toy.cnr.security.exception.InvalidUsernameException;
import com.toy.cnr.security.model.detail.AuthenticatedUser;
import com.toy.cnr.security.port.SecurityUserLoaderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@Service
@Profile("!mock")
@Primary
public class SecurityUserLoaderServiceAdaptor implements SecurityUserLoaderService {

    private final UserService userService;

    public SecurityUserLoaderServiceAdaptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return switch (userService.findByEmail(email)) {
            case CommandResult.Success(var data, var msg) -> createUserDetails(data);
            case CommandResult.ValidationError(var errors) -> {
                log.error("Validation error while loading user by email : {}, errors : {}", email, errors);
                throw InvalidUsernameException.of();
            }
            case CommandResult.BusinessError(var reason) -> {
                log.error("Business error while loading user by email : {}, reason : {}", email, reason);
                throw InvalidUsernameException.of();
            }
        };
    }

    @Override
    public void updateLastLoginAt(String username, LocalDateTime lastLoginAt) {
        userService.updateLastLoginAt(username, lastLoginAt);
    }

    private UserDetails createUserDetails(User user) {
        return AuthenticatedUser.of(
            user.email(),
            user.password(),
            true,
            true,
            true,
            Set.of(),
            user
        );
    }
}
