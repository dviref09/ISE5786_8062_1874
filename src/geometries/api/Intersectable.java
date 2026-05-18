package geometries.api;

import java.util.List;
import primitives.Point;
import primitives.Ray;

/**
 * An abstract class representing a body that can be intersected.
 */
public abstract class Intersectable {
	/**
	 * Finds the intersections between a ray and the intersectable body.
	 * @param ray The ray which were finding its intersections with the body.
	 * @return The intersection points.
	 */
	public abstract List<Point> findIntersections(Ray ray);

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
		Intersection (Geometry geometry, Point point) {
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
