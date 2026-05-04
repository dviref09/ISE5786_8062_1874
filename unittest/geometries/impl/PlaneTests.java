package geometries.impl;

import java.util.List;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit test class for {@link Plane}.
 * @author Amichai Feigelson
 */

class PlaneTests {
    /**
     * Delta value for accuracy when comparing double values.
     */
    private final static double DELTA = 1e-6;

    /**
     * Test data
     */
    private final static Point point1 = new Point(0, 0, 1);
    private final static Point point2 = new Point(0, 9, 2);
    private final static Point point3 = new Point(0, 10, 8);
    private final static Point point4 = new Point(0, 2, 1);
    private final static Point point5 = new Point(0, 4, 1);
    private final static Plane plane1 = new Plane(point1, new Vector(9, 0, 0));
    private final static Plane plane2 = new Plane(point1, point2, point3);
    private final static Vector orthogonalVector1 = point1.subtract(point2);
    private final static Vector orthogonalVector2 = point1.subtract(point3);
    // this is for findIntersection tests
    private final static Plane testPlane = new Plane(new Point(0, 0, 1), new Vector(0, 0, 1));

    /**
     * Failure messages for the tests
     */
    private final static String LENGTH_FAILURE_MESSAGE = "The normal should be of length 1.";
    private final static String DIRECTION_FAILURE_MESSAGE = "The dot product of the normal and a vector made from 2 points in the plane should be 0.";
    private final static String CONSTRUCTOR_FAILURE_MESSAGE1 = "Constructor should throw an exception when trying to create plane with 2 or more same points.";
    private final static String CONSTRUCTOR_FAILURE_MESSAGE2 = "Constructor should throw an exception when trying to create plane with 3 points are collinear to each other.";
    private final static String INTERSECTION_FAILURE_MESSAGE = "Wrong intersection point with plane";
    private final static String NO_INTERSECTION_FAILURE_MESSAGE = "Ray should not have intersections with plane";
    private final static String PARALLEL_RAY_FAILURE_MESSAGE = "Parallel ray should not have intersections";
    private final static String START_IN_PLANE_FAILURE_MESSAGE = "Ray starting in the plane should not have intersections";

