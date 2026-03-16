package geometries.impl;

import geometries.api.Geometry;

/**
 * Class representing a triangle in 3D space.
 * @author Amichai Feigelson
 */
public class Trianle extends Polygon {
	/**
	 * Constructs a triangle given three vertices.
	 * @param p1 The first vertex of the triangle.
	 * @param p2 The second vertex of the triangle.
	 * @param p3 The third vertex of the triangle.
	 */
	public Triangle(Point p1, Point p2, Point p3) {
		super(p1, p2, p3);
	}

	@Override
	public boolean equals(Object other) {
		if(this == other)
			return true;
		if(other == null || getClass() != other.getClass())
			return false;
		return super.equals(other);
	}

	@Override
	public String toString() {
		return "Triangle:\n\t" + "Vertex 1: " + _vertices[0] +
				"\n\t" + "Vertex 2: " + _vertices[1] +
				"\n\t" + "Vertex 3: " + _vertices[2];
	}

	@Override
	public int hashCode() {
		return Objects.hash(_vertices[0], _vertices[1], _vertices[2]);
	}
}