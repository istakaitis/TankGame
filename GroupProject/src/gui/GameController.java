package gui;

import client.Client;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.shape.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.image.*;
import javafx.scene.text.Text;
import javafx.scene.canvas.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class GameController extends AnimationTimer {

	public Client client;

	final int WIDTH = 640;
	final int HEIGHT = 480;

	boolean isPlayer1 = false;
	boolean isPlayer2 = false;

	Tank player1 = new Tank(200, 230, 2, 2, 0);
	Tank player2 = new Tank(425, 230, 2, 2, 180);

	ArrayList<Block> blockGroup = new ArrayList<Block>();

	ArrayList<String> input = new ArrayList<String>();

	@FXML
	private AnchorPane gameWindow;

	@FXML
	private Button startButton;

	@FXML
	private Button returnToLobbyButton;

	@FXML
	private ImageView tankImage;

	@FXML
	private ImageView tankImage2;

	@FXML
	private Circle missileImage;

	@FXML
	private Circle missileImage2;

	@FXML
	private ImageView wallImage1, wallImage2;

	@FXML
	private ImageView wallImage3;

	@FXML
	private ImageView wallImage4;

	@FXML
	private ImageView wallImage5;

	@FXML
	private Text livesText1, livesText2, gameOver;

	@FXML
	private ImageView player1Life1, player1Life2, player1Life3;
	
	@FXML
	private ImageView player2Life1, player2Life2, player2Life3;

	@FXML
	private ImageView controlPicture;

	public void enableStartButton(int playerNumber) {
		if (playerNumber == 1) {
			isPlayer1 = true;
		} else {
			isPlayer2 = true;
		}
		Platform.runLater(() -> {
			startButton.setDisable(false);
			startButton.setText("Start Playing!");
			startButton.setTranslateX(40);
			controlPicture.setVisible(false);

			wallImage1.setVisible(true);
			wallImage2.setVisible(true);
			wallImage3.setVisible(true);
			wallImage4.setVisible(true);
			wallImage5.setVisible(true);

			blockGroup.add(new Block(140, 20, 30, 200, 0));
			blockGroup.add(new Block(140, 250, 30, 200, 0));
			blockGroup.add(new Block(500, 20, 30, 200, 0));
			blockGroup.add(new Block(500, 250, 30, 200, 0));
			blockGroup.add(new Block(315, 125, 30, 200, 0));
		});
	}

	@FXML
	void showTank(ActionEvent event) {
		client.startPlaying();

		Platform.runLater(() -> {
			livesText1.setVisible(true);
			livesText2.setVisible(true);
			
			player1Life1.setVisible(true);
			player1Life2.setVisible(true);
			player1Life3.setVisible(true);
			player2Life1.setVisible(true);
			player2Life2.setVisible(true);
			player2Life3.setVisible(true);
		});
	}

	@FXML
	public void goToLobby(ActionEvent event) {
		int wins = 0;
		int losses = 0;
		int kills = 0;
		int deaths = 0;
		if (isPlayer1 == true) {
			if (player1.getLives() > 0) {
				wins = 1;
				losses = 0;
				kills = 3;
				deaths = 3 - player1.getLives();
			} else {
				wins = 0;
				losses = 1;
				kills = 3 - player2.getLives();
				deaths = 3;
			}
		} else if (isPlayer2 == true) {
			if (player2.getLives() > 0) {
				wins = 1;
				losses = 0;
				kills = 3;
				deaths = 3 - player2.getLives();
			} else  {
				wins = 0;
				losses = 1;
				kills = 3 - player1.getLives();
				deaths = 3;
			}
		}
		int[] statistics = {wins, losses, kills, deaths};
		try {
			client.attemptUpdateStatistics(statistics);
			client.attemptLeaveRoom();
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Lobby.fxml"));
			Parent LobbyScreenParent = fxmlLoader.load();

			Scene LobbyScreenScene = new Scene(LobbyScreenParent);

			Stage nextWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();

			// set correct controller
			client.lobbyController = fxmlLoader.getController();
			client.lobbyController.client = client;

			nextWindow.setScene(LobbyScreenScene);
			nextWindow.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void startPlayer1() {
		Platform.runLater(() -> {
			tankImage.setVisible(true);
			startButton.setVisible(false);
			tankImage.requestFocus();
		});
	}

	public void startPlayer2() {
		Platform.runLater(() -> {
			tankImage2.setVisible(true);
			startButton.setVisible(false);
			tankImage2.requestFocus();
		});
	}

	public void updateOpponentTankPosition(double[] tankPosition) {
		double xPos = tankPosition[0];
		double yPos = tankPosition[1];
		double angle = tankPosition[2];
		Platform.runLater(() -> {
			if (isPlayer1 == true) {
				tankImage2.setVisible(true);
				player2.setPositioning(xPos, yPos, angle);
			} else {
				tankImage.setVisible(true);
				player1.setPositioning(xPos, yPos, angle);
			}
		});
	}

	public void updateOpponentMissilePosition(double[] missilePosition) {
		double xPos = missilePosition[0];
		double yPos = missilePosition[1];
		double xVel = missilePosition[2];
		double yVel = missilePosition[3];
		double active = missilePosition[4];

		Platform.runLater(() -> {
			if (isPlayer1 == true) {
				missileImage2.setVisible(true);
				player2.getMissile().setPositioning(xPos, yPos, xVel, yVel, active, player2.getAngle());
			} else {
				missileImage.setVisible(true);
				player1.getMissile().setPositioning(xPos, yPos, xVel, yVel, active, player1.getAngle());
			}
		});
	}

	@FXML
	void keyPress(KeyEvent event) {
		String code = event.getCode().toString();
		if (!input.contains(code))
			input.add(code);
	}

	@FXML
	void keyRelease(KeyEvent event) {
		String code = event.getCode().toString();
		input.remove(code);
	}

	@Override
	public void start() {
		super.start();
	}

	@Override
	public void stop() {
		super.stop();
	}

	@Override
	public void handle(long nanos) {
		if (isPlayer1 == true) {
			if (input.contains("UP")) {
				player1.moveForwards();
				client.sendCurrentTankPosition(player1.getPositioning());
			}

			if (input.contains("DOWN")) {
				player1.moveBackwards();
				client.sendCurrentTankPosition(player1.getPositioning());
			}

			if (input.contains("LEFT")) {
				player1.decreaseAngle();
				client.sendCurrentTankPosition(player1.getPositioning());
			}

			if (input.contains("RIGHT")) {
				player1.increaseAngle();
				client.sendCurrentTankPosition(player1.getPositioning());
			}

			if (input.contains("SPACE")) {
				if (!player1.getMissile().getActive()) {
					player1.shoot();
					missileImage.setVisible(true);
					client.sendCurrentMissilePosition(player1.getMissile().getPositioning());
				}
			}
		} else {
			if (input.contains("UP")) {
				player2.moveForwards();
				client.sendCurrentTankPosition(player2.getPositioning());
			}

			if (input.contains("DOWN")) {
				player2.moveBackwards();
				client.sendCurrentTankPosition(player2.getPositioning());
			}

			if (input.contains("LEFT")) {
				player2.decreaseAngle();
				client.sendCurrentTankPosition(player2.getPositioning());
			}

			if (input.contains("RIGHT")) {
				player2.increaseAngle();
				client.sendCurrentTankPosition(player2.getPositioning());
			}

			if (input.contains("SPACE")) {
				if (!player2.getMissile().getActive()) {
					player2.shoot();
					missileImage2.setVisible(true);
					client.sendCurrentMissilePosition(player2.getMissile().getPositioning());
				}
			}
		}

		if (player1.getMissile().getActive()) {
			if (!player1.updateMissile(WIDTH, HEIGHT, blockGroup)) {
				missileImage.setVisible(false);
			}

			Missile missileInstance = player1.getMissile();
			if (player2.checkMissileCollisions(missileInstance.getPositionX(), missileInstance.getPositionY(),
					missileInstance.RADIUS)) {
				missileInstance.resetCollisions();
				player2.respawn();
				missileImage.setVisible(false);
			}

			missileImage.setCenterX(missileInstance.getPositionX());
			missileImage.setCenterY(missileInstance.getPositionY());
		}

		if (player2.getMissile().getActive()) {
			if (!player2.updateMissile(WIDTH, HEIGHT, blockGroup)) {
				missileImage2.setVisible(false);
			}

			Missile missileInstance = player2.getMissile();
			if (player1.checkMissileCollisions(missileInstance.getPositionX(), missileInstance.getPositionY(),
					missileInstance.RADIUS)) {
				missileInstance.resetCollisions();
				player1.respawn();
				missileImage2.setVisible(false);
			}

			missileImage2.setCenterX(missileInstance.getPositionX());
			missileImage2.setCenterY(missileInstance.getPositionY());
		}

		ImageView[] rectArray = { wallImage1, wallImage2, wallImage3, wallImage4, wallImage5 };
		for (ImageView b : rectArray) {
			Boolean intersect1 = tankImage.intersects(b.getBoundsInLocal());
			Boolean intersect2 = tankImage2.intersects(b.getBoundsInLocal());

			if (intersect1) {
				player1.respawn();
			}

			if (intersect2) {
				player2.respawn();
			}
		}
		
		if (player1.getLives() == 2) {
			player1Life3.setVisible(false);
		}
		
		if (player1.getLives() == 1) {
			player1Life2.setVisible(false);
		}
		
		if (player2.getLives() == 2) {
			player2Life3.setVisible(false);
		}
		
		if (player2.getLives() == 1) {
			player2Life2.setVisible(false);
		}

		if ((player1.getLives() <= 0) || (player2.getLives() <= 0)) {
			stop();
			tankImage.setVisible(false);
			tankImage2.setVisible(false);
			livesText1.setVisible(false);
			livesText2.setVisible(false);
			
			player1Life1.setVisible(false);
			player1Life2.setVisible(false);
			player1Life3.setVisible(false);
			player2Life1.setVisible(false);
			player2Life2.setVisible(false);
			player2Life3.setVisible(false);
			
			for (ImageView b : rectArray) {
				b.setVisible(false);
			}
			
			if ((isPlayer1 == true) && (player1.getLives() > 0)) {
				gameOver.setText("You Won!");
			} else if ((isPlayer2 == true) && (player2.getLives() > 0)) {
				gameOver.setText("You Won!");
			} else {
				gameOver.setText("You Lost!");
			}
			
			gameOver.setVisible(true);
			returnToLobbyButton.setVisible(true);			
		}

		player1.checkEdgeCollisions(WIDTH, HEIGHT);
		tankImage.setX(player1.getPositionX());
		tankImage.setY(player1.getPositionY());
		tankImage.setRotate(player1.getAngle());

		player2.checkEdgeCollisions(WIDTH, HEIGHT);
		tankImage2.setX(player2.getPositionX());
		tankImage2.setY(player2.getPositionY());
		tankImage2.setRotate(player2.getAngle());

		livesText1.setText("Player 1: ");
		livesText2.setText("Player 2: ");
	}

}
