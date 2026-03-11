package com.toy.cnr.application.user.service;

import com.toy.cnr.domain.common.CommandResult;
import com.toy.cnr.port.common.RepositoryResult;
import com.toy.cnr.port.user.UserAuthLocalRepository;
import org.springframework.stereotype.Service;

@Service
public class UserAuthLocalService {

    private final UserAuthLocalRepository userAuthLocalRepository;

    public UserAuthLocalService(UserAuthLocalRepository userAuthLocalRepository) {
        this.userAuthLocalRepository = userAuthLocalRepository;
    }

    public CommandResult<String> findPasswordHashByEmail(String email) {
        return switch (userAuthLocalRepository.findPasswordHashByEmail(email)) {
            case RepositoryResult.Found(var hash) -> new CommandResult.Success<>(hash, null);
            case RepositoryResult.NotFound(var msg) -> new CommandResult.BusinessError<>(msg);
            case RepositoryResult.Error(var t) -> new CommandResult.BusinessError<>(t.getMessage());
        };
    }
}
