package com.kileyowen.math;

public class Vector2f {
	private float[] coords;

	public Vector2f() {
		coords = new float[2];
	}

	public Vector2f(float x, float y) {
		coords = new float[2];
		setCoords(x, y);
	}

	public Vector2f(float[] coords) {
		this.coords = new float[2];
		setCoords(coords);
	}

	public void setCoords(float x, float y) {
		coords[0] = x;
		coords[1] = y;
	}

	public void setCoords(float[] coords) {
		this.coords[0] = coords[0];
		this.coords[1] = coords[1];
	}

	public float getX(){
		return coords[0];
	}
	
	public float getY(){
		return coords[1];
	}
	
	public float[] getCoords(){
		return coords.clone();
	}
}
