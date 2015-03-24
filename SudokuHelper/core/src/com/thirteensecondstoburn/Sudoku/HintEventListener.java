package com.thirteensecondstoburn.Sudoku;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

public class HintEventListener extends ActorGestureListener {
	@Override
	public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
		Hint hint = (Hint)event.getListenerActor();
        int used = ++hint.getGame().settings.hintsUsed;
        if(!SudokuGame.IS_FREE_VERSION || used <= 50) {
            hint.showHint();
        }
        if(SudokuGame.IS_FREE_VERSION && (used % 5 == 0 || used > 50)) {
            if(used > 50) {
                hint.getGame().settings.hintsUsed--;
            }
            hint.getGame().setScreen(hint.getGame().getNagScreen(true));
        }
	}
}
