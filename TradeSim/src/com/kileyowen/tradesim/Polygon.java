package com.kileyowen.tradesim;

import java.util.ArrayList;
import java.util.List;

import com.kileyowen.math.Vector3f;
import com.kileyowen.tradesim.Rect;

public class Polygon {

	private List<Vector3f> points;
	private float[] vertices, texCoords;
	private byte[] indexes;

	public Polygon() {
		points = new ArrayList<Vector3f>();
		addPoint(new Vector3f().setCoords(0, 0, 0));
	}

	public static Polygon drawRegularPolygon(int sides, float aspectRatio) {
		Polygon shape = new Polygon();
		System.out.println(aspectRatio);
		for (int i = 0; i < sides; i++) {
			double theta = ((float)i / sides) * (Math.PI * 2f);
			shape.addPoint(new Vector3f((float)Math.cos(theta) / 2, (float)(Math.sin(theta) * aspectRatio) / 2, 0));
		}
		return shape;
	}

	public Rect getRect() {
		float x0 = points.get(0).getX(), x1 = points.get(0).getX(), y0 = points.get(0).getY(),
				y1 = points.get(0).getY();
		for (int i = 0; i < points.size(); i++) {
			x0 = Math.min(x0, points.get(i).getX());
			x1 = Math.max(x1, points.get(i).getX());
			y0 = Math.min(y0, points.get(i).getY());
			y1 = Math.max(y1, points.get(i).getY());
		}
		return new Rect(x0, x1, y0, y1);
	}

	public Polygon addPoint(Vector3f vector) {
		points.add(vector);
		if (points.size() >= 64) {
			throw new Error("Too Many Points on Polygon");
		}
		return this;
	}

	public Polygon sortArray() {
		List<Vector3f> result = new ArrayList<Vector3f>();
		int length = points.size();
		while (result.size() < length) {
			double lastATan = -Math.PI - 1;
			int indexOfLastATan = -1;
			for (int i = 0; i < points.size(); i++) {
				Vector3f point = points.get(i);
				if (point.getX() == 0 && point.getY() == 0) {
					lastATan = Math.PI + 1;
					indexOfLastATan = i;
				} else {
					double atan = Math.atan2(point.getY(), point.getX());
					if (atan > lastATan) {
						lastATan = atan;
						indexOfLastATan = i;
					}
				}
			}
			result.add(points.get(indexOfLastATan));
			points.remove(indexOfLastATan);
		}
		points = result;
		return this;
	}

	public Polygon generateArrays() {
		indexes = new byte[(points.size() - 1) * 3];
		vertices = new float[points.size() * 3];
		texCoords = new float[points.size() * 2];
		float bigX = 0, bigY = 0, smallX = 0, smallY = 0;
		for (int i = 0; i < points.size(); i++) {
			vertices[i * 3 + 0] = points.get(i).getX();
			vertices[i * 3 + 1] = points.get(i).getY();
			vertices[i * 3 + 2] = points.get(i).getZ();
			bigX = Math.max(bigX, points.get(i).getX());
			bigY = Math.max(bigY, points.get(i).getY());
			smallX = Math.min(smallX, points.get(i).getX());
			smallY = Math.min(smallY, points.get(i).getY());
		}
		for (int i = 0; i < points.size(); i++) {
			texCoords[i * 2 + 0] = (points.get(i).getX() - smallX) / (bigX - smallX);
			texCoords[i * 2 + 1] = (points.get(i).getY() - smallY) / (bigY - smallY);
		}
		for (int i = 0; i < points.size() - 2; i++) {
			indexes[i * 3 + 0] = 0;
			indexes[i * 3 + 1] = (byte) (i + 1);
			indexes[i * 3 + 2] = (byte) (i + 2);
		}
		indexes[indexes.length - 3] = 0;
		indexes[indexes.length - 2] = 1;
		indexes[indexes.length - 1] = (byte) (points.size() - 1);
		return this;
	}

	public float[] getVertexArray() {
		/*
		 * String out = ""; for (int i = 0; i < vertices.length; i++) { out +=
		 * vertices[i] + " "; } System.out.println("Vertices " + out);
		 */
		return vertices;
	}

	public byte[] getIndexArray() {
		/*
		 * String out = ""; for (int i = 0; i < indexes.length; i++) { out +=
		 * indexes[i] + " "; } System.out.println("Indexes " + out);
		 */
		return indexes;
	}

	public float[] getTexCoordArray() {
		return texCoords;
	}
}
