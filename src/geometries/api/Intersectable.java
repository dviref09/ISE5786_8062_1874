package geometries.api;

import java.util.List;

import geometries.impl.AABB;
import lighting.LightSource;
import primitives.Material;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * An abstract class representing a body that can be intersected.
 */
public abstract class Intersectable {
	/**
	 * Static field that controls whether the calcIntersections will use cbr
	 */
	private static boolean cbrEnabled = false;
	/**
	 * The AABB of the body
	 */
	protected AABB aabb = null;

	/**
	 * Calculates the intersections between a ray and intersectable body.
	 * @param ray The ray which were finding its intersections with the body.
	 * @return The intersections as an instance of Intersection class.
	 */
	public final List<Intersection> calcIntersections(Ray ray) {
		return calcIntersections(ray, Double.POSITIVE_INFINITY);
	}

	/**
	 * Calc the intersection between a ray and intersectable body. returns only the intersections in a certain distance from
	 * the start of the ray.
	 * @param ray         The ray which were finding its intersections with the body.
	 * @param maxDistance The maximum distance from the start of the ray to the intersection point.
	 * @return The intersection between the body and the ray in the certain distance.
	 */
	public final List<Intersection> calcIntersections(Ray ray, double maxDistance) {
		if (cbrEnabled) {
			AABB currentAABB = getAABB();
			if (currentAABB != null && !currentAABB.isIntersects(ray)) {
				return null;
			}
		}
		return calcIntersectionsHelper(ray, maxDistance);
	}

	/**
	 * A helper method for calcIntersections that will be the one to be overridden by the subclasses (by the NVI / Template
	 * Method design patterns)
	 * @param ray The ray which were finding its intersections with the body.
	 * @return The intersections.
	 */
	protected abstract List<Intersection> calcIntersectionsHelper(Ray ray, double maxDistance);

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
	 * Returns the AABB of the body
	 * @return The AABB of the body
	 */
	public AABB getAABB() {
		return null;
	}

	/**
	 * Enables the use of cbr in the calcIntersections method
	 */
	public static void enableCBR() {
		cbrEnabled = true;
	}

	/**
	 * A class for representing an intersection, a collection of a point and a geometry
	 */
	public static final class Intersection {
		/**
		 * The geometry of the intersection
		 */
		public final Geometry geometry;
		/**
		 * The point of the intersection
		 */
		public final Point point;
		/**
		 * The material of the geometry of the intersection
		 */
		public final Material material;

		// cache for intersection
		/**
		 * The normal at intersection point
		 */
		public Vector normal;
		/**
		 * The hitting ray from the camera to the intersection
		 */
		public Vector v;
		/**
		 * The dot product between v and the normal
		 */
		public double vNormal;
		/**
		 * The light source currently being checked
		 */
		public LightSource light;
		/**
		 * The ray from the light source to the intersection
		 */
		public Vector l;
		/**
		 * The dot product between l and the normal
		 */
		public double lNormal;
		/**
		 * The reflected vector from the surface of the intersection
		 */
		public Vector r;
		/**
		 * The reflected vector from the surface of the intersection from the lightsource
		 */
		public Vector rLight;

		/**
		 * Constructor
		 * @param geometry The geometry of the intersection
		 * @param point    The point of the intersection
		 */
		public Intersection(Geometry geometry, Point point) {
			this.geometry = geometry;
			this.point = point;
			material = (geometry == null ? new Material() : geometry.getMaterial());
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
