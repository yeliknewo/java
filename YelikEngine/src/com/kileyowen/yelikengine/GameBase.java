package com.kileyowen.yelikengine;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.system.MemoryUtil.NULL;

import static com.kileyowen.math.Numbers.*;

import java.nio.ByteBuffer;

import org.lwjgl.Sys;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

public abstract class GameBase implements Runnable {
	private double targetFPS, nanosecsPerFrame;
	private boolean running;
	private String threadName;
	private Thread thread;

	public GameBase() {

	}

	public void init() {
		System.out.println("starting LWJGL " + Sys.getVersion());

		targetFPS = 60.0;
		threadName = "YelikEngine";
	}

	private void start() {
		running = true;
		thread = new Thread(this, threadName);
		thread.start();
	}

	private void setTargetFPS(double targetFPS) {
		this.targetFPS = targetFPS;
		nanosecsPerFrame = NANOSECONDS_PER_SECOND / targetFPS;
	}

	private void stop() {
		running = false;
	}

	@Override
	public void run() {
		long processedTime, lastTime;
		lastTime = processedTime = System.nanoTime();
		int frames = 0;
		int ticks = 0;
		double delta = 0;

		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nanosecsPerFrame;
			lastTime = now;
			while (delta > 1) {
				tick();
				ticks++;
				delta--;
			}
		}
	}

	protected abstract void tick();
	protected abstract void render();

}
