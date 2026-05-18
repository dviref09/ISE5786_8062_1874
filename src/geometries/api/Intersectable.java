package geometries.api;

import java.util.List;
import primitives.Point;
import primitives.Ray;

/**
 * An abstract class representing a body that can be intersected.
 */
public abstract class Intersectable {
	/**
	 * Calculates the intersections between a ray and intersectable body.
	 * @param ray The ray which were finding its intersections with the body.
	 * @return The intersections as an instance of Intersection class.
	 */
	public final List<Intersection> calcIntersections(Ray ray) {
		return calcIntersectionsHelper(ray);
	}

	/**
	 * A helper method for calcIntersections that will be the one to be overridden by the subclasses (by the NVI / Template Method design patterns)\
	 * @param ray The ray which were finding its intersections with the body.
	 * @return The intersections.
	 */
	protected abstract List<Intersection> calcIntersectionsHelper(Ray ray);

	/**
	 * Finds the intersections between a ray and the intersectable body.
	 * @param ray The ray which were finding its intersections with the body.
	 * @return The intersection points.
	 */
	public final List<Point> findIntersections(Ray ray) {
		List<Intersection> intersections = calcIntersections(ray);
		return (intersections == null ? null
				: intersections.stream().map(intersection -> intersection.point).toList());
	}

	/**
	 * A class for representing an intersection, a collection of a point and a geometry
	 */
	public final static class Intersection {
		/**
		 * The geometry of the intersection
		 */
		public final Geometry geometry;
		/**
		 * The point of the intersection
		 */
		public final Point point;

		/**
		 * Constructor
		 * @param geometry The geometry of the intersection
		 * @param point The point of the intersection
		 */
		public Intersection (Geometry geometry, Point point) {
			this.geometry = geometry;
			this.point = point;
		}

		@Override
		public boolean equals(Object other) {
			if (this == other)
				return true;
			if (other == null || getClass() != other.getClass())
				return false;
			return geometry == ((Intersection) other).geometry && point.equals(((Intersection) other).point);
		}

		@Override
		public String toString() {
			return "Geometry: " + geometry
					+ "Point: " + point;
		}
	}
}
