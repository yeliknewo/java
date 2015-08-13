package com.kileyowen.tradesim;

import com.kileyowen.math.Matrix4f;
import com.kileyowen.math.Vector3f;
import com.kileyowen.opengl.BufferManager;
import com.kileyowen.opengl.Shader;
import com.kileyowen.opengl.Texture;
import com.kileyowen.puppets.GameObject;

public class Sprite implements GameObject {

	private Matrix4f transform;
	private Vector3f rotation, position, scale;
	private boolean transformCurrent;
	private Texture texture;
	private BufferManager buffers;
	private Polygon shape;
	private World world;

	public Sprite(World world) {
		this.world = world;
		rotation = new Vector3f();
		position = new Vector3f();
		scale = new Vector3f(1, 1, 1);
		
		updateTransform();
		transformCurrent = false;
	}
	
	public World getWorld(){
		return world;
	}
	
	public Sprite setBuffersByPolygon(Polygon polygon) {
		setBuffers(polygon.getVertexArray(), polygon.getIndexArray(), polygon.getTexCoordArray());
		return this;
	}
	
	public boolean doesPointOverlap(Vector3f point){
		if(!getPolygon().getRect().isPointInRect(point)){
			return false;
		}
		
		return true;
	}
	
	public Polygon getPolygon(){
		return shape;
	}
	
	public Sprite setPolygon(Polygon shape){
		this.shape = shape.sortArray().generateArrays();
		return this;
	}
	
	public Sprite queueUpdateTransform(){
		transformCurrent = false;
		return this;
	}

	protected Sprite setBuffers(float[] vertices, byte[] indexes, float[] texCoords) {
		buffers = new BufferManager(vertices, indexes, texCoords);
		return this;
	}

	protected Sprite setTexture(Texture texture) {
		this.texture = texture;
		return this;
	}

	protected Sprite setTexPath(String texturePath) {
		this.texture = new Texture(texturePath);
		return this;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public Vector3f getScale() {
		return scale;
	}
	
	public Vector3f getVectorBetween(Sprite other){
		double theta = Math.atan2(other.getPosition().getY() - getPosition().getY(), other.getPosition().getX() - getPosition().getX());
		return new Vector3f((float)Math.cos(theta), (float)Math.sin(theta), 0);
	}
	
	public float getDistance(TradeNode other) {
		return (float)Math.hypot(getPosition().getX() - other.getPosition().getX(), getPosition().getY() - other.getPosition().getY());
	}

	public void updateTransform() {
		transform = Matrix4f.identity().multiply(Matrix4f.translate(position)).multiply(Matrix4f.rotateAll(rotation))
				.multiply(Matrix4f.scaleAll(scale));
		transformCurrent = true;
	}

	@Override
	public void update() {
		if (!transformCurrent) {
			updateTransform();
		}
	}

	@Override
	public void render() {
		texture.bind();
		Shader.shader1.enable();
		Shader.shader1.setUniformMat4f("u_ml_matrix", transform);
		buffers.draw();
		Shader.shader1.disable();
		texture.unbind();
	}
}