    /**
     * Test method for {@link Plane#getNormal(Point)}
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Getting a normal for point on the plane
        Vector result = plane2.getNormal(point4);
        assertEquals(1, result.length(), DELTA, LENGTH_FAILURE_MESSAGE);
        assertEquals(0, result.dotProduct(orthogonalVector1), DELTA, DIRECTION_FAILURE_MESSAGE);
        assertEquals(0, result.dotProduct(orthogonalVector2), DELTA, DIRECTION_FAILURE_MESSAGE);

        // =============== Boundary Values Tests ==================
        // BV01: Getting a normal for the point used in the plane constructor
        result = plane2.getNormal(point1);
        assertEquals(1, result.length(), DELTA, LENGTH_FAILURE_MESSAGE);
        assertEquals(0, result.dotProduct(orthogonalVector1), DELTA, DIRECTION_FAILURE_MESSAGE);
        assertEquals(0, result.dotProduct(orthogonalVector2), DELTA, DIRECTION_FAILURE_MESSAGE);
    }

    /**
     * Test method for {@link Plane#Plane(Point, Vector)}
     */
    @Test
    void testConstructorNormal() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Getting a normal for point on the plane
        Vector result = plane1.getNormal(point4);
        assertEquals(1, result.length(), DELTA, LENGTH_FAILURE_MESSAGE);
    }

    /**
     * Test method for {@link Plane#Plane(Point, Point, Point)}
     */
    @Test
    void testConstructorPoints() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Creating a plane with 3 distinct points
        Vector result = plane2.getNormal(point4);
        assertEquals(1, result.length(), DELTA, LENGTH_FAILURE_MESSAGE);
        assertEquals(0, result.dotProduct(orthogonalVector1), DELTA, DIRECTION_FAILURE_MESSAGE);
        assertEquals(0, result.dotProduct(orthogonalVector2), DELTA, DIRECTION_FAILURE_MESSAGE);

        // =============== Boundary Values Tests ==================
        // Group: Two or more points are the same
        // BV01: The first two points are the same
        assertThrows(IllegalArgumentException.class, () -> new Plane(point1, point1, point2), CONSTRUCTOR_FAILURE_MESSAGE1);
        // BV02: The first and third are the same
        assertThrows(IllegalArgumentException.class, () -> new Plane(point1, point2, point1), CONSTRUCTOR_FAILURE_MESSAGE1);
        // BV03: The last two are the same
        assertThrows(IllegalArgumentException.class, () -> new Plane(point2, point1, point1), CONSTRUCTOR_FAILURE_MESSAGE1);
        // BV04: All three points are the same
        assertThrows(IllegalArgumentException.class, () -> new Plane(point1, point1, point1), CONSTRUCTOR_FAILURE_MESSAGE1);

        // BV05: All three points are collinear
        assertThrows(IllegalArgumentException.class, () -> new Plane(point1, point4, point5), CONSTRUCTOR_FAILURE_MESSAGE2);
    }

    /**
     * Test method for {@link Plane#findIntersections(Ray)}.
     */
    @Test
    void testFindIntersections() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Ray intersects the plane (1 point)
        Ray testRay = new Ray(new Point(0, 0, 2), new Vector(1, 1, -1));
        List<Point> expectedIntersectionList = List.of(new Point(1, 1, 1));
        assertEquals(expectedIntersectionList, testPlane.findIntersections(testRay), INTERSECTION_FAILURE_MESSAGE);

        // EP02: Ray does not intersect the plane (0 points)
        testRay = new Ray(new Point(0, 0, 2), new Vector(1, 1, 1));
        assertNull(testPlane.findIntersections(testRay), NO_INTERSECTION_FAILURE_MESSAGE);

        // =============== Boundary Values Tests ==================
        // **** Group: Ray is parallel to the plane
        // BV01: Ray included in the plane (0 points)
        testRay = new Ray(new Point(1, 1, 1), new Vector(1, 0, 0));
        assertNull(testPlane.findIntersections(testRay), PARALLEL_RAY_FAILURE_MESSAGE);

        // BV02: Ray not included in the plane (0 points)
        testRay = new Ray(new Point(1, 1, 2), new Vector(1, 0, 0));
        assertNull(testPlane.findIntersections(testRay), PARALLEL_RAY_FAILURE_MESSAGE);

        // **** Group: Ray is orthogonal to the plane
        // BV03: Ray starts before the plane (1 point)
        testRay = new Ray(new Point(1, 1, 0), new Vector(0, 0, 1));
        expectedIntersectionList = List.of(new Point(1, 1, 1));
        assertEquals(expectedIntersectionList, testPlane.findIntersections(testRay), INTERSECTION_FAILURE_MESSAGE);

        // BV04: Ray starts in the plane (0 points)
        testRay = new Ray(new Point(1, 1, 1), new Vector(0, 0, 1));
        assertNull(testPlane.findIntersections(testRay), START_IN_PLANE_FAILURE_MESSAGE);

        // BV05: Ray starts after the plane (0 points)
        testRay = new Ray(new Point(1, 1, 2), new Vector(0, 0, 1));
        assertNull(testPlane.findIntersections(testRay), NO_INTERSECTION_FAILURE_MESSAGE);

        // **** Group: Special cases
        // BV06: Ray is neither orthogonal nor parallel and begins at the plane (0 points)
        testRay = new Ray(new Point(2, 2, 1), new Vector(1, 1, 1));
        assertNull(testPlane.findIntersections(testRay), START_IN_PLANE_FAILURE_MESSAGE);

        // BV07: Ray begins at the plane's reference point (0 points)
        testRay = new Ray(new Point(0, 0, 1), new Vector(1, 1, 1));
        assertNull(testPlane.findIntersections(testRay), START_IN_PLANE_FAILURE_MESSAGE);
    }
}