package com.kileyowen.geneticalgorithm;

public class Color {

	private int red;
	private int green;
	private int blue;

	public Color(int RGB) {
		red = (int) ((RGB >> 16) & 0xFF);
		green = (int) ((RGB >> 8) & 0xFF);
		blue = (int) ((RGB >> 0) & 0xFF);
	}

	public Color(int red, int green, int blue, int alpha) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public int getRGB() {
		return (red << 16) | (green << 8) | (blue << 0);
	}

	public int getRed() {
		return red;
	}

	public int getGreen() {
		return green;
	}

	public int getBlue() {
		return blue;
	}
}
