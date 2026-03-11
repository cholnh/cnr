package com.toy.cnr.external.kakao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "kakao-auth-client", url = "${kakao.oauth.auth-host}")
public interface KakaoAuthClient {

    @PostMapping(path = "/oauth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    Map<String, Object> fetchAccessToken(@RequestBody Map<String, ?> form);
}
