package com.thirteensecondstoburn.Sudoku;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

public class UndoRedoEventListener extends ActorGestureListener {
	@Override
	public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
		UndoRedo undo = (UndoRedo)event.getListenerActor();
		undo.undoRedo();
	}
}
