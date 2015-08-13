package com.kileyowen.tradesim;

import java.util.ArrayList;
import java.util.List;

import com.kileyowen.math.Vector3f;

public class TradeNode extends Sprite {
	private List<TradeNode> connections;
	private boolean roadsCurrent;

	public TradeNode(World world) {
		super(world);
		setPolygon(new Polygon()
				.addPoint(new Vector3f(-0.5f, -0.5f, 0))
				.addPoint(new Vector3f(-0.5f, 0.5f, 0))
				.addPoint(new Vector3f(0.5f, 0, 0))
				);

		setTexPath("assets/red.png");
		setBuffersByPolygon(getPolygon());

		connections = new ArrayList<TradeNode>();
		setRoadsCurrent(true);
	}

	public boolean areRoadsCurrent(){
		return roadsCurrent;
	}
	
	public TradeNode setRoadsCurrent(boolean current){
		roadsCurrent = current;
		return this;
	}

	public List<TradeNode> getConnections() {
		return connections;
	}

	public TradeNode addConnection(TradeNode other) {
		if (other.equals(this) || connections.contains(other)) {
			return this;
		}
		getWorld().setWorldCurrent(false);
		setRoadsCurrent(false);
		connections.add(other);
		if (!other.connections.contains(this)) {
			other.addConnection(this);
		}
		return this;
	}
	
	public TradeNode removeConnection(TradeNode other){
		if(connections.contains(other)){
			connections.remove(other);
			setRoadsCurrent(false);
		}
		if(other.getConnections().contains(this)){
			other.getConnections().remove(this);
		}
		return this;
	}
	
	public TradeNode removeAllConnections(){
		while(connections.size() > 0){
			removeConnection(connections.get(0));
		}
		return this;
	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	public void render() {
		super.render();
	}
}
