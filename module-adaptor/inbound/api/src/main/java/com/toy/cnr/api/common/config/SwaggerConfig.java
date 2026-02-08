package com.toy.cnr.api.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger (OpenAPI 3.0) 설정.
 * <p>
 * Swagger UI 접속: <a href="http://localhost:8080/swagger-ui/index.html">http://localhost:8080/swagger-ui/index.html</a>
 * <br>
 * API Docs JSON: <a href="http://localhost:8080/v3/api-docs">http://localhost:8080/v3/api-docs</a>
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Cops N Robbers API")
                .description("헥사고날 아키텍처 기반 REST API")
                .version("v1.0.0")
            )
            .servers(List.of(
                new Server().url("http://localhost:8080").description("Local"),
                new Server().url("https://api.cnr.example.com").description("Production")
            ));
    }
}
