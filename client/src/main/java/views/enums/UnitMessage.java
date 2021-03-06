package views.enums;

public enum UnitMessage {
    INVALID_POSITION,
    NOT_COMBAT_UNIT,
    NOT_NONCOMBAT_UNIT,
    NOT_PLAYERS_TURN,
    NO_SELECTED_UNIT,
    NO_PERMISSION,
    NO_MOVE_POINT,
    IS_SLEPT,
    IS_ALERT,
    IS_AWAKE,
    IS_FORTIFYING,
    FULL_HEALTH,
    NO_IMPROVEMENT,
    SAME_SIDE,
    // Move Messages
    NOT_ACCESSIBLE_TILE,
    SAME_TARGET_TILE,
    FULL_TARGET_TILE,
    ENEMY_BORDERS,
    // Settler messages
    NOT_SETTLER_UNIT,
    NOT_WORKER_UNIT,
    NEAR_CITY_BOARDERS,
    NEAR_ENEMY_UNITS,
    // Worker messages
    NOT_PLAYER_TILE,
    CITY_TILE,
    SUCCESS, 
    // prepare siege messages
    NO_SIEGE_UNIT;
}
