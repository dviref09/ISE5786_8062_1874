package geometries.impl;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit test class for {@link Geometries}.
 * @author Dvir Farkash
 */
public class GeometriesTests {
    /**
     * Test data
     */
    Sphere testSphere = new Sphere(new Point(1, 0, 0), 1);
    Triangle testTriangle = new Triangle(new Point(-1, -1, 0), new Point(-1, 1, 0), new Point(-1, 0, 2));
    Plane testPlane = new Plane(new Point(-2, 0, 0), new Vector(1, 0, 0));
    Geometries testGeometries = new Geometries(testSphere, testTriangle, testPlane);
    Point rayOrigin = new Point(-3, 0, 1.5);

    /**
     * Failure messages for the tests
     */
    private final static String FIND_INTERSECTIONS_FAILURE_MESSAGE = "The number of intersection points is incorrect";

    /**
     * Test method for {@link Geometries#findIntersections(Ray)}
     */
    @Test
    void testFindIntersections() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Some of the geometric bodies get intersected
        Ray testRay = new Ray(rayOrigin, new Vector(1, 0, 0));
        assertEquals(2, testGeometries.findIntersections(testRay).size(), FIND_INTERSECTIONS_FAILURE_MESSAGE);

        // =============== Boundary Values Tests ==================
        // BV01: No geometric body get intersected
        testRay = new Ray(rayOrigin, new Vector(-1, 0, 0));
        assertNull(testGeometries.findIntersections(testRay), FIND_INTERSECTIONS_FAILURE_MESSAGE);

        // BV02: One geometric body get intersected
        testRay = new Ray(rayOrigin, new Vector(4, 0, 1));
        assertEquals(1, testGeometries.findIntersections(testRay).size(), FIND_INTERSECTIONS_FAILURE_MESSAGE);

        // BV03: All geometric bodies get intersected
        testRay = new Ray(rayOrigin, new Vector(4, 0, -1));
        assertEquals(4, testGeometries.findIntersections(testRay).size(), FIND_INTERSECTIONS_FAILURE_MESSAGE);
    }
}
