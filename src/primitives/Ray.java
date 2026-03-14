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
	private Point origin;
	/**
	 * The direction vector of the ray.<br />
	 * The vector is normalized.
	 */
	private Vector direction;

	/**
	 * Constructs a new ray with the specified origin and direction.
	 * @param origin
	 * @param direction
	 */
	public Ray(Point origin, Vector direction) {
		this.origin = origin;
		this.direction = direction.normalize();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null || getClass() != other.getClass())
			return false;
		return origin.equals(((Ray) other).origin) && direction.equals(((Ray) other).direction);
	}

	@Override
	public String toString() {
		return "Ray:\n\t" + "Origin: " + origin + "\n\t" + "Direction: " + direction;
	}

	@Override
	public int hashCode() {
		return Objects.hash(origin, direction);
	}
}