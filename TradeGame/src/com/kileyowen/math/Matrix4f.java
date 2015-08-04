package com.kileyowen.math;

import java.nio.FloatBuffer;
import com.kileyowen.particles.List2D;
import com.kileyowen.tradegame.Utilities;

public class Matrix4f extends List2D<Float> {

	public Matrix4f() {
		super(4, 4);
		for (int i = 0; i < getLength(); i++) {
			setI(i, 0f);
		}
	}

	public FloatBuffer toFloatBuffer() {
		return Utilities.createFloatBuffer(getArray());
	}

	private float[] getArray() {
		float[] array = new float[4 * 4];
		for (int i = 0; i < array.length; i++) {
			array[i] = getI(i);
		}
		return array;
	}

	public static Matrix4f identity() {
		Matrix4f matrix = new Matrix4f();
		for (int i = 0; i < 4; i++) {
			matrix.set(i, i, 1f);
		}
		return matrix;
	}

	public static Matrix4f orthographic(float left, float right, float bottom, float top, float near, float far) {
		Matrix4f matrix = identity();
		matrix.set(0, 0, 2 / (right - left));
		matrix.set(1, 1, 2 / (top / bottom));
		matrix.set(2, 3, -2 / (far - near));
		matrix.set(4, 0, -(right + left) / (right - left));
		matrix.set(4, 1, -(top + bottom) / (top - bottom));
		matrix.set(4, 3, -(far + near) / (far - near));
		return matrix;
	}

	public static Matrix4f translate(Vector3f vector) {
		Matrix4f matrix = identity();
		matrix.set(4, 0, vector.getX());
		matrix.set(4, 1, vector.getY());
		matrix.set(4, 2, vector.getZ());
		return matrix;
	}

	public static Matrix4f rotateXDeg(float degrees) {
		return rotateX((float) Math.toRadians(degrees));
	}

	public static Matrix4f rotateX(float radians) {
		Matrix4f matrix = identity();
		float cos = (float) Math.cos(radians);
		float sin = (float) Math.sin(radians);
		matrix.set(1, 1, cos);
		matrix.set(2, 1, sin);
		matrix.set(1, 2, -sin);
		matrix.set(2, 2, cos);
		return matrix;
	}

	public static Matrix4f rotateYDeg(float degrees) {
		return rotateX((float) Math.toRadians(degrees));
	}

	public static Matrix4f rotateY(float radians) {
		Matrix4f matrix = identity();
		float cos = (float) Math.cos(radians);
		float sin = (float) Math.sin(radians);
		matrix.set(0, 0, cos);
		matrix.set(2, 0, sin);
		matrix.set(0, 2, -sin);
		matrix.set(2, 2, cos);
		return matrix;
	}

	public static Matrix4f rotateZDeg(float degrees) {
		return rotateX((float) Math.toRadians(degrees));
	}

	public static Matrix4f rotateZ(float radians) {
		Matrix4f matrix = identity();
		float cos = (float) Math.cos(radians);
		float sin = (float) Math.sin(radians);
		matrix.set(0, 0, cos);
		matrix.set(1, 0, sin);
		matrix.set(0, 1, -sin);
		matrix.set(1, 1, cos);
		return matrix;
	}

	public Matrix4f multiply(Matrix4f matrix) {
		Matrix4f result = identity();
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				float sum = 0f;
				for (int e = 0; e < 4; e++) {
					sum += get(x, e) * matrix.get(e, y);
				}
				result.set(x, y, sum);
			}
		}
		return result;
	}
}
