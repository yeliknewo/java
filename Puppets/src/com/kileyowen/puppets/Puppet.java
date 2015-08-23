package com.kileyowen.puppets;

import java.util.ArrayList;
import java.util.List;

import com.kileyowen.math.Vector3f;

public class Puppet implements GameObject {
	private List<BodyPart> parts;

	private BodyPart head, leftEyeJoint, leftEye, rightEyeJoint, rightEye;

	private int elbowSpinSpeed;

	public Puppet() {
		parts = new ArrayList<BodyPart>();
		elbowSpinSpeed = 1;
		float baseSize = 1, headWidthByBase = 3, headHeightByBase = 4, eyeWidthByHeadWidth = 0.2f,
				eyeXByEyeWidth = 1f, eyeHeightByEyeWidth = 0.5f, eyeYByHeadHeight = 0;
		
		float headWidth = baseSize * headWidthByBase, headHeight = baseSize * headHeightByBase;
		

		Polygon headShape = new Polygon()
				.addPoint(new Vector3f(-headWidth / 2, -headHeight / 2, 0))
				.addPoint(new Vector3f(-headWidth / 2, headHeight / 2, 0))
				.addPoint(new Vector3f(headWidth / 2, -headHeight / 2, 0))
				.addPoint(new Vector3f(headWidth / 2, headHeight / 2, 0));
		headShape.sortArray();
		headShape.generateArrays();
		
		float eyeWidth = eyeWidthByHeadWidth * headWidth, eyeHeight = eyeHeightByEyeWidth * eyeWidth;

		Polygon eyeShape = new Polygon()
				.addPoint(new Vector3f(-eyeWidth / 2, -eyeHeight / 2, 0))
				.addPoint(new Vector3f(-eyeWidth / 2, eyeHeight / 2, 0))
				.addPoint(new Vector3f(eyeWidth / 2, -eyeHeight / 2, 0))
				.addPoint(new Vector3f(eyeWidth / 2, eyeHeight / 2, 0))
				;
		eyeShape.sortArray();
		eyeShape.generateArrays();

		head = new BodyPart("assets/test.png", headShape.getVertexArray(), headShape.getIndexArray(),
				headShape.getTexCoordArray());
		parts.add(head);
		
		float eyeX = eyeXByEyeWidth * eyeWidth, eyeY = eyeYByHeadHeight * headHeight;
		
		leftEyeJoint = new BodyPart();
		leftEyeJoint.translate(new Vector3f(-eyeX, eyeY, -1));
		leftEyeJoint.setParent(head);
		parts.add(leftEyeJoint);
		
		leftEye = new BodyPart("assets/test.png", eyeShape.getVertexArray(), eyeShape.getIndexArray(), eyeShape.getTexCoordArray());
		leftEye.setParent(leftEyeJoint);
		parts.add(leftEye);
		
		rightEyeJoint = new BodyPart();
		rightEyeJoint.translate(new Vector3f(eyeX, eyeY, -1));
		rightEyeJoint.setParent(head);
		parts.add(rightEyeJoint);
		
		rightEye = new BodyPart("assets/test.png", eyeShape.getVertexArray(), eyeShape.getIndexArray(), eyeShape.getTexCoordArray());
		rightEye.setParent(rightEyeJoint);
		parts.add(rightEye);

		/*body = new BodyPart("assets/test.png", bodyShape.getVertexArray(), bodyShape.getIndexArray(),
				bodyShape.getTexCoordArray());
		body.setParent(head);
		body.translate(new Vector3f(0, -3, 0));
		parts.add(body);

		leftArmUpper = new BodyPart("assets/test.png", armShape.getVertexArray(), armShape.getIndexArray(),
				armShape.getTexCoordArray());
		leftArmUpper.setParent(body);
		leftArmUpper.translate(new Vector3f(-3, 1, 0));
		parts.add(leftArmUpper);

		rightArmUpper = new BodyPart("assets/test.png", armShape.getVertexArray(), armShape.getIndexArray(),
				armShape.getTexCoordArray());
		rightArmUpper.setParent(body);
		rightArmUpper.translate(new Vector3f(3, 1, 0));
		parts.add(rightArmUpper);

		leftElbowJoint = new BodyPart();
		leftElbowJoint.setParent(leftArmUpper);
		leftElbowJoint.translate(new Vector3f(-1, 0, 0));
		parts.add(leftElbowJoint);

		leftArmLower = new BodyPart("assets/test.png", armShape.getVertexArray(), armShape.getIndexArray(),
				armShape.getTexCoordArray());
		leftArmLower.setParent(leftElbowJoint);
		leftArmLower.translate(new Vector3f(-1, 0, 0));
		parts.add(leftArmLower);

		rightElbowJoint = new BodyPart();
		rightElbowJoint.setParent(rightArmUpper);
		rightElbowJoint.translate(new Vector3f(1, 0, 0));
		parts.add(rightElbowJoint);

		rightArmLower = new BodyPart("assets/test.png", armShape.getVertexArray(), armShape.getIndexArray(),
				armShape.getTexCoordArray());
		rightArmLower.setParent(rightElbowJoint);
		rightArmLower.translate(new Vector3f(1, 0, 0));
		parts.add(rightArmLower);*/
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
