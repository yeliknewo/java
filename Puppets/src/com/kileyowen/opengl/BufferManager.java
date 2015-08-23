package com.kileyowen.opengl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import com.kileyowen.puppets.Utilities;

public class BufferManager {
	private int vertexArray, vertexBuffer, indexBuffer, textureCoordinateBuffer, count;

	public BufferManager(float[] vertices, byte[] indices, float[] textureCoordinates) {
		count = indices.length;
		vertexArray = glGenVertexArrays();
		glBindVertexArray(vertexArray);

		vertexBuffer = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
		glBufferData(GL_ARRAY_BUFFER, Utilities.createFloatBuffer(vertices), GL_STATIC_DRAW);
		glVertexAttribPointer(Shader.VERTEX_ATTRIB, 3, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(0);

		textureCoordinateBuffer = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, textureCoordinateBuffer);
		glBufferData(GL_ARRAY_BUFFER, Utilities.createFloatBuffer(textureCoordinates), GL_STATIC_DRAW);
		glVertexAttribPointer(Shader.TEXTURE_COORDS_ATTRIB, 2, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(Shader.TEXTURE_COORDS_ATTRIB);

		indexBuffer = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, Utilities.createByteBuffer(indices), GL_STATIC_DRAW);

		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}

	public void bind() {
		glBindVertexArray(vertexArray);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
	}

	public void unbind() {
		glBindVertexArray(0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	public void draw() {
		bind();
		glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_BYTE, 0);
		unbind();
	}
}
