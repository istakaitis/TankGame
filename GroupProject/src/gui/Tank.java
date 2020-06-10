package gui;

import java.util.ArrayList;

/**
 * Tank is a class to represent the game logic for a
 * tank in a tank game.
 *
 * @Author Jacob Bale
 * @Version 02/03/2020
 */

public class Tank {
	
    final int HEIGHT, WIDTH;
    private double xPos, yPos, xVel, yVel, angle;
    private int lives;
    Missile missile;
    double[] respawnPos = new double[3];
    
    public Tank (double xPos, double yPos, double xVel, double yVel, double angle) {
        this.HEIGHT = 20;
        this.WIDTH = 30;
        this.xPos = xPos;
        this.yPos = yPos;
        this.xVel = xVel;
        this.yVel = yVel;
        this.angle = angle;
        this.missile = new Missile(0,0,3,3,0);
        this.lives = 3;
        this.respawnPos[0] = xPos;
        this.respawnPos[1] = yPos;
        this.respawnPos[2] = angle;
    }
    
    public double[] getPositioning() {
    	double[] positioningArray = {this.xPos, this.yPos, this.angle};
    	return positioningArray;
    }
    
    public void setPositioning(double newX, double newY, double newAngle) {
    	this.xPos = newX;
    	this.yPos = newY;
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

    public int getHeight() {
        return this.HEIGHT;
    }

    public int getWidth() {
        return this.WIDTH;
    }

    public Missile getMissile() {
        return this.missile;
    }
    
    public int getLives() {
    	return this.lives;
    }

    public double[] angleToVector(double angle) {
        double a = Math.toRadians(angle);
        double[] vector = {Math.cos(a), Math.sin(a)};
        return vector;
    }

    public double[] rotateXY(double x, double y) {
        double a = Math.toRadians(this.angle);
        double[] rotatedXY = {
                Math.cos(a) * (x - (this.xPos + this.WIDTH/2)) -
                        Math.sin(a) * (y - (this.yPos + this.HEIGHT/2)) + (this.xPos + this.WIDTH/2),
                Math.sin(a) * (x - (this.xPos + this.WIDTH/2)) +
                        Math.cos(a) * (y - (this.yPos + this.HEIGHT/2)) + (this.yPos + this.HEIGHT/2)};
        return rotatedXY;
    }

    public void moveForwards() {
        double[] forward = angleToVector(this.angle);
        this.xPos += forward[0] * this.xVel;
        this.yPos += forward[1] * this.yVel;
    }

    public void moveBackwards() {
        double[] backward = angleToVector(this.angle);
        this.xPos -= backward[0] * this.xVel ;
        this.yPos -= backward[1] * this.yVel;
    }

    public void increaseAngle() {
        this.angle += 1.20;
    }

    public void decreaseAngle() {
        this.angle -= 1.20;
    }
    
    public void respawn() {
    	this.xPos = this.respawnPos[0];
    	this.yPos = this.respawnPos[1];
    	this.angle = this.respawnPos[2];
    	this.lives -= 1;
    }

    public void shoot() {
        double[] pointXY = rotateXY(this.xPos + WIDTH + 6, this.yPos + HEIGHT - 10);
        
        this.missile.setPositionX(pointXY[0]);
        this.missile.setPositionY(pointXY[1]);
        this.missile.setXVel(10);
        this.missile.setYVel(10);
        this.missile.setAngle(this.getAngle());
        this.missile.setActive(true);
    }
    
	public double findDistance(double fromX, double fromY, double toX, double toY) {
		double a = Math.abs(fromX - toX);
		double b = Math.abs(fromY - toY);

		return Math.sqrt((a * a) + (b * b));
	}
    
    public boolean checkMissileCollisions(double xMissile, double yMissile, double radius) {
		double blockAngleR = -1 * Math.toRadians(this.angle);

		// Rotate circle so it is on the same axis as rectangle
		double unrotatedCircleX = Math.cos(blockAngleR) * (xMissile - (this.xPos + this.WIDTH / 2))
				- Math.sin(blockAngleR) * (yMissile - (this.yPos + this.HEIGHT / 2)) + (this.xPos + this.WIDTH / 2);
		double unrotatedCircleY = Math.sin(blockAngleR) * (xMissile - (this.xPos + this.WIDTH / 2))
				+ Math.cos(blockAngleR) * (yMissile - (this.yPos + this.HEIGHT / 2)) + (this.yPos + this.HEIGHT / 2);

		// find closest point on rectangle relative to rotated circle
		double closestX, closestY;

		// Find the unrotated closest x point from center of unrotated circle
		if (unrotatedCircleX < this.xPos) {
			closestX = this.xPos;
		} else if (unrotatedCircleX > this.xPos + this.WIDTH) {
			closestX = this.xPos + this.WIDTH;
		} else {
			closestX = unrotatedCircleX;
		}

		// Find the unrotated closest y point from center of unrotated circle
		if (unrotatedCircleY < this.yPos) {
			closestY = this.yPos;
		} else if (unrotatedCircleY > this.yPos + this.HEIGHT) {
			closestY = this.yPos + this.HEIGHT;
		} else {
			closestY = unrotatedCircleY;
		}

		double distance = findDistance(unrotatedCircleX, unrotatedCircleY, closestX, closestY);
		if (distance < radius) {
			return true;
		} else {
			return false;
		}
	}
    
	public void checkEdgeCollisions(double width, double height) {
		if ((this.xPos) > width) {
			this.xPos = this.xPos % width;
		} else if ((this.xPos) < 0) {
			this.xPos = (this.xPos + width);
		} else if ((this.yPos) > height) {
			this.yPos = this.yPos % height;
		} else if ((this.yPos) < 0)  {
			this.yPos = (this.yPos + height);
		}
	}
	
	public boolean updateMissile(double width, double height, ArrayList<Block> blockGroup) {
		this.missile.moveForwards();
		
		this.missile.checkEdgeCollisions(width, height);
		
		for (Block b : blockGroup) {
			this.missile.checkBlockCollisions(b.getPositionX(), b.getPositionY(), b.getWidth(), b.getHeight(),
					b.getAngle());
		}
		
		if (checkMissileCollisions(this.missile.getPositionX(), this.missile.getPositionY(),
				this.missile.RADIUS)) {
			this.missile.resetCollisions();
			respawn();
			return false;
		}
		
		if (this.missile.getCollisions() >= 3) {
			this.missile.resetCollisions();
			return false;
		}
		
		return true;
	}
}

