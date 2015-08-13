package com.kileyowen.tradesim;

import com.kileyowen.math.Vector3f;

public class TradeRoad extends Sprite {
	private TradeNode start, end;
	private boolean betweenCurrent;

	public TradeRoad(World world) {
		super(world);
		setPolygon(new Polygon()
				.addPoint(new Vector3f(-0.5f, -0.5f, 0))
				.addPoint(new Vector3f(-0.5f, 0.5f, 0))
				.addPoint(new Vector3f(0.5f, -0.5f, 0))
				.addPoint(new Vector3f(0.5f, 0.5f, 0)));

		setTexPath("assets/blue.png");
		setBuffersByPolygon(getPolygon());

		betweenCurrent = true;
	}
	
	public boolean isEitherNodeCurrent(){
		return start.areRoadsCurrent() || end.areRoadsCurrent();
	}

	public TradeRoad queueUpdateBetween() {
		betweenCurrent = false;
		return this;
	}

	public TradeRoad setBetween(TradeNode start, TradeNode end) {
		this.start = start;
		this.end = end;
		queueUpdateBetween();
		return this;
	}

	public TradeRoad updateBetween() {
		double diffX = end.getPosition().getX() - start.getPosition().getX(),
				diffY = end.getPosition().getY() - start.getPosition().getY();
		double theta = Math.atan2(diffY, diffX);
		getRotation().setCoords(0, 0, (float) theta);
		getPosition().setCoords(
				(end.getPosition().getX() + start.getPosition().getX()) / 2f,
				(end.getPosition().getY() + start.getPosition().getY()) / 2f,
				1);
		getScale().setCoords((float) Math.hypot(diffX, diffY), 0.1f, 1);
		betweenCurrent = true;
		queueUpdateTransform();
		return this;
	}

	@Override
	public void update() {
		if (!betweenCurrent) {
			updateBetween();
		}
		super.update();
	}

	@Override
	public void render() {
		super.render();
	}
}
