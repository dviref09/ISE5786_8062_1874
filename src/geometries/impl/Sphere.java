package geometries.impl;

import java.util.List;
import java.util.Objects;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static primitives.Util.alignZero;

/**
 * Class representing a shphere in 3D space.
 * @author Amichai Feigelson
 */
public final class Sphere extends RadialGeometry {
    /**
     * The center point of the sphere.
     */
    private final Point _center;
    
    /**
     * Constructor a sphere from center point and radius.
     * @param center The sphere's center point.
     * @param radius The sphere's radius.
     */
    public Sphere(Point center, double radius) {
        super(radius);
        _center = center;
    }
    
    @Override
    public Vector getNormal(Point point) {
        return point.subtract(_center).normalize();
    }
    
    @Override
    protected List<Intersection> calcIntersectionsHelper(Ray ray, double maxDistance) {
        if (_center.equals(ray.origin())) {
            return alignZero(_radius - maxDistance) <= 0
                    ? List.of(new Intersection(this, ray.getPoint(_radius)))
                    : null;
        }
        
        Vector pointToCenter = _center.subtract(ray.origin());
        
        double tm = alignZero(pointToCenter.dotProduct(ray.direction()));
        double centerToRayDistance = Math.sqrt(pointToCenter.lengthSquared() - tm * tm);
        
        if (alignZero(centerToRayDistance - _radius) >= 0) {
            return null;
        }
        
        double th = Math.sqrt(_radiusSquared - centerToRayDistance * centerToRayDistance);
        double t1 = alignZero(tm - th);
        double t2 = alignZero(tm + th);
        
        boolean t1Valid = t1 > 0 && t1 <= maxDistance;
        boolean t2Valid = t2 > 0 && t2 <= maxDistance;
        
        if (t1Valid && t2Valid) {
            return List.of(new Intersection(this, ray.getPoint(t1)), new Intersection(this, ray.getPoint(t2)));
        } else if (t1Valid) {
            return List.of(new Intersection(this, ray.getPoint(t1)));
        } else if (t2Valid) {
            return List.of(new Intersection(this, ray.getPoint(t2)));
        }
        
        return null;
    }
    
    @Override
    public boolean equals(Object other) {
        return super.equals(other) && _center.equals(((Sphere) other)._center);
    }
    
    @Override
    public String toString() {
        return "Sphere: Center: " + _center + " " + super.toString();
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), _center);
    }
}