package com.kileyowen.tradesim;

import java.util.ArrayList;
import java.util.List;

import com.kileyowen.math.Matrix4f;
import com.kileyowen.math.Vector3f;
import com.kileyowen.opengl.Shader;
import com.kileyowen.puppets.GameObject;
import com.kileyowen.puppets.MouseHandler;

public class World implements GameObject {
	private Vector3f camera;
	private List<GameObject> gameObjects;
	private List<TradeNode> nodes;
	private List<TradeRoad> roads, unusedRoads;
	private float worldWidth, worldHeight, orthoX0, orthoX1, orthoY0, orthoY1, orthoZ0, orthoZ1, aspectRatio;
	private int screenWidth, screenHeight;
	private boolean worldCurrent;

	public World(int screenWidth, int screenHeight, float worldWidth, float worldHeight) {
		gameObjects = new ArrayList<GameObject>();
		nodes = new ArrayList<TradeNode>();
		roads = new ArrayList<TradeRoad>();
		unusedRoads = new ArrayList<TradeRoad>();
		camera = new Vector3f();

		setResolution(screenWidth, screenHeight);

		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;

		Trader trader = new Trader(this);
		trader.getPosition().setCoords(worldWidth / 2, worldHeight / 2, 0);
		gameObjects.add(trader);

		int nodeCount = 10, connectionCount = 4;

		for (int i = 0; i < nodeCount; i++) {
			TradeNode node = new TradeNode(this);
			node.getPosition().setCoords(i * (worldWidth / nodeCount) + 0.5f, (float) Math.random() * worldHeight, 0);
			addNode(node);
		}

		int index1, index2;
		for (int i = 0; i < connectionCount; i++) {
			index1 = Main.rand.nextInt(nodes.size());
			do {
				index2 = Main.rand.nextInt(nodes.size());
			} while (index1 == index2);
			nodes.get(index1).addConnection(nodes.get(index2));
		}

		camera.setCoords(worldWidth / 2, worldHeight / 2, 0);

		worldCurrent = false;

		for (int i = 0; i < 10; i++) {
			trader.addNodeToPath(nodes.get(Main.rand.nextInt(nodes.size())));
		}
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

	public float getAspectRatio() {
		return aspectRatio;
	}

	public void setOrthographicSize(float x0, float x1, float y0, float y1, float z0, float z1) {
		orthoX0 = x0;
		orthoX1 = x1;
		orthoY0 = y0;
		orthoY1 = y1;
		orthoZ0 = z0;
		orthoZ1 = z1;
	}

	public TradeNode removeNode(TradeNode node) {
		node.removeAllConnections();
		gameObjects.remove(node);
		nodes.remove(node);
		return node;
	}

	public TradeRoad removeRoad(TradeRoad road) {
		gameObjects.remove(road);
		roads.remove(road);
		unusedRoads.add(road);
		return road;
	}

	public TradeNode addNode(TradeNode node) {
		gameObjects.add(node);
		nodes.add(node);
		return node;
	}

	public TradeRoad addRoad(TradeRoad road) {
		gameObjects.add(road);
		roads.add(road);
		return road;
	}

	public void drawRoads() {
		if (nodes.size() == 0) {
			return;
		}
		List<TradeNode> closed = new ArrayList<TradeNode>();
		List<TradeNode> open = new ArrayList<TradeNode>();
		open.add(nodes.get(0));
		while (open.size() > 0) {
			TradeNode node = open.get(0);
			open.remove(node);
			for (int i = 0; i < node.getConnections().size(); i++) {
				TradeNode other = node.getConnections().get(i);
				if (!closed.contains(other)) {
					getOpenTradeRoad().setBetween(node, other);
				}
			}
			closed.add(node);
			if (closed.size() < nodes.size() && open.size() == 0) {
				for (int i = 0; i < nodes.size(); i++) {
					TradeNode filler = nodes.get(i);
					if (!open.contains(filler) && !closed.contains(filler)) {
						open.add(filler);
						break;
					}
				}
			}
		}
		setWorldCurrent(true);
	}

	public World setWorldCurrent(boolean current) {
		this.worldCurrent = current;
		return this;
	}

	public TradeRoad getOpenTradeRoad() {
		if (unusedRoads.size() > 0) {
			return addRoad(unusedRoads.get(0));
		}
		return addRoad(new TradeRoad(this));
	}

	public Vector3f getCamera() {
		return camera;
	}

	@Override
	public void update() {
		if (!worldCurrent) {
			drawRoads();
		}
		for (int i = 0; i < gameObjects.size(); i++) {
			gameObjects.get(i).update();
		}
		screenToWorldPoint(MouseHandler.getXPos(), MouseHandler.getYPos());
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
}
