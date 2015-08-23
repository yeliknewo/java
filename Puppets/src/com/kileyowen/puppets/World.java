package com.kileyowen.puppets;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.kileyowen.math.Matrix4f;
import com.kileyowen.math.Vector3f;
import com.kileyowen.opengl.Shader;

import static org.lwjgl.glfw.GLFW.*;

public class World implements GameObject {
	private Vector3f camera;
	private List<GameObject> gameObjects;
	private float orthoX0, orthoX1, orthoY0, orthoY1, orthoZ0, orthoZ1, aspectRatio;
	private int screenWidth, screenHeight;

	public World() {
		camera = new Vector3f().setCoords(0, 0, 0);
		gameObjects = new ArrayList<GameObject>();
		gameObjects.add(new Puppet());
	}

	public Vector3f getCamera() {
		return camera;
	}

	public Vector3f screenToWorldPoint(double x, double y) {
		y = screenHeight - y;
		float orthoWidth = orthoX1 - orthoX0, orthoHeight = orthoY1 - orthoY0;
		x = x / screenWidth * orthoWidth + camera.getX() - orthoWidth / 2;
		y = y / screenHeight * orthoHeight + camera.getY() - orthoHeight / 2;
		return new Vector3f((float) x, (float) y, 0);
	}

	public void setResolution(int screenWidth, int screenHeight) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		aspectRatio = (float) (screenWidth) / (float) (screenHeight);
	}

	public void setOrthographicSize(float x0, float x1, float y0, float y1, float z0, float z1) {
		orthoX0 = x0;
		orthoX1 = x1;
		orthoY0 = y0;
		orthoY1 = y1;
		orthoZ0 = z0;
		orthoZ1 = z1;
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
