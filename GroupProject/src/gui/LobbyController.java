package gui;

import java.io.IOException;
import java.util.ArrayList;

import client.Client;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LobbyController {

	public Client client;

	ArrayList<Button> listOfRooms = new ArrayList<Button>();

	@FXML
	private Button roomOne;

	@FXML
	private Button roomTwo;

	@FXML
	private Button roomThree;

	@FXML
	private Button roomFour;

	@FXML
	private Button roomFive;

	@FXML
	private ListView<String> chatArea;

	@FXML
	private TextArea enterMessageField;

	@FXML
	private Button viewRoomButton;

	@FXML
	private Button sendMessageButton;

	@FXML
	private Button statsButton;

	@FXML
	private Button closeStatsButton;

	@FXML
	private Button launchGameButton;

	@FXML
	private AnchorPane statisticsWindow;

	@FXML
	private Text nameText;

	@FXML
	private Text gamesPlayedText;

	@FXML
	private Text winsText;

	@FXML
	private Text lossesText;

	@FXML
	private Text winRateText;

	@FXML
	private Text killsText;

	@FXML
	private Text deathsText;

	@FXML
	private Text kdRatioText;

    @FXML
    private Text kdRatioTop;

    @FXML
    private Text killsTop;

    @FXML
    private Text winRateTop;

    @FXML
    private Text winsTop;

	/**
	 * A getter for the message that is written in the chat field.
	 * 
	 * @return The chat message.
	 */
	public String getMessageToSendLobby() {
		return enterMessageField.getText();
	}

	/**
	 * This method calls the client method attemptMessageToLobby, when the send
	 * button is pressed.
	 * 
	 * @param event Interaction with send button.
	 */
	public void sendMessageToLobby(ActionEvent event) {
		String message = getMessageToSendLobby();
		// if not empty message, send to lobby
		if (message.trim().length() > 0) {
			client.attemptMessageToLobby(message);
		}
	}

	/**
	 * This method applies the chat message to the chat area and indicates which
	 * user sent the message.
	 * 
	 * @param username The username.
	 * @param message  The chat message.
	 */
	public void applyMessageToChatArea(String username, String message) {
		Platform.runLater(() -> {
			chatArea.getItems().add(username + ": " + message);
			enterMessageField.setText("");
		});
	}

	@FXML
	public void showStatistics(ActionEvent event) {
		client.grabStatistics();
		statsButton.setVisible(false);
	}

	public void updateStatistics(String[] stats) {
		nameText.setText(stats[0]);
		gamesPlayedText.setText(stats[1]);
		winsText.setText(stats[2]);
		lossesText.setText(stats[3]);
		winRateText.setText(stats[4]);
		killsText.setText(stats[5]);
		deathsText.setText(stats[6]);
		kdRatioText.setText(stats[7]);
		winsTop.setText(stats[8]);
		winRateTop.setText(stats[9]);
		killsTop.setText(stats[10]);
		kdRatioTop.setText(stats[11]);
		
		statisticsWindow.setVisible(true);
		closeStatsButton.setVisible(true);
	}

	@FXML
	void hideStatistics(ActionEvent event) {
		closeStatsButton.setVisible(false);
		statisticsWindow.setVisible(false);
		statsButton.setVisible(true);
	}

	/**
	 * This method calls the attemptViewRooms method in the client when the view
	 * rooms button is pressed.
	 * 
	 * @param event Interaction with the view room button.
	 */
	@FXML
	public void viewRoomStatus(ActionEvent event) {
		viewRoomButton.setText("Refresh rooms");
		client.attemptViewRooms();
	}

	/**
	 * This method changes the status of the rooms in the GUI to the correct status.
	 * 
	 * @param roomStatus The statuses of each room.
	 */
	public void updateRooms(String[] roomStatus) {
		// update room one
		Platform.runLater(() -> {
			roomOne.setVisible(true);
			roomOne.setText(roomStatus[0]);
			if (roomStatus[0].equals("Room 1 Full")) {
				roomOne.setDisable(true);
			} else {
				roomOne.setDisable(false);
			}
		});
		Platform.runLater(() -> {
			// update room two
			roomTwo.setVisible(true);
			roomTwo.setText(roomStatus[1]);
			if (roomStatus[1].equals("Room 2 Full")) {
				roomTwo.setDisable(true);
			} else {
				roomTwo.setDisable(false);
			}
		});
		Platform.runLater(() -> {
			// update room three
			roomThree.setVisible(true);
			roomThree.setText(roomStatus[2]);
			if (roomStatus[2].equals("Room 3 Full")) {
				roomThree.setDisable(true);
			} else {
				roomThree.setDisable(false);
			}
		});
		Platform.runLater(() -> {
			// update room four
			roomFour.setVisible(true);
			roomFour.setText(roomStatus[3]);
			if (roomStatus[3].equals("Room 4 Full")) {
				roomFour.setDisable(true);
			} else {
				roomFour.setDisable(false);
			}
		});
		Platform.runLater(() -> {
			// update room five
			roomFive.setVisible(true);
			roomFive.setText(roomStatus[4]);
			if (roomStatus[4].equals("Room 5 Full")) {
				roomFive.setDisable(true);
			} else {
				roomFive.setDisable(false);
			}
		});
	}

	/**
	 * This method calls the client attemptJoinEmptyRoom or attemptJoinSecondPlayer
	 * method when the client attempts to join room 1. The method also disables
	 * further interaction with other rooms.
	 * 
	 * @param event Interaction with the join room 1 button.
	 */
	@FXML
	public void joinRoomOne(ActionEvent event) {
		if (roomOne.getText().equals("Join Room 1 (0/2)")) {
			client.attemptJoinEmptyRoom(1);
			roomOne.setText("Waiting for opponent");
		} else {
			client.attemptJoinSecondPlayer(1);
		}
		viewRoomButton.setVisible(false);
		roomOne.setDisable(true);
		roomTwo.setDisable(true);
		roomThree.setDisable(true);
		roomFour.setDisable(true);
		roomFive.setDisable(true);

	}

	/**
	 * This method calls the client attemptJoinEmptyRoom or attemptJoinSecondPlayer
	 * method when the client attempts to join room 2. The method also disables
	 * further interaction with other rooms.
	 * 
	 * @param event Interaction with the join room 2 button.
	 */
	@FXML
	public void joinRoomTwo(ActionEvent event) {
		if (roomTwo.getText().equals("Join Room 2 (0/2)")) {
			client.attemptJoinEmptyRoom(2);
			roomTwo.setText("Waiting for opponent");
		} else {
			client.attemptJoinSecondPlayer(2);
		}
		viewRoomButton.setVisible(false);
		roomOne.setDisable(true);
		roomTwo.setDisable(true);
		roomThree.setDisable(true);
		roomFour.setDisable(true);
		roomFive.setDisable(true);

	}

	/**
	 * This method calls the client attemptJoinEmptyRoom or attemptJoinSecondPlayer
	 * method when the client attempts to join room 3. The method also disables
	 * further interaction with other rooms.
	 * 
	 * @param event Interaction with the join room 3 button.
	 */
	@FXML
	public void joinRoomThree(ActionEvent event) {
		if (roomThree.getText().equals("Join Room 3 (0/2)")) {
			client.attemptJoinEmptyRoom(3);
			roomThree.setText("Waiting for opponent");
		} else {
			client.attemptJoinSecondPlayer(3);
		}
		viewRoomButton.setVisible(false);
		roomOne.setDisable(true);
		roomTwo.setDisable(true);
		roomThree.setDisable(true);
		roomFour.setDisable(true);
		roomFive.setDisable(true);
	}

	/**
	 * This method calls the client attemptJoinEmptyRoom or attemptJoinSecondPlayer
	 * method when the client attempts to join room 4. The method also disables
	 * further interaction with other rooms.
	 * 
	 * @param event Interaction with the join room 4 button.
	 */
	@FXML
	public void joinRoomFour(ActionEvent event) {
		if (roomFour.getText().equals("Join Room 4 (0/2)")) {
			client.attemptJoinEmptyRoom(4);
			roomFour.setText("Waiting for opponent");
		} else {
			client.attemptJoinSecondPlayer(4);
		}
		viewRoomButton.setVisible(false);
		roomOne.setDisable(true);
		roomTwo.setDisable(true);
		roomThree.setDisable(true);
		roomFour.setDisable(true);
		roomFive.setDisable(true);
	}

	/**
	 * This method calls the client attemptJoinEmptyRoom or attemptJoinSecondPlayer
	 * method when the client attempts to join room 5. The method also disables
	 * further interaction with other rooms.
	 * 
	 * @param event Interaction with the join room 5 button.
	 */
	@FXML
	public void joinRoomFive(ActionEvent event) {
		if (roomFive.getText().equals("Join Room 5 (0/2)")) {
			client.attemptJoinEmptyRoom(5);
			roomFive.setText("Waiting for opponent");
		} else {
			client.attemptJoinSecondPlayer(5);
		}
		viewRoomButton.setVisible(false);
		roomOne.setDisable(true);
		roomTwo.setDisable(true);
		roomThree.setDisable(true);
		roomFour.setDisable(true);
		roomFive.setDisable(true);
	}

	/**
	 * This method updates the GUI to show that there is 1 player in a particular
	 * room.
	 * 
	 * @param roomNumber The room number.
	 */
	public void onePlayerInRoom(int roomNumber) {
		switch (roomNumber) {
		case 1:
			Platform.runLater(() -> {
				roomOne.setText("Join Room 1 (1/2)");
			});
			break;
		case 2:
			Platform.runLater(() -> {
				roomTwo.setText("Join Room 2 (1/2)");
			});
			break;
		case 3:
			Platform.runLater(() -> {
				roomThree.setText("Join Room 3 (1/2)");
			});
			break;
		case 4:
			Platform.runLater(() -> {
				roomFour.setText("Join Room 4 (1/2)");
			});
			break;
		case 5:
			Platform.runLater(() -> {
				roomFive.setText("Join Room 5 (1/2)");
			});
			break;
		}
	}

	/**
	 * This method updates the GUI to show that a particular room is full.
	 * 
	 * @param roomNumber The room number.
	 */
	public void setFullRoom(int roomNumber) {
		switch (roomNumber) {
		case 1:
			Platform.runLater(() -> {
				roomOne.setDisable(true);
				roomOne.setText("Room 1 Full");
			});
			break;
		case 2:
			Platform.runLater(() -> {
				roomTwo.setDisable(true);
				roomTwo.setText("Room 2 Full");
			});
			break;
		case 3:
			Platform.runLater(() -> {
				roomThree.setDisable(true);
				roomThree.setText("Room 3 Full");
			});
			break;
		case 4:
			Platform.runLater(() -> {
				roomFour.setDisable(true);
				roomFour.setText("Room 4 Full");
			});
			break;
		case 5:
			Platform.runLater(() -> {
				roomFive.setDisable(true);
				roomFive.setText("Room 5 Full");
			});
			break;
		}
	}

	/**
	 * This method makes the launch game button appear on the GUI.
	 */
	public void setLaunchButton() {
		Platform.runLater(() -> {
			launchGameButton.setVisible(true);
		});
	}

	/**
	 * This method transitions the GUI to the game screen.
	 * 
	 * @param event Interaction with the launch game button.
	 */
	@FXML
	public void goToGame(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameScreen.fxml"));
			Parent GameScreenParent = fxmlLoader.load();

			Scene GameScreenScene = new Scene(GameScreenParent);

			Stage nextWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();

			// set correct controller
			client.gameController = fxmlLoader.getController();
			client.gameController.start();
			client.gameController.client = client;

			client.checkPlayersInGame();

			nextWindow.setScene(GameScreenScene);
			nextWindow.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
