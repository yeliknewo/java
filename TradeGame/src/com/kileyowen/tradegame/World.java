package com.kileyowen.tradegame;

import com.kileyowen.math.Matrix4f;
import com.kileyowen.math.Vector3f;
import com.kileyowen.tradegame.opengl.Shader;

public class World implements GameObject {
	private Vector3f camera;

	public World() {
		camera = new Vector3f();
	}

	public Vector3f getCamera() {
		return camera;
	}

	@Override
	public void render() {
		Shader.shader1.enable();
		Shader.shader1.setUniformMat4f("u_ml_matrix", Matrix4f.translate(getCamera()));
	}

	@Override
	public void update() {

	}
}
