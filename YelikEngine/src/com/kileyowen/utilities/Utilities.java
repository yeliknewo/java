package com.kileyowen.utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.kileyowen.math.Matrix4f;

public class Utilities {

	public static FloatBuffer createFloatBuffer(Matrix4f matrix) {
		return createFloatBuffer(matrix.getArray());
	}

	public static FloatBuffer createFloatBuffer(float[] array) {
		FloatBuffer result = ByteBuffer.allocateDirect(array.length << 2).order(ByteOrder.nativeOrder())
				.asFloatBuffer();
		result.put(array).flip();
		return result;
	}

	public static IntBuffer createIntBuffer(int[] array) {
		IntBuffer result = ByteBuffer.allocateDirect(array.length << 2).order(ByteOrder.nativeOrder()).asIntBuffer();
		result.put(array).flip();
		return result;
	}

	public static ByteBuffer createByteBuffer(byte[] array) {
		ByteBuffer result = ByteBuffer.allocateDirect(array.length).order(ByteOrder.nativeOrder());
		result.put(array).flip();
		return result;
	}

	public static String loadAsString(String path) {
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String buffer = "";
			while ((buffer = reader.readLine()) != null) {
				result.append(buffer);
				result.append("\n");
			}
			reader.close();

		} catch (IOException e) {
			System.err.println(e);
		}
		return result.toString();
	}
}
