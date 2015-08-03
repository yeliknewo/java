package com.kileyowen.particles;

import java.util.ArrayList;
import java.util.List;

public class EntityDot extends Entity {
	private CollisionGroup walls;
	private float lastX;
	private float lastY;

	public EntityDot(int id, CollisionGroup walls) {
		super(id);
		this.walls = walls;
	}

	@Override
	public void update(World world, Mouse mouse) {
		if (mouse.getLeftButton()) {
			double theta = Math.atan2(getY() - mouse.getY(), getX() - mouse.getX());
			setSpeed((float) (getSpeedX() - getAccel() * Math.cos(theta) + (Math.random() - 0.5f) * 3f),
					(float) (getSpeedY() - getAccel() * Math.sin(theta) + (Math.random() - 0.5f) * 3f));
		}
		setSpeed(getSpeedX() * getFriction(), getSpeedY() * getFriction());
		setRGB(Math.round(getX() * 255f / world.getRect().getX1()), Math.round(getY() * 255f / world.getRect().getY1()),
				255);
		lastX = getX();
		lastY = getY();
		super.update(world, mouse);
		List<Entity> others;
		List<Integer> collided = new ArrayList<Integer>();
		do{
			others = walls.checkCollide(this);
			int width = getWidth();
			int height = getHeight();
			float speedX;
			float speedY;
			float newX;
			float newY;
			for (int i = 0; i < others.size(); i++) {
				Entity other = others.get(i);
				if (other.isStationary()) {
					speedX = getSpeedX();
					speedY = getSpeedY();
					newX = getX();
					newY = getY();
					float speedXMult = 1;
					float speedYMult = 1;
					boolean triggered = false;
					Rect rect = other.getRect();
					if (lastX < rect.getX0()) {
						if (speedX > 0) {
							speedXMult = -1;
						}
						newX = rect.getX0() - width;
						triggered = true;
					} else if (lastX > rect.getX1()) {
						if (speedX < 0) {
							speedXMult = -1;
						}
						newX = rect.getX1() + width;
						triggered = true;
					}
					if (lastY < rect.getY0()) {
						if (speedY > 0) {
							speedYMult = -1;
						}
						newY = rect.getY0() - height;
						triggered = true;
					} else if (lastY > rect.getY1()) {
						if (speedY < 0) {
							speedYMult = -1;
						}
						newY = rect.getY1() + height;
						triggered = true;
					}
					if(!triggered){
						
					}
					setPos(newX, newY);
					setSpeed(getSpeedX() * speedXMult, getSpeedY() * speedYMult);
					collided.add(other.getId());
				}
			}
			for(int i = 0; i < collided.size(); i++){
				for(int i2 = 0; i < others.size(); i++){
					if(collided.get(i) == others.get(i).getId()){
						others.remove(i2);
						break;
					}
				}
			}
		}while(others.size() > 0);
		int[] point = { getX(), getY() };
		if (!world.getRect().doesPointOverlap(point)) {
			// outside world
		}
	}
}