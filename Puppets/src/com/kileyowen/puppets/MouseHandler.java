package com.kileyowen.puppets;

import org.lwjgl.glfw.GLFWCursorPosCallback;

public class MouseHandler extends GLFWCursorPosCallback {
	
	private static double xPos, yPos;

	@Override
	public void invoke(long window, double xpos, double ypos) {
		xPos = xpos;
		yPos = ypos;
	}
	
	public static double getXPos(){
		return xPos;
	}
	
	public static double getYPos(){
		return yPos;
	}
}
