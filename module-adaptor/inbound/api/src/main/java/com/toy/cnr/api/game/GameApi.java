package com.toy.cnr.api.game;

import com.toy.cnr.api.common.util.ResponseMapper;
import com.toy.cnr.api.game.request.LocationPublishRequest;
import com.toy.cnr.api.game.request.LocationSubscribeRequest;
import com.toy.cnr.api.game.response.LocationResponse;
import com.toy.cnr.api.game.usecase.GameUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Tag(name = "Game", description = "실시간 좌표 브로드캐스트 API")
@RestController
@RequestMapping("/v1/game")
public class GameApi {

    private final GameUseCase gameUseCase;

    public GameApi(GameUseCase gameUseCase) {
        this.gameUseCase = gameUseCase;
    }

    @Operation(
        summary = "좌표 발행",
        description = "플레이어 좌표를 Redis GeoHash에 저장하고 Pub/Sub으로 구독자들에게 브로드캐스트합니다."
    )
    @PostMapping("/location")
    public ResponseEntity<LocationResponse> publishLocation(
        @RequestBody LocationPublishRequest request
    ) {
        return ResponseMapper.toResponseEntity(
            gameUseCase.publishLocation(request)
        );
    }

    @Operation(
        summary = "좌표 구독 (SSE)",
        description = "지정한 플레이어들의 좌표 변경을 실시간으로 수신합니다. "
            + "Server-Sent Events 스트림으로 응답됩니다."
    )
    @PostMapping(
        value = "/location/subscribe",
        produces = MediaType.TEXT_EVENT_STREAM_VALUE
    )
    public SseEmitter subscribe(@RequestBody LocationSubscribeRequest request) {
        var emitter = new SseEmitter(0L); // timeout 없음

        gameUseCase.subscribeToPlayers(
            request.gameId(),
            request.playerIds(),
            location -> {
                try {
                    emitter.send(
                        SseEmitter.event()
                            .name("location")
                            .data(location)
                    );
                } catch (IOException e) {
                    emitter.completeWithError(e);
                }
            }
        );

        // 연결 종료 시 구독 해제
        emitter.onCompletion(() ->
            gameUseCase.unsubscribe(request.gameId(), request.playerIds())
        );
        emitter.onTimeout(() ->
            gameUseCase.unsubscribe(request.gameId(), request.playerIds())
        );
        emitter.onError(e ->
            gameUseCase.unsubscribe(request.gameId(), request.playerIds())
        );

        return emitter;
    }
}
