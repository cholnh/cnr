package com.toy.cnr.external.kakao;

import com.toy.cnr.port.common.RepositoryResult;
import com.toy.cnr.port.oauth.OAuthProviderRepository;
import com.toy.cnr.port.oauth.model.OAuthUserInfoDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Repository
public class KakaoOAuthProviderRepositoryImpl implements OAuthProviderRepository {

    private static final String PROVIDER_KAKAO = "kakao";

    @Value("${kakao.oauth.token-url}")
    private String tokenUrl;

    @Value("${kakao.oauth.user-info-url}")
    private String userInfoUrl;

    @Value("${kakao.oauth.client-id}")
    private String clientId;

    @Value("${kakao.oauth.redirect-uri}")
    private String redirectUri;

    private final RestClient restClient;

    public KakaoOAuthProviderRepositoryImpl() {
        this.restClient = RestClient.create();
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

    @SuppressWarnings("unchecked")
    private String fetchAccessToken(String code) {
        var params = new LinkedMultiValueMap<String, String>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        var response = restClient.post()
            .uri(tokenUrl)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(params)
            .retrieve()
            .body(Map.class);

        return (String) response.get("access_token");
    }

    @SuppressWarnings("unchecked")
    private OAuthUserInfoDto fetchKakaoUserInfo(String accessToken) {
        var response = restClient.get()
            .uri(userInfoUrl)
            .header("Authorization", "Bearer " + accessToken)
            .retrieve()
            .body(Map.class);

        var id = String.valueOf(response.get("id"));
        var kakaoAccount = (Map<String, Object>) response.getOrDefault("kakao_account", Map.of());
        var profile = (Map<String, Object>) kakaoAccount.getOrDefault("profile", Map.of());

        var email = (String) kakaoAccount.getOrDefault("email", id + "@kakao.placeholder");
        var nickname = (String) profile.getOrDefault("nickname", "kakao_user");

        return new OAuthUserInfoDto(id, email, nickname);
    }
}
