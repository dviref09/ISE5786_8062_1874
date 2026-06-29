package geometries.impl;

import java.util.ArrayList;
import java.util.List;

import geometries.api.Intersectable;
import primitives.Point;
import primitives.Ray;

/**
 * Composite class for all geometric bodies
 * @author Dvir Farkash
 */
public final class Geometries extends Intersectable {
    /**
     * The list of the geometric bodies
     */
    private final List<Intersectable> geometries = new ArrayList<>();
    
    /**
     * Is the geometries has an infinite geometric body
     */
    private boolean hasInfiniteBody = false;
    
    /**
     * Constructor
     * @param geometries List of geomtries to have in this instance
     */
    public Geometries(Intersectable... geometries) {
        this.geometries.addAll(List.of(geometries));
    }
    
    /**
     * Method for adding new geometric bodies to the list
     * @param geometries List of geometries to add to the geometries instance
     */
    public void add(Intersectable... geometries) {
        this.geometries.addAll(List.of(geometries));
    }
    
    /**
     * Method for adding new geometric bodies to the list
     * @param geometries Another geometries object to add to the current one
     */
    public void add(Geometries geometries) {
        this.geometries.addAll(geometries.geometries);
    }
    
    @Override
    public AABB getAABB() {
        if (hasInfiniteBody) return null;
        if (aabb == null) {
            for (Intersectable geometry : geometries) {
                AABB geometryAABB = geometry.getAABB();
                if (geometryAABB == null) {
                    // if one geometry has null AABB then this means it's must be infinite then the whole geometries
                    // is infinite (we ignore from the case of a body with no findIntersection implementation because
                    // he shouldn't be in the scene from the first place)
                    hasInfiniteBody = true;
                    aabb = null;
                    break;
                }
                if (aabb == null) {
                    aabb = geometryAABB;
                } else {
                    aabb = new AABB(
                            new Point(
                                    Math.min(aabb.getMin().x(), geometryAABB.getMin().x()),
                                    Math.min(aabb.getMin().y(), geometryAABB.getMin().y()),
                                    Math.min(aabb.getMin().z(), geometryAABB.getMin().z())
                            ),
                            new Point(
                                    Math.max(aabb.getMax().x(), geometryAABB.getMax().x()),
                                    Math.max(aabb.getMax().y(), geometryAABB.getMax().y()),
                                    Math.max(aabb.getMax().z(), geometryAABB.getMax().z())
                            )
                    );
                }
            }
        }
        return aabb;
    }
    
    @Override
    protected List<Intersection> calcIntersectionsHelper(Ray ray, double maxDistance) {
        List<Intersection> intersections = null;
        for (Intersectable geometry : geometries) {
            List<Intersection> geometryIntersections = geometry.calcIntersections(ray, maxDistance);
            if (geometryIntersections != null) {
                if (intersections == null) {
                    intersections = new ArrayList<>(geometryIntersections);
                } else {
                    intersections.addAll(geometryIntersections);
                }
            }
        }
        return intersections;
    }
}
