package com.kileyowen.math;

public class Vector3f {
	private float[] coords;

	public Vector3f() {
		coords = new float[3];
	}

	public Vector3f(float x, float y, float z) {
		coords = new float[3];
		setCoords(x, y, z);
	}

	public Vector3f(float[] coords) {
		this.coords = new float[3];
		setCoords(coords);
	}

	public void setCoords(float x, float y, float z) {
		coords[0] = x;
		coords[1] = y;
		coords[2] = z;
	}

	public void setCoords(float[] coords) {
		for (int i = 0; i < 3; i++) {
			this.coords[i] = coords[i];
		}
	}

	public void addCoords(float x, float y, float z) {
		coords[0] += x;
		coords[1] += y;
		coords[2] += z;
	}

	public void add(Vector3f vector) {
		addCoords(vector.getX(), vector.getY(), vector.getZ());
	}

	public void scale(float scalar) {
		setCoords(getX() * scalar, getY() * scalar, getZ() * scalar);
	}

	public void negate() {
		for (int i = 0; i < coords.length; i++) {
			coords[i] *= -1;
		}
	}

	public float getX() {
		return coords[0];
	}

	public float getY() {
		return coords[1];
	}

	public float getZ() {
		return coords[2];
	}

	public float[] getCoords() {
		return coords.clone();
	}

	public Vector4f toVec4f() {
		float[] coords4f = new float[4];
		for (int i = 0; i < 3; i++) {
			coords4f[i] = coords[i];
		}
		coords4f[3] = 1f;
		return new Vector4f(coords4f);
	}
}
