package com.kileyowen.puppets;

import com.kileyowen.math.Matrix4f;
import com.kileyowen.math.Vector3f;
import com.kileyowen.opengl.BufferManager;
import com.kileyowen.opengl.Shader;
import com.kileyowen.opengl.Texture;

public class Test implements GameObject {
	private Texture texture;
	private Vector3f position;
	private BufferManager buffers;

	public Test() {
		texture = new Texture("assets/test.png");
		position = new Vector3f().setCoords(0, 0, 0);
		buffers = new BufferManager(getVertexArray(), getIndexArray(), getTextureCoordinateArray());
	}

	private float[] getTextureCoordinateArray() {
		float[] array = {
				0, 0,
				0, 1f, 
				1f, 0, 
				1f, 1f
				};
		return array;
	}

	private byte[] getIndexArray() {
		byte[] array = {
				0, 1, 2, 
				2, 1, 3 
				};
		return array;
	}

	private float[] getVertexArray() {
		float[] array = { 
				-1f, -1f, 0,
				-1f, 1f, 0,
				1f, -1f, 0, 
				1f, 1f, 0
				};
		return array;
	}

	@Override
	public void update() {
		
	}

	@Override
	public void render() {
		texture.bind();
		Shader.shader1.enable();
		Shader.shader1.setUniformMat4f("u_ml_matrix", Matrix4f.translate(position));
		buffers.draw();
		Shader.shader1.disable();
		texture.unbind();
	}
}
