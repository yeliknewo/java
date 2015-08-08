package com.kileyowen.particles;

import java.util.ArrayList;
import java.util.List;

public class List2D<T> {
	private List<T> list;
	private int width;
	private int height;

	public List2D(int width, int height) {
		this.width = width;
		this.height = height;
		list = new ArrayList<T>();
		for(int i = 0;i < getWidth() * getHeight(); i++){
			list.add(null);
		}
	}

	public boolean set(int x, int y, T value) {
		if (x < 0 || y < 0 || x >= getWidth() || y >= getHeight())
			return false;
		list.set(y * width + x, value);
		return true;
	}

	public T get(int x, int y) {
		if (y < 0 || x < 0 || y >= getHeight() || x >= getWidth())
			return null;
		return list.get(y * width + x);
	}

	public T getI(int i) {
		if (i >= list.size() || i < 0)
			return null;
		return list.get(i);
	}

	public boolean setI(int i, T value) {
		if (i >= list.size() || i < 0)
			return false;
		list.set(i, value);
		return true;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getSize() {
		return list.size();
	}

	public List<T> getList() {
		return list;
	}
}
