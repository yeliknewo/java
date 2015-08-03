package com.kileyowen.particles;

import java.util.List;

public class EntityBlockade extends Entity {
	private CollisionGroup particles;
	private int size;

	public EntityBlockade(int id, CollisionGroup particles) {
		super(id);
		this.particles = particles;
	}
	
	public Entity setSize(int width, int height){
		super.setSize(width, height);
		this.size = Math.min(width, height);
		return this;
	}

	public boolean isStationary() {
		return true;
	}
	
	@Override
	public void collided(World world, CollisionGroup group, Entity entity){
		super.collided(world, group, entity);
		if(group.equals(particles)){
			entity.destroy(world);
			size--;
			if(size < 1){
				destroy(world);
			}
			if(size % 2 == 1){
				setSize(size, size);
				setPos(getX() + 1, getY() + 1);
			}
		}
	}
}
