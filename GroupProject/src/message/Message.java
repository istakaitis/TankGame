package message;

import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = 1L;

	private Description description;
	private String[] stringMessage;
	private int[] intMessage;
	private double[] doubleMessage;

	/**
	 * Constructor for string messages
	 * 
	 * @param description
	 * @param stringMessage
	 */
	public Message(Description description, String... stringMessage) {
		this.description = description;
		this.stringMessage = stringMessage;
	}

	/**
	 * Constructor for int messages
	 * 
	 * @param description
	 * @param intMessage
	 */
	public Message(Description description, int... intMessage) {
		this.description = description;
		this.intMessage = intMessage;
	}
	
	/**
	 * 
	 * @param description
	 * @param doubleMessage
	 */
	public Message(Description description, double... doubleMessage) {
		this.description = description;
		this.doubleMessage = doubleMessage;
	}

	/**
	 * Constructor for messages only containing descriptions
	 * 
	 * @param description
	 * @param trueOrFalse
	 */
	public Message(Description description) {
		this.description = description;
	}

	/**
	 * A getter for the description.
	 * 
	 * @return A description.
	 */
	public Description getDescription() {
		return this.description;
	}

	/**
	 * A getter for the string message.
	 * 
	 * @return A string message.
	 */
	public String[] getStringMessage() {
		return this.stringMessage;
	}

	/**
	 * A getter for the integer message.
	 * 
	 * @return An integer message.
	 */
	public int[] getIntMessage() {
		return this.intMessage;
	}
	
	/**
	 * 
	 * @return
	 */
	public double[] getDoubleMessage() {
		return this.doubleMessage;
	}
}
