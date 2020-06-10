package gui;

import javafx.scene.shape.Rectangle;

public class Missile {
	final int RADIUS;
	private double xPos, yPos, xVel, yVel, angle;
	private int collisions;
	private double active;

	public Missile(double xPos, double yPos, double xVel, double yVel, double angle) {

		this.RADIUS = 5;
		// double[] startingPositionAngle = angleToVector(angle);
		this.xPos = xPos;// += startingPositionAngle[0];
		this.yPos = yPos;// += startingPositionAngle[1];
		this.xVel = xVel;
		this.yVel = yVel;
		this.angle = angle;
		this.collisions = 0;
		this.active = 0;
	}

	public double[] getPositioning() {
		double[] positioningArray = { this.xPos, this.yPos, this.xVel, this.yVel, this.active };
		return positioningArray;
	}

	public void setPositioning(double newXPos, double newYPos, double newXVel, double newYVel, double newActive, double newAngle) {
		this.xPos = newXPos;
		this.yPos = newYPos;
		this.xVel = newXVel;
		this.yVel = newYVel;
		this.active = newActive;
		this.angle = newAngle;
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

	public int getRadius() {
		return this.RADIUS;
	}

	public int getCollisions() {
		return this.collisions;
	}

	public boolean getActive() {

		if (this.active == 0) {
			return false;
		} else {
			return true;
		}
	}

	public void setPositionX(double newX) {
		this.xPos = newX;
	}

	public void setPositionY(double newY) {
		this.yPos = newY;
	}

	public void setXVel(double newXVel) {
		this.xVel = newXVel;
	}

	public void setYVel(double newYVel) {
		this.yVel = newYVel;
	}

	public void setAngle(double newAngle) {
		this.angle = newAngle;
	}

	public void setActive(boolean active) {
		if (active == false) {
			this.active = 0;
		} else {
			this.active = 1;
		}
	}

	public void resetCollisions() {
		this.collisions = 0;
		this.active = 0;
	}

	public double[] angleToVector(double angle) {
		double a = Math.toRadians(angle);
		double[] vector = { Math.cos(a), Math.sin(a) };
		return vector;
	}

	public void moveForwards() {
		double[] forward = angleToVector(this.angle);
		this.xPos += forward[0] * this.xVel;
		this.yPos += forward[1] * this.yVel;
	}

	public void checkEdgeCollisions(double width, double height) {
		if ((this.xPos + this.RADIUS) > width) {
			this.xPos = this.xPos % width;
		} else if ((this.xPos - this.RADIUS) < 0) {
			this.xPos = (this.xPos + width);
		} else if ((this.yPos + this.RADIUS) > height) {
			this.yPos = this.yPos % height;
		} else if ((this.yPos - this.RADIUS) < 0)  {
			this.yPos = (this.yPos + height);
		}
	}

	public double findDistance(double fromX, double fromY, double toX, double toY) {
		double a = Math.abs(fromX - toX);
		double b = Math.abs(fromY - toY);

		return Math.sqrt((a * a) + (b * b));
	}

	public void checkBlockCollisions(double xBlock, double yBlock, double widthBlock, double heightBlock,
			double angleBlock) {
		double blockAngleR = -1 * Math.toRadians(angleBlock);

		// Rotate circle so it is on the same axis as rectangle
		double unrotatedCircleX = Math.cos(blockAngleR) * (this.xPos - (xBlock + widthBlock / 2))
				- Math.sin(blockAngleR) * (this.yPos - (yBlock + heightBlock / 2)) + (xBlock + widthBlock / 2);
		double unrotatedCircleY = Math.sin(blockAngleR) * (this.xPos - (xBlock + widthBlock / 2))
				+ Math.cos(blockAngleR) * (this.yPos - (yBlock + heightBlock / 2)) + (yBlock + heightBlock / 2);

		// find closest point on rectangle relative to rotated circle
		double closestX, closestY;
		boolean hitsTopBottom = true;

		// Find the unrotated closest x point from center of unrotated circle
		if (unrotatedCircleX < xBlock) {
			closestX = xBlock;
		} else if (unrotatedCircleX > xBlock + widthBlock) {
			closestX = xBlock + widthBlock;
		} else {
			closestX = unrotatedCircleX;
		}

		// Find the unrotated closest y point from center of unrotated circle
		if (unrotatedCircleY < yBlock) {
			closestY = yBlock;
		} else if (unrotatedCircleY > yBlock + heightBlock) {
			closestY = yBlock + heightBlock;
		} else {
			closestY = unrotatedCircleY;
		}

		double distance = findDistance(unrotatedCircleX, unrotatedCircleY, closestX, closestY);
		
		
		if (distance < this.RADIUS) {
				
			if ((this.xPos < closestX)) {
				hitsTopBottom = true;
				System.out.println("Hitting left side");
			} else if (this.xPos > closestX) {
				System.out.println("Hitting right side");
				hitsTopBottom = true;
			} else if ((this.yPos < closestY)) {
				System.out.println("Hitting top");
				hitsTopBottom = false;
			} else if ((this.yPos > closestY)) {
				System.out.println("Hitting bottom");
				hitsTopBottom = false;
			}			
			
			collisions += 1;

			if (hitsTopBottom) {	
				this.xVel *= -1;
			} else {
				this.yVel *= -1;
			}
		}
	}
}
