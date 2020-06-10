package message;

public enum Description {
	// server full message
	SERVER_FULL,
	// logging in
	LOGIN_ATTEMPT,
	LOGIN_SUCCESSFUL,
	LOGIN_FAILED,
	ACCOUNT_LOCKED,
	ALREADY_LOGGED_IN,
	// registering
	REGISTER_ATTEMPT,
	REGISTER_SUCCESSFUL,
	REGISTER_FAILED,
	INVALID_CHARACTERS,
	// sending messages in the lobby
	SEND_MESSAGE_TO_LOBBY,
	APPLY_MESSAGE_TO_LOBBY,
	// viewing statistics in the lobby
	VIEW_STATS,
	SHOW_STATS,
	// viewing room status in lobby
	VIEW_ROOMS,
	ROOM_STATUS,
	// joining room in the lobby
	JOIN_EMPTY_ROOM,
	JOIN_ROOM_SECOND_PLAYER,
	APPROVE_EMPTY_ROOM,
	APPROVE_SECOND_PLAYER,
	LAUNCH_GAME,
	// playing the game
	CHECK_BOTH_IN_GAME,
	BOTH_IN_GAME,
	START_PLAYING,
	PLAYER_NUMBER,
	SEND_TANK_POSITION,
	UPDATE_TANK_POSITION,
	SEND_MISSILE_POSITION,
	UPDATE_MISSILE_POSITION,
	// ending the game
	LEAVE_ROOM,
	UPDATE_STATS
}
