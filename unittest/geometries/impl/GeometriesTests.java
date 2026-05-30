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
    private final static Sphere testSphere = new Sphere(new Point(1, 0, 0), 1);
    private final static Triangle testTriangle =
            new Triangle(new Point(-1, -1, 0), new Point(-1, 1, 0), new Point(-1, 0, 2));
    private final static Plane testPlane = new Plane(new Point(-2, 0, 0), new Vector(1, 0, 0));
    private final static Geometries testGeometries = new Geometries(testSphere, testTriangle, testPlane);
    private final static Geometries emptyGeometries = new Geometries();
    private final static Point rayOrigin = new Point(-3, 0, 1.5);
    
    /**
     * Failure messages for the tests
     */
    private static final String FIND_INTERSECTIONS_FAILURE_MESSAGE = "The number of intersection points is incorrect";
    
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
        
        // BV04: There is no bodies at all
        testRay = new Ray(rayOrigin, new Vector(1, 1, 1));
        assertNull(emptyGeometries.findIntersections(testRay), FIND_INTERSECTIONS_FAILURE_MESSAGE);
    }
    
    /**
     * Test method for {@link Geometries#calcIntersections(primitives.Ray, double)}
     */
    @Test
    void testCalcIntersectionWithMaxDistance() {
        // **All the test cases based on the case where the ray intersection all the geometric bodies
        Point rayOrigin = new Point(-3, 0, 0.5);
        Vector rayDirection = new Vector(1, 0, 0);
        Ray testRay = new Ray(rayOrigin, rayDirection);
        // ============ Equivalence Partitions Tests ==============
        // EP01: Max distance is longer than the most distant object
        assertEquals(4, testGeometries.calcIntersections(testRay, 10).size(),
                FIND_INTERSECTIONS_FAILURE_MESSAGE);
        
        // EP02: Max distant have some of the geometries
        assertEquals(3, testGeometries.calcIntersections(testRay, 4).size(),
                FIND_INTERSECTIONS_FAILURE_MESSAGE);
        // EP03: Max distant is less than the distant to the closest object
        assertNull(testGeometries.calcIntersections(testRay, 0.5),
                FIND_INTERSECTIONS_FAILURE_MESSAGE);
        
        // =============== Boundary Values Tests ==================
        // BV01: Max distant is exactly on one geometry
        assertEquals(2, testGeometries.calcIntersections(testRay, 2).size(),
                FIND_INTERSECTIONS_FAILURE_MESSAGE);
    }
}
