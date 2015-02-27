package com.thirteensecondstoburn.Asteroids.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.thirteensecondstoburn.Asteroids.AsteroidsGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Asteroids";
        config.width = 1920;
        config.height = 1080;

		new LwjglApplication(new AsteroidsGame(), config);
	}
}
