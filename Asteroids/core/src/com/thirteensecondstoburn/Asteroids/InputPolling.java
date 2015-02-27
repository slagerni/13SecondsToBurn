package com.thirteensecondstoburn.Asteroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class InputPolling {

	public void check() {
	
		float pitch = Gdx.input.getPitch();
		if(pitch > 10 || pitch < -10) {
			if(Data.mainShip.getVelocity() == 0.0) {
				pitch = pitch / 3;
			}
			Data.mainShip.setDirection(Data.mainShip.getDirection() + pitch);
		}
			
		if(Gdx.input.isKeyPressed(Input.Keys.A)) {
			Data.mainShip.setTargetingDirection(Data.mainShip.targetingDirection + 3f);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.getPitch() < -10) {
			Data.mainShip.setTargetingDirection(Data.mainShip.targetingDirection - 3f);
		}
		if(Gdx.input.isKeyPressed( Input.Keys.LEFT)) {
			Data.mainShip.setDirection(Data.mainShip.getDirection() + 3f);
		}
		if(Gdx.input.isKeyPressed( Input.Keys.RIGHT)) {
			Data.mainShip.setDirection(Data.mainShip.getDirection() - 3f);
		}

	}
}
