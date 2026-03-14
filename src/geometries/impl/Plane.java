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
	private Point point;
	/**
	 * The normal vector to the plane. The normal is normlized.
	 */
	private Vector normal;

	/**
	 * Constructs a plane given three points in space.
	 * @param p1 The first point defining the plane
	 * @param p2 The second point defining the plane
	 * @param p3 The third point defining the plane
	 */
	public Plane(Point p1, Point p2, Point p3) {
		this.point = p1;
		// To be continued...
	}

	/**
	 * Constructs a plane given a point and a normal vector.
	 * @param point  A point on the plane
	 * @param normal The normal vector to the plane
	 */
	public Plane(Point point, Vector normal) {
		this.point = point;
		this.normal = normal.normalize();
	}

	@Override
	public Vector getNormal(Point point) {
		return normal;
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj) && point.equals((Plane) obj.point) && normal.equals(((Plane) obj).normal);
	}

	@Override
	public String toString() {
		return "Plane:\n\t" + "Point: " + point + "\n\t" + "Normal: " + normal;
	}

	@Override
	public int hashCode() {
		return Objects.hash(point, normal);
	}
}
