package geometries.api;

import primitives.Point;
import primitives.Vector;

/**
 * An abstract class for representing a geometric body in 3D space.
 * @author Amichai Feigelson
 */
public abstract class Geometry  extends Intersectable{
	/**
	 * Calculates the normal vector at a given point on the surface of the geometric body.
	 * @param point The point on the surface of the geometric body where the normal vector is to be calculated.
	 * @return The normal vector at the point.
	 */
	public abstract Vector getNormal(Point point);

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null || getClass() != other.getClass())
			return false;
		return true;
	}

	// We didn't override hashCode and toString methods because we don't have any fields in this class, so we don't have
	// anything to hash or to print.
}