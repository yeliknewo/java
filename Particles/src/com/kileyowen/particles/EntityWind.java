package com.kileyowen.particles;

public class EntityWind extends Entity {
	private CollisionGroup particles;

	public EntityWind(int id, CollisionGroup particles) {
		super(id);
		this.particles = particles;
	}
	
	@Override
	public boolean isStationary(){
		return true;
	}

	@Override
	public void collided(World world, CollisionGroup group, Entity entity) {
		if (group.equals(particles)) {
			entity.setSpeed(entity.getSpeedX() + getSpeedX(), entity.getSpeedY() + getSpeedY());
		}
	}
}
