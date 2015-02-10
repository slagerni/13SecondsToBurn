package com.thirteensecondstoburn.Sudoku.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.thirteensecondstoburn.Sudoku.SudokuGame;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(540, 960);
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return new SudokuGame();
        }
}