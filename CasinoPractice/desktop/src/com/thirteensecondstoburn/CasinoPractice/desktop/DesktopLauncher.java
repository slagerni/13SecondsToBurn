package com.thirteensecondstoburn.CasinoPractice.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.thirteensecondstoburn.CasinoPractice.CasinoPracticeGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Casino Practice";
        config.resizable = false;
        // 1/2 my galaxy phone
        config.width = 1920/2;
        config.height = 1080/2;
		new LwjglApplication(new CasinoPracticeGame(), config);
	}
}
