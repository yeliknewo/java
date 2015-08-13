package com.kileyowen.tradesim;

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

import java.nio.ByteBuffer;
import java.util.Random;

import org.lwjgl.Sys;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import com.kileyowen.math.Matrix4f;
import com.kileyowen.opengl.Shader;
import com.kileyowen.puppets.KeyboardHandler;
import com.kileyowen.puppets.MouseHandler;

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

	public static Random rand = new Random();

	private void init() {
		System.out.println("Starting LWJGL " + Sys.getVersion());

		glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));
		if (glfwInit() != GL11.GL_TRUE) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

		int WIDTH = 640;
		int HEIGHT = 480;

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

		world = new World(WIDTH, HEIGHT, 20f, 20f / aspectRatio);

		glClearColor(0, 0, 0, 0);

		glEnable(GL_DEPTH_TEST);

		glActiveTexture(GL_TEXTURE1);

		Shader.loadAll();

		Shader.shader1.enable();
		float x0 = -10, x1 = 10, y0 = -10 / aspectRatio, y1 = 10f / aspectRatio, z0 = -10, z1 = 10;
		Matrix4f perspectiveMatrix = Matrix4f.orthographic(x0, x1, y0, y1, z0, z1);
		world.setOrthographicSize(x0, x1, y0, y1, z0, z1);
		Shader.shader1.setUniformMat4f("u_vw_matrix", Matrix4f.translate(world.getCamera()));
		Shader.shader1.setUniformMat4f("u_pr_matrix", perspectiveMatrix);
		Shader.shader1.setUniform1i("u_tex", 1);
		Shader.shader1.disable();
	}

	public void update() {
		glfwPollEvents();
		world.update();
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
