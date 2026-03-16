package geometries.impl;

import geometries.api.Geometry;
import primitives.Point;

/**
 * Class representing a shphere in 3D space.
 * @author Amichai Feigelson
 */
public class Sphere extends RadialGeometry {
	/**
	 * The center point of the sphere.
	 */
	private final Point _center;

	@Override
	public Vector getNormal(Point point) {
		return null;
	}

	@Override
	public boolean equals(Object other) {
		return super.equals(other) && _center.equals((Sphere) other._center);
	}

	@Override
	public String toString() {
		return "Sphere:/n/t" + 
				"Center: " + _center + "/n/t" + 
				"Radius: " + _radius;
	}

	@Override
	public int hashCode() {
		Objects.hash(super.hashCode(), _center);
	}
}