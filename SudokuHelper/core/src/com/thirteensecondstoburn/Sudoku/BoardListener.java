package com.thirteensecondstoburn.Sudoku;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class BoardListener extends ClickListener {
	Stage stage;
	Data data;
	ZoomOut zoomOut;
	
	public BoardListener(Stage stage, Data data, ZoomOut zoomOut) {
		super();
		this.stage = stage;
		this.data = data;
		this.zoomOut = zoomOut;
	}
	
	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		return true;
	}
	
	@Override
	public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
		if(event.getRelatedActor() == null || !((Cell)event.getRelatedActor()).isZoomedTo()) {
            int width = Gdx.graphics.getWidth();
            OrthographicCamera cam = (OrthographicCamera)stage.getCamera();
            cam.zoom = 1;
            cam.position.set(width/2, width/2, 0);
            cam.update();
			data.setZoom(-1);
			zoomOut.setVisible(false);
		}
	}
}
