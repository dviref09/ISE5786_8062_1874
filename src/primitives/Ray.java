package primitives;

import java.util.Objects;

/**
 * A class representing a ray in 3D space, which is a straight line starting at a point.
 * @author Amichai Feigelson
 */
public class Ray {
	/**
	 * The origin point of the ray.
	 */
	private final Point _origin;
	/**
	 * The direction vector of the ray.<br />
	 * The vector is normalized.
	 */
	private final Vector _direction;

	/**
	 * Constructs a new ray with the specified origin and direction.
	 * @param origin    The origin point of the ray.
	 * @param direction The direction vector of the ray. It will be normalized.
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

	/**
	 * Function for calculating the formula p0 + t*v for points on the ray
	 * @param t The t parameter in the formula
	 * @return The result point of the formula
	 */
	public Point getPoint(double t) {
		try {
			return _origin.add(_direction.scale(t));
		}
		catch (IllegalArgumentException e) {
			return _origin;
		}
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
		return "Ray:" + _origin + _direction;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_origin, _direction);
	}
}