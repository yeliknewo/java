package com.kileyowen.tradegame;

import org.lwjgl.Sys;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import java.nio.ByteBuffer;

import com.kileyowen.math.Matrix4f;
import com.kileyowen.tradegame.opengl.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main implements Runnable {
	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;
	private GLFWCursorPosCallback cursorPosCallback;

	private long window;
	private String title;
	private Thread thread;
	private boolean running;
	private float aspectRatio;
	private World world;

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

		aspectRatio = WIDTH / HEIGHT;

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

		System.out.println("OpenGL: " + glGetString(GL_VERSION));

		glEnable(GL_DEPTH_TEST);

		glActiveTexture(GL_TEXTURE1);

		glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

		Shader.loadAll();

		Shader.shader1.enable();
		Matrix4f perspectiveMatrix = Matrix4f.orthographic(-10f, 10f, -10f / aspectRatio, 10f / aspectRatio, -10f, 10f);
		Shader.shader1.setUniformMat4f("u_vw_matrix", Matrix4f.translate(world.getCamera()));
		Shader.shader1.setUniformMat4f("u_pr_matrix", perspectiveMatrix);
		Shader.shader1.setUniform1i("tex", 1);

		Shader.shader1.disable();
	}

	public void update() {
		glfwPollEvents();
		if (KeyboardHandler.isKeyDown(GLFW_KEY_SPACE)) {
			System.out.println("Space Key Pressed");
		}
		if (glfwWindowShouldClose(window) == GL_TRUE) {
			stop();
		}
	}

	public void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		world.render();
		glfwSwapBuffers(window);
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
