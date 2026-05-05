package geometries.impl;

import java.util.List;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit test class for {@link geometries.impl.Triangle}.
 * @author Dvir Farkash
 */
class TriangleTests {
    /**
     * Delta value for accuracy when comparing double values.
     */
    private final static double DELTA = 1e-6;

    /**
     * Test data.
     */
    private final static Point point1 = new Point(0, 0, 0);
    private final static Point point2 = new Point(1, 0, 0);
    private final static Point point3 = new Point(0, 1, 0);
    private final static Point testPoint = new Point(0.25, 0.25, 0);
    private final static Triangle testTriangleNormal = new Triangle(point1, point2, point3);

    private final static Vector orthogonalVector1 = point1.subtract(point2);
    private final static Vector orthogonalVector2 = point1.subtract(point3);

    private final static Point point4 = new Point(0, 0, 1);
    private final static Point point5 = new Point(2, 0, 1);
    private final static Point point6 = new Point(0, 2, 1);
    private final static Triangle testTriangleIntersection = new Triangle(point4, point5, point6);

    /**
     * Failure messages for the tests
     */
    private final static String LENGTH_FAILURE_MESSAGE = "The normal should be of length 1.";
    private final static String DIRECTION_FAILURE_MESSAGE = "The dot product of the normal and a vector made from 2 points in the triangle should be 0.";
    private final static String INTERSECTION_FAILURE_MESSAGE = "Wrong intersection point inside triangle.";
    private final static String INTERSECTION_OUTSIDE_FAILURE_MESSAGE = "Ray outside the triangle should return null.";
    private final static String INTERSECTION_ON_EDGE_FAILURE_MESSAGE = "Ray on triangle edge should return null.";
    private final static String INTERSECTION_PLANE_FAILURE_MESSAGE = "The ray doesnt should not intersect the plane at all";

