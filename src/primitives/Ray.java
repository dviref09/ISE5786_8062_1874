package primitives;

import java.util.List;
import java.util.Objects;

import static geometries.api.Intersectable.Intersection;
import static primitives.Util.alignZero;

/**
 * A class representing a ray in 3D space, which is a straight line starting at a point.
 * @author Amichai Feigelson
 */
public final class Ray {
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
     * A shift constant
     */
    private static final double DELTA = 0.1;
    
    /**
     * Constructs a new ray with the specified origin and direction.
     * @param origin The origin point of the ray.
     * @param direction The direction vector of the ray. It will be normalized.
     */
    public Ray(Point origin, Vector direction) {
        this._origin = origin;
        this._direction = direction.normalize();
    }
    
    /**
     * Constructs a new ray from the origin to the target
     * @param origin The origin point of the rat
     * @param target The target point of the ray
     */
    public Ray(Point origin, Point target) {
        this._origin = origin;
        this._direction = target.subtract(origin).normalize();
    }
    
    /**
     * Constructs a new ray with the point and direction
     * and before constructing the ray shift the point in the direction of the shift vector
     * @param origin The origin point of the ray.
     * @param direction The direction vector of the ray. It will be normalized.
     * @param normal The vector that the point will be moving in his direction
     */
    public Ray(Point origin, Vector direction, Vector normal) {
        double directionNormal = alignZero(direction.dotProduct(normal));
        if (directionNormal == 0) {
            this._origin = origin;
        } else {
            this._origin = origin.add(normal.scale(directionNormal > 0 ? DELTA : -DELTA));
        }
        
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
    // end of getters
    
    /**
     * Method for calculating the formula p0 + t*v for points on the ray
     * @param t The t parameter in the formula
     * @return The result point of the formula
     */
    public Point getPoint(double t) {
        // try catch block is used instead of simple if statement because an edge case where isZero(t) is false but
        // after scaling the direction by t one of the coordinates of the multiplication too small
        // (based on real case that happened)
        try {
            return _origin.add(_direction.scale(t));
        } catch (IllegalArgumentException e) {
            return _origin;
        }
    }
    
    /**
     * Method for calculating the closest intersection to the start of the ray from a list of intersections on the ray
     * @param intersections The list of intersections on the ray
     * @return the closest intersection to the start of the ray
     */
    public Intersection findClosestIntersection(List<Intersection> intersections) {
        if (intersections == null) {
            return null;
        }
        
        double MinimumDistanceSquared = Double.POSITIVE_INFINITY;
        Intersection closestIntersection = null;
        
        for (Intersection intersection : intersections) {
            double currentDistanceSquared = intersection.point.distanceSquared(_origin);
            if (currentDistanceSquared < MinimumDistanceSquared) {
                MinimumDistanceSquared = currentDistanceSquared;
                closestIntersection = intersection;
            }
        }
        return closestIntersection;
    }
    
    /**
     * Method for calculating the closest point to the start of the ray from a list of points on the ray
     * @param points The list of points on the ray
     * @return the closest point to the start of the ray
     */
    public Point findClosestPoint(List<Point> points) {
        return (points == null ? null
                : findClosestIntersection(
                points.stream()
                      .map(point -> new Intersection(null, point))
                      .toList()
        ).point
        );
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