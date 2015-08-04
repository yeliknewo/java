package com.kileyowen.math;

public class Vector3f {
	
	private float[] coords;

	public Vector3f() {
		coords = new float[3];
	}
	
	public float getX(){
		return coords[0];
	}
	
	public float getY(){
		return coords[1];
	}
	
	public float getZ(){
		return coords[2];
	}
}
