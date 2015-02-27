package com.thirteensecondstoburn.Asteroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class BasicInput implements InputProcessor {
	int startX1 = -1;
	int startX2 = -1;
	int startY1 = -1;
	int startY2 = -1;
	long lastSpeedUp = 0;

	@Override
	public boolean keyDown(int keycode) {
		// anything in here is just a convenience for testing on the desktop
		switch (keycode) {
		case Input.Keys.W:
			Data.mainShip.accelerate();
			break;
		case Input.Keys.S:
			Data.mainShip.decelerate();
			break;
		case Input.Keys.SPACE:
			if (Data.currentGameState == Data.GAME_STATE_ACTIVE) {
				Data.mainShip.fireTorp(Data.mainShip.targetingDirection);
				Sounds.fireTorp();
			}
			break;

		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (pointer == 0) {
			startX1 = screenX;
			startY1 = screenY;
		} else {
			startX2 = screenX;
			startY2 = screenY;
		}

		int screenWidth = Gdx.graphics.getWidth();
		int screenHeight = Gdx.graphics.getHeight();
		int sideWidth = (screenWidth - screenHeight) / 2;

		if (Data.currentGameState == Data.GAME_STATE_ACTIVE) {
			int centerX = Gdx.graphics.getWidth() / 2;
			int centerY = Gdx.graphics.getHeight() / 2;

			double theta = Math.atan2(screenX - centerX, screenY - centerY);
			theta -= Math.PI / 2;
			double angle = Math.toDegrees(theta);
			if (angle < 0) {
				angle += 360;
			}

			if (screenX > sideWidth && screenX < screenWidth - sideWidth) {
				// this should only happen on the desktop for debugging
				if (button == Input.Buttons.RIGHT) {
					Data.mainShip.targetingDirection = (float) angle;
				} else {
					Data.mainShip.setDirection((int) angle);
				}
				return true;
			}
			if (screenX > screenWidth - sideWidth
					&& screenY > screenHeight - sideWidth) {
				Data.mainShip.fireTorp(Data.mainShip.targetingDirection);
				Sounds.fireTorp();
				return true;
			}

		} else if (screenX > sideWidth && screenX < screenWidth - sideWidth) {
			if (Data.currentGameState == Data.GAME_STATE_OVER) {
				Data.startNewGame();
			} else {
				Data.currentGameState = Data.GAME_STATE_ACTIVE;
				Data.mainShip.setState(Ship.STATE_ACTIVE);
			}
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		int startX, startY;
		if (pointer == 0) {
			startX = startX1;
			startY = startY1;
		} else {
			startX = startX2;
			startY = startY2;
		}
		int screenHeight = Gdx.graphics.getHeight();

		// make sure we started and ended above the targeting box
		if (startX < 420 && startY < screenHeight - 420 && screenX < 420
				&& screenY < screenHeight - 420) {
			if (System.currentTimeMillis() - lastSpeedUp > 250) {
				// make sure we moved more then just a touch
				if (startY - screenY > 20 || screenY - startY > 20) {
					if (startY < screenY) {
						Data.mainShip.decelerate();
						lastSpeedUp = System.currentTimeMillis();
					} else {
						Data.mainShip.accelerate();
						lastSpeedUp = System.currentTimeMillis();
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		int screenHeight = Gdx.graphics.getHeight();

		if (screenX < 420 && screenY > screenHeight - 420) {
			int centerX = 210;
			int centerY = screenHeight - 210;

			double theta = Math.atan2(screenX - centerX, screenY - centerY);
			theta -= Math.PI / 2;
			double angle = Math.toDegrees(theta);
			if (angle < 0) {
				angle += 360;
			}

			Data.mainShip.targetingDirection = (float) angle;
			return true;
		}

		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
