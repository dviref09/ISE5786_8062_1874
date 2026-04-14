package geometries.impl;

import java.util.Objects;

import primitives.Point;
import primitives.Vector;

/**
 * Class representing a shphere in 3D space.
 * @author Amichai Feigelson
 */
public class Sphere extends RadialGeometry {
	/**
	 * The center point of the sphere.
	 */
	private final Point _center;

	/**
	 * Constructor a sphere from center point and radius.
	 * @param center The sphere's center point.
	 * @param radius The sphere's radius.
	 */
	public Sphere(Point center, double radius) {
		super(radius);
		_center = center;
	}

	@Override
	public Vector getNormal(Point point) {
		return point.subtract(_center).normalize();
	}

	@Override
	public boolean equals(Object other) {
		return super.equals(other) && _center.equals(((Sphere) other)._center);
	}

	@Override
	public String toString() {
		return "Sphere: Center: " + _center + " " + super.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), _center);
	}
}