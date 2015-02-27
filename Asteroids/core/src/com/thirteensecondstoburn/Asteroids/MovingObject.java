package com.thirteensecondstoburn.Asteroids;

import java.util.*;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

public class MovingObject {
	public static final int STATE_ACTIVE = 0;
	public static final int STATE_EXPLODE = 1;
	public static final int STATE_DEAD = 2;

	protected int state;
	private int explosionState = -1;
	public int explosionIndex= 0;
	private float explosionTotalDuration = 1500f;
	private float explostionDelta = 0;
	protected float explosionFrames = 5f;

	private float direction;
	private double xPos;
	private double yPos;
	private double velocity;
	protected static Random rand = new Random(System.currentTimeMillis());
	private int texIndex;
	private int sizeIndex;

	public MovingObject() {
	}

	public float getDirection() {
		return direction;
	}

	public void setDirection(float dir) {
		direction = dir;
	}

	public double getXPos() {
		return xPos;
	}

	public void setXPos(double x) {
		xPos = x;
	}

	public double getYPos() {
		return yPos;
	}

	public void setYPos(double y) {
		yPos = y;
	}
	
	public void setPosition(double x, double y) {
		setXPos(x);
		setYPos(y);
	}

	public double getVelocity() {
		return velocity;
	}

	public void setVelocity(double vel) {
		velocity = vel;
	}

	public void update(float deltaTime) {
		xPos += (velocity * deltaTime) * Math.cos(Math.toRadians(direction));
		if (xPos > Data.universeSize) {
			xPos -= Data.universeSize;
		}
		if (xPos < 0) {
			xPos += Data.universeSize;
		}
		yPos += (velocity * deltaTime) * Math.sin(Math.toRadians(direction));
		if (yPos > Data.universeSize) {
			yPos -= Data.universeSize;
		}
		if (yPos < 0) {
			yPos += Data.universeSize;
		}
	}

	public int getSizeIndex() {
		return sizeIndex;
	}

	public void setSizeIndex(int index) {
		sizeIndex = index;
	}

	public int getTextureIndex() {
		return texIndex;
	}

	public void setTextureIndex(int index) {
		texIndex = index;
	}

	public String toString() {
		return ("X: " + xPos + " Y: " + yPos + " DIR: " + direction
				+ " Velocity: " + velocity);
	}

	public boolean isActive() {
		return (state == STATE_ACTIVE);
	}
	
	public boolean isDead() {
		return (state == STATE_DEAD);
	}

	public void setState(int state) {
		this.state = state;
		if (state == STATE_EXPLODE) {
			explosionState = 0;
			explostionDelta = 0;
		}
	}

	public int getState() {
		return state;
	}

	public int getExplosionState() {
		return explosionState;
	}

	public boolean collide(double px, double py, int radius) {
		Circle otherCircle = new Circle((float) px, (float) py, radius);

		boolean collided = false;

		int size = Data.universeSize;
		for (int x = -size; x <= size; x += size) {
			for (int y = -size; y <= size; y += size) {
				Circle asteroidCircle = new Circle(x + (float) getXPos(), y
						+ (float) getYPos(), Data.asteroidSizes[getSizeIndex()]);
				if (Intersector.overlaps(asteroidCircle, otherCircle)) {
					collided = true;
				}
			}
		}

		return collided;
	}

	public void updateExplosion(float deltaTime) {
		explostionDelta += deltaTime;
		explosionState = (int) (explostionDelta * 1000f)
				/ (int) (explosionTotalDuration / explosionFrames);
		if (explostionDelta > explosionTotalDuration || explosionState > 4) {
			explosionState = -1;
			state = STATE_DEAD;
		}
	}

}