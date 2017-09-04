package com.rpbytes.tikichapas.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.rpbytes.tikichapas.TikiChapasGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 576;
		config.width = 1024;
		//config.fullscreen = true;
		new LwjglApplication(new TikiChapasGame(), config);
	}
}
