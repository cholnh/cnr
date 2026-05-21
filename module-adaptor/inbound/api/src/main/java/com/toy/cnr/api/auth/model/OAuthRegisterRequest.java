package com.toy.cnr.api.auth.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "OAuth 회원가입 요청")
public record OAuthRegisterRequest(
    @Schema(description = "OAuth provider (예: kakao)", example = "kakao", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "provider 는 필수입니다.")
    String provider,

    @Schema(description = "OAuth 인가 코드", example = "AUTHORIZATION_CODE", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "code 는 필수입니다.")
    String code,

    @Schema(description = "CSRF 방지용 state 값", example = "csrf-state-value", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "state 는 필수입니다.")
    String state
) {}
