package com.kileyowen.puppets;

import java.util.ArrayList;
import java.util.List;

import com.kileyowen.math.Vector3f;

public class Puppet implements GameObject {
	private List<BodyPart> parts;

	private BodyPart head, body, leftArmUpper, rightArmUpper, leftArmLower, rightArmLower, leftElbowJoint, rightElbowJoint;
	
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
				.addPoint(new Vector3f().setCoords(-1, -0.75f, 0))
				.addPoint(new Vector3f().setCoords(1, -0.75f, 0))
				.addPoint(new Vector3f().setCoords(-1, 0.75f, 0))
				.addPoint(new Vector3f().setCoords(1, 0.75f, 0))
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
		
		leftArmUpper = new BodyPart("assets/test.png", armShape.getVertexArray(), armShape.getIndexArray(), armShape.getTexCoordArray());
		leftArmUpper.setParent(body);
		leftArmUpper.translate(new Vector3f().setCoords(-3, 1, 0));
		parts.add(leftArmUpper);
		
		rightArmUpper = new BodyPart("assets/test.png", armShape.getVertexArray(), armShape.getIndexArray(), armShape.getTexCoordArray());
		rightArmUpper.setParent(body);
		rightArmUpper.translate(new Vector3f().setCoords(3, 1, 0));
		parts.add(rightArmUpper);
		
		leftElbowJoint = new BodyPart();
		leftElbowJoint.setParent(leftArmUpper);
		leftElbowJoint.translate(new Vector3f().setCoords(-1, 0, 0));
		leftElbowJoint.rotate(new Vector3f().setCoords(0, 0, (float)Math.toRadians(90)));
		parts.add(leftElbowJoint);
		
		leftArmLower = new BodyPart("assets/test.png", armShape.getVertexArray(), armShape.getIndexArray(), armShape.getTexCoordArray());
		leftArmLower.setParent(leftElbowJoint);
		leftArmLower.translate(new Vector3f().setCoords(-1, 0, 0));
		parts.add(leftArmLower);
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
