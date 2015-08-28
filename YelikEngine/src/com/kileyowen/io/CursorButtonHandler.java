package com.kileyowen.io;

import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class CursorButtonHandler extends GLFWMouseButtonCallback {
	private boolean[] buttons;

	public CursorButtonHandler() {
		buttons = new boolean[3];
	}

	@Override
	public void invoke(long window, int button, int action, int mods) {
		buttons[button] = action != 0;
	}

	public boolean isButtonDown(int button) {
		return buttons[button];
	}
	
	public boolean isLeftDown(){
		return buttons[0];
	}
	
	public boolean isRightDown(){
		return buttons[1];
	}
	
	public boolean isMiddleDown(){
		return buttons[2];
	}
}
