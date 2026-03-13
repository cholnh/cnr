package com.toy.cnr.api.game.response;

import com.toy.cnr.domain.game.GameEvent;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(description = "게임 이벤트 응답")
public record GameEventResponse(
    @Schema(description = "게임 세션 ID", example = "game-1")
    String gameId,

    @Schema(
        description = "이벤트 타입",
        example = "PLAYER_ARRESTED",
        allowableValues = {
            "PLAYER_ARRESTED",
            "PLAYER_RESCUED",
            "PRISON_ESCAPE_WARNING",
            "ANNOUNCEMENT",
            "GAME_STARTED",
            "GAME_ENDED",
            "ROLE_ASSIGNED"
        }
    )
    String type,

    @Schema(description = "이벤트 타입별 페이로드")
    Map<String, String> data,

    @Schema(description = "이벤트 발생 시각 (epoch millis)", example = "1707350400000")
    long timestamp
) {
    public static GameEventResponse from(GameEvent event) {
        return switch (event) {
            case GameEvent.PlayerArrested e -> new GameEventResponse(
                e.gameId(), "PLAYER_ARRESTED",
                Map.of("copsId", e.copsId(), "robberId", e.robberId()),
                e.timestamp()
            );
            case GameEvent.PlayerRescued e -> new GameEventResponse(
                e.gameId(), "PLAYER_RESCUED",
                Map.of("rescuerId", e.rescuerId(), "rescuedId", e.rescuedId()),
                e.timestamp()
            );
            case GameEvent.PrisonEscapeWarning e -> new GameEventResponse(
                e.gameId(), "PRISON_ESCAPE_WARNING",
                Map.of("playerId", e.playerId()),
                e.timestamp()
            );
            case GameEvent.Announcement e -> new GameEventResponse(
                e.gameId(), "ANNOUNCEMENT",
                Map.of("senderId", e.senderId(), "message", e.message()),
                e.timestamp()
            );
            case GameEvent.GameStarted e -> new GameEventResponse(
                e.gameId(), "GAME_STARTED",
                Map.of(),
                e.timestamp()
            );
            case GameEvent.GameEnded e -> new GameEventResponse(
                e.gameId(), "GAME_ENDED",
                Map.of("winnerRole", e.winnerRole()),
                e.timestamp()
            );
            case GameEvent.RoleAssigned e -> new GameEventResponse(
                e.gameId(), "ROLE_ASSIGNED",
                Map.of("playerId", e.playerId(), "role", e.role()),
                e.timestamp()
            );
        };
    }
}
