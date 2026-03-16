package geometries.impl;

import java.util.Objects;

import geometries.api.Geometry;

/**
 * Represents a geometry with a radius
 * @author Dvir Farkash
 */
public abstract class RadialGeometry extends Geometry {
	/**
	 * The radius of the radial geometric body
	 */
	protected double _radius;
	/**
	 * The radius squared of the radial geometric body
	 */
	protected double _radiusSquared;

	/**
	 * Constructor with radius parameter
	 * @param radius The radius of the radial geometric body
	 */
	public RadialGeometry(double radius) {
		this._radius = radius;
		this._radiusSquared = radius * radius;
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj) && Util.isZero(_radius - ((RadialGeometry) obj)._radius);
	}

	@Override
	public String toString() {
		return Double.toString(_radius);
	}

	@Override
	public int hashCode() {
		return Objects.hash(_radius);
	}

}
