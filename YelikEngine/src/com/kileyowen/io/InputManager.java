package com.kileyowen.io;

import static org.lwjgl.glfw.GLFW.*;

import com.kileyowen.math.Vector2f;

public class InputManager {
	private CursorPosHandler cursorPos;
	private CursorButtonHandler cursorButton;
	private KeyHandler keys;

	public InputManager(long window) {
		glfwSetKeyCallback(window, keys = new KeyHandler());
		glfwSetCursorPosCallback(window, cursorPos = new CursorPosHandler());
		glfwSetMouseButtonCallback(window, cursorButton = new CursorButtonHandler());
	}

	public boolean isKeyDown(int keyCode) {
		return keys.isKeyDown(keyCode);
	}

	public Vector2f getCursorPos() {
		return cursorPos.getPos();
	}

	public float getCursorPosX() {
		return cursorPos.getX();
	}

	public float getCursorPosY() {
		return cursorPos.getY();
	}

	public boolean getLeftMouseDown() {
		return cursorButton.isLeftDown();
	}

	public boolean getRightMouseDown() {
		return cursorButton.isRightDown();
	}

	public boolean getMiddleMouseDown() {
		return cursorButton.isMiddleDown();
	}
}
