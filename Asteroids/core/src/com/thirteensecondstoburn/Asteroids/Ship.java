package com.thirteensecondstoburn.Asteroids;

public class Ship extends MovingObject {

	private Torp torps[] = new Torp[Data.maxTorps];
	public int score;
	public float desiredDirection;
	public float targetingDirection = 90;
	public float rotationRate = 100f;
	private float[] speeds = {0f, 12f, 27f};
	private int torpDamage = 12;
	public int speedIndex = 0;
	public float acceleration = 2f;
	public float desiredVelocity = 0;
	private int tractorRange = 10;

	public Ship() {
		for (int i = 0; i < torps.length; ++i) {
			torps[i] = new Torp();
		}
		setTextureIndex(rand.nextInt(2));
		setSizeIndex(0);
		score = 0;
		reset();
	}

	public void reset() {
		desiredDirection = 90;
		super.setDirection(90);
		setXPos(Data.universeSize / 2);
		setYPos(Data.universeSize / 2);
		setVelocity(0);
		desiredVelocity = 0;
		speedIndex = 0;
		setState(STATE_ACTIVE);
		for (int i = 0; i < torps.length; ++i) {
			torps[i].setState(Torp.STATE_DEAD);
		}
	}

	public boolean fireTorp(float direction) {
		for (int i = 0; i < torps.length; ++i) {
			if (!torps[i].isActive()) {
				torps[i].fireTorp(getXPos(), getYPos(), direction);
				return true;
			}
		}
		return false;
	}

	public Torp[] getTorps() {
		return torps;
	}
	
	public int getTorpDamage() {
		return torpDamage;
	}

	public void setDirection(float dir) {
		desiredDirection = dir;
		if (desiredDirection < 0)
			desiredDirection += 360;
	}
	
	public void setTargetingDirection(float dir) {
		targetingDirection = dir;
	}

	public void update(float deltaTime) {
		if (Data.currentGameState == Data.GAME_STATE_ACTIVE) {
			updateDirection(deltaTime);
			updateVelocity(deltaTime);
			super.update(deltaTime);
		}

		if (state == STATE_EXPLODE) {
			updateExplosion(deltaTime);
		}

		for (int i = 0; i < torps.length; ++i) {
			torps[i].update(deltaTime);
		}
	}

	private void updateDirection(float deltaTime) {
		float actual = getDirection();
		float desired = desiredDirection;

		// super.setDirection(desiredDirection);
		// if(true) return;
		if (Math.abs(actual - desired) < 1)
			return;

		boolean pos = true;
		if ((desired < actual && (360 - actual + desired > actual - desired))
				|| (desired > actual && (360 - desired + actual < desired
						- actual))) {
			pos = false;
		}
		double rate = rotationRate / (getVelocity() * .15 + 1);
		if (pos) {
			actual += rate * deltaTime;
			if (Math.abs(actual - desired) < 1
					|| Math.abs(actual - 360 - desired) < 1)
				actual = desired;
			super.setDirection(actual);
		} else {
			actual -= rate * deltaTime;
			if (actual < 0)
				actual += 360;
			if (Math.abs(actual - desired) < 1
					|| Math.abs(actual - 360 - desired) < 1)
				actual = desired;
			super.setDirection(actual);
		}
	}
	
	private void updateVelocity(float deltaTime) {
		float actual = (float)getVelocity();
		float desired = desiredVelocity;
		
		if(actual == desired) return;
		if(Math.abs(actual - desired) < 1f) {
			setVelocity(desired);
			return;
		}
		
		if(actual < desired) {
			actual += deltaTime * acceleration;
		} else {
			actual -= deltaTime * acceleration;
		}
		setVelocity(actual);
	}
	
	public void accelerate() {
		if(speedIndex < speeds.length -1) {
			desiredVelocity = speeds[++speedIndex];
			Sounds.speedUp();
		}
	}

	public void decelerate() {
		if(speedIndex > 0) {
			desiredVelocity = speeds[--speedIndex];
			Sounds.speedDown();
		}
	}
	
	public int getTractorRange() {
		return tractorRange;
	}
}