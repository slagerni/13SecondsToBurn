package com.thirteensecondstoburn.Sudoku;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

public class CellEventListener extends ActorGestureListener {
	Stage stage;
	long timeDown;
	
	CellEventListener(Stage stage) {
		super();
		this.stage = stage;
	}
	
	@Override 
	public boolean longPress (Actor actor, float stageX, float stageY) {
		Cell cell = (Cell)actor;
		if(cell.getNumber() > 0 || !cell.isZoomedTo()) return true;
		
		int col = (int)(stageX/(cell.getWidth()/3));
		int row = (int)((cell.getHeight() - stageY)/(cell.getHeight()/3));
		cell.setNumber((3*row + col) + 1);
		return true;		
	}
	
	@Override
	public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
		timeDown = System.currentTimeMillis();
		//return true;
	}
	
	@Override
	public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
		Cell cell = (Cell)event.getListenerActor();
		if(System.currentTimeMillis() - timeDown > 1000 || cell.getNumber() > 0) {
			event.setRelatedActor(cell);
			return;
		}
		
		event.setRelatedActor(cell);
		if(cell.getZoomedTo() == -1) {
			int onlyPoss = cell.getOnlyPossibility();
			if(onlyPoss > -1) {
				cell.setNumber(onlyPoss + 1);
				return;
			}

            OrthographicCamera cam = (OrthographicCamera)stage.getCamera();

			float scale = 4.5f;
            float newx = cell.getX() + cell.getWidth()/2f;
            float newy = cell.getY() + cell.getHeight()/2f;
			cam.position.set(newx, newy, 0);
            cam.zoom = 1/scale;
            cam.update();
			cell.setZoom();
			return;
		} else if(cell.isZoomedTo()) {
			// actually do some clicking here
			if(cell.getNumber() > 0) {
				cell.setNumber(0);
			} else {
				int col = (int)(x/(cell.getWidth()/3));
				int row = (int)((cell.getHeight() - y)/(cell.getHeight()/3));
				cell.togglePossible((3*row + col));
				
			}
			return;
		}
	}
}
