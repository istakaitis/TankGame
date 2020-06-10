package gui;

import java.io.IOException;

import client.Client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import message.Description;
import message.Message;

public class LoginController {

	public Client client;

	@FXML
	private TextField usernameText;

	@FXML
	private PasswordField passwordText;

	@FXML
	private Button loginButton;

	@FXML
	private Button registerButton;

	@FXML
	private Text loginWarningMessage;

	@FXML
	private Text lockedWarningMessage;

	@FXML
	private Text registerWarningMessage;

	@FXML
	private Text invalidFormatMessage;

	@FXML
	private Text alreadyLoggedInMessage;

	@FXML
	private Text serverFullMessage;

	@FXML
	private Text funFact;

	/**
	 * Method generates random text shown on the login screen using FunFact
	 */

	public void setRandomText() {
		funFact.setText(new FunFact().toString());
	}

	/**
	 * A getter for the username.
	 * 
	 * @return The username.
	 */
	public String getUsername() {
		return usernameText.getText();
	}

	/**
	 * A getter for the password.
	 * 
	 * @return The password.
	 */
	public String getPassword() {
		return passwordText.getText();
	}

	/**
	 * This method changes the scene to the lobby if the login details are correct
	 * or displays an error message if they are incorrect.
	 * 
	 * @param event Interaction with the login button.
	 */
	@FXML
	public void login(ActionEvent event) {
		String username = getUsername();
		String password = getPassword();

		Message loginSuccess = client.attemptLogin(username, password);
		// if login unsuccessful, display error message on GUI
		if (loginSuccess.getDescription() == Description.SERVER_FULL) {
			// hide other warnings
			registerWarningMessage.setVisible(false);
			lockedWarningMessage.setVisible(false);
			invalidFormatMessage.setVisible(false);
			alreadyLoggedInMessage.setVisible(false);
			loginWarningMessage.setVisible(false);
			// show relevant warning
			serverFullMessage.setVisible(true);
		} else if (loginSuccess.getDescription() == Description.LOGIN_FAILED) {
			// hide other warnings
			registerWarningMessage.setVisible(false);
			lockedWarningMessage.setVisible(false);
			invalidFormatMessage.setVisible(false);
			alreadyLoggedInMessage.setVisible(false);
			serverFullMessage.setVisible(false);
			// show relevant warning
			loginWarningMessage.setVisible(true);
		} else if (loginSuccess.getDescription() == Description.ACCOUNT_LOCKED) {
			// hide other warnings
			registerWarningMessage.setVisible(false);
			loginWarningMessage.setVisible(false);
			invalidFormatMessage.setVisible(false);
			alreadyLoggedInMessage.setVisible(false);
			serverFullMessage.setVisible(false);
			// show relevant warning
			lockedWarningMessage.setVisible(true);
		} else if (loginSuccess.getDescription() == Description.ALREADY_LOGGED_IN) {
			// hide other warnings
			registerWarningMessage.setVisible(false);
			lockedWarningMessage.setVisible(false);
			loginWarningMessage.setVisible(false);
			invalidFormatMessage.setVisible(false);
			serverFullMessage.setVisible(false);
			// show relevant warning
			alreadyLoggedInMessage.setVisible(true);
		} else {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Lobby.fxml"));
				Parent lobbyParent = fxmlLoader.load();

				Scene lobbyScene = new Scene(lobbyParent);

				// set new stage
				Stage nextWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
				nextWindow.setScene(lobbyScene);

				// set correct controller
				client.lobbyController = fxmlLoader.getController();
				client.lobbyController.client = client;

				// start clientListener
				client.createClientListener();
				client.clientListener.start();

				// if window is closed, terminate the client
				nextWindow.setOnCloseRequest(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent t) {
						Platform.exit();
						System.exit(0);
					}
				});

				nextWindow.show();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param event Interaction with the register button.
	 */
	@FXML
	public void register(ActionEvent event) {
		String username = getUsername();
		String password = getPassword();

		Message registerSuccess = client.attemptRegister(username, password);

		if (registerSuccess.getDescription() == Description.SERVER_FULL) {
			// hide other warnings
			registerWarningMessage.setVisible(false);
			lockedWarningMessage.setVisible(false);
			invalidFormatMessage.setVisible(false);
			alreadyLoggedInMessage.setVisible(false);
			loginWarningMessage.setVisible(false);
			// show relevant warning
			serverFullMessage.setVisible(true);
		} else if (registerSuccess.getDescription() == Description.REGISTER_FAILED) {
			// hide other warnings
			loginWarningMessage.setVisible(false);
			lockedWarningMessage.setVisible(false);
			registerWarningMessage.setVisible(false);
			alreadyLoggedInMessage.setVisible(false);
			serverFullMessage.setVisible(false);
			// show relevant warning
			registerWarningMessage.setVisible(true);
		} else if (registerSuccess.getDescription() == Description.INVALID_CHARACTERS) {
			// hide other warnings
			loginWarningMessage.setVisible(false);
			lockedWarningMessage.setVisible(false);
			invalidFormatMessage.setVisible(false);
			alreadyLoggedInMessage.setVisible(false);
			serverFullMessage.setVisible(false);
			// show relevant warning
			invalidFormatMessage.setVisible(true);
		} else {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Lobby.fxml"));
				Parent lobbyParent = fxmlLoader.load();

				Scene lobbyScene = new Scene(lobbyParent);

				// set new stage
				Stage nextWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
				nextWindow.setScene(lobbyScene);

				// set correct controller
				client.lobbyController = fxmlLoader.getController();
				client.lobbyController.client = client;

				// start clientListener
				client.createClientListener();
				client.clientListener.start();

				// if window is closed, terminate the client
				nextWindow.setOnCloseRequest(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent t) {
						Platform.exit();
						System.exit(0);
					}
				});

				nextWindow.show();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
