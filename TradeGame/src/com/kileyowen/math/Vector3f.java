package com.kileyowen.math;

public class Vector3f {

	private float[] coords;

	public Vector3f() {
		coords = new float[3];
	}

	public Vector3f setCoords(float x, float y, float z) {
		coords[0] = x;
		coords[1] = y;
		coords[2] = z;
		return this;
	}

	public Vector3f addCoords(float x, float y, float z) {
		coords[0] += x;
		coords[1] += y;
		coords[2] += z;
		return this;
	}

	public Vector3f add(Vector3f vector) {
		addCoords(vector.getX(), vector.getY(), vector.getZ());
		return this;
	}

	public Vector3f negate() {
		return new Vector3f().setCoords(-getX(), -getY(), -getZ());
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
}
