package com.thirteensecondstoburn.Asteroids;

public class Ore extends MovingObject {
	private int value = 0;

	private float rotation = rand.nextInt(360);
	private float rotationRate = rand.nextFloat() * 172.0f;
	public float xRotation = rand.nextFloat() * 2f - 1.0f;
	public float yRotation = rand.nextFloat() * 2f - 1.0f;
	public float zRotation = rand.nextFloat() * 2f - 1.0f;

	public Ore(double x, double y, double velocity, int value, int sizeIndex) {
		this.value = value;
		setDirection(rand.nextInt(360));
		setPosition(x, y);
		setVelocity(velocity);
		setSizeIndex(sizeIndex);
	}

	public int getValue() {
		return value;
	}
	
	public float getRotation() {
		return rotation;
	}

	public void update(float deltaTime) {
		rotation += rotationRate * deltaTime;
		if (rotation > 360)
			rotation -= 360;
		super.update(deltaTime);
	}

}
