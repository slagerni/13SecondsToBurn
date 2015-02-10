package com.thirteensecondstoburn.Sudoku.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.thirteensecondstoburn.Sudoku.SudokuGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Sudoku Helper";
        config.resizable = false;
        // 1/2 my galaxy phone
        config.height = 1920/2;
        config.width = 1080/2;
        // kindle
//        config.height = 853;
//        config.width = 533;
        // kindle fire HD / Galaxy Note
//        config.height = 1280;
//        config.width = 800;

		new LwjglApplication(new SudokuGame(), config);
	}
}
