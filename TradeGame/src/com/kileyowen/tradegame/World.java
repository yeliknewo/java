package com.kileyowen.tradegame;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.kileyowen.math.Matrix4f;
import com.kileyowen.math.Vector3f;
import com.kileyowen.tradegame.opengl.Shader;

import static org.lwjgl.glfw.GLFW.*;

public class World implements GameObject {
	private Vector3f camera;
	private List<GameObject> gameObjects;

	public World() {
		camera = new Vector3f().setCoords(0, 0, 0);
		gameObjects = new ArrayList<GameObject>();
		gameObjects.add(new Puppet());
	}

	public Vector3f getCamera() {
		return camera;
	}

	@Override
	public void render() {
		Shader.shader1.enable();
		Shader.shader1.setUniformMat4f("u_vw_matrix", Matrix4f.translate(getCamera().negate()));
		Shader.shader1.disable();

		for (int i = 0; i < gameObjects.size(); i++) {
			gameObjects.get(i).render();
		}
	}

	@Override
	public void update() {
		if (KeyboardHandler.isKeyDown(GLFW_KEY_LEFT)) {
			camera.addCoords(-1 / 10f, 0, 0);
		}
		if (KeyboardHandler.isKeyDown(GLFW_KEY_RIGHT)) {
			camera.addCoords(1 / 10f, 0, 0);
		}
		if (KeyboardHandler.isKeyDown(GLFW_KEY_UP)) {
			camera.addCoords(0, 1 / 10f, 0);
		}
		if (KeyboardHandler.isKeyDown(GLFW_KEY_DOWN)) {
			camera.addCoords(0, -1 / 10f, 0);
		}
		if (KeyboardHandler.isKeyDown(GLFW.GLFW_KEY_KP_SUBTRACT)) {
			camera.addCoords(0, 0, -1 / 10f);
		}
		if (KeyboardHandler.isKeyDown(GLFW_KEY_KP_ADD)) {
			camera.addCoords(0, 0, 1 / 10f);
		}

		for (int i = 0; i < gameObjects.size(); i++) {
			gameObjects.get(i).update();
		}
	}
}
