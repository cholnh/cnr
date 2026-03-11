package com.toy.cnr.application.user.service;

import com.toy.cnr.domain.common.CommandResult;
import com.toy.cnr.domain.user.User;
import com.toy.cnr.port.common.RepositoryResult;
import com.toy.cnr.port.oauth.OAuthProviderRepository;
import org.springframework.stereotype.Service;

@Service
public class OAuthUserService {

    private final OAuthProviderRepository oAuthProviderRepository;
    private final UserService userService;

    public OAuthUserService(OAuthProviderRepository oAuthProviderRepository, UserService userService) {
        this.oAuthProviderRepository = oAuthProviderRepository;
        this.userService = userService;
    }

    public CommandResult<User> findOrCreateByOAuthCode(String provider, String code) {
        return switch (oAuthProviderRepository.fetchUserInfo(provider, code)) {
            case RepositoryResult.Found(var info) -> userService.findOrCreateByOAuth(
                provider,
                info.oauthId(),
                info.email(),
                info.name(),
                code
            );
            case RepositoryResult.NotFound(var msg) -> new CommandResult.BusinessError<>(msg);
            case RepositoryResult.Error(var t) -> new CommandResult.BusinessError<>(t.getMessage());
        };
    }
}
