package com.toy.cnr.api.auth.api;

import com.toy.cnr.security.model.authentication.BearerAuthenticationToken;
import com.toy.cnr.security.model.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/auth")
public class AuthDocsApi {

    @Operation(
        summary = "token 발급",
        description = "인증을 위한 토큰을 발급합니다. 등록된 계정 정보를 통해 인증을 시도합니다.\n"
            + "```\n"
            + "curl --location --request POST 'https://{{url}}/v1/auth/token' \n"
            + "--form 'email=\"user@email.com\"' \n"
            + "--form 'password=\"CREDENTIAL\"'"
            + "```\n",
        tags = "token-api"
    )
    @PostMapping("/token")
    private ResponseEntity<SuccessResponse<BearerAuthenticationToken>> accessToken(
        @RequestParam("email") String email,
        @RequestParam("password") String password
    ) throws IllegalAccessException {
        throw throwByApiDocsMethodAccess();
    }

    @Operation(
        summary = "token 재 발급",
        description = "cookie(http-only) 에 포함되어 있는 refreshToken 을 사용하여 인증 토큰을 재 발급 받습니다.\n"
            + "```\n"
            + "curl --location --request POST 'https://{{url}}/v1/auth/refresh' \n"
            + "```\n",
        tags = "token-api"
    )
    @Hidden
    @PostMapping("/refresh")
    private ResponseEntity<SuccessResponse<BearerAuthenticationToken>> refreshToken() throws IllegalAccessException {
        throw throwByApiDocsMethodAccess();
    }

    private IllegalAccessException throwByApiDocsMethodAccess() {
        return new IllegalAccessException("This method is for using api documentation. It is not valid.");
    }
}
