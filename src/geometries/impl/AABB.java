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
     * Find if the ray intersects the AABB or not
     * Uses the slab method
     * @param ray The ray to check for intersection
     * @return true if the ray intersects the AABB, false if not
     */
    public boolean isIntersects(Ray ray) {
        double[] originValues = {ray.origin().x(), ray.origin().y(), ray.origin().z()};
        double[] directionValues = {ray.direction().x(), ray.direction().y(), ray.direction().z()};
        double[] minValues = {_min.x(), _min.y(), _min.z()};
        double[] maxValues = {_max.x(), _max.y(), _max.z()};
        
        double tMin = 0;
        double tMax = Double.POSITIVE_INFINITY;
        
        for (int i = 0; i < 3; i++) {
            if (isZero(directionValues[i]) && (originValues[i] <= minValues[i] || originValues[i] >= maxValues[i])) {
                return false;
            }
            
            double tMinCurrent = (minValues[i] - originValues[i]) / directionValues[i];
            double tMaxCurrent = (maxValues[i] - originValues[i]) / directionValues[i];
            
            if (tMinCurrent > tMaxCurrent) {
                double temp = tMinCurrent;
                tMinCurrent = tMaxCurrent;
                tMaxCurrent = temp;
            }
            
            tMin = Math.max(tMin, tMinCurrent);
            tMax = Math.min(tMax, tMaxCurrent);
            
            if (tMin >= tMax) {
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
