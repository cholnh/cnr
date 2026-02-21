package com.toy.cnr.security.model.authentication;

import lombok.Getter;

@Getter
public class OAuthToken extends UnAuthentication {

    private final String provider;
    private final String code;

    private OAuthToken(String provider, String code) {
        this.provider = provider;
        this.code = code;
    }

    public static OAuthToken unauthenticated(String provider, String code) {
        return new OAuthToken(provider, code);
    }
}
