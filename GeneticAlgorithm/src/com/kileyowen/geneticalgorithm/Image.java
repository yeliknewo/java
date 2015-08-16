package com.kileyowen.geneticalgorithm;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Image {
	private int width;
	private int height;
	private Color[][] pixels;
	private double fitness;

	public Image(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new Color[getHeight()][getWidth()];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				setPixel(x, y, new Color(0));
			}
		}
		fitness = 0;
	}

	public Image(String path) {
		readFile(path);
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public double getFitness() {
		return fitness;
	}

	public void setPixel(int x, int y, Color c) {
		pixels[y][x] = c;
	}

	public Color getPixel(int x, int y) {
		return pixels[y][x];
	}

	public List<Integer> getAllPixelRGB() {
		List<Integer> output = new ArrayList<Integer>();
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				output.add(getPixel(x, y).getRGB());
			}
		}
		return output;
	}

	public void setAllPixelRGB(List<Integer> input) {
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				setPixel(x, y, new Color(input.get(y * getWidth() + x)));
			}
		}
	}

	public Image clone() {
		Image clone = new Image(getWidth(), getHeight());
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				clone.setPixel(x, y, getPixel(x, y));
			}
		}
		return clone;
	}

	public void readFile(String path) {
		try {
			BufferedImage bufferedImage = ImageIO.read(new File(path));
			width = bufferedImage.getWidth();
			height = bufferedImage.getHeight();
			pixels = new Color[getHeight()][getWidth()];
			for (int y = 0; y < getHeight(); y++) {
				for (int x = 0; x < getWidth(); x++) {
					pixels[y][x] = new Color(bufferedImage.getRGB(x, y));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeFile(String path) {
		BufferedImage bufferedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				Color c = pixels[y][x];
				if (c != null) {
					bufferedImage.setRGB(x, y, c.getRGB());
				}
			}
		}
		try {
			File file = new File(path);
			file.mkdirs();
			ImageIO.write(bufferedImage, "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
