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
     * Constructor
     */
    public Geometries(Intersectable... geometries) {
        this.geometries.addAll(List.of(geometries));
    }

    /**
     * Method for adding new geometric bodies to the list
     */
    public void add(Intersectable... geometries) {
        this.geometries.addAll(List.of(geometries));
    }

    @Override
    public List<Intersection> calcIntersectionsHelper(Ray ray) {
        List<Intersection> intersections = null;
        for (Intersectable geometry : geometries) {
            List<Intersection> geometryIntersections = geometry.calcIntersections(ray);
            if (geometryIntersections != null) {
                if (intersections == null) {
                    intersections = new ArrayList<>(geometryIntersections);
                }
                else {
                    intersections.addAll(geometryIntersections);
                }
            }
        }
        return intersections;
    }

}
