package com.sorry.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.sorry.game.Sorry;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "Sorry! The Board Game";
		config.width = 512;
		config.width = 512;

		new LwjglApplication(new Sorry(), config);
	}
}
