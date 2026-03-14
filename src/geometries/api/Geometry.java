package geometries.api;

/**
 * An abstract class for representing a geometric body in 3D space.
 * @author Amichai Feigelson
 */
public abstract class Geometry {
    /**
     * Calculates the normal vector at a given point on the surface of the geometric body.
     * @param point The point on the surface of the geometric body where the normal vector is to be calculated.
     * @return The normal vector at the point.
     */
    public Vector getNormal(Point point);
}