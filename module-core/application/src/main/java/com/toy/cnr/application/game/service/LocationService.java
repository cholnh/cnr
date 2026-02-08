package com.toy.cnr.application.game.service;

import com.toy.cnr.application.common.ResultMapper;
import com.toy.cnr.application.game.mapper.LocationMapper;
import com.toy.cnr.domain.common.CommandResult;
import com.toy.cnr.domain.game.LocationPublishCommand;
import com.toy.cnr.domain.game.PlayerLocation;
import com.toy.cnr.port.game.LocationPublisher;
import com.toy.cnr.port.game.LocationStore;
import com.toy.cnr.port.game.LocationSubscriber;
import com.toy.cnr.port.game.model.LocationDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

/**
 * 좌표 발행/구독 서비스.
 * <p>
 * LocationStore(GeoHash), LocationPublisher(Pub/Sub PUBLISH),
 * LocationSubscriber(Pub/Sub SUBSCRIBE) 포트를 사용합니다.
 */
@Service
public class LocationService {

    private final LocationStore locationStore;
    private final LocationPublisher locationPublisher;
    private final LocationSubscriber locationSubscriber;

    public LocationService(
        LocationStore locationStore,
        LocationPublisher locationPublisher,
        LocationSubscriber locationSubscriber
    ) {
        this.locationStore = locationStore;
        this.locationPublisher = locationPublisher;
        this.locationSubscriber = locationSubscriber;
    }

    /**
     * 플레이어 좌표를 GeoHash에 저장하고 Pub/Sub으로 발행합니다.
     */
    public CommandResult<PlayerLocation> publishLocation(LocationPublishCommand command) {
        long timestamp = System.currentTimeMillis();

        var storeResult = locationStore.saveLocation(
            command.gameId(),
            command.playerId(),
            command.longitude(),
            command.latitude()
        );

        return ResultMapper.toCommandResult(storeResult)
            .map(unused -> {
                var dto = LocationMapper.toDto(command, timestamp);
                locationPublisher.publish(command.gameId(), command.playerId(), dto);
                return LocationMapper.fromCommand(command, timestamp);
            });
    }

    /**
     * 여러 플레이어의 좌표 채널을 구독합니다.
     *
     * @param gameId     게임 세션 ID
     * @param playerIds  구독할 플레이어 ID 목록
     * @param onLocation 좌표 수신 시 호출되는 콜백 (도메인 모델 전달)
     */
    public void subscribe(
        String gameId,
        List<String> playerIds,
        Consumer<PlayerLocation> onLocation
    ) {
        for (var playerId : playerIds) {
            locationSubscriber.subscribe(gameId, playerId, dto ->
                onLocation.accept(LocationMapper.toDomain(gameId, dto))
            );
        }
    }

    /**
     * 여러 플레이어의 좌표 채널 구독을 해제합니다.
     */
    public void unsubscribe(String gameId, List<String> playerIds) {
        for (var playerId : playerIds) {
            locationSubscriber.unsubscribe(gameId, playerId);
        }
    }
}
