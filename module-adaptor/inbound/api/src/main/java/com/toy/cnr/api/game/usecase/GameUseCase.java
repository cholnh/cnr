package com.toy.cnr.api.game.usecase;

import com.toy.cnr.api.game.request.LocationPublishRequest;
import com.toy.cnr.api.game.response.LocationResponse;
import com.toy.cnr.application.game.service.LocationService;
import com.toy.cnr.domain.common.CommandResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;

/**
 * Game 도메인 유즈케이스.
 * <p>
 * 좌표 발행/구독을 오케스트레이션합니다.
 * HTTP 관심사({@code SseEmitter}, {@code ResponseEntity})는 Controller가 담당합니다.
 */
@Component
public class GameUseCase {

    private final LocationService locationService;

    public GameUseCase(LocationService locationService) {
        this.locationService = locationService;
    }

    /**
     * 좌표를 발행합니다 (GeoHash 저장 + Pub/Sub 발행).
     */
    public CommandResult<LocationResponse> publishLocation(LocationPublishRequest request) {
        return locationService.publishLocation(request.toCommand())
            .map(LocationResponse::from);
    }

    /**
     * 여러 플레이어의 좌표를 구독합니다.
     * Consumer 콜백으로 좌표를 전달하며, HTTP 관심사(SseEmitter)를 알지 못합니다.
     *
     * @param gameId     게임 세션 ID
     * @param playerIds  구독할 플레이어 ID 목록
     * @param onLocation 좌표 수신 시 호출되는 콜백
     */
    public void subscribeToPlayers(
        String gameId,
        List<String> playerIds,
        Consumer<LocationResponse> onLocation
    ) {
        locationService.subscribe(gameId, playerIds, location ->
            onLocation.accept(LocationResponse.from(location))
        );
    }

    /**
     * 구독을 해제합니다.
     */
    public void unsubscribe(String gameId, List<String> playerIds) {
        locationService.unsubscribe(gameId, playerIds);
    }
}
