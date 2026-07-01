package geometries.impl;

import primitives.Point;
import primitives.Ray;

import static primitives.Util.isZero;

/**
 * Axis aligned bounding box in 3D space used to wrap geometric bodies
 * @author Dvir Farkash
 */
public class AABB {
	/**
	 * The point of the minimum values in all the axes
	 */
	private final Point _min;
	/**
	 * The point of the maximum values in all the axes
	 */
	private final Point _max;

	/**
	 * Constructs an AABB with the given minimum and maximum points
	 * @param min The minimum point of the AABB
	 * @param max The maximum point of the AABB
	 */
	public AABB(Point min, Point max) {
		this._min = min;
		this._max = max;
	}

	// getters

	/**
	 * Getter for min point of the AABB
	 * @return The min point of the AABB
	 */
	public Point getMin() {
		return _min;
	}

	/**
	 * Getter for max point of the AABB
	 * @return The max point of the AABB
	 */
	public Point getMax() {
		return _max;
	}

	/**
	 * Find if the ray intersects the AABB or not Uses the slab method
	 * @param ray The ray to check for intersection
	 * @return true if the ray intersects the AABB, false if not
	 */
	public boolean isIntersects(Ray ray) {
		double originX = ray.origin().x();
		double originY = ray.origin().y();
		double originZ = ray.origin().z();
		double directionX = ray.direction().x();
		double directionY = ray.direction().y();
		double directionZ = ray.direction().z();

		double tMin = 0;
		double tMax = Double.POSITIVE_INFINITY;

		if (isZero(directionX)) {
			if (originX <= _min.x() || originX >= _max.x()) {
				return false;
			}
		} else {
			double invDirection = 1.0 / directionX;
			double t1 = (_min.x() - originX) * invDirection;
			double t2 = (_max.x() - originX) * invDirection;
			if (t1 > t2) {
				double temp = t1;
				t1 = t2;
				t2 = temp;
			}
			tMin = Math.max(tMin, t1);
			tMax = Math.min(tMax, t2);
			if (tMax <= tMin) {
				return false;
			}
		}

		if (isZero(directionY)) {
			if (originY <= _min.y() || originY >= _max.y()) {
				return false;
			}
		} else {
			double invDirection = 1.0 / directionY;
			double t1 = (_min.y() - originY) * invDirection;
			double t2 = (_max.y() - originY) * invDirection;
			if (t1 > t2) {
				double temp = t1;
				t1 = t2;
				t2 = temp;
			}
			tMin = Math.max(tMin, t1);
			tMax = Math.min(tMax, t2);
			if (tMax <= tMin) {
				return false;
			}
		}

		if (isZero(directionZ)) {
			if (originZ <= _min.z() || originZ >= _max.z()) {
				return false;
			}
		} else {
			double invDirection = 1.0 / directionZ;
			double t1 = (_min.z() - originZ) * invDirection;
			double t2 = (_max.z() - originZ) * invDirection;
			if (t1 > t2) {
				double temp = t1;
				t1 = t2;
				t2 = temp;
			}
			tMin = Math.max(tMin, t1);
			tMax = Math.min(tMax, t2);
			if (tMax <= tMin) {
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if (other == null || other.getClass() != this.getClass()) {
			return false;
		}
		return _min.equals(((AABB) other)._min) && _max.equals(((AABB) other)._max);
	}
}
