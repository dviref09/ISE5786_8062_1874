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
	protected double radius;
	/**
	 * The radius squared of the radial geometric body
	 */
	protected double radiusSquared;

	/**
	 * Constructor with radius parameter
	 * @param radius The radius of the radial geometric body
	 */
	public RadialGeometry(double radius) {
		this.radius = radius;
		this.radiusSquared = radius * radius;
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj) && Double.compare(radius, ((RadialGeometry) obj).radius) == 0;
	}

	@Override
	public String toString() {
		return Double.toString(radius);
	}

	@Override
	public int hashCode() {
		return Objects.hash(radius);
	}

}
