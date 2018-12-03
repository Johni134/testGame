package ru.brainmove.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import ru.brainmove.Star2DGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		float aspect = 3f / 4f;
		config.width = 350;
		config.height = (int) (config.width / aspect);
		config.resizable = false;
		new LwjglApplication(new Star2DGame(), config);
	}
}
