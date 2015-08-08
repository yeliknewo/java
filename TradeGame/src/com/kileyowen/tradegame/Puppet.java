package com.kileyowen.tradegame;

import java.util.ArrayList;
import java.util.List;

import com.kileyowen.math.Vector3f;

public class Puppet implements GameObject {
	private List<BodyPart> parts;

	private BodyPart head, body, leftArm, rightArm;
	
	public Puppet() {
		parts = new ArrayList<BodyPart>();

		Polygon headShape = new Polygon()
				.addPoint(new Vector3f().setCoords(-1, -1, 0))
				.addPoint(new Vector3f().setCoords(-1, 1, 0))
				.addPoint(new Vector3f().setCoords(1, -1, 0))
				.addPoint(new Vector3f().setCoords(1, 1, 0));
		headShape.sortArray();
		headShape.generateArrays();

		Polygon bodyShape = new Polygon()
				.addPoint(new Vector3f().setCoords(-2, -2, 0))
				.addPoint(new Vector3f().setCoords(-2, 2, 0))
				.addPoint(new Vector3f().setCoords(2, -2, 0))
				.addPoint(new Vector3f().setCoords(2, 2, 0));
		bodyShape.sortArray();
		bodyShape.generateArrays();
		
		Polygon armShape = new Polygon()
				.addPoint(new Vector3f().setCoords(-2, -1, 0))
				.addPoint(new Vector3f().setCoords(2, -1, 0))
				.addPoint(new Vector3f().setCoords(-2, 1, 0))
				.addPoint(new Vector3f().setCoords(2, 1, 0))
				;
		armShape.sortArray();
		armShape.generateArrays();

		head = new BodyPart("assets/test.png", headShape.getVertexArray(), headShape.getIndexArray(), headShape.getTexCoordArray());
		parts.add(head);

		body = new BodyPart("assets/test.png", bodyShape.getVertexArray(), bodyShape.getIndexArray(),
				bodyShape.getTexCoordArray());
		body.setParent(head);
		body.translate(new Vector3f().setCoords(0, -3, 0));
		parts.add(body);
		
		leftArm = new BodyPart("assets/test.png", armShape.getVertexArray(), armShape.getIndexArray(), armShape.getTexCoordArray());
		leftArm.setParent(body);
		leftArm.translate(new Vector3f().setCoords(-3, 0, 0));
		parts.add(leftArm);
		
		rightArm = new BodyPart("assets/test.png", armShape.getVertexArray(), armShape.getIndexArray(), armShape.getTexCoordArray());
		rightArm.setParent(body);
		rightArm.translate(new Vector3f().setCoords(3, 0, 0));
		parts.add(rightArm);
	}

	@Override
	public void update() {
		for (int i = 0; i < parts.size(); i++) {
			parts.get(i).update();
		}
	}

	@Override
	public void render() {
		for (int i = 0; i < parts.size(); i++) {
			parts.get(i).render();
		}
	}

}
