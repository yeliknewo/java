package com.kileyowen.tradesim;

import java.util.ArrayList;
import java.util.List;

public class Trader extends Sprite {
	
	private List<TradeNode> tradePath;
	private float speed;

	public Trader(World world) {
		super(world);
		setPolygon(Polygon.drawRegularPolygon(20, getWorld().getAspectRatio()));
		setTexPath("assets/yellow.png");
		setBuffersByPolygon(getPolygon());
		tradePath = new ArrayList<TradeNode>();
		speed = 0.1f;
	}
	
	public Trader addNodeToPath(TradeNode node){
		tradePath.add(node);
		return this;
	}

	@Override 
	public void update(){
		if(tradePath.size() != 0){
			getPosition().add(getVectorBetween(tradePath.get(0)).scale(Math.min(speed, getDistance(tradePath.get(0)))));
			queueUpdateTransform();
		}
		super.update();
	}

	@Override
	public void render(){
		super.render();
	}
}
