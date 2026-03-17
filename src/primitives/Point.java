package primitives;

/**
 * A class for representing a point in 3D space.
 * @author Amichai Feigelson
 */
public class Point {
	/**
	 * The coordinates of the point.
	 */
	protected final Double3 _xyz;

	/**
	 * A constant representing the axis origin.
	 */
	public static final Point ZERO = new Point(Double3.ZERO);

	/**
	 * Constructs a new point from the coordinates given in the parameters.
	 * @param x The x coordinate of the point.
	 * @param y The y coordinate of the point.
	 * @param z the z coordinate of the point.
	 */
	public Point(double x, double y, double z) {
		_xyz = new Double3(x, y, z);
	}

	/**
	 * Constructs a new point from the coordinates given in the parameters.
	 * @param coordinates The coordinates of the point.
	 */
	public Point(Double3 coordinates) {
		_xyz = coordinates;
	}

	/**
	 * Subtracts another point from our point.
	 * @param other The point to subtract from our point.
	 * @return The result vector.
	 * @throws IllegalArgumentException When the two points are equal.
	 */
	public Vector subtract(Point other) {
		return new Vector(this._xyz.subtract(other._xyz));
	}

	/*
	 * The following segment of code has been generated through github copilot with the following prompt: "can you write
	 * this? make sure to keep DRY principle, the same style of code, use Double3 when possible, and don't create
	 * temporary variables. And create javadoc comments, simmilarly to what is already in the code"
	 */

	/**
	 * Adds a vector to this point, returning a new point.
	 * @param vector The vector to add.
	 * @return A new point after adding the vector to this point.
	 */
	public Point add(Vector vector) {
		return new Point(_xyz.add(vector._xyz));
	}

	/**
	 * Calculates the squared distance between this point and another point.
	 * @param other The other point.
	 * @return The squared distance between the two points.
	 */
	public double distanceSquared(Point other) {
		double distanceX = this._xyz._d1() - other._xyz._d1();
		double distanceY = this._xyz._d2() - other._xyz._d2();
		double distanceZ = this._xyz._d3() - other._xyz._d3();
		return distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ;
	}

	/**
	 * Calculates the distance between this point and another point.
	 * @param other The other point.
	 * @return The distance between the two points.
	 */
	public double distance(Point other) {
		return Math.sqrt(distanceSquared(other));
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null || getClass() != other.getClass())
			return false;
		return _xyz.equals(((Point) other)._xyz);
	}

	@Override
	public String toString() {
		return "Point: " + _xyz;
	}

	@Override
	public int hashCode() {
		return _xyz.hashCode();
	}
}