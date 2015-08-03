package com.kileyowen.particles;

public class Entity implements Renderable {
	private int color;
	private int width;
	private int height;
	private float posX;
	private float posY;
	private float speedX;
	private float speedY;
	private float accel;
	private float friction;
	private boolean stationary;
	private CollisionGroup collisionGroup;
	private int id;

	public Entity(int id) {
		setPos(0, 0);
		setSize(1, 1);
		setColor(0);
		setAccel(0);
		setSpeed(0, 0);
		setFriction(1);
		setId(id);
	}

	private Entity setId(int id) {
		this.id = id;
		return this;
	}

	public Entity setSize(int width, int height) {
		this.width = width;
		this.height = height;
		return this;
	}

	public Entity setPos(float x, float y) {
		this.posX = x;
		this.posY = y;
		return this;
	}

	public Entity setColor(int color) {
		this.color = color;
		return this;
	}

	public Entity setAccel(float accel) {
		this.accel = accel;
		return this;
	}

	public Entity setRGB(int red, int green, int blue) {
		return setColor(red << 16 | green << 8 | blue << 0);
	}

	public Entity setSpeed(float speedX, float speedY) {
		this.speedX = speedX;
		this.speedY = speedY;
		return this;
	}

	public Entity setCollisionGroup(CollisionGroup group) {
		this.collisionGroup = group;
		collisionGroup.add(this);
		return this;
	}

	public Entity setFriction(float friction) {
		this.friction = friction;
		return this;
	}

	public int getId() {
		return id;
	}

	public int getX() {
		return Math.round(posX);
	}

	public int getY() {
		return Math.round(posY);
	}

	public float getSpeedX() {
		return speedX;
	}

	public float getSpeedY() {
		return speedY;
	}

	public float getAccel() {
		return accel;
	}

	public float getFriction() {
		return friction;
	}

	public CollisionGroup getCollisionGroup() {
		return collisionGroup;
	}

	public Rect getRect() {
		int x = getX();
		int y = getY();
		return new Rect(x, y, width + x - 1, height + y - 1);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isStationary() {
		return stationary;
	}

	public void collided(World world, CollisionGroup group, Entity entity) {

	}

	public boolean checkCollide(Entity other) {
		return checkCollide(other.getRect());
	}
	
	public boolean checkCollide(Rect rect){
		if (getWidth() > 1 || getHeight() > 1) {
			return rect.doesRectOverlap(getRect());
		} else {
			int[] point = { getX(), getY() };
			return rect.doesPointOverlap(point);
		}
	}

	@Override
	public void render(List2D<Integer> view) {
		int y = getY();
		int x = getX();
		for (int iy = 0; iy < height; iy++) {
			for (int ix = 0; ix < width; ix++) {
				view.set(x + ix, y + iy, color);
			}
		}
	}

	public void update(World world, Mouse mouse) {
		if (!isStationary()) {
			setSpeed(getSpeedX() * getFriction(), getSpeedY() * getFriction());
			posX += speedX;
			posY += speedY;
		}
	}

	public void destroy(World world) {
		world.remove(this);
		CollisionGroup cg = getCollisionGroup();
		if (cg != null) {
			cg.remove(this);
		}
	}
}
