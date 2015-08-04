package com.kileyowen.math;

import java.nio.FloatBuffer;
import com.kileyowen.particles.List2D;
import com.kileyowen.tradegame.Utilities;

public class Matrix4f extends List2D<Float> {

	public Matrix4f() {
		super(4, 4);
		for(int i = 0;i< getLength();i++){
			setI(i, 0f);
		}
	}

	public FloatBuffer toFloatBuffer() {
		return Utilities.createFloatBuffer(getArray());
	}

	private float[] getArray() {
		float[] array = new float[4*4];
		for(int i = 0;i < array.length;i++){
			array[i] = getI(i);
		}
		return array;
	}
	
	public static Matrix4f indentity(){
		Matrix4f matrix = new Matrix4f();
		for(int i = 0;i<4;i++){
			matrix.set(i, i, 1f);
		}
		return matrix;
	}
}
