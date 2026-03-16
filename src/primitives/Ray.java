package primitives;

import java.util.Objects;

/**
 * A class representing a ray in 3D space, which is a straight line starting at
 * a point.
 * @author Amichai Feigelson
 */
public class Ray {
	/**
	 * The origin point of the ray.
	 */
	private Point _origin;
	/**
	 * The direction vector of the ray.<br />
	 * The vector is normalized.
	 */
	private Vector _direction;

	/**
	 * Constructs a new ray with the specified origin and direction.
	 * @param origin
	 * @param direction
	 */
	public Ray(Point origin, Vector direction) {
		this._origin = origin;
		this._direction = direction.normalize();
	}

	// getters

	/**
	 * Getter for the origin point.
	 * @return The origin point of the ray.
	 */
	public Point origin() {
		return _origin;
	}

	/**
	 * Getter for the direction vector.
	 * @return The direction vector of the ray.
	 */
	public Vector direction() {
		return _direction;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null || getClass() != other.getClass())
			return false;
		return _origin.equals(((Ray) other)._origin) && _direction.equals(((Ray) other)._direction);
	}

	@Override
	public String toString() {
		return "Ray:\n\t" + "Origin: " + _origin + "\n\t" + "Direction: " + _direction;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_origin, _direction);
	}
}