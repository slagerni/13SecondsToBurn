package com.thirteensecondstoburn.Asteroids;

public class Asteroid extends MovingObject {

	private float rotation = rand.nextInt(360);
	private float rotationRate = rand.nextFloat() * 40.0f;
	public float xRotation = rand.nextFloat() * 2f - 1.0f;
	public float yRotation = rand.nextFloat() * 2f - 1.0f;
	public float zRotation = rand.nextFloat() * 2f - 1.0f;
	
	private int damage;
	private int maxDamage;

	public Asteroid() {
		reset();
	}

	public void reset() {
		setDirection(rand.nextInt(360));
		setXPos(rand.nextInt(Data.universeSize / 2) - Data.universeSize / 4);
		setYPos(rand.nextInt(Data.universeSize / 2) - Data.universeSize / 4);
		setVelocity(5.0 + Data.level * rand.nextDouble());
		setTextureIndex(rand.nextInt(2));
		setSizeIndex(rand.nextInt(3));
		maxDamage = damage = (3 - getSizeIndex()) * 10 + (Data.level);
		state = STATE_ACTIVE;
		explosionIndex = rand.nextInt(Data.SMALL_EXPLOSIONS);
	}

	public float getRotation() {
		return rotation;
	}

	public void damage(int amount) {
		damage -= amount;
		if (damage <= 0) {
			setState(STATE_EXPLODE);
			Sounds.otherExplode();
			Data.addNewOre(new Ore(getXPos(), getYPos(), getVelocity()/2.0, getSizeIndex() * Data.level, getSizeIndex()));
		}
	}

	public int getDamage() {
		return damage;
	}
	
	public int getMaxDamage() {
		return maxDamage;
	}

	public void update(float deltaTime) {
		switch (state) {
		case STATE_EXPLODE:
			updateExplosion(deltaTime);
			break;
		default:
			rotation += rotationRate * deltaTime;
			if (rotation > 360)
				rotation -= 360;
			super.update(deltaTime);
		}
	}
}