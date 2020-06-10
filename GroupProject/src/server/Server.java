package server;

import java.net.*;
import java.util.ArrayList;

import com.tools.linkmode.SSHService;

import java.io.*;

public class Server {

	/**
	 * An ArrayList of logged in users.
	 */
	private static ArrayList<ServerThread> loggedInUsers = new ArrayList<ServerThread>();

	// ArrayLists of users in each room
	private static ArrayList<ServerThread> roomOneUsers = new ArrayList<ServerThread>();
	private static ArrayList<ServerThread> roomTwoUsers = new ArrayList<ServerThread>();
	private static ArrayList<ServerThread> roomThreeUsers = new ArrayList<ServerThread>();
	private static ArrayList<ServerThread> roomFourUsers = new ArrayList<ServerThread>();
	private static ArrayList<ServerThread> roomFiveUsers = new ArrayList<ServerThread>();

	/**
	 * A getter for the logged in users.
	 * 
	 * @return The users logged into the server.
	 */
	public static ArrayList<ServerThread> getLoggedInUsers() {
		return loggedInUsers;
	}

	/**
	 * A getter for the users in room 1.
	 * 
	 * @return The users in room 1.
	 */
	public static ArrayList<ServerThread> getRoomOneUsers() {
		return roomOneUsers;
	}

	/**
	 * A getter for the users in room 2.
	 * 
	 * @return The users in room 2.
	 */
	public static ArrayList<ServerThread> getRoomTwoUsers() {
		return roomTwoUsers;
	}

	/**
	 * A getter for the users in room 3.
	 * 
	 * @return The users in room 3.
	 */
	public static ArrayList<ServerThread> getRoomThreeUsers() {
		return roomThreeUsers;
	}

	/**
	 * A getter for the users in room 4.
	 * 
	 * @return The users in room 4.
	 */
	public static ArrayList<ServerThread> getRoomFourUsers() {
		return roomFourUsers;
	}

	/**
	 * A getter for the users in room 5.
	 * 
	 * @return The users in room 5.
	 */
	public static ArrayList<ServerThread> getRoomFiveUsers() {
		return roomFiveUsers;
	}

	/**
	 * The main method of the server which accepts client sockets and start a server
	 * thread.
	 */
	public static void main(String[] args) {
		
		SSHService.sshRun();

		try (ServerSocket serverSocket = new ServerSocket(50000)) {
			while (true) {
				new ServerThread(serverSocket.accept()).start();
			}
		} catch (IOException e) {
			System.err.println("Could not listen in on port 50000");
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
