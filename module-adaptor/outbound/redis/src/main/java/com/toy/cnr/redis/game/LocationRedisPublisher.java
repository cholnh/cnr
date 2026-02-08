package com.toy.cnr.redis.game;

import com.toy.cnr.port.game.LocationPublisher;
import com.toy.cnr.port.game.model.LocationDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Redis Pub/Sub 기반 좌표 이벤트 발행 구현.
 * <p>
 * Channel: {@code game:{gameId}:player:{playerId}:location}
 * <br>
 * Redis command: PUBLISH
 */
@Component
public class LocationRedisPublisher implements LocationPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    public LocationRedisPublisher(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void publish(String gameId, String playerId, LocationDto location) {
        var channel = buildChannel(gameId, playerId);
        redisTemplate.convertAndSend(channel, location);
    }

    private String buildChannel(String gameId, String playerId) {
        return "game:" + gameId + ":player:" + playerId + ":location";
    }
}
