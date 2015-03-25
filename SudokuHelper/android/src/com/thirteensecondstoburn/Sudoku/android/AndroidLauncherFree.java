package com.thirteensecondstoburn.Sudoku.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.thirteensecondstoburn.Sudoku.SudokuGame;

/**
 * Created by Nick on 3/25/2015.
 */
public class AndroidLauncherFree extends AndroidApplication {
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new SudokuGame(true), config);
    }
}
