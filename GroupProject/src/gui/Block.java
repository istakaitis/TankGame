package gui;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class Block {
	
	private double height, width, xPos, yPos, angle;

	public Block(double xPos, double yPos, double width, double height, double angle) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.height = height;
		this.width = width;
		this.angle = angle;
	}

	public double getPositionX() {
		return this.xPos;
	}

	public double getPositionY() {
		return this.yPos;
	}

	public double getAngle() {
		return this.angle;
	}

	public double getHeight() {
		return this.height;
	}

	public double getWidth() {
		return this.width;
	}

	public Rectangle drawRectangle() {
		Rectangle blockImage = new Rectangle(this.xPos, this.yPos, this.width, this.height);
		blockImage.setFill(Color.GREY);
		return blockImage;
	}

	public double[] angleToVector(double angle) {
		double a = Math.toRadians(angle);
		double[] vector = { Math.cos(a), Math.sin(a) };
		return vector;
	}
}
