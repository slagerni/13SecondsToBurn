package com.thirteensecondstoburn.Sudoku;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

public class HighlightEventListener extends ActorGestureListener {
	@Override
	public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
		Highlighter hi = (Highlighter)event.getListenerActor();
		hi.toggle();
	}
}
