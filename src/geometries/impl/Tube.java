package geometries.impl;

import geometries.api.Geometry;
import primitives.Ray;


/**
 * Class representing a tube in 3D space.
 */
public class Tube extends RadialGeometry {
	/**
	 * The axis that the tube wraps around.
	 */
	protected Ray _axis;
	
	@Override
	public boolean equals(Object other) {
		return super.equals(other) && _axis.equals((Tube) other._axis));
	}

	@Override
	public String toString() {
		return "Tube:\n\t" +
				"Radius: " + super.toString() + "\n\t" +
				"Axis: " + _axis;
				/* since axis is a ray, and Ray is itself made up of point and vector,
				 * it will be a problem, because we will have just 1 tab for the point and vector
				 * when it needs to be 2 tabs (cause it's Point/Vector in Ray in Tube) */
	}

	@Override
	public int hashCode() {
		Objects.hash(super.hashCode(), _axis);
	}
}