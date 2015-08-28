package com.kileyowen.opengl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.util.HashMap;
import java.util.Map;

import com.kileyowen.math.*;
import static com.kileyowen.utilities.Utilities.*;

public class Shader {
	public static final int VERTEX_ATTRIB = 0, TEXTURE_COORDS_ATTRIB = 1;

	private final int ID;

	private Map<String, Integer> locationCache = new HashMap<String, Integer>();

	private boolean enabled = false;

	public Shader(String vertex, String fragment) {
		ID = load(vertex, fragment);
	}

	private int load(String vertexPath, String fragmentPath) {
		String vertexSource = loadAsString(vertexPath), fragmentSource = loadAsString(fragmentPath);
		int program = glCreateProgram(), vertexID = glCreateShader(GL_VERTEX_SHADER),
				fragmentID = glCreateShader(GL_FRAGMENT_SHADER);

		glShaderSource(vertexID, vertexSource);
		glCompileShader(vertexID);
		if (glGetShaderi(vertexID, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Failed to compile Vertex Shader: " + vertexPath);
			System.err.println(glGetShaderInfoLog(vertexID));
		}

		glShaderSource(fragmentID, fragmentSource);
		glCompileShader(fragmentID);
		if (glGetShaderi(fragmentID, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Failed to compile Fragment Shader: " + fragmentPath);
			System.err.println(glGetShaderInfoLog(fragmentID));
		}
		
		glAttachShader(program, vertexID);
		glAttachShader(program, fragmentID);
		
		glLinkProgram(program);
		glValidateProgram(program);
		
		return program;
	}

	public int getUniform(String name) {
		if (locationCache.containsKey(name)) {
			return locationCache.get(name);
		}
		int result = glGetUniformLocation(ID, name);
		if (result == -1)
			System.err.println("Could not find uniform variable'" + name + "'!");
		else
			locationCache.put(name, result);
		return glGetUniformLocation(ID, name);
	}

	public void setUniform1i(String name, int value) {
		if (!enabled)
			enable();
		glUniform1i(getUniform(name), value);
	}

	public void setUniform1f(String name, float value) {
		if (!enabled)
			enable();
		glUniform1f(getUniform(name), value);
	}

	public void setUniform2f(String name, float x, float y) {
		if (!enabled)
			enable();
		glUniform2f(getUniform(name), x, y);
	}

	public void setUniform3f(String name, Vector3f vector) {
		if (!enabled)
			enable();
		glUniform3f(getUniform(name), vector.getX(), vector.getY(), vector.getZ());
	}

	public void setUniformMat4f(String name, Matrix4f matrix) {
		if (!enabled)
			enable();
		glUniformMatrix4fv(getUniform(name), false, matrix.toFloatBuffer());
	}

	public void enable() {
		glUseProgram(ID);
		enabled = true;
	}

	public void disable() {
		glUseProgram(0);
		enabled = false;
	}

}