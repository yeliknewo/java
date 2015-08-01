package com.kileyowen.particles;

public class EntityWall extends Entity {

	public EntityWall(int id) {
		super(id);
	}
	
	@Override
	public boolean isStationary(){
		return true;
	}
	
	@Override
	public void update(World world, Mouse mouse){
		
	}
}
