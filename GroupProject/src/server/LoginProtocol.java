package server;

import com.tools.linkmode.*;

import message.Description;
import message.Message;

public class LoginProtocol {

	public static Message processLogin(String username, String password) {

		for (ServerThread user : Server.getLoggedInUsers()) {
			if (user.getUsername().equals(username)) {
				return new Message(Description.ALREADY_LOGGED_IN);
			}
		}

		SSHService service = new SSHService();
		User nullUser = null;

		User user = service.selectUser(username);
		if (!(user == nullUser)) {
			if (Server.getLoggedInUsers().size() >= 20) {
				return new Message(Description.SERVER_FULL);
			} else if (user.getLogin_attempts() >= 3) {
				return new Message(Description.ACCOUNT_LOCKED);
			} else if (user.getPassword().equals(password)) {
				user.setLogin_attempts(0);
				service.updateUser(user);
				return new Message(Description.LOGIN_SUCCESSFUL);
			} else {
				user.setLogin_attempts(user.getLogin_attempts() + 1);
				service.updateUser(user);
				return new Message(Description.LOGIN_FAILED);
			}
		}
		return new Message(Description.LOGIN_FAILED);
	}

	public static Message processRegister(String username, String password) {
		if ((!username.matches("[a-zA-Z0-9]+") || !password.matches("[a-zA-Z0-9]+"))
				|| (username.length() > 20 || password.length() > 20)) {
			return new Message(Description.INVALID_CHARACTERS);
		}

		SSHService service = new SSHService();
		User nullUser = null;

		User user = service.selectUser(username);
		// if username already registered, prevent registration
		if (!(user == nullUser)) {
			return new Message(Description.REGISTER_FAILED);
		} else if (Server.getLoggedInUsers().size() >= 20) {
			return new Message(Description.SERVER_FULL);
		} else {
			User newUser = new User();
			Statistics newStats = new Statistics();
			// update user table
			newUser.setUsername(username);
			newUser.setPassword(password);
			newUser.setLogin_attempts(0);
			service.insertUser(newUser);
			// update statistics table
			newStats.setUsername(username);
			newStats.setGames_played(0);
			newStats.setWins(0);
			newStats.setLosses(0);
			newStats.setTotal_kills(0);
			newStats.setTotal_deaths(0);
			newStats.setKill_death_ratio();
			newStats.setWin_rate();
			service.updateStatistics(newStats);

			return new Message(Description.REGISTER_SUCCESSFUL);
		}

	}
}
