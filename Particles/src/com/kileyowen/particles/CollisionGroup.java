package com.kileyowen.particles;

import java.util.ArrayList;
import java.util.List;

public class CollisionGroup {
	List<Entity> entities;

	public CollisionGroup() {
		entities = new ArrayList<Entity>();
	}

	public CollisionGroup add(Entity entity) {
		entities.add(entity);
		return this;
	}

	public boolean remove(Entity entity) {
		return entities.remove(entity);
	}

	public boolean contains(Entity entity) {
		return entities.contains(entity);
	}

	public int size() {
		return entities.size();
	}

	public Entity get(int index) {
		return entities.get(index);
	}

	public CollisionGroup concat(CollisionGroup group) {
		CollisionGroup temp = new CollisionGroup();
		for (int i = 0; i < size(); i++) {
			temp.add(get(i));
		}
		for (int i = 0; i < group.size(); i++) {
			Entity e = group.get(i);
			if (!temp.contains(e)) {
				temp.add(e);
			}
		}
		return temp;
	}
	
	public List<Entity> checkCollide(Rect rect){
		List<Entity> collided = new ArrayList<Entity>();
		for (int i = 0; i < size(); i++) {
			Entity temp = get(i);
			if (temp.checkCollide(rect)) {
				collided.add(temp);
			}
		}
		return collided;
	}

	public List<Entity> checkCollide(Entity entity) {
		List<Entity> collided = new ArrayList<Entity>();
		for (int i = 0; i < size(); i++) {
			Entity temp = get(i);
			if (temp.getId() == entity.getId()) {
				continue;
			}
			if (entity.checkCollide(temp)) {
				collided.add(temp);
			}
		}
		return collided;
	}
}
