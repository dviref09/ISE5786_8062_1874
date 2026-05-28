package geometries.impl;

import java.util.List;
import java.util.Objects;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Class representing an tube (infinite cylinder) in 3D space.
 * @author Dvir Farkash
 */
public class Tube extends RadialGeometry {
    /**
     * The axis that the tube wraps around.
     */
    protected final Ray _axis;
    
    /**
     * Construct a tube with the given axis and radius.
     * @param axis The axis that the tube wraps around.
     * @param radius The tube's radius.
     */
    public Tube(double radius, Ray axis) {
        super(radius);
        _axis = axis;
    }
    
    @Override
    public Vector getNormal(Point point) {
        Vector originToPoint = point.subtract(_axis.origin());
        double t = originToPoint.dotProduct(_axis.direction());
        
        try {
            Point tubeCenterPoint = _axis.origin().add(_axis.direction().scale(t));
            return point.subtract(tubeCenterPoint).normalize();
        } catch (IllegalArgumentException e) {
            return originToPoint.normalize();
        }
    }
    
    @Override
    protected List<Intersection> calcIntersectionsHelper(Ray ray, double maxDistance) {
        return null;
    }
    
    @Override
    public boolean equals(Object other) {
        return super.equals(other) && _axis.equals(((Tube) other)._axis);
    }
    
    @Override
    public String toString() {
        return "Tube: " + " Axis: " + _axis + " " + super.toString();
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), _axis);
    }
}