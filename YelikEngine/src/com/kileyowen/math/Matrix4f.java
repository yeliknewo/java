package com.kileyowen.math;

import java.nio.FloatBuffer;
import static com.kileyowen.utilities.Utilities.*;

public class Matrix4f {

	private float[][] matArray;

	public Matrix4f() {
		matArray = new float[4][4];
	}

	public FloatBuffer toFloatBuffer() {
		return createFloatBuffer(getArray());
	}

	public float[] getArray() {
		float[] array = new float[4 * 4];
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				array[y * 4 + x] = matArray[y][x];
			}
		}
		return array;
	}

	public static Matrix4f identity() {
		Matrix4f matrix = new Matrix4f();
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				if (y == x) {
					matrix.matArray[y][x] = 1f;
				} else {
					matrix.matArray[y][x] = 0f;
				}
			}
		}
		return matrix;
	}

	public static Matrix4f perspective(float left, float right, float bottom, float top, float near, float far,
			float FoV, float aspect) {
		float y2 = near * (float) Math.tan(Math.toRadians(FoV));
		float y1 = -y2;
		float x1 = y1 * aspect;
		float x2 = y2 * aspect;
		return frustrum(x1, x2, y1, y2, near, far);
	}

	public static Matrix4f frustrum(float left, float right, float bottom, float top, float near, float far) {
		Matrix4f matrix = identity();

		matrix.matArray[0][0] = (2 * near) / (right - left);
		matrix.matArray[1][1] = (2 * near) / (top - bottom);
		matrix.matArray[2][2] = (near + far) / (near - far);
		matrix.matArray[3][3] = 0f;

		matrix.matArray[0][2] = (right + left) / (right - left);
		matrix.matArray[1][2] = (top + bottom) / (top - bottom);
		matrix.matArray[3][2] = -1f;
		matrix.matArray[2][3] = (-2 * far * near) / (far - near);

		return matrix;
	}

	public static Matrix4f orthographic(float left, float right, float bottom, float top, float near, float far) {
		Matrix4f matrix = identity();

		matrix.matArray[0][0] = 2 / (right - left);
		matrix.matArray[1][1] = 2 / (top - bottom);
		matrix.matArray[2][2] = 2 / (far - near);
		matrix.matArray[0][3] = -(right + left) / (right - left);
		matrix.matArray[1][3] = -(top + bottom) / (top - bottom);
		matrix.matArray[2][3] = -(far + near) / (far - near);

		return matrix;
	}

	public static Matrix4f translate(Vector3f vector) {
		Matrix4f matrix = identity();

		matrix.matArray[3][0] = vector.getX();
		matrix.matArray[3][1] = vector.getY();
		matrix.matArray[3][2] = vector.getZ();

		return matrix;
	}

	public static Matrix4f rotateXDeg(float degrees) {
		return rotateX((float) Math.toRadians(degrees));
	}

	public static Matrix4f rotateX(float radians) {
		Matrix4f matrix = identity();
		float cos = (float) Math.cos(radians);
		float sin = (float) Math.sin(radians);

		matrix.matArray[1][1] = cos;
		matrix.matArray[1][2] = sin;
		matrix.matArray[2][1] = -sin;
		matrix.matArray[2][2] = cos;

		return matrix;
	}

	public static Matrix4f rotateYDeg(float degrees) {
		return rotateY((float) Math.toRadians(degrees));
	}

	public static Matrix4f rotateY(float radians) {
		Matrix4f matrix = identity();
		float cos = (float) Math.cos(radians);
		float sin = (float) Math.sin(radians);

		matrix.matArray[0][0] = cos;
		matrix.matArray[0][2] = sin;
		matrix.matArray[2][0] = -sin;
		matrix.matArray[2][2] = cos;

		return matrix;
	}

	public static Matrix4f rotateZDeg(float degrees) {
		return rotateZ((float) Math.toRadians(degrees));
	}

	public static Matrix4f rotateZ(float radians) {
		Matrix4f matrix = identity();
		float cos = (float) Math.cos(radians);
		float sin = (float) Math.sin(radians);

		matrix.matArray[0][0] = cos;
		matrix.matArray[0][1] = sin;
		matrix.matArray[1][0] = -sin;
		matrix.matArray[1][1] = cos;

		return matrix;
	}

	public static Matrix4f rotateAll(Vector3f vec) {
		return Matrix4f.rotateX(vec.getX()).multiply(Matrix4f.rotateY(vec.getY()))
				.multiply(Matrix4f.rotateZ(vec.getZ()));
	}

	public static Matrix4f scaleX(float scale) {
		Matrix4f matrix = identity();

		matrix.matArray[0][0] = scale;

		return matrix;
	}

	public static Matrix4f scaleY(float scale) {
		Matrix4f matrix = identity();

		matrix.matArray[1][1] = scale;

		return matrix;
	}

	public static Matrix4f scaleZ(float scale) {
		Matrix4f matrix = identity();

		matrix.matArray[2][2] = scale;

		return matrix;
	}

	public static Matrix4f scaleAll(Vector3f scale) {
		Matrix4f matrix = identity();

		matrix.matArray[0][0] = scale.getX();
		matrix.matArray[1][1] = scale.getY();
		matrix.matArray[2][2] = scale.getZ();

		return matrix;
	}

	public Matrix4f multiply(Matrix4f matrix) {
		Matrix4f result = identity();
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				float sum = 0f;
				for (int e = 0; e < 4; e++) {
					sum += matArray[e][x] * matrix.matArray[y][e];
				}
				result.matArray[y][x] = sum;
			}
		}
		return result;
	}

	public Vector4f multiply(Vector4f vector) {
		float[] inCoords = vector.getCoords(), outCoords = new float[4];

		for (int y = 0; y < 4; y++) {
			float sum = 0;
			for (int x = 0; x < 4; x++) {
				sum += inCoords[y] * matArray[y][x];
			}
			outCoords[y] = sum;
		}

		return new Vector4f(outCoords);
	}

	public Vector3f multiply(Vector3f vector) {
		return multiply(vector.toVec4f()).toVec3f();
	}
}
