package com.thirteensecondstoburn.Asteroids;

import java.util.*;

import com.badlogic.gdx.Gdx;

public class Data extends Thread {
	public static String debugString = "";

	public static final int universeSize = 1180 / 2;
	public static final int viewportSize = 1080;
	public static int[] asteroidSizes = { 40, 25, 15 };
	public static int[] oreSizes = { 15, 10, 5 };
	public static float[] oreScales = { 15f/10f, 1f, 5f/10f };
	public static float[] asteroidScales = { 40f / 25f, 1f, 15f / 25f };
	public static int asteroidCount = 5;
	public static int[] shipSizes = { 25, 30, 40 };
	public static int[] torpSizes = { 4, 5, 6 };
	public static Ship mainShip;
	public static int maxTorps = 8;
	public static int level = 1;
	public static int lives = 3;
	public static final int BIG_EXPLOSION_FRAMES = 7;
	public static final int SMALL_EXPLOSION_FRAMES = 5;
	public static final int SMALL_EXPLOSIONS = 4;
	public static final int UPDATE_RATE = 5;

	public static final int GAME_STATE_PRE_GAME = 0;
	public static final int GAME_STATE_ACTIVE = 1;
	public static final int GAME_STATE_DEAD = 2;
	public static final int GAME_STATE_OVER = 3;

	public static int currentGameState = GAME_STATE_PRE_GAME;

	public boolean stop = false;

	static Vector<Asteroid> asteroids = new Vector<Asteroid>();
	static Vector<Ore> oreInstances = new Vector<Ore>();

	static Vector<Ship> ships = new Vector<Ship>();
	{
		mainShip = new Ship();
		ships.add(mainShip);
	}

	public Data() {
	}

	public void run() {
		while (!stop) {
			switch (currentGameState) {
			case GAME_STATE_ACTIVE:
			case GAME_STATE_DEAD:
				float dt = Gdx.graphics.getDeltaTime();
				for (int i = 0; i < asteroids.size(); ++i) {
					asteroids.get(i).update(dt);
				}
				for (int i = 0; i < ships.size(); ++i) {
					ships.get(i).update(dt);
				}
				for(int i=0; i<oreInstances.size(); ++i) {
					oreInstances.get(i).update(dt);
				}
				collisionDetection();
				if (levelComplete()) {
					resetLevel();
				}
			}
			try {
				Thread.sleep(UPDATE_RATE);
			} catch (InterruptedException iex) {
			}
		}
	}

	static Vector<Asteroid> getAsteroids() {
		return asteroids;
	}

	static Vector<Ship> getShips() {
		return ships;
	}

	private void collisionDetection() {
		for (int j = 0; j < asteroids.size(); ++j) {
			Asteroid ast = (Asteroid) asteroids.get(j);
			if (ast.isActive()) {
				for (int i = 0; i < mainShip.getTorps().length; ++i) {
					if (mainShip.getTorps()[i].isActive()) {
						if (ast.collide(mainShip.getTorps()[i].getXPos(), mainShip.getTorps()[i].getYPos(), 1)) {
							ast.damage(mainShip.getTorpDamage());
							mainShip.getTorps()[i].setState(Torp.STATE_EXPLODE);
							Sounds.torpHit();
							if (ast.getState() != Asteroid.STATE_ACTIVE) {
								mainShip.score += asteroidSizes[ast.getSizeIndex()];
							}
						}
					}
				}
				if (Data.currentGameState == Data.GAME_STATE_ACTIVE
						&& ast.collide(mainShip.getXPos(), mainShip.getYPos(),
								Data.shipSizes[mainShip.getSizeIndex()] - 1)) {
					currentGameState = GAME_STATE_DEAD;
					Sounds.myExplosion();
					ast.setState(Asteroid.STATE_EXPLODE);
					mainShip.setState(Ship.STATE_EXPLODE);
					mainShip.setVelocity(0);
					mainShip.speedIndex = 0;
					mainShip.desiredVelocity = 0;
					lives--;
					if (lives == 0) {
						currentGameState = GAME_STATE_OVER;
					}
				}
			}
		}
		
		for(int i=0; i<oreInstances.size(); i++) {
			Ore ore = oreInstances.get(i);
			if(ore.collide(Data.mainShip.getXPos(), Data.mainShip.getYPos(), Data.mainShip.getTractorRange())) {
				oreInstances.remove(ore);
				mainShip.score += ore.getValue();
				Sounds.collect();
			}
		}
	}

	private boolean levelComplete() {
		if(oreInstances.size() > 0) return false;
		for (int j = 0; j < asteroids.size(); ++j) {
			if (!((Asteroid) asteroids.get(j)).isDead()) {
				return false;
			}
		}
		return true;
	}

	private static void resetLevel() {
		for (int j = 0; j < ships.size(); ++j) {
			((Ship) ships.get(j)).reset();
		}

		createAsteroids(5 + (level / 3));
		oreInstances.clear();
		level++;
		currentGameState = GAME_STATE_PRE_GAME;
	}

	public static void startNewGame() {
		mainShip.score = 0;
		level = 0;
		lives = 3;
		resetLevel();
	}
	
	private static void createAsteroids(int num) {
		asteroidCount = num;
		asteroids.clear();
		for (int i = 0; i < num; ++i) {
			asteroids.add(new Asteroid());
		}
	}
	
	public static void addNewOre(Ore ore) {
		oreInstances.add(ore);
	}

}