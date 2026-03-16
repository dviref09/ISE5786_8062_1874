package geometries.impl;

import geometries.api.Geometry;

/**
 * Class representing a cylinder in 3D space.
 */
public class Cylinder extends Tube {
	/**
	 * The height (or lenghth) of the cylinder.
	 */
	private double _height;
	
	@Override
	public boolean equals(Object other) {
		return super.equals(other) && Util.isZero(height - ((Cylinder) other).height;
	}

	@Override
	public String toString() {
		return "Cylinder:\n\t" +
				super.toString() + "\n\t" +
				"Height: " + Double.toString(_height);
				// same problem as in Tube, but even worse
	}

	@Override
	public int hashCode() {
		Objects.hash(super.hashCode(), _height);
	}
}