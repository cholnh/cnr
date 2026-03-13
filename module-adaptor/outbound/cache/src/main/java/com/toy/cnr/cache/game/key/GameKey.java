package com.toy.cnr.cache.game.key;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class GameKey {

    public static String location(String gameId, String playerId) {
        return "game:" + gameId + ":player:" + playerId + ":location";
    }

    public static String events(String gameId) {
        return "game:" + gameId + ":events";
    }

    public static String locations(String gameId) {
        return "game:" + gameId + ":locations";
    }

    public static String generateSubscriberId(String gameId) {
        return gameId + ":" + UUID.randomUUID();
    }
}
