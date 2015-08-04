package com.kileyowen.particles;

public class Rect {
	private int x0;
	private int y0;
	private int x1;
	private int y1;

	public Rect(int x0, int y0, int x1, int y1) {
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
	}

	public int getX0() {
		return x0;
	}

	public int getY0() {
		return y0;
	}

	public int getX1() {
		return x1;
	}

	public int getY1() {
		return y1;
	}

	public boolean doesRectOverlap(Rect other) {
		int[] p00 = { other.getX0(), other.getY0() };
		int[] p10 = { other.getX1(), other.getY0() };
		int[] p01 = { other.getX0(), other.getY1() };
		int[] p11 = { other.getX1(), other.getY1() };
		return doesPointOverlap(p00) || doesPointOverlap(p01) || doesPointOverlap(p10) || doesPointOverlap(p11);
	}

	public boolean doesPointOverlap(int[] point) {
		int x = point[0];
		int y = point[1];
		if (x >= getX0() && x <= getX1() && y >= getY0() && y <= getY1()) {
			return true;
		}
		return false;
	}
}
