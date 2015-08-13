package com.kileyowen.neuralnetwork;

import java.util.HashMap;
import java.util.Iterator;

public class HashMap2D<T> {
	private HashMap<Long, HashMap<Long, T>> map;
	private long width, height;
	private boolean dimensionsCurrent;

	public HashMap2D() {
		map = new HashMap<Long, HashMap<Long, T>>();
		width = height = 0;
		dimensionsCurrent = true;
	}

	public T get(long x, long y) {
		if (hasY(x)) {
			if (hasX(y, x)) {
				return map.get(y).get(x);
			}
		}
		return null;
	}

	public void set(long x, long y, T thing) {
		if (!map.containsKey(y)) {
			map.put(y, new HashMap<Long, T>());
		}
		map.get(y).put(x, thing);
		dimensionsCurrent = false;
	}

	private void updateDimensions() {
		Iterator<Long> yIterator = map.keySet().iterator();
		while (yIterator.hasNext()) {
			long y = yIterator.next();
			HashMap<Long, T> xMap = map.get(y);
			height = Math.max(height, y);
			Iterator<Long> xIterator = xMap.keySet().iterator();
			while (xIterator.hasNext()) {
				width = Math.max(width, xIterator.next());
			}
		}
	}

	public long getRandomY() {
		if(map.size() == 0){
			return 0;
		}
		int index = NeuralNetwork.rand.nextInt(map.size());
		Iterator<Long> iterator = map.keySet().iterator();
		for (int i = 0; i < index; i++) {
			iterator.next();
		}
		return iterator.next();
	}

	public long getRandomX(long y) {
		if(!hasY(y)){
			return 0;
		}
		int index = NeuralNetwork.rand.nextInt(map.get(y).size());
		Iterator<Long> iterator = map.get(y).keySet().iterator();
		for (int i = 0; i < index; i++) {
			iterator.next();
		}
		return iterator.next();
	}

	public boolean hasY(long y) {
		return map.containsKey(y);
	}

	public boolean hasX(long y, long x) {
		if (hasY(y)) {
			return map.get(y).containsKey(x);
		}
		return false;
	}

	public long getWidth() {
		if (!dimensionsCurrent) {
			updateDimensions();
		}
		return width;

	}

	public long getHeight() {
		if (!dimensionsCurrent) {
			updateDimensions();
		}
		return height;
	}
}
