package com.toy.cnr.api.room.response;

import com.toy.cnr.domain.room.Room;
import com.toy.cnr.domain.room.MapZone;
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
        double actionRadiusMeters,
        MapZoneResponse mapZone
    ) {
        public record MapZoneResponse(
            GeoPointResponse rallyPoint,
            List<GeoPointResponse> playArea,
            List<GeoPointResponse> prisonArea,
            List<GeoPointResponse> restrictedArea
        ) {
            public static MapZoneResponse from(MapZone zone) {
                if (zone == null) return null;
                return new MapZoneResponse(
                    GeoPointResponse.from(zone.rallyPoint()),
                    GeoPointResponse.fromList(zone.playArea()),
                    GeoPointResponse.fromList(zone.prisonArea()),
                    GeoPointResponse.fromList(zone.restrictedArea())
                );
            }
        }

        public record GeoPointResponse(double latitude, double longitude) {
            public static GeoPointResponse from(com.toy.cnr.domain.room.GeoPoint point) {
                if (point == null) return null;
                return new GeoPointResponse(point.latitude(), point.longitude());
            }

            public static List<GeoPointResponse> fromList(List<com.toy.cnr.domain.room.GeoPoint> points) {
                if (points == null) return null;
                return points.stream().map(GeoPointResponse::from).toList();
            }
        }

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
                settings.actionRadiusMeters(),
                MapZoneResponse.from(settings.mapZone())
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
