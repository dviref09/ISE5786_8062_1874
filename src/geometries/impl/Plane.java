package geometries.impl;

import java.util.Objects;

import geometries.api.Geometry;
import primitives.Point;
import primitives.Vector;

/**
 * Class for representing a plane in 3D space, defined by a point and a normal vector.
 * @author Dvir Farkash
 */
public class Plane extends Geometry {
	/**
	 * A point on the plane
	 */
	private final Point _point;
	/**
	 * The normal vector to the plane. The normal is normlized.
	 */
	private final Vector _normal;

	/**
	 * Constructs a plane given three points in space.
	 * @param p1 The first point defining the plane
	 * @param p2 The second point defining the plane
	 * @param p3 The third point defining the plane
	 */
	public Plane(Point p1, Point p2, Point p3) {
		this._point = p1;

		if(p1.equals(p2) || p1.equals(p3) || p2.equals(p3)) {
			throw new IllegalArgumentException("Two or more points are the same.");
		}

		Vector v1 = p1.subtract(p2);
		Vector v2 = p1.subtract(p3);

		try {
			v1.crossProduct(v2);
		}
		catch(IllegalArgumentException e) {
			throw new IllegalArgumentException("All three points are collinear to each other.");
		}

		this._normal = v1.crossProduct(v2).normalize();
	}

	/**
	 * Constructs a plane given a point and a normal vector.
	 * @param point  A point on the plane
	 * @param normal The normal vector to the plane
	 */
	public Plane(Point point, Vector normal) {
		this._point = point;
		this._normal = normal.normalize();
	}

	@Override
	public Vector getNormal(Point point) {
		return _normal;
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj) && _point.equals(((Plane) obj)._point) && _normal.equals(((Plane) obj)._normal);
	}

	@Override
	public String toString() {
		return "Plane: " + _point + "Normal " + _normal;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_point, _normal);
	}
}
