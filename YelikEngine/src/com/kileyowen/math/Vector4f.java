package com.kileyowen.math;

public class Vector4f {
	private float[] coords;

	public Vector4f(float x, float y, float z, float w) {
		coords = new float[4];
		setCoords(x, y, z, w);
	}

	public Vector4f(float[] coords) {
		this.coords = new float[4];
		setCoords(coords);
	}

	public Vector4f() {
		coords = new float[4];
	}

	public void setCoords(float x, float y, float z, float w) {
		coords[0] = x;
		coords[1] = y;
		coords[2] = z;
		coords[3] = w;
	}

	public void setCoords(float[] coords) {
		for (int i = 0; i < 4; i++) {
			this.coords[i] = coords[i];
		}
	}

	public float[] getCoords() {
		return coords.clone();
	}

	public Vector3f toVec3f() {
		return new Vector3f(coords[0], coords[1], coords[2]);
	}
}
