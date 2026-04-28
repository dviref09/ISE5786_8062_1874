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
	 * @param ray The ray which were finding it's intersections with the body.
	 * @return The intersection points.
	 */
	public abstract List<Point> findIntersections(Ray ray);
}
