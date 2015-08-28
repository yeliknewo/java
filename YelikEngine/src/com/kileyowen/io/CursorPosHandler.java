package com.kileyowen.io;

import org.lwjgl.glfw.GLFWCursorPosCallback;

import com.kileyowen.math.Vector2f;

public class CursorPosHandler extends GLFWCursorPosCallback {

	private Vector2f pos;

	public CursorPosHandler() {
		pos = new Vector2f();
	}

	@Override
	public void invoke(long window, double xPos, double yPos) {
		pos.setCoords((float) xPos, (float) yPos);
	}

	public Vector2f getPos() {
		return pos;
	}
	
	public float getX(){
		return pos.getX();
	}
	
	public float getY(){
		return pos.getY();
	}
}
