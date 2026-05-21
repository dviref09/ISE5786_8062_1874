package geometries.impl;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static geometries.api.Intersectable.Intersection;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for {@link Plane}.
 * @author Amichai Feigelson
 */
class PlaneTests {
    /**
     * Delta value for accuracy when comparing double values.
     */
    private static final double DELTA = 1e-6;

    /**
     * Test data
     */
    // this is for constructor and getNormal tests
    private static final Point point1 = new Point(0, 0, 1);
    private static final Point point2 = new Point(0, 9, 2);
    private static final Point point3 = new Point(0, 10, 8);
    private static final Point point4 = new Point(0, 2, 1);
    private static final Point point5 = new Point(0, 4, 1);
    private static final Plane plane1 = new Plane(point1, new Vector(9, 0, 0));
    private static final Plane plane2 = new Plane(point1, point2, point3);
    private static final Vector orthogonalVector1 = point1.subtract(point2);
    private static final Vector orthogonalVector2 = point1.subtract(point3);

    // this is for findIntersection tests
    private static final Plane testPlane = new Plane(new Point(0, 0, 1), new Vector(0, 0, 1));

    /**
     * Failure messages for the tests
     */
    private static final String LENGTH_FAILURE_MESSAGE = "The normal should be of length 1.";
    private static final String DIRECTION_FAILURE_MESSAGE = "The dot product of the normal and a vector made from 2 points in the plane should be 0.";
    private static final String CONSTRUCTOR_FAILURE_MESSAGE1 = "Constructor should throw an exception when trying to create plane with 2 or more same points.";
    private static final String CONSTRUCTOR_FAILURE_MESSAGE2 = "Constructor should throw an exception when trying to create plane with 3 points are collinear to each other.";
    private static final String INTERSECTION_FAILURE_MESSAGE = "Wrong intersection point with plane";
    private static final String NO_INTERSECTION_FAILURE_MESSAGE = "Ray should not have intersections with plane";
    private static final String PARALLEL_RAY_FAILURE_MESSAGE = "Parallel ray should not have intersections";
    private static final String START_IN_PLANE_FAILURE_MESSAGE = "Ray starting in the plane should not have intersections";
    private static final String CALC_INTERSECTIONS_FAILURE = "The geometry in the intersection wasn't the body that was intersected";

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
        Point intersectionPoint = new Point(1, 1, 1);
        List<Point> expectedIntersectionList = List.of(intersectionPoint);
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
        intersectionPoint = new Point(1, 1, 1);
        expectedIntersectionList = List.of(intersectionPoint);
        assertEquals(expectedIntersectionList, testPlane.findIntersections(testRay), INTERSECTION_FAILURE_MESSAGE);

        // BV04: Ray starts in the plane (0 points)
        testRay = new Ray(new Point(1, 1, 1), new Vector(0, 0, 1));
        assertNull(testPlane.findIntersections(testRay), START_IN_PLANE_FAILURE_MESSAGE);

        // BV05: Ray starts after the plane (0 points)
        testRay = new Ray(new Point(1, 1, 2), new Vector(0, 0, 1));
        assertNull(testPlane.findIntersections(testRay), NO_INTERSECTION_FAILURE_MESSAGE);

        // **** Group: Ray is neither orthogonal nor parallel and starts at the plane
        // BV06: Ray starts at a general point on the plane (0 points)
        testRay = new Ray(new Point(2, 2, 1), new Vector(1, 1, 1));
        assertNull(testPlane.findIntersections(testRay), START_IN_PLANE_FAILURE_MESSAGE);

        // BV07: Ray starts at the plane's reference point (0 points)
        testRay = new Ray(new Point(0, 0, 1), new Vector(1, 1, 1));
        assertNull(testPlane.findIntersections(testRay), START_IN_PLANE_FAILURE_MESSAGE);
    }

    /**
     * Test method for {@link Plane#calcIntersections(Ray)}
     */
    @Test
    void testCalcIntersections() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Basic Test
        Ray testRay = new Ray(new Point(0, 0, 2), new Vector(1, 1, -1));
        assertSame(testPlane, testPlane.calcIntersections(testRay).get(0).geometry, CALC_INTERSECTIONS_FAILURE);
    }
}