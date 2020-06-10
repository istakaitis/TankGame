package client;

import java.io.IOException;
import java.io.ObjectInputStream;

import message.Message;

public class ClientListener extends Thread {

	private Client client;
	private ObjectInputStream input;
	private Message messageFromServer;
	private boolean running;

	public ClientListener(Client client, ObjectInputStream input) {
		this.client = client;
		this.input = input;
		this.running = true;
	}
	
	public void setRunning(boolean bool) {
		this.running = bool;
	}

	public void run() {
		while (running == true) {
			try {
				messageFromServer = (Message) input.readObject();
				switch (messageFromServer.getDescription()) {
				case APPLY_MESSAGE_TO_LOBBY:
					showMessageToLobby();
					break;
				case SHOW_STATS:
					showStatistics();
					break;
				case APPROVE_EMPTY_ROOM:
					updateEmptyRoom();
					break;
				case APPROVE_SECOND_PLAYER:
					updateFullRoom();
					break;
				case ROOM_STATUS:
					showRoomStatus();
					break;
				case LAUNCH_GAME:
					launchGame();
					break;
				case BOTH_IN_GAME:
					confirmBothInGame();
					break;
				case PLAYER_NUMBER:
					assignPlayerNumber();
					break;
				case UPDATE_TANK_POSITION:
					tankPosition();
					break;
				case UPDATE_MISSILE_POSITION:
					missilePosition();
					break;
				default:
					break;
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 */
	public void showMessageToLobby() {
		String username = messageFromServer.getStringMessage()[0];
		String messageToApply = messageFromServer.getStringMessage()[1];
		client.applyMessageToLobby(username, messageToApply);
	}
	
	public void showStatistics() {
		String[] stats = messageFromServer.getStringMessage();
		client.viewStatistics(stats);
	}
	
	public void showRoomStatus() {
		String[] roomStatus = messageFromServer.getStringMessage(); 
		client.viewRooms(roomStatus);
	}
	
	/**
	 * 
	 */
	public void updateEmptyRoom() {
		int roomNumber = messageFromServer.getIntMessage()[0];
		client.updateEmptyRoom(roomNumber);
	}
	
	public void updateFullRoom() {
		int roomNumber = messageFromServer.getIntMessage()[0];
		client.updateFullRoom(roomNumber);
	}
	
	public void launchGame() {
		client.proceedToGame();
	}
	
	public void assignPlayerNumber() {
		if (messageFromServer.getIntMessage()[0] == 1) {
			client.assignPlayer(1);
		} else {
			client.assignPlayer(2);
		}
	}
	
	public void confirmBothInGame() {
		client.bothPlayersInGame(messageFromServer.getIntMessage()[0]);
	}
	
	public void tankPosition() {
		client.receiveTankPosition(messageFromServer.getDoubleMessage());
	}
	
	
	public void missilePosition() {
		client.receiveMissilePosition(messageFromServer.getDoubleMessage());
	}
}
