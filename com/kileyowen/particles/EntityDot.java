package com.kileyowen.particles;

import java.util.List;

public class EntityDot extends Entity {
	private CollisionGroup walls;
	private int lastX;
	private int lastY;
	private int lastX2;
	private int lastY2;

	public EntityDot(int id, CollisionGroup walls) {
		super(id);
		this.walls = walls;
	}

	@Override
	public void update(World world, Mouse mouse) {
		if (getId() % 10 == 0) {
			setSpeed(getSpeedX(), getSpeedY() + 0.5f);
		}
		if (getId() % 9 == 0) {
			setSpeed(getSpeedX() + 0.5f, getSpeedY());
		}
		if (getId() % 8 == 0) {
			setSpeed(getSpeedX() - 0.5f, getSpeedY());
		}
		if (getId() % 7 == 0) {
			setSpeed(getSpeedX(), getSpeedY() - 0.5f);
		}
		if (mouse.getLeftButton()) {
			double theta = Math.atan2(getY() - mouse.getY(), getX() - mouse.getX());
			setSpeed((float) (getSpeedX() - getAccel() * Math.cos(theta) + (Math.random() - 0.5f) * 3f),
					(float) (getSpeedY() - getAccel() * Math.sin(theta) + (Math.random() - 0.5f) * 3f));
		}
		setSpeed(getSpeedX() * getFriction(), getSpeedY() * getFriction());
		setRGB(Math.round(getX() * 255f / world.getRect().getX1()), Math.round(getY() * 255f / world.getRect().getY1()),
				255);
		lastX2 = lastX;
		lastY2 = lastY;
		lastX = getX();
		lastY = getY();
		super.update(world, mouse);
		List<Entity> others = walls.checkCollide(this);
		if (others.size() > 0) {
			int width = getWidth();
			int height = getHeight();
			for (int i = 0; i < others.size(); i++) {
				Entity other = others.get(i);
				if (other.isStationary()) {
					float speedX = getSpeedX();
					float speedY = getSpeedY();
					float newX = getX();
					float newY = getY();
					float speedXMult = 1;
					float speedYMult = 1;
					Rect rect = other.getRect();
					if (lastX < rect.getX0()) {
						if (lastY < rect.getY0()) {
							if (speedX > 0) {
								speedXMult = -1;
							}
							if (speedY > 0) {
								speedYMult = -1;
							}
							newX = rect.getX0() - width;
							newY = rect.getY0() - height;
						} else if (lastY >= rect.getY0() && lastY <= rect.getY1()) {
							if (speedX > 0) {
								speedXMult = -1;
							}
							newX = rect.getX0() - width;
						} else {
							if (speedX > 0) {
								speedXMult = -1;
							}
							if (speedY < 0) {
								speedYMult = -1;
							}
							newX = rect.getX0() - width;
							newY = rect.getY1() + height;
						}
					} else if (lastX >= rect.getX0() && lastX <= rect.getX1()) {
						if (lastY < rect.getY0()) {
							if (speedY > 0) {
								speedYMult = -1;
							}
							newY = rect.getY0() - height;
						} else if (lastY >= rect.getY0() && lastY <= rect.getY1()) {
							System.out.println(other.getId() + " " + lastX2 + " " + lastY2 + " " + lastX + " "
									+ lastY + " " + newX + " " + newY + " " + rect.getX0() + " " + rect.getY0() + " "
									+ rect.getX1() + " " + rect.getY1());
						} else {
							if (speedY < 0) {
								speedYMult = -1;
							}
							newY = rect.getY1() + height;
						}
					} else {
						if (lastY < rect.getY0()) {
							if (speedX < 0) {
								speedXMult = -1;
							}
							if (speedY > 0) {
								speedYMult = -1;
							}
							newX = rect.getX1() + width;
							newY = rect.getY0() - height;
						} else if (lastY >= rect.getY0() && lastY <= rect.getY1()) {
							if (speedX < 0) {
								speedXMult = -1;
							}
							newX = rect.getX1() + width;
						} else {
							if (speedX < 0) {
								speedXMult = -1;
							}
							if (speedY < 0) {
								speedYMult = -1;
							}
							newX = rect.getX1() + width;
							newY = rect.getY1() + height;
						}
					}
					setPos(newX, newY);
					setSpeed(getSpeedX() * speedXMult, getSpeedY() * speedYMult);
				}
			}
		}
		int[] point = { getX(), getY() };
		if (!world.getRect().doesPointOverlap(point)) {
			// outside world
		}
	}
}