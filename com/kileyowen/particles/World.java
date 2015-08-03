package com.kileyowen.particles;

import java.util.List;

public class World implements Renderable {
	private Rect rect;
	private CollisionGroup entities;
	private CollisionGroup particles;
	private CollisionGroup walls;
	private CollisionGroup winds;
	private int lastId;

	public World(int width, int height) {
		lastId = 0;

		rect = new Rect(0, 0, width, height);
		entities = new CollisionGroup();
		particles = new CollisionGroup();
		walls = new CollisionGroup();
		winds = new CollisionGroup();

		int wallGirth = 50;
		// Boundaries
		entities.add(
				new EntityWall(lastId++).setCollisionGroup(walls).setSize(wallGirth, height).setRGB(255, 255, 255));
		entities.add(new EntityWall(lastId++).setCollisionGroup(walls).setSize(width, wallGirth).setRGB(255, 255, 255));
		entities.add(new EntityWall(lastId++).setCollisionGroup(walls).setSize(wallGirth, height).setRGB(255, 255, 255)
				.setPos(width - wallGirth, 0));
		entities.add(new EntityWall(lastId++).setCollisionGroup(walls).setSize(width, wallGirth).setRGB(255, 255, 255)
				.setPos(0, height - wallGirth));

		for (int i = 0; i < 50; i++) {
			entities.add(new EntityBlockade(lastId++, particles).setCollisionGroup(walls).setSize(wallGirth, wallGirth)
					.setRGB(255, 255, 255).setPos((float) Math.random() * width, (float) Math.random() * height));
		}

		// four winds
		int windGirth = 100;
		entities.add(new EntityWind(lastId++, particles).setCollisionGroup(winds).setSpeed(5, 0).setRGB(255, 0, 0)
				.setSize(windGirth, windGirth).setPos(wallGirth, wallGirth));
		entities.add(new EntityWind(lastId++, particles).setCollisionGroup(winds).setSpeed(0, 5).setRGB(255, 0, 0)
				.setSize(windGirth, windGirth).setPos(width - wallGirth - windGirth, wallGirth));
		entities.add(new EntityWind(lastId++, particles).setCollisionGroup(winds).setSpeed(-5, 0).setRGB(255, 0, 0)
				.setSize(windGirth, windGirth).setPos(width - wallGirth - windGirth, height - wallGirth - windGirth));
		entities.add(new EntityWind(lastId++, particles).setCollisionGroup(winds).setSpeed(0, -5).setRGB(255, 0, 0)
				.setSize(windGirth, windGirth).setPos(wallGirth, height - wallGirth - windGirth));
	}

	public int getCount() {
		return entities.size();
	}

	public Rect getRect() {
		return rect;
	}

	public void remove(Entity entity) {
		entities.remove(entity);
	}

	@Override
	public void render(List2D<Integer> view) {
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).render(view);
		}
	}

	public void update(Mouse mouse) {
		if (mouse.getRightButton()) {
			entities.add(new EntityDot(lastId++, walls, winds).setPos(mouse.getX(), mouse.getY()).setAccel(1)
					.setRGB(255, 0, 255).setCollisionGroup(particles).setFriction(0.99f).setSize(3, 3));
		}
		List<Entity> entitiesInView = entities.checkCollide(getRect());
		for (int i = 0; i < entitiesInView.size(); i++) {
			entitiesInView.get(i).update(this, mouse);
		}
	}
}
