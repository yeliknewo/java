package com.kileyowen.tradesim;

import com.kileyowen.math.Vector3f;

public class Rect {
	private float x0;
	private float x1;
	private float y0;
	private float y1;

	public Rect(float x0, float x1, float y0, float y1) {
		this.x0 = x0;
		this.x1 = x1;
		this.y0 = y0;
		this.y1 = y1;
	}
	
	public boolean isPointInRect(Vector3f point){
		if(point.getX() < x0 || point.getX() > x1 || point.getY() < y0 || point.getY() > y1){
			return false;
		}
		return true;
	}
}
