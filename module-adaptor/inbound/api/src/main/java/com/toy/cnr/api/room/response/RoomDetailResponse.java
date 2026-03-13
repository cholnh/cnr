package com.toy.cnr.api.room.response;

import com.toy.cnr.domain.room.Room;
import com.toy.cnr.domain.room.RoomSettings;

import java.util.List;

public record RoomDetailResponse(
    String roomId,
    String hostId,
    String status,
    SettingsResponse settings,
    List<RoomPlayerResponse> players,
    long createdAt
) {
    public record SettingsResponse(
        String gameMode,
        int minPlayers,
        int maxPlayers,
        int copsCount,
        int robbersCount,
        int gameDurationMinutes,
        int escapeTimeMinutes,
        double actionRadiusMeters
    ) {
        public static SettingsResponse from(RoomSettings settings) {
            if (settings == null) return null;
            return new SettingsResponse(
                settings.gameMode().name(),
                settings.minPlayers(),
                settings.maxPlayers(),
                settings.copsCount(),
                settings.robbersCount(),
                settings.gameDurationMinutes(),
                settings.escapeTimeMinutes(),
                settings.actionRadiusMeters()
            );
        }
    }

    public static RoomDetailResponse from(Room room) {
        return new RoomDetailResponse(
            room.roomId(),
            room.hostId(),
            room.status().name(),
            SettingsResponse.from(room.settings()),
            room.players().stream().map(RoomPlayerResponse::from).toList(),
            room.createdAt()
        );
    }
}
