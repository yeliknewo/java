package com.kileyowen.math;

public class Vector3f {
	
	private float[] coords;

	public Vector3f() {
		createCoords(0f,0f,0f);
	}
	
	public Vector3f(float x, float y, float z){
		createCoords(x,y,z);
	}
	
	private void createCoords(float x, float y, float z){
		coords[0] = x;
		coords[1] = y;
		coords[2] = z;
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
