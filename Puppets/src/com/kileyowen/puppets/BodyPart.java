package com.kileyowen.puppets;

import com.kileyowen.math.Matrix4f;
import com.kileyowen.math.Vector3f;
import com.kileyowen.opengl.BufferManager;
import com.kileyowen.opengl.Shader;
import com.kileyowen.opengl.Texture;

public class BodyPart implements GameObject {
	private BodyPart parent;
	private Texture texture;
	private Matrix4f transform;
	private BufferManager buffers;
	private Vector3f rotation, position;
	private boolean transformCurrent, isJoint;

	public BodyPart(String texturePath, float[] vertexArray, byte[] indexArray, float[] texCoordArray) {
		texture = new Texture(texturePath);
		transform = Matrix4f.identity();
		rotation = new Vector3f();
		position = new Vector3f();
		buffers = new BufferManager(vertexArray, indexArray, texCoordArray);
		transformCurrent = true;
		isJoint = false;
	}

	public BodyPart() {
		isJoint = true;
		transform = Matrix4f.identity();
		rotation = new Vector3f();
		position = new Vector3f();
		transformCurrent = true;
	}

	public BodyPart setParent(BodyPart parent) {
		this.parent = parent;
		return this;
	}

	public void translate(Vector3f vector) {
		position.add(vector);
		transformCurrent = false;
	}

	public void rotate(Vector3f vector) {
		rotation.add(vector);
		transformCurrent = false;
	}

	public Matrix4f getTransformChain() {
		if (!transformCurrent) {
			updateTransform();
		}
		if (parent != null) {
			return parent.getTransformChain().multiply(transform);
		}
		return transform;
	}

	public void updateTransform() {
		transform = Matrix4f.identity().multiply(Matrix4f.translate(position)).multiply(Matrix4f.rotateAll(rotation));
		transformCurrent = true;
	}

	@Override
	public void update() {
		
	}

	@Override
	public void render() {
		if (isJoint) {
			return;
		}
		texture.bind();
		Shader.shader1.enable();
		Shader.shader1.setUniformMat4f("u_ml_matrix", getTransformChain());
		buffers.draw();
		Shader.shader1.disable();
		texture.unbind();
	}

}
