package com.kileyowen.geneticalgorithm;

import java.util.HashMap;
import java.util.Iterator;

public class HashMap2D<T> {
	private HashMap<Integer, HashMap<Integer, T>> map;
	private int width, height;
	private boolean dimensionsCurrent;

	public HashMap2D() {
		map = new HashMap<Integer, HashMap<Integer, T>>();
		width = height = 0;
		dimensionsCurrent = true;
	}

	public T get(int x, int y) {
		if (hasY(y)) {
			if (hasX(y, x)) {
				return map.get(y).get(x);
			}
		}
		return null;
	}

	public void set(int x, int y, T thing) {
		if (!map.containsKey(y)) {
			map.put(y, new HashMap<Integer, T>());
		}
		map.get(y).put(x, thing);
		dimensionsCurrent = false;
	}

	private void updateDimensions() {
		Iterator<Integer> yIterator = map.keySet().iterator();
		while (yIterator.hasNext()) {
			int y = yIterator.next();
			HashMap<Integer, T> xMap = map.get(y);
			height = Math.max(height, y);
			Iterator<Integer> xIterator = xMap.keySet().iterator();
			while (xIterator.hasNext()) {
				width = Math.max(width, xIterator.next());
			}
		}
		dimensionsCurrent = true;
	}

	public Iterator<Integer> getYIterator() {
		return map.keySet().iterator();
	}

	public Iterator<Integer> getXIterator(int y) {
		return map.get(y).keySet().iterator();
	}

	public int getRandomY() {
		if (map.size() == 0) {
			return 0;
		}
		int index = GeneticAlgorithm.rand.nextInt(map.size());
		Iterator<Integer> iterator = map.keySet().iterator();
		for (int i = 0; i < index; i++) {
			iterator.next();
		}
		return iterator.next();
	}

	public int getRandomX(int y) {
		if (!hasY(y)) {
			return 0;
		}
		int index = GeneticAlgorithm.rand.nextInt(map.get(y).size());
		Iterator<Integer> iterator = map.get(y).keySet().iterator();
		for (int i = 0; i < index; i++) {
			iterator.next();
		}
		return iterator.next();
	}

	public boolean hasY(int y) {
		return map.containsKey(y);
	}

	public boolean hasX(int y, int x) {
		if (hasY(y)) {
			return map.get(y).containsKey(x);
		}
		return false;
	}

	public int getWidth() {
		if (!dimensionsCurrent) {
			updateDimensions();
		}
		return width;

	}

	public int getHeight() {
		if (!dimensionsCurrent) {
			updateDimensions();
		}
		return height;
	}
}
