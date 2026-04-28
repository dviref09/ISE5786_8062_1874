package geometries.impl;

import java.util.List;
import java.util.Objects;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Class representing a triangle in 3D space.
 * @author Amichai Feigelson
 */
public class Triangle extends Polygon {
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
	public List<Point> findIntersections(Ray ray) {
		return null;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null || getClass() != other.getClass())
			return false;
		return super.equals(other);
	}

	@Override
	public String toString() {
		return "Triangle: Vertex 1: " + _vertices.get(0) +
				" Vertex 2: " + _vertices.get(1) +
				" Vertex 3: " + _vertices.get(2);
	}

	@Override
	public int hashCode() {
		return Objects.hash(_vertices);
	}
}