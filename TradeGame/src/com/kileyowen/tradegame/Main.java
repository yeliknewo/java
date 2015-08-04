package com.kileyowen.tradegame;

import org.lwjgl.Sys;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import java.nio.ByteBuffer;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import com.kileyowen.tradegame.opengl.*;

public class Main implements Runnable {
	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;
	private GLFWCursorPosCallback cursorPosCallback;

	private long window;
	private String title;
	private Thread thread;
	private boolean running;

	private void init() {
		System.out.println("Starting LWJGL " + Sys.getVersion());

		glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));
		if (glfwInit() != GL11.GL_TRUE) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

		int WIDTH = 300;
		int HEIGHT = 300;

		window = glfwCreateWindow(WIDTH, HEIGHT, title, NULL, NULL);
		if (window == NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
		}

		glfwSetKeyCallback(window, keyCallback = new KeyboardHandler());
		glfwSetCursorPosCallback(window, cursorPosCallback = new MouseHandler());

		ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

		glfwSetWindowPos(window, (GLFWvidmode.width(vidmode) - WIDTH) / 2, (GLFWvidmode.height(vidmode) - HEIGHT) / 2);

		glfwMakeContextCurrent(window);

		glfwSwapInterval(1);

		glfwShowWindow(window);

		title = "TradeGame";

		GLContext.createFromCurrent();

		glEnable(GL_DEPTH_TEST);
		
		ShaderUtilities.load("shaders/bg.vert","shaders/bg.frag");
	}

	public void update() {
		if (KeyboardHandler.isKeyDown(GLFW_KEY_SPACE)) {
			System.out.println("Space Key Pressed");
		}
		if (glfwWindowShouldClose(window) == GL_TRUE) {
			stop();
		}
	}

	public void render() {
		glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glfwSwapBuffers(window);
		glfwPollEvents();
	}

	private void start() {
		running = true;
		thread = new Thread(this, "TradeSim");
		thread.start();
	}

	private void stop() {
		running = false;
	}

	@Override
	public void run() {
		init();
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0;
		int frames = 0;
		int updates = 0;
		double delta = 0;
		running = true;

		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta > 1) {
				update();
				updates++;
				delta--;
			}
			render();
			frames++;
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				glfwSetWindowTitle(window, title + " | " + updates + " ups " + frames + " fps");
				updates = 0;
				frames = 0;
			}
		}
		
		keyCallback.release();
		cursorPosCallback.release();
		glfwDestroyWindow(window);
		glfwTerminate();
	}

	public static void main(String[] args) {
		Main main = new Main();
		main.start();
	}
}
