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
public class Geometries extends Intersectable {
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
    public List<Point> findIntersections(Ray ray) {
        List<Point> intersectionsList = null;
        for (Intersectable geometry : geometries) {
            List<Point> list = geometry.findIntersections(ray);
            if (list != null) {
                if (intersectionsList == null) {
                    intersectionsList = new ArrayList<>(list);
                }
                else {
                    intersectionsList.addAll(list);
                }
            }
        }
        return intersectionsList;
    }

}
