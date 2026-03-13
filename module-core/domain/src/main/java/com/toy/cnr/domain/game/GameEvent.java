package com.toy.cnr.domain.game;

/**
 * 게임 내 발생하는 이벤트를 나타내는 sealed interface.
 * <p>
 * 각 permit 타입이 하나의 이벤트 종류를 표현하며, switch 패턴 매칭으로 모든 케이스를 처리합니다.
 */
public sealed interface GameEvent
    permits GameEvent.PlayerArrested,
            GameEvent.PlayerRescued,
            GameEvent.PrisonEscapeWarning,
            GameEvent.Announcement,
            GameEvent.GameStarted,
            GameEvent.GameEnded,
            GameEvent.RoleAssigned
{
    String gameId();
    long timestamp();

    /** 경찰이 도둑을 체포 */
    record PlayerArrested(
        String gameId,
        String copsId,
        String robberId,
        long timestamp
    ) implements GameEvent {}

    /** 도둑이 체포된 동료를 구출 */
    record PlayerRescued(
        String gameId,
        String rescuerId,
        String rescuedId,
        long timestamp
    ) implements GameEvent {}

    /** 체포된 도둑이 감옥 범위를 이탈 */
    record PrisonEscapeWarning(
        String gameId,
        String playerId,
        long timestamp
    ) implements GameEvent {}

    /** 방장 공지 */
    record Announcement(
        String gameId,
        String senderId,
        String message,
        long timestamp
    ) implements GameEvent {}

    /** 게임 시작 */
    record GameStarted(
        String gameId,
        long timestamp
    ) implements GameEvent {}

    /** 게임 종료 */
    record GameEnded(
        String gameId,
        String winnerRole,
        long timestamp
    ) implements GameEvent {}

    /** 인게임 역할 배정 */
    record RoleAssigned(
        String gameId,
        String playerId,
        String role,
        long timestamp
    ) implements GameEvent {}
}