    /**
     * Test method for {@link geometries.impl.Triangle#getNormal(Point)}
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Getting a normal for point on the triangle
        Vector result = testTriangleNormal.getNormal(testPoint);

        assertEquals(1, result.length(), DELTA, LENGTH_FAILURE_MESSAGE);
        assertEquals(0, result.dotProduct(orthogonalVector1), DELTA, DIRECTION_FAILURE_MESSAGE);
        assertEquals(0, result.dotProduct(orthogonalVector2), DELTA, DIRECTION_FAILURE_MESSAGE);
    }

    /**
     * Test method for {@link geometries.impl.Triangle#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Inside triangle (1 point)
        Ray testRay = new Ray(new Point(-0.5, -0.5, 0), new Vector(1, 1, 1));
        Point intersectionPoint = new Point(0.5, 0.5, 1);
        List<Point> expectedIntersectionList = List.of(intersectionPoint);
        assertEquals(expectedIntersectionList, testTriangleIntersection.findIntersections(testRay), INTERSECTION_FAILURE_MESSAGE);

        // EP02: Outside against an edge (0 points)
        testRay = new Ray(new Point(0.5, 0.5, 0), new Vector(1, 1, 1));
        assertNull(testTriangleIntersection.findIntersections(testRay), INTERSECTION_OUTSIDE_FAILURE_MESSAGE);

        // EP03: Outside against a vertex (0 points)
        testRay = new Ray(new Point(-1.5, -1.5, 0), new Vector(1, 1, 1));
        assertNull(testTriangleIntersection.findIntersections(testRay), INTERSECTION_OUTSIDE_FAILURE_MESSAGE);

        // EP04: Ray does not intersect the plane (0 points)
        testRay = new Ray(new Point(-0.5, -0.5, 0), new Vector(-1, -1, -1));
        assertNull(testTriangleIntersection.findIntersections(testRay), INTERSECTION_PLANE_FAILURE_MESSAGE);

        // =============== Boundary Values Tests ==================
        // **** Group: Triangle-specific boundary cases
        // BV01: Point on edge (0 points)
        testRay = new Ray(new Point(1, -1, 0), new Vector(0, 1, 1));
        assertNull(testTriangleIntersection.findIntersections(testRay), INTERSECTION_ON_EDGE_FAILURE_MESSAGE);

        // BV02: Point in vertex (0 points)
        testRay = new Ray(new Point(1, 1, 0), new Vector(1, -1, 1));
        assertNull(testTriangleIntersection.findIntersections(testRay), INTERSECTION_ON_EDGE_FAILURE_MESSAGE);

        // BV03: Point on edge continuation (0 points)
        testRay = new Ray(new Point(3, -1, 0), new Vector(0, 1, 1));
        assertNull(testTriangleIntersection.findIntersections(testRay), INTERSECTION_OUTSIDE_FAILURE_MESSAGE);

        // **** Group: Plane-based boundary cases
        // **** Sub-Group: Ray is parallel to the plane
        // BV04: Ray is parallel to the plane, ray is not included in the plane (0 points)
        testRay = new Ray(new Point(0.5, 0.5, 0), new Vector(1, 1, 0));
        assertNull(testTriangleIntersection.findIntersections(testRay), INTERSECTION_PLANE_FAILURE_MESSAGE);

        // BV05: Ray is parallel to the plane, ray is included in the plane (0 points)
        testRay = new Ray(new Point(0.5, 0.5, 1), new Vector(1, 1, 0));
        assertNull(testTriangleIntersection.findIntersections(testRay), INTERSECTION_PLANE_FAILURE_MESSAGE);

        // **** Sub-Group: Ray is orthogonal to the plane
        // BV06: Ray is orthogonal to the plane, starts after the plane (0 points)
        testRay = new Ray(new Point(0.5, 0.5, 2), new Vector(0, 0, 1));
        assertNull(testTriangleIntersection.findIntersections(testRay), INTERSECTION_PLANE_FAILURE_MESSAGE);

        // BV07: Ray is orthogonal to the plane, starts in the plane (0 points)
        testRay = new Ray(new Point(0.5, 0.5, 1), new Vector(0, 0, 1));
        assertNull(testTriangleIntersection.findIntersections(testRay), INTERSECTION_PLANE_FAILURE_MESSAGE);

        // **** Sub-Group: Ray is neither orthogonal nor parallel and starts at the plane
        // BV08: Ray starts at a general point on the plane (0 points)
        testRay = new Ray(new Point(0.5, 0.5, 1), new Vector(1, 1, 1));
        assertNull(testTriangleIntersection.findIntersections(testRay), INTERSECTION_PLANE_FAILURE_MESSAGE);

        // BV09: Ray starts at the plane's reference point (0 points)
        testRay = new Ray(point4, new Vector(1, 1, 1));
        assertNull(testTriangleIntersection.findIntersections(testRay), INTERSECTION_PLANE_FAILURE_MESSAGE);

        // **** Sub-Group: Ray is orthogonal to the plane and starts before the plane (6 cases - the triangle intersection tests)
        // **** Sub-Group: Triangle EP cases
        // BV10: Inside triangle (1 point)
        testRay = new Ray(new Point(0.5, 0.5, 0), new Vector(0, 0, 1));
        intersectionPoint = new Point(0.5, 0.5, 1);
        expectedIntersectionList = List.of(intersectionPoint);
        assertEquals(expectedIntersectionList, testTriangleIntersection.findIntersections(testRay), INTERSECTION_FAILURE_MESSAGE);

        // BV11: Outside against an edge (0 points)
        testRay = new Ray(new Point(1.5, 1.5, 0), new Vector(0, 0, 1));
        assertNull(testTriangleIntersection.findIntersections(testRay), INTERSECTION_OUTSIDE_FAILURE_MESSAGE);

        // BV12: Outside against a vertex (0 points)
        testRay = new Ray(new Point(-1, -1, 0), new Vector(0, 0, 1));
        assertNull(testTriangleIntersection.findIntersections(testRay), INTERSECTION_OUTSIDE_FAILURE_MESSAGE);

        // **** Sub-Group: Triangle BVA cases
        // BV13: Point on edge (0 points)
        testRay = new Ray(new Point(1, 0, 0), new Vector(0, 0, 1));
        assertNull(testTriangleIntersection.findIntersections(testRay), INTERSECTION_ON_EDGE_FAILURE_MESSAGE);

        // BV14: Point in vertex (0 points)
        testRay = new Ray(new Point(2, 0, 0), new Vector(0, 0, 1));
        assertNull(testTriangleIntersection.findIntersections(testRay), INTERSECTION_ON_EDGE_FAILURE_MESSAGE);

        // BV15: Point on edge continuation (0 points)
        testRay = new Ray(new Point(3, 0, 0), new Vector(0, 0, 1));
        assertNull(testTriangleIntersection.findIntersections(testRay), INTERSECTION_OUTSIDE_FAILURE_MESSAGE);
    }
}