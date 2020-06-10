package client;

import java.io.*;
import java.net.*;

import gui.GameController;
import gui.LobbyController;
import gui.LoginController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import message.Description;
import message.Message;

/**
 * 
 * @author Ignas Stakaitis
 */
public class Client extends Application {

	public LoginController loginController;
	public LobbyController lobbyController;
	public GameController gameController;
	private Socket socket;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	public ClientListener clientListener;

	/**
	 * The constructor for the client, which initialises the socket,
	 * ObjectOutputStream and ObjectInputStream.
	 */
	public Client() {
		try {
			socket = new Socket("localhost", 50000);
			output = new ObjectOutputStream(socket.getOutputStream());
			input = new ObjectInputStream(socket.getInputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			try {
				socket.close();
				this.output.close();
				this.input.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method initialises the clientListener, which is a thread for listening
	 * for messages from the server while in the lobby.
	 */
	public void createClientListener() {
		clientListener = new ClientListener(this, input);
	}


	/**
	 * This method sends a login attempt message to the server and receives a login
	 * success or failure from the server.
	 * 
	 * @param username The username.
	 * @param password The password.
	 * @return true if login is successful, false otherwise.
	 */
	public Message attemptLogin(String username, String password) {
		try {
			Message loginDetails = new Message(Description.LOGIN_ATTEMPT, username, password);
			output.writeObject(loginDetails);
			Message loggedInStatus = (Message) input.readObject();
			return loggedInStatus;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Message(Description.LOGIN_FAILED);
	}

	public Message attemptRegister(String username, String password) {
		try {
			Message registrationDetails = new Message(Description.REGISTER_ATTEMPT, username, password);
			output.writeObject(registrationDetails);
			Message registrationStatus = (Message) input.readObject();
			return registrationStatus;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return new Message(Description.REGISTER_FAILED);
	}

	/**
	 * This method sends a chat message to the server, for the purpose of displaying
	 * this message on other clients GUIs.
	 * 
	 * @param message The chat message to be sent.
	 */
	public void attemptMessageToLobby(String message) {
		Message messageToLobby = new Message(Description.SEND_MESSAGE_TO_LOBBY, message);
		try {
			output.writeObject(messageToLobby);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method applies the chat message to chat area of the GUI.
	 * 
	 * @param username       The username.
	 * @param messageToApply The message to be displayed in the chat.
	 */
	public void applyMessageToLobby(String username, String messageToApply) {
		this.lobbyController.applyMessageToChatArea(username, messageToApply);
	}

	public void grabStatistics() {
		Message viewStats = new Message(Description.VIEW_STATS);
		try {
			output.writeObject(viewStats);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void viewStatistics(String[] stats) {
		this.lobbyController.updateStatistics(stats);
	}

	/**
	 * This method sends a view rooms message to the server, in order to display the
	 * rooms in the GUI.
	 */
	public void attemptViewRooms() {
		Message viewRoomStatus = new Message(Description.VIEW_ROOMS);
		try {
			output.writeObject(viewRoomStatus);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method applies the display of the rooms to the GUI.
	 * 
	 * @param roomStatus A string array of room statuses.
	 */
	public void viewRooms(String[] roomStatus) {
		this.lobbyController.updateRooms(roomStatus);
	}

	/**
	 * This method sends a message to the server when an empty room is to be joined.
	 * 
	 * @param roomNumber the number of the room.
	 */
	public void attemptJoinEmptyRoom(int roomNumber) {
		Message joinRoomMessage = new Message(Description.JOIN_EMPTY_ROOM, roomNumber);
		try {
			output.writeObject(joinRoomMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method updates the status of an empty room, showing that a player has
	 * join the room.
	 * 
	 * @param roomNumber The room number.
	 */
	public void updateEmptyRoom(int roomNumber) {
		this.lobbyController.onePlayerInRoom(roomNumber);
	}

	/**
	 * This method sends a message to the server when a partially filled room is to
	 * be joined.
	 * 
	 * @param roomNumber The room number.
	 */
	public void attemptJoinSecondPlayer(int roomNumber) {
		Message joinRoomMessage = new Message(Description.JOIN_ROOM_SECOND_PLAYER, roomNumber);
		try {
			output.writeObject(joinRoomMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method updates the status of a partially filled room, showing that a
	 * second player has join the room.
	 * 
	 * @param roomNumber
	 */
	public void updateFullRoom(int roomNumber) {
		this.lobbyController.setFullRoom(roomNumber);

	}

	/**
	 * This method updates the GUI to display a launch game button, allowing players
	 * to start the game.
	 */
	public void proceedToGame() {
		this.lobbyController.setLaunchButton();
	}
	
	/**
	 * 
	 */
	public void checkPlayersInGame() {
		Message checkPlayer = new Message(Description.CHECK_BOTH_IN_GAME);
		try {
			output.writeObject(checkPlayer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 */
	public void bothPlayersInGame(int playerNumber) {
		this.gameController.enableStartButton(playerNumber);
	}

	/**
	 * 
	 */
	public void startPlaying() {
		Message requestPlayerNumber = new Message(Description.START_PLAYING);
		try {
			output.writeObject(requestPlayerNumber);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param playerNumber
	 */
	public void assignPlayer(int playerNumber) {
		if (playerNumber == 1) {
			this.gameController.startPlayer1();
		} else {
			this.gameController.startPlayer2();
		}
	}
	
	/**
	 * 
	 * @param tankPosition
	 */
	public void sendCurrentTankPosition(double[] tankPosition) {
		Message currentPosition = new Message(Description.SEND_TANK_POSITION, tankPosition);
		try {
			output.writeObject(currentPosition);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param tankPosition
	 */
	public void receiveTankPosition(double[] tankPosition) {
		this.gameController.updateOpponentTankPosition(tankPosition);
	}
	
	/**
	 * 
	 * @param missilePosition
	 */
	public void sendCurrentMissilePosition(double[] missilePosition) {
		Message currentPosition = new Message(Description.SEND_MISSILE_POSITION, missilePosition);
		try {
			output.writeObject(currentPosition);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param missilePosition
	 */
	public void receiveMissilePosition(double[] missilePosition) {
		this.gameController.updateOpponentMissilePosition(missilePosition);
	}
	
	public void attemptLeaveRoom() {
		Message leaveRoomMessage = new Message(Description.LEAVE_ROOM);
		try {
			output.writeObject(leaveRoomMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void attemptUpdateStatistics(int[] stats) {
		Message statistics = new Message(Description.UPDATE_STATS, stats);
		try {
			output.writeObject(statistics);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start(Stage stage) throws Exception {

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/Login.fxml"));
		Parent root = fxmlLoader.load();
		loginController = fxmlLoader.getController();
		loginController.client = this;
		this.loginController.setRandomText();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}

	/**
	 * This method launches the GUI and initialises the client.
	 */
	public static void main(String[] args) {
		launch();
		new Client();
	}
}
