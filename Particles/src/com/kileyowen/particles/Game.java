package com.kileyowen.particles;

import java.awt.*;
import java.awt.image.*;

public class Game extends Canvas implements Runnable {
	private Frame frame;
	private String title;
	private Dimension resolution;
	private boolean running = false;
	private Thread thread;
	private BufferedImage image;
	private int[] pixels;
	private List2D<Integer> view;
	private World world;
	private Mouse mouse;

	public Game() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice screen = ge.getDefaultScreenDevice();
		resolution = screen.getDefaultConfiguration().getBounds().getSize();

		title = "Ring Trees";

		image = new BufferedImage(resolution.width, resolution.height, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

		view = new List2D<Integer>(resolution.width, resolution.height);
		for (int y = 0; y < view.getHeight(); y++) {
			for (int x = 0; x < view.getWidth(); x++) {
				if (!view.set(x, y, 0)) {
					System.out.println(x + " " + y);
				}
			}
		}

		world = new World(resolution.width, resolution.height);

		mouse = new Mouse(this);

		frame = new Frame();
		frame.setUndecorated(true);
		frame.setVisible(true);
		frame.setSize(resolution);
		frame.setTitle(title);
		frame.setResizable(false);

		frame.add(this);

		start();
	}

	private void start() {
		running = true;
		thread = new Thread(this, "Display");
		thread.start();
	}

	private void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0;
		int frames = 0;
		int updates = 0;
		double delta = 0;
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
				frame.setTitle(title + " | " + updates + " ups " + frames + " fps");
				updates = 0;
				frames = 0;
			}
		}
	}

	private void update() {
		if (mouse.getMouseIn()) {
			world.update(mouse);
		}
	}

	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		for (int i = 0; i < view.getSize(); i++) {
			view.setI(i, 0);
		}
		world.render(view);
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = view.getI(i);
		}
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, resolution.width, resolution.height, null);
		g.dispose();
		bs.show();
	}
}
