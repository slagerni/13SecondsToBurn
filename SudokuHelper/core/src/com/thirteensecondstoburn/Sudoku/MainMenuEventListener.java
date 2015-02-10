package com.thirteensecondstoburn.Sudoku;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

public class MainMenuEventListener extends ActorGestureListener {
	@Override
	public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
		Gdx.input.setOnscreenKeyboardVisible(false);
		MainMenu mm = (MainMenu)event.getListenerActor();
		mm.showMainMenu();		
	}
}
