package com.thirteensecondstoburn.Asteroids;

public class Torp extends MovingObject {

	private float fuseLength;
	private float rotation = 0f;
	private float rotationRate = 1080;
	private float fuse = 0f;
	private double baseVelocity = 30;

	public Torp() {
		setDirection(0);
		setXPos(0);
		setYPos(0);
		setVelocity(baseVelocity);
		setTextureIndex(rand.nextInt(2));
		setSizeIndex(0);
		fuseLength = 10;
	}

	public void fireTorp(double xPos, double yPos, float direction) {
		setXPos(xPos);
		setYPos(yPos);
		setDirection(direction);
		fuse = 0;
		setState(Torp.STATE_ACTIVE);
		setVelocity(baseVelocity + Data.mainShip.getVelocity()/2.0);
	}

	public void update(float deltaTime) {
		if (isActive()) {
			fuse += deltaTime;
			if (fuse > fuseLength) {
				setState(Torp.STATE_DEAD);
				return;
			}
			rotation = (rotationRate * deltaTime);
			if (rotation > 360)
				rotation -= 360;
			super.update(deltaTime);
		} else if(getState() == Torp.STATE_EXPLODE) {
			updateExplosion(deltaTime);
		}
	}

	public float getRotation() {
		return rotation;
	}
}