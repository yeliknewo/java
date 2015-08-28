package com.kileyowen.io;

import org.lwjgl.glfw.GLFWKeyCallback;
import static org.lwjgl.glfw.GLFW.*;

public class KeyHandler extends GLFWKeyCallback {
	
	private boolean[] keys;
	
	public KeyHandler(){
		keys = new boolean[65536];
	}

	@Override
	public void invoke(long window, int key, int scanCode, int action, int mods) {
		keys[key] = action != GLFW_RELEASE;
	}
	
	public boolean isKeyDown(int keyCode){
		return keys[keyCode];
	}
}
