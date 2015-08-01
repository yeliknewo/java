package com.kileyowen.particles;

import java.util.List;

public class EntityDot extends Entity {
	private CollisionGroup walls;
	private int lastX;
	private int lastY;

	public EntityDot(int id, CollisionGroup walls) {
		super(id);
		this.walls = walls;
	}

	@Override
	public void update(World world, Mouse mouse) {
		if (mouse.getLeftButton()) {
			double theta = Math.atan2(getY() - mouse.getY(), getX() - mouse.getX());
			setSpeed((float) (getSpeedX() - getAccel() * Math.cos(theta) + (Math.random() - 0.5f) * 0.5f),
					(float) (getSpeedY() - getAccel() * Math.sin(theta) + (Math.random() - 0.5f) * 0.5f));
		}
		setSpeed(getSpeedX() * getFriction(), getSpeedY() * getFriction());
		setRGB(Math.round(getX() * 255f / world.getRect().getX1()), Math.round(getY() * 255f / world.getRect().getY1()),255);
		lastX = getX();
		lastY = getY();
		super.update(world, mouse);
		List<Entity> other = walls.checkCollide(this);
		if (other.size() > 0) {
			float speedX = getSpeedX();
			float speedY = getSpeedY();
			int width = getWidth();
			int height = getHeight();
			for (int i = 0; i < other.size(); i++) {
				if (other.get(i).isStationary()) {
					float newX = getX();
					float newY = getY();
					float speedXMult = 1;
					float speedYMult = 1;
					Rect rect = other.get(i).getRect();
					if (lastX < rect.getX0()) {
						if (lastY < rect.getY0()) {
							if(speedX > 0){
								speedXMult = -1;
							}
							if(speedY > 0){
								speedYMult = -1;
							}
							newX = rect.getX0() - width;
							newY = rect.getY0() - height;
						} else if (lastY >= rect.getY0() && lastY <= rect.getY1()) {
							if(speedX > 0){
								speedXMult = -1;
							}
							newX = rect.getX0() - width;
						} else {
							if(speedX > 0){
								speedXMult = -1;
							}
							if(speedY < 0){
								speedYMult = -1;
							}
							newX = rect.getX0() - width;
							newY = rect.getY1() + height;
						}
					} else if (lastX >= rect.getX0() && lastX <= rect.getX1()) {
						if (lastY < rect.getY0()) {
							if(speedY > 0){
								speedYMult = -1;
							}
							newY = rect.getY0() - height;
						} else if (lastY >= rect.getY0() && lastY <= rect.getY1()) {

						} else {
							if(speedY < 0){
								speedYMult = -1;
							}
							newY = rect.getY1() + height;
						}
					} else {
						if (lastY < rect.getY0()) {
							if(speedX < 0){
								speedXMult = -1;
							}
							if(speedY > 0){
								speedYMult = -1;
							}
							newX = rect.getX1() + width;
							newY = rect.getY0() - height;
						} else if (lastY >= rect.getY0() && lastY <= rect.getY1()) {
							if(speedX < 0){
								speedXMult = -1;
							}
							newX = rect.getX1() + width;
						} else {
							if(speedX < 0){
								speedXMult = -1;
							}
							if(speedY < 0){
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
		int[] point = {getX(), getY()};
		if(!world.getRect().doesPointOverlap(point)){
			//outside world
		}
	}
}
