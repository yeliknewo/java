package com.kileyowen.tradegame;

import org.lwjgl.glfw.GLFWKeyCallback;
import static org.lwjgl.glfw.GLFW.*;

public class KeyboardHandler extends GLFWKeyCallback {
	
	private static boolean[] keys = new boolean[65536];

	@Override
	public void invoke(long window, int key, int scanCode, int action, int mods) {
		keys[key] = action != GLFW_RELEASE;
	}
	
	public static boolean isKeyDown(int keyCode){
		return keys[keyCode];
	}
}
