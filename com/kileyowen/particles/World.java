package com.kileyowen.particles;

import java.util.ArrayList;
import java.util.List;

public class World implements Renderable {
	private List<Entity> entities;
	private Rect rect;
	private CollisionGroup particles;
	private CollisionGroup walls;
	private int lastId;

	public World(int width, int height) {
		lastId = 0;
		
		entities = new ArrayList<Entity>();
		rect = new Rect(0, 0, width, height);
		particles = new CollisionGroup();
		walls = new CollisionGroup();

		int wallGirth = 50;
		//Boundaries
		entities.add(new EntityWall(lastId++).setCollisionGroup(walls).setSize(wallGirth, height).setRGB(255, 255, 255));
		entities.add(new EntityWall(lastId++).setCollisionGroup(walls).setSize(width, wallGirth).setRGB(255, 255, 255));
		entities.add(new EntityWall(lastId++).setCollisionGroup(walls).setSize(wallGirth, height).setRGB(255, 255, 255)
				.setPos(width - wallGirth, 0));
		entities.add(new EntityWall(lastId++).setCollisionGroup(walls).setSize(width, wallGirth).setRGB(255, 255, 255).setPos(0,
				height - wallGirth));
		
		//Center
		entities.add(new EntityWall(lastId++).setCollisionGroup(walls).setSize(wallGirth, wallGirth).setRGB(255, 255, 255)
				.setPos((width - wallGirth) / 2, (height - wallGirth) / 2));
		entities.add(new EntityWall(lastId++).setCollisionGroup(walls).setSize(wallGirth, wallGirth).setRGB(255, 255, 255)
				.setPos((width - wallGirth) / 2, (height + wallGirth) / 2));
		entities.add(new EntityWall(lastId++).setCollisionGroup(walls).setSize(wallGirth, wallGirth).setRGB(255, 255, 255)
				.setPos((width + wallGirth) / 2, (height + wallGirth) / 2));
		entities.add(new EntityWall(lastId++).setCollisionGroup(walls).setSize(wallGirth, wallGirth).setRGB(255, 255, 255)
				.setPos((width + wallGirth) / 2, (height - wallGirth * 3) / 2));
		entities.add(new EntityWall(lastId++).setCollisionGroup(walls).setSize(wallGirth, wallGirth).setRGB(255, 255, 255)
				.setPos((width - wallGirth) / 2, (height - wallGirth * 3) / 2));
		entities.add(new EntityWall(lastId++).setCollisionGroup(walls).setSize(wallGirth, wallGirth).setRGB(255, 255, 255)
				.setPos((width - wallGirth * 3) / 2, (height - wallGirth * 3) / 2));
		
		//left center
		entities.add(new EntityWall(lastId++).setCollisionGroup(walls).setSize(wallGirth, wallGirth).setRGB(255, 255, 255)
				.setPos((width - wallGirth * 5) / 2 - 2, (height - wallGirth) / 2));
		entities.add(new EntityWall(lastId++).setCollisionGroup(walls).setSize(wallGirth, wallGirth * 3).setRGB(255, 255, 255)
				.setPos((width - wallGirth * 7) / 2 - 2, (height - wallGirth * 3) / 2));
		entities.add(new EntityWall(lastId++).setCollisionGroup(walls).setSize(wallGirth, wallGirth).setRGB(255, 255, 255)
				.setPos((width - wallGirth * 5) / 2 - 2, (height - wallGirth * 5) / 2));

		
		
	}

	public int getCount() {
		return entities.size();
	}

	public Rect getRect() {
		return rect;
	}

	@Override
	public void render(List2D<Integer> view) {
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).render(view);
		}
	}

	public void update(Mouse mouse) {
		if (mouse.getRightButton() && particles.size() == 0) {
			entities.add(new EntityDot(lastId++, walls).setPos(mouse.getX(), mouse.getY()).setAccel(1).setRGB(255, 0, 255)
					.setCollisionGroup(particles).setFriction(0.97f).setSize(1, 1));
		}
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).update(this, mouse);
		}
	}
}
