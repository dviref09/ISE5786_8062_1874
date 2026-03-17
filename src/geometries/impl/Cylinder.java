package geometries.impl;

import java.util.Objects;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

/**
 * Class representing a cylinder in 3D space.
 */
public class Cylinder extends Tube {
	/**
	 * The height (or lenghth) of the cylinder.
	 */
	private final double _height;

	/**
	 * Construct a cylinder with the given axis ray, radius, and height.
	 * @param axis   The axis that the cylinder wraps around.
	 * @param radius The cylinder's radius.
	 * @param height The cylinder's height.
	 */
	public Cylinder(double radius, Ray axis, double height) {
		super(radius, axis);
		_height = height;
	}

	@Override
	public Vector getNormal(Point point) {
		return null;
	}

	@Override
	public boolean equals(Object other) {
		return super.equals(other) && Util.isZero(_height - ((Cylinder) other)._height);
	}

	@Override
	public String toString() {
		return "Cylinder: " + super.toString() + " Height: " + _height;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), _height);
	}
}