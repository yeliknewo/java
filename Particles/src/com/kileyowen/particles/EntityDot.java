package com.kileyowen.particles;

import java.util.List;

public class EntityDot extends Entity {
	private CollisionGroup walls;
	private CollisionGroup winds;
	private float lastX;
	private float lastY;

	public EntityDot(int id, CollisionGroup walls, CollisionGroup winds) {
		super(id);
		this.walls = walls;
		this.winds = winds;
	}

	public void checkWind(World world) {
		List<Entity> others = winds.checkCollide(this);
		if (others.size() == 0) {
			return;
		}
		for (int i = 0; i < others.size(); i++) {
			Entity other = others.get(i);
			other.collided(world, getCollisionGroup(), this);
		}
	}

	public int checkWalls(World world, int depth) {
		List<Entity> others = walls.checkCollide(this);
		if (others.size() == 0) {
			return -1;
		}
		int width = getWidth();
		int height = getHeight();
		float speedX = getSpeedX();
		float speedY = getSpeedY();
		float newX = getX();
		float newY = getY();
		float speedXMult = 1;
		float speedYMult = 1;
		for (int i = 0; i < others.size(); i++) {
			Entity other = others.get(i);
			other.collided(world, getCollisionGroup(), this);
			if (other.isStationary()) {
				Rect rect = other.getRect();
				if (lastX <= rect.getX0()) {
					if (speedX > 0) {
						speedXMult = -1;
					}
					newX = rect.getX0() - width;
				} else if (lastX >= rect.getX1()) {
					if (speedX < 0) {
						speedXMult = -1;
					}
					newX = rect.getX1() + width;
				}
				if (lastY <= rect.getY0()) {
					if (speedY > 0) {
						speedYMult = -1;
					}
					newY = rect.getY0() - height;
				} else if (lastY >= rect.getY1()) {
					if (speedY < 0) {
						speedYMult = -1;
					}
					newY = rect.getY1() + height;
				}
			}
		}
		setPos(newX, newY);
		setSpeed(speedX * speedXMult, speedY * speedYMult);
		if (depth > 0) {
			if (checkWalls(world, depth - 1) == -1) {
				return -1;
			} else {
				setPos(lastX, lastY);
			}
		}
		return 0;
	}

	@Override
	public void update(World world, Mouse mouse) {
		if (mouse.getLeftButton()) {
			double theta = Math.atan2(getY() - mouse.getY(), getX() - mouse.getX());
			setSpeed((float) (getSpeedX() - getAccel() * Math.cos(theta) + (Math.random() - 0.5f) * 3f),
					(float) (getSpeedY() - getAccel() * Math.sin(theta) + (Math.random() - 0.5f) * 3f));
		}
		checkWind(world);
		setRGB(Math.round(getX() * 255f / world.getRect().getX1()), Math.round(getY() * 255f / world.getRect().getY1()),
				getId() % 255);
		lastX = getX();
		lastY = getY();
		super.update(world, mouse);
		checkWalls(world, 3);
		int[] point = { getX(), getY() };
		if (!world.getRect().doesPointOverlap(point)) {
			// outside world
		}
	}
}