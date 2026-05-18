package primitives;

import geometries.api.Intersectable;
import geometries.impl.Plane;
import geometries.impl.Sphere;
import geometries.impl.Triangle;
import org.junit.jupiter.api.Test;

import java.util.List;

import static geometries.api.Intersectable.Intersection;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for {@link primitives.Ray}.
 *
 * @author Dvir Farkash
 */
class RayTests {
    /**
     * Test data
     */
    Point testOrigin = new Point(0, 0, 1);
    Vector testDirection = new Vector(0, 0, 1);
    Ray testRay = new Ray(testOrigin, testDirection);

    /**
     * Points on the ray for findClosestPoint test
     */
    Point point1 = new Point(0,0,2);
    Point point2 = new Point(0,0,3);
    Point point3 = new Point(0,0,4);

    /**
     * Intersections with points on the ray and geometric bodies for findClosestIntersection test
     */
    Sphere testSphereIntersection = new Sphere(new Point(1, 0, 0), 1);
    Plane testPlaneIntersection = new Plane(new Point(0, 0, 1), new Vector(0, 0, 1));
    Triangle testTriangleIntersection = new Triangle(new Point(0, 0, 1), new Point(0, -1, -1), new Point(0, 1, -1));
    Intersection intersection1 = new Intersection(testSphereIntersection, new Point(0,0,2));
    Intersection intersection2 = new Intersection(testPlaneIntersection, new Point(0,0,3));
    Intersection intersection3 = new Intersection(testTriangleIntersection, new Point(0,0,4));



    /**
     * Messages for assertion failures.
     */
    static final String CONSTRUCTOR_TEST_ORIGIN_FAILURE_MESSAGE = "Constructor didn't set the origin properly";
    static final String CONSTRUCTOR_TEST_DIRECTION_FAILURE_MESSAGE = "Constructor didn't set the direction properly";
    static final String GET_POINT_FAILURE_MESSAGE = "The result point is not correct";
    static final String FIND_CLOSEST_INTERSECTION_WRONG_POINT_FAILURE_MESSAGE = "The result point is not the closest one";
    static final String FIND_CLOSEST_INTERSECTION_NULL_FAILURE_MESSAGE = "The result should be null";

    /**
     * Test method for {@link Ray#Ray(Point, Vector)}.
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Constructing a ray with a valid origin and direction
        Point origin = new Point(1, 2, 3);
        Vector direction = new Vector(0, 4, 0);
        Ray testRay = new Ray(origin, direction);
        Vector expectedDirection = new Vector(0, 1, 0); // The direction should be normalized

        assertEquals(origin, testRay.origin(), CONSTRUCTOR_TEST_ORIGIN_FAILURE_MESSAGE);
        assertEquals(expectedDirection, testRay.direction(), CONSTRUCTOR_TEST_DIRECTION_FAILURE_MESSAGE);
    }

    /**
     * Test method for {@link Ray#getPoint(double)}
     */
    @Test
    void testGetPoint() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Positive t value
        double t = 2;
        Point expectedResult = new Point(0, 0, 3);

        assertEquals(expectedResult, testRay.getPoint(t), GET_POINT_FAILURE_MESSAGE);

        // EP02: Negative t value
        t = -2;
        expectedResult = new Point(0, 0, -1);

        assertEquals(expectedResult, testRay.getPoint(t), GET_POINT_FAILURE_MESSAGE);

        // =============== Boundary Values Tests ==================
        // BV01: t value is zero
        t = 0;
        expectedResult = testOrigin;

        assertEquals(expectedResult, testRay.getPoint(t), GET_POINT_FAILURE_MESSAGE);
    }

    /**
     * Test method for {@link Ray#findClosestPoint(List)}
     */
    @Test
    void testFindClosestPoint() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: List of at least 3 point and the middle one is the closest
        List<Point> testPoints = List.of(point2, point1, point3);
        assertEquals(point1, testRay.findClosestPoint(testPoints), FIND_CLOSEST_INTERSECTION_WRONG_POINT_FAILURE_MESSAGE);

        // =============== Boundary Values Tests ==================
        // BV01: The list of points is null
        assertNull(testRay.findClosestPoint(null), FIND_CLOSEST_INTERSECTION_NULL_FAILURE_MESSAGE);

        // BV02: List of at least 3 points and the first one is the closest
        testPoints = List.of(point1, point2, point3);
        assertEquals(point1, testRay.findClosestPoint(testPoints), FIND_CLOSEST_INTERSECTION_WRONG_POINT_FAILURE_MESSAGE);

        // BV03: List of at least 3 points and the last one is the closest
        testPoints = List.of(point3, point2, point1);
        assertEquals(point1, testRay.findClosestPoint(testPoints), FIND_CLOSEST_INTERSECTION_WRONG_POINT_FAILURE_MESSAGE);

        // BV04: List of at least 3 points and there is two point that are the same and the same points are the closest
        testPoints = List.of(point3, point1, point1);
        assertEquals(point1, testRay.findClosestPoint(testPoints), FIND_CLOSEST_INTERSECTION_WRONG_POINT_FAILURE_MESSAGE);

        // BV05: List of at least 3 points and there is two point that are the same and the same points are not the closest
        testPoints = List.of(point3, point3, point1);
        assertEquals(point1, testRay.findClosestPoint(testPoints), FIND_CLOSEST_INTERSECTION_WRONG_POINT_FAILURE_MESSAGE);
    }

    /**
     * Test method for {@link Ray#findClosestIntersection(List)}
     */
    @Test
    void testFindClosestIntersection() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: List of at least 3 point and the middle one is the closest
        List<Intersection> testIntersections = List.of(intersection2, intersection1, intersection3);
        assertEquals(intersection1, testRay.findClosestIntersection(testIntersections), FIND_CLOSEST_INTERSECTION_WRONG_POINT_FAILURE_MESSAGE);

        // =============== Boundary Values Tests ==================
        // BV01: The list of points is null
        assertNull(testRay.findClosestIntersection(null), FIND_CLOSEST_INTERSECTION_NULL_FAILURE_MESSAGE);

        // BV02: List of at least 3 points and the first one is the closest
        testIntersections = List.of(intersection1, intersection2, intersection3);
        assertEquals(intersection1, testRay.findClosestIntersection(testIntersections), FIND_CLOSEST_INTERSECTION_WRONG_POINT_FAILURE_MESSAGE);

        // BV03: List of at least 3 points and the last one is the closest
        testIntersections = List.of(intersection3, intersection2, intersection1);
        assertEquals(intersection1, testRay.findClosestIntersection(testIntersections), FIND_CLOSEST_INTERSECTION_WRONG_POINT_FAILURE_MESSAGE);

        // BV04: List of at least 3 points and there is two point that are the same and the same points are the closest
        testIntersections = List.of(intersection3, intersection1, intersection1);
        assertEquals(intersection1, testRay.findClosestIntersection(testIntersections), FIND_CLOSEST_INTERSECTION_WRONG_POINT_FAILURE_MESSAGE);

        // BV05: List of at least 3 points and there is two point that are the same and the same points are not the closest
        testIntersections = List.of(intersection3, intersection3, intersection1);
        assertEquals(intersection1, testRay.findClosestIntersection(testIntersections), FIND_CLOSEST_INTERSECTION_WRONG_POINT_FAILURE_MESSAGE);
    }
}
