package server;

import java.io.*;
import java.net.*;

import com.tools.linkmode.SSHService;
import com.tools.linkmode.Statistics;

import message.Description;
import message.Message;

public class ServerThread extends Thread {

	private Socket socket;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Message messageFromClient;
	private String username;
	private boolean isInGame;

	/**
	 * A getter for the username.
	 * 
	 * @return The username.
	 */
	public String getUsername() {
		return this.username;
	}

	public boolean getInGame() {
		return this.isInGame;
	}

	/**
	 * A setter for the username.
	 * 
	 * @param username The username.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	public void setInGame(boolean isInGame) {
		this.isInGame = isInGame;
	}

	/**
	 * The constructor for the serverThread class, which initialises the socket,
	 * ObjectOutputStream and ObjectInputStream.
	 * 
	 * @param socket A socket.
	 */
	public ServerThread(Socket socket) {
		try {
			this.socket = socket;
			this.output = new ObjectOutputStream(socket.getOutputStream());
			this.input = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The run method which listens for messages from the servers and calls a
	 * specific method depending on the message received.
	 */
	public void run() {
		while (true) {
			try {
				messageFromClient = (Message) input.readObject();
				switch (messageFromClient.getDescription()) {
				case LOGIN_ATTEMPT:
					validateLogin();
					break;
				case REGISTER_ATTEMPT:
					validateRegister();
					break;
				case SEND_MESSAGE_TO_LOBBY:
					applyMessageToLobby();
					break;
				case VIEW_STATS:
					findStatistics();
					break;
				case VIEW_ROOMS:
					checkRoomStatus();
					break;
				case JOIN_EMPTY_ROOM:
					joinEmptyRoom();
					break;
				case JOIN_ROOM_SECOND_PLAYER:
					joinAsSecondPlayer();
					break;
				case CHECK_BOTH_IN_GAME:
					bothInGame();
					break;
				case START_PLAYING:
					assignPlayer();
					break;
				case SEND_TANK_POSITION:
					updateTankPosition();
					break;
				case SEND_MISSILE_POSITION:
					updateMissilePosition();
					break;
				case UPDATE_STATS:
					updateStatistics();
					break;
				case LEAVE_ROOM:
					leaveRoom();
					break;
				default:
					break;
				}
			} catch (SocketException e) {
				try {
					// remove logged out user
					disconnectUser();
					this.socket.close();
					this.output.close();
					this.input.close();
					// breaks out of the infinite while loop
					break;
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * This method checks if the login details are correct and sends a login
	 * successful message to the client if the details are correct and login failed
	 * message if the details are incorrect.
	 */
	public void validateLogin() {
		try {
			String usernameAttempt = messageFromClient.getStringMessage()[0];
			String passwordAttempt = messageFromClient.getStringMessage()[1];
			// checks if username and password are correct
			Message response = LoginProtocol.processLogin(usernameAttempt, passwordAttempt);
			output.writeObject(response);
			// if login is successful, user is added to the list of logged in users
			if (response.getDescription() == Description.LOGIN_SUCCESSFUL) {
				setUsername(usernameAttempt);
				Server.getLoggedInUsers().add(this);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void validateRegister() {
		try {
			String usernameAttempt = messageFromClient.getStringMessage()[0];
			String passwordAttempt = messageFromClient.getStringMessage()[1];
			// checks if username and password are correct
			Message response = LoginProtocol.processRegister(usernameAttempt, passwordAttempt);
			output.writeObject(response);
			// if login is successful, user is added to the list of logged in users
			if (response.getDescription() == Description.REGISTER_SUCCESSFUL) {
				setUsername(usernameAttempt);
				Server.getLoggedInUsers().add(this);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method sends the message from a client back to the clients, in order for
	 * the message to show up in the lobby.
	 */
	public void applyMessageToLobby() {
		String messageToApply = messageFromClient.getStringMessage()[0];
		Message response = new Message(Description.APPLY_MESSAGE_TO_LOBBY, this.getUsername(), messageToApply);
		for (ServerThread user : Server.getLoggedInUsers()) {
			try {
				user.output.writeObject(response);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void findStatistics() {
		SSHService service = new SSHService();
		String username = this.getUsername();
		Statistics stats = service.selectStatistics(username);
		String gamesPlayed = "" + stats.getGames_played();
		String wins = "" + stats.getWins();
		String losses = "" + stats.getLosses();
		String winRate = Double.parseDouble(stats.getWin_rate()) * 100 + "%";
		String kills = "" + stats.getTotal_kills();
		String deaths = "" + stats.getTotal_deaths();
		String kdRatio = stats.getKill_death_ratio();
		String topWins = service.selectTopWins().getUsername() + " (" + service.selectTopWins().getWins() + ")";
		String topWinRate = service.selectTopWinRate().getUsername() + " (" + Double.parseDouble(service.selectTopWinRate().getWin_rate()) * 100 + "%)";
		String topKills = service.selectTopKills().getUsername() + " (" + service.selectTopKills().getTotal_kills() + ")";
		String topKDR = service.selectTopK_D_R().getUsername() + " (" + service.selectTopK_D_R().getKill_death_ratio() + ")";
		Message response = new Message(Description.SHOW_STATS, username, gamesPlayed, wins, losses, winRate, kills,
				deaths, kdRatio, topWins, topWinRate, topKills, topKDR);
		try {
			output.writeObject(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method sends an update of the status of the 5 rooms to the client,
	 * indicating whether they are empty, partially filled, or full.
	 */
	public void checkRoomStatus() {
		String[] roomStatus = new String[5];
		// check room one
		if (Server.getRoomOneUsers().size() == 0) {
			roomStatus[0] = "Join Room 1 (0/2)";
		} else if (Server.getRoomOneUsers().size() == 1) {
			roomStatus[0] = "Join Room 1 (1/2)";
		} else {
			roomStatus[0] = "Room 1 Full";
		}
		// check room two
		if (Server.getRoomTwoUsers().size() == 0) {
			roomStatus[1] = "Join Room 2 (0/2)";
		} else if (Server.getRoomTwoUsers().size() == 1) {
			roomStatus[1] = "Join Room 2 (1/2)";
		} else {
			roomStatus[1] = "Room 2 Full";
		}
		// check room three
		if (Server.getRoomThreeUsers().size() == 0) {
			roomStatus[2] = "Join Room 3 (0/2)";
		} else if (Server.getRoomThreeUsers().size() == 1) {
			roomStatus[2] = "Join Room 3 (1/2)";
		} else {
			roomStatus[2] = "Room 3 Full";
		}
		// check room four
		if (Server.getRoomFourUsers().size() == 0) {
			roomStatus[3] = "Join Room 4 (0/2)";
		} else if (Server.getRoomFourUsers().size() == 1) {
			roomStatus[3] = "Join Room 4 (1/2)";
		} else {
			roomStatus[3] = "Room 4 Full";
		}
		// check room five
		if (Server.getRoomFiveUsers().size() == 0) {
			roomStatus[4] = "Join Room 5 (0/2)";
		} else if (Server.getRoomFiveUsers().size() == 1) {
			roomStatus[4] = "Join Room 5 (1/2)";
		} else {
			roomStatus[4] = "Room 5 Full";
		}
		Message response = new Message(Description.ROOM_STATUS, roomStatus);
		try {
			output.writeObject(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method sends a message to the client which updates the status of a room
	 * when the first player joins an empty room.
	 */
	public void joinEmptyRoom() {
		int room = messageFromClient.getIntMessage()[0];

		// adds user to the correct room
		switch (room) {
		case 1:
			Server.getRoomOneUsers().add(this);
			break;
		case 2:
			Server.getRoomTwoUsers().add(this);
			break;
		case 3:
			Server.getRoomThreeUsers().add(this);
			break;
		case 4:
			Server.getRoomFourUsers().add(this);
			break;
		case 5:
			Server.getRoomFiveUsers().add(this);
			break;
		}

		Message response = new Message(Description.APPROVE_EMPTY_ROOM, room);
		for (ServerThread user : Server.getLoggedInUsers()) {
			try {
				if (!user.getUsername().equals(this.getUsername())) {
					user.output.writeObject(response);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method sends a message to the client which updates the status of a room
	 * when the second player joins a partially filled room.
	 */
	public void joinAsSecondPlayer() {
		int room = messageFromClient.getIntMessage()[0];

		switch (room) {
		case 1:
			Server.getRoomOneUsers().add(this);
			break;
		case 2:
			Server.getRoomTwoUsers().add(this);
			break;
		case 3:
			Server.getRoomThreeUsers().add(this);
			break;
		case 4:
			Server.getRoomFourUsers().add(this);
			break;
		case 5:
			Server.getRoomFiveUsers().add(this);
			break;
		}

		Message response = new Message(Description.APPROVE_SECOND_PLAYER, room);
		// update room status for all users
		for (ServerThread user : Server.getLoggedInUsers()) {
			try {
				user.output.writeObject(response);
			} catch (IOException e) {
				e.printStackTrace();
			}
			// add launch button to appropriate players
		}
		// this message adds the launch button to the GUI
		Message launchGame = new Message(Description.LAUNCH_GAME);
		if (Server.getRoomOneUsers().contains(this)) {
			for (ServerThread user : Server.getRoomOneUsers()) {
				try {
					user.output.writeObject(launchGame);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else if (Server.getRoomTwoUsers().contains(this)) {
			for (ServerThread user : Server.getRoomTwoUsers()) {
				try {
					user.output.writeObject(launchGame);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else if (Server.getRoomThreeUsers().contains(this)) {
			for (ServerThread user : Server.getRoomThreeUsers()) {
				try {
					user.output.writeObject(launchGame);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else if (Server.getRoomFourUsers().contains(this)) {
			for (ServerThread user : Server.getRoomFourUsers()) {
				try {
					user.output.writeObject(launchGame);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else if (Server.getRoomFiveUsers().contains(this)) {
			for (ServerThread user : Server.getRoomFiveUsers()) {
				try {
					user.output.writeObject(launchGame);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * This method disconnects users from the server and/or the room they are in.
	 */
	public void disconnectUser() {
		if (Server.getRoomOneUsers().contains(this)) {
			Server.getRoomOneUsers().remove(this);
		} else if (Server.getRoomTwoUsers().contains(this)) {
			Server.getRoomTwoUsers().remove(this);
		} else if (Server.getRoomThreeUsers().contains(this)) {
			Server.getRoomThreeUsers().remove(this);
		} else if (Server.getRoomFourUsers().contains(this)) {
			Server.getRoomFourUsers().remove(this);
		} else if (Server.getRoomFiveUsers().contains(this)) {
			Server.getRoomFiveUsers().remove(this);
		}
		Server.getLoggedInUsers().remove(this);
	}

	public void assignPlayer() {
		try {
			if (Server.getRoomOneUsers().contains(this)) {
				if (Server.getRoomOneUsers().indexOf(this) == 0) {
					output.writeObject(new Message(Description.PLAYER_NUMBER, 1));
				} else {
					output.writeObject(new Message(Description.PLAYER_NUMBER, 2));
				}
			} else if (Server.getRoomTwoUsers().contains(this)) {
				if (Server.getRoomTwoUsers().indexOf(this) == 0) {
					output.writeObject(new Message(Description.PLAYER_NUMBER, 1));
				} else {
					output.writeObject(new Message(Description.PLAYER_NUMBER, 2));
				}
			} else if (Server.getRoomThreeUsers().contains(this)) {
				if (Server.getRoomThreeUsers().indexOf(this) == 0) {
					output.writeObject(new Message(Description.PLAYER_NUMBER, 1));
				} else {
					output.writeObject(new Message(Description.PLAYER_NUMBER, 2));
				}
			} else if (Server.getRoomFourUsers().contains(this)) {
				if (Server.getRoomFourUsers().indexOf(this) == 0) {
					output.writeObject(new Message(Description.PLAYER_NUMBER, 1));
				} else {
					output.writeObject(new Message(Description.PLAYER_NUMBER, 2));
				}
			} else if (Server.getRoomFiveUsers().contains(this)) {
				if (Server.getRoomFiveUsers().indexOf(this) == 0) {
					output.writeObject(new Message(Description.PLAYER_NUMBER, 1));
				} else {
					output.writeObject(new Message(Description.PLAYER_NUMBER, 2));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void bothInGame() {
		this.setInGame(true);
		Message response1 = new Message(Description.BOTH_IN_GAME, 1);
		Message response2 = new Message(Description.BOTH_IN_GAME, 2);
		try {
			if (Server.getRoomOneUsers().contains(this)) {
				for (ServerThread user : Server.getRoomOneUsers()) {
					if (user.getInGame() == false) {
						return;
					}
				}
				Server.getRoomOneUsers().get(0).output.writeObject(response1);
				Server.getRoomOneUsers().get(1).output.writeObject(response2);

			} else if (Server.getRoomTwoUsers().contains(this)) {
				for (ServerThread user : Server.getRoomTwoUsers()) {
					if (user.getInGame() == false) {
						return;
					}
				}
				Server.getRoomTwoUsers().get(0).output.writeObject(response1);
				Server.getRoomTwoUsers().get(1).output.writeObject(response2);
			} else if (Server.getRoomThreeUsers().contains(this)) {
				for (ServerThread user : Server.getRoomThreeUsers()) {
					if (user.getInGame() == false) {
						return;
					}
				}
				Server.getRoomThreeUsers().get(0).output.writeObject(response1);
				Server.getRoomThreeUsers().get(1).output.writeObject(response2);
			} else if (Server.getRoomFourUsers().contains(this)) {
				for (ServerThread user : Server.getRoomFourUsers()) {
					if (user.getInGame() == false) {
						return;
					}
				}
				Server.getRoomFourUsers().get(0).output.writeObject(response1);
				Server.getRoomFourUsers().get(1).output.writeObject(response2);
			} else if (Server.getRoomFiveUsers().contains(this)) {
				for (ServerThread user : Server.getRoomFiveUsers()) {
					if (user.getInGame() == false) {
						return;
					}
				}
				Server.getRoomFiveUsers().get(0).output.writeObject(response1);
				Server.getRoomFiveUsers().get(1).output.writeObject(response2);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updateTankPosition() {
		double[] tankPosition = messageFromClient.getDoubleMessage();
		Message response = new Message(Description.UPDATE_TANK_POSITION, tankPosition);
		try {
			if (Server.getRoomOneUsers().contains(this)) {
				for (ServerThread user : Server.getRoomOneUsers()) {
					if (!user.equals(this)) {
						user.output.writeObject(response);
					}
				}
			} else if (Server.getRoomTwoUsers().contains(this)) {
				for (ServerThread user : Server.getRoomTwoUsers()) {
					if (!user.equals(this)) {
						user.output.writeObject(response);
					}
				}
			} else if (Server.getRoomThreeUsers().contains(this)) {
				for (ServerThread user : Server.getRoomThreeUsers()) {
					if (!user.equals(this)) {
						user.output.writeObject(response);
					}
				}
			} else if (Server.getRoomFourUsers().contains(this)) {
				for (ServerThread user : Server.getRoomFourUsers()) {
					if (!user.equals(this)) {
						user.output.writeObject(response);
					}
				}
			} else if (Server.getRoomFiveUsers().contains(this)) {
				for (ServerThread user : Server.getRoomFiveUsers()) {
					if (!user.equals(this)) {
						user.output.writeObject(response);
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updateMissilePosition() {
		double[] missilePosition = messageFromClient.getDoubleMessage();
		Message response = new Message(Description.UPDATE_MISSILE_POSITION, missilePosition);
		try {
			if (Server.getRoomOneUsers().contains(this)) {
				for (ServerThread user : Server.getRoomOneUsers()) {
					if (!user.equals(this)) {
						user.output.writeObject(response);
					}
				}
			} else if (Server.getRoomTwoUsers().contains(this)) {
				for (ServerThread user : Server.getRoomTwoUsers()) {
					if (!user.equals(this)) {
						user.output.writeObject(response);
					}
				}
			} else if (Server.getRoomThreeUsers().contains(this)) {
				for (ServerThread user : Server.getRoomThreeUsers()) {
					if (!user.equals(this)) {
						user.output.writeObject(response);
					}
				}
			} else if (Server.getRoomFourUsers().contains(this)) {
				for (ServerThread user : Server.getRoomFourUsers()) {
					if (!user.equals(this)) {
						user.output.writeObject(response);
					}
				}
			} else if (Server.getRoomFiveUsers().contains(this)) {
				for (ServerThread user : Server.getRoomFiveUsers()) {
					if (!user.equals(this)) {
						user.output.writeObject(response);
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void updateStatistics() {
		int[] statistics = messageFromClient.getIntMessage();
		SSHService service = new SSHService();
		Statistics stats = service.selectStatistics(username);
		stats.setGames_played(stats.getGames_played() + 1);
		stats.setWins(stats.getWins() + statistics[0]);
		stats.setLosses(stats.getLosses() + statistics[1]);
		stats.setTotal_kills(stats.getTotal_kills() + statistics[2]);
		stats.setTotal_deaths(stats.getTotal_deaths() + statistics[3]);
		stats.setWin_rate();
		stats.setKill_death_ratio();
		service.updateStatistics(stats);
	}

	public void leaveRoom() {
		if (Server.getRoomOneUsers().contains(this)) {
			Server.getRoomOneUsers().remove(this);
		} else if (Server.getRoomTwoUsers().contains(this)) {
			Server.getRoomTwoUsers().remove(this);
		} else if (Server.getRoomThreeUsers().contains(this)) {
			Server.getRoomThreeUsers().remove(this);
		} else if (Server.getRoomFourUsers().contains(this)) {
			Server.getRoomFourUsers().remove(this);
		} else if (Server.getRoomFiveUsers().contains(this)) {
			Server.getRoomFiveUsers().remove(this);
		}
	}

}
