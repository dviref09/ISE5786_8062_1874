package primitives;

/**
 * A class for representing a vector in 3D space.
 * @author Dvir Farkash
 */
public class Vector extends Point {
	/**
	 * The unit vector for x axis
	 */
	public static final Vector AXIS_X = new Vector(1, 0, 0);
	/**
	 * The unit vector for y axis
	 */
	public static final Vector AXIS_Y = new Vector(0, 1, 0);
	/**
	 * The unit vector for z axis
	 */
	public static final Vector AXIS_Z = new Vector(0, 0, 1);

	/**
	 * Constructs a new vector with the specified coordinates.
	 * @param x the x coordinate of the vector
	 * @param y the y coordinate of the vector
	 * @param z the z coordinate of the vector
	 */
	public Vector(double x, double y, double z) {
		super(x, y, z);
		if (_coordinates.equals(Double3.ZERO)) {
			throw new IllegalArgumentException("Zero vector is not allowed");
		}
	}

	/**
	 * Constructs a new vector with the specified coordinates.
	 * @param coordinates The coordinates of the vector
	 */
	public Vector(Double3 coordinates) {
		super(coordinates);
		if (_coordinates.equals(Double3.ZERO)) {
			throw new IllegalArgumentException("Zero vector is not allowed");
		}
	}
	/*
	 * The following segment of code has been generated through github copilot with the following prompt: "תכין לי את
	 * רשימת המתודות שכתובה בתמונה, אם יש מתודה שאתה לא בטוח מה היא אמורה לעשות תשאל אותי. תייצר גם הערת javadoc, תקפיד
	 * על כך שסגנון ההערכות והקוד יהיה דומה לשאר הקוד וההערות."
	 */

	/**
	 * Adds another vector to this vector.
	 * @param other The vector to add.
	 * @return A new vector that is the sum of the two vectors.
	 * @throws IllegalArgumentException When the result is the zero vector.
	 */
	public Vector add(Vector other) {
		return new Vector(_coordinates.add(other._coordinates));
	}

	/**
	 * Scales this vector by a scalar value.
	 * @param factor The factor of multiplication.
	 * @return A new vector that is the result of the scaling.
	 * @throws IllegalArgumentException When the factor is zero.
	 */
	public Vector scale(double factor) {
		return new Vector(_coordinates.scale(factor));
	}

	/**
	 * Calculates the dot product of this vector with another vector.
	 * @param other The other vector.
	 * @return The dot product of the two vectors.
	 */
	public double dotProduct(Vector other) {
		return _coordinates._d1() * other._coordinates._d1() + _coordinates._d2() * other._coordinates._d2()
				+ _coordinates._d3() * other._coordinates._d3();
	}

	/**
	 * Calculates the cross product of this vector with another vector.
	 * @param other The other vector.
	 * @return A new vector that is the cross product of the two vectors.
	 * @throws IllegalArgumentException When the two vectors are parallel.
	 */
	public Vector crossProduct(Vector other) {
		double otherX = other._coordinates._d1(), otherY = other._coordinates._d2(), otherZ = other._coordinates._d3();
		double thisX = _coordinates._d1(), thisY = _coordinates._d2(), thisZ = _coordinates._d3();
		double resultX = thisY * otherZ - thisZ * otherY;
		double resultY = thisZ * otherX - thisX * otherZ;
		double resultZ = thisX * otherY - thisY * otherX;
		return new Vector(resultX, resultY, resultZ);
	}

	/**
	 * Calculates the squared length of this vector.
	 * @return The squared length of the vector.
	 */
	public double lengthSquared() {
		return dotProduct(this);
	}

	/**
	 * Calculates the length of this vector.
	 * @return The length of the vector.
	 */
	public double length() {
		return Math.sqrt(lengthSquared());
	}

	/**
	 * Returns a new normalized (unit) vector in the same direction as this vector.
	 * @return A new unit vector in the direction of this vector.
	 */
	public Vector normalize() {
		return scale(1 / length());
	}

	/*
	 * We didn't implement hashCode, toString and equals methods, because their implementation is the same as the super
	 * class, so we didn't want to needlessly repeat ourselves
	 */
}