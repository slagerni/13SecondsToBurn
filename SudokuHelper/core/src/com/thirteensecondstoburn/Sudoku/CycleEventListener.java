package com.thirteensecondstoburn.Sudoku;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

public class CycleEventListener extends ActorGestureListener {
	@Override
	public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
		Cycle cycle = (Cycle)event.getListenerActor();
		cycle.doCycle();
	}
}
