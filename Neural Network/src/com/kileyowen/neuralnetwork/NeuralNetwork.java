package com.kileyowen.neuralnetwork;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import javax.imageio.ImageIO;

public class NeuralNetwork {
	private HashMap2D<Color> in;
	private HashMap<Long, HashMap2D<Color>> rules;
	public static Random rand;

	public NeuralNetwork(HashMap2D<Color> in) {
		this.in = in;
		rules = new HashMap<Long, HashMap2D<Color>>();
	}

	public HashMap2D<Color> getOutput(int depth, int bredth, int reach) {
		HashMap2D<Color> out = new HashMap2D<Color>();
		{
			for (int i = 0; i < depth; i++) {
				long y, x;
				do {
					y = in.getRandomY();
					x = in.getRandomX(y);
				} while (y == 0 || x == 0);
				Color cur = in.get(x, y);
				long curRGB = cur.getRGB();
				for (int i2 = 0; i2 < bredth; i2++) {
					int x2 = rand.nextInt(reach) - (int)Math.floor(reach / 2f);
					int y2 = rand.nextInt(reach) - (int)Math.floor(reach / 2f);

					if (!rules.containsKey(curRGB)) {
						rules.put(curRGB, new HashMap2D<Color>());
					}
					rules.get(curRGB).set(x2, y2, in.get(Math.max(0, x + x2), Math.max(0, y + y2)));
				}
			}
		}
		long y, x;
		do {
			y = in.getRandomY();
			x = in.getRandomX(y);

		} while (y == 0 || x == 0);
		Color c = in.get(x, y);
		out.set(x, y, c);

		for (int i = 0; i < in.getWidth() * in.getHeight(); i++) {
			long cRGB = c.getRGB();
			long y2, x2;
			do {
				y2 = rules.get(cRGB).getRandomY();
				x2 = rules.get(cRGB).getRandomX(y2);
			} while (y == 0 || x == 0);
			c = rules.get(cRGB).get(x2, y2);
			out.set(x2 + x, y2 + y, c);
			x += x2;
			y += y2;
		}

		return out;
	}

	public static HashMap2D<Color> readImage(String path) {
		BufferedImage bufferedImage;
		try {
			bufferedImage = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		HashMap2D<Color> image = new HashMap2D<Color>();
		for (int y = 0; y < bufferedImage.getHeight(); y++) {
			for (int x = 0; x < bufferedImage.getWidth(); x++) {
				int color = bufferedImage.getRGB(x, y);
				
				image.set(x, y, new Color(color));
			}
		}
		return image;
	}

	public static void writeImage(HashMap2D<Color> in, String path) {
		BufferedImage image = new BufferedImage((int) in.getWidth(), (int) in.getHeight(), BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				Color c = in.get(x, y);
				if (c != null) {
					image.setRGB(x, y, c.getRGB());
				}
			}
		}
		try {
			ImageIO.write(image, "png", new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		rand = new Random();
		NeuralNetwork neural = new NeuralNetwork(readImage("assets/cat.jpg"));
		writeImage(neural.getOutput(100, 100, 3), "assets/output.png");
	}
}
