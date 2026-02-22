package com.toy.cnr.external.kakao;

import com.toy.cnr.port.common.RepositoryResult;
import com.toy.cnr.port.oauth.OAuthProviderRepository;
import com.toy.cnr.port.oauth.model.OAuthUserInfoDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class KakaoOAuthProviderRepositoryImpl implements OAuthProviderRepository {

    private static final String PROVIDER_KAKAO = "kakao";

    private final KakaoAuthClient kakaoAuthClient;
    private final KakaoApiClient kakaoApiClient;

    @Value("${kakao.oauth.client-id}")
    private String clientId;

    @Value("${kakao.oauth.redirect-uri}")
    private String redirectUri;

    public KakaoOAuthProviderRepositoryImpl(KakaoAuthClient kakaoAuthClient, KakaoApiClient kakaoApiClient) {
        this.kakaoAuthClient = kakaoAuthClient;
        this.kakaoApiClient = kakaoApiClient;
    }

    @Override
    public RepositoryResult<OAuthUserInfoDto> fetchUserInfo(String provider, String code) {
        if (!PROVIDER_KAKAO.equalsIgnoreCase(provider)) {
            return new RepositoryResult.NotFound<>("Unsupported provider: " + provider);
        }
        return RepositoryResult.wrap(() -> {
            var accessToken = fetchAccessToken(code);
            var userInfo = fetchKakaoUserInfo(accessToken);
            return new RepositoryResult.Found<>(userInfo);
        });
    }

    private String fetchAccessToken(String code) {
        var params = Map.of(
            "grant_type", "authorization_code",
            "client_id", clientId,
            "redirect_uri", redirectUri,
            "code", code
        );

        var response = kakaoAuthClient.fetchAccessToken(params);

        return (String) response.get("access_token");
    }

    @SuppressWarnings("unchecked")
    private OAuthUserInfoDto fetchKakaoUserInfo(String accessToken) {
        var response = kakaoApiClient.fetchUserInfo("Bearer " + accessToken);

        var id = String.valueOf(response.get("id"));
        var kakaoAccount = (Map<String, Object>) response.getOrDefault("kakao_account", Map.of());
        var profile = (Map<String, Object>) kakaoAccount.getOrDefault("profile", Map.of());

        var email = (String) kakaoAccount.getOrDefault("email", id + "@kakao.placeholder");
        var nickname = (String) profile.getOrDefault("nickname", "kakao_user");

        return new OAuthUserInfoDto(id, email, nickname);
    }
}
