package primitives;

import java.util.List;

import geometries.impl.Plane;
import geometries.impl.Sphere;
import geometries.impl.Triangle;
import org.junit.jupiter.api.Test;

import static geometries.api.Intersectable.Intersection;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit test class for {@link primitives.Ray}.
 * @author Dvir Farkash
 */
class RayTests {
    /**
     * Test data
     */
    private final Point testOrigin = new Point(0, 0, 1);
    private final Vector testDirection = new Vector(0, 0, 1);
    private final Ray testRay = new Ray(testOrigin, testDirection);
    
    /**
     * Points on the ray for findClosestPoint  and getPoints tests
     */
    private final Point point1 = new Point(0, 0, 2);
    private final Point point2 = new Point(0, 0, 3);
    private final Point point3 = new Point(0, 0, 4);
    /**
     * Intersections with points on the ray and geometric bodies for findClosestIntersection test
     */
    private final Sphere testSphereIntersection = new Sphere(new Point(1, 0, 0), 1);
    private final Plane testPlaneIntersection = new Plane(new Point(0, 0, 1), new Vector(0, 0, 1));
    private final Triangle testTriangleIntersection =
            new Triangle(new Point(0, 0, 1), new Point(0, -1, -1), new Point(0, 1, -1));
    private final Intersection intersection1 = new Intersection(testSphereIntersection, new Point(0, 0, 2));
    private final Intersection intersection2 = new Intersection(testPlaneIntersection, new Point(0, 0, 3));
    private final Intersection intersection3 = new Intersection(testTriangleIntersection, new Point(0, 0, 4));
    
    /**
     * Messages for assertion failures.
     */
    private static final String CONSTRUCTOR_TEST_ORIGIN_FAILURE_MESSAGE = "Constructor didn't set the origin properly";
    private static final String CONSTRUCTOR_TEST_DIRECTION_FAILURE_MESSAGE =
            "Constructor didn't set the direction properly";
    private static final String CONSTRUCTOR_TARGET_AND_ORIGIN_SAME =
            "Constructor should throw an exception when origin and target are the same point";
    private static final String GET_POINT_FAILURE_MESSAGE = "The result point is not correct";
    private static final String FIND_CLOSEST_INTERSECTION_WRONG_POINT_FAILURE_MESSAGE =
            "The result point is not the closest one";
    private static final String FIND_CLOSEST_INTERSECTION_NULL_FAILURE_MESSAGE = "The result should be null";
    
    
    /**
     * Test method for {@link Ray#Ray(Point, Vector)}.
     */
    @Test
    void testConstructor1() {
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
     * Test method for {@link Ray#Ray(Point, Vector, Vector)}}
     */
    @Test
    void testConstructor2() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: normal * direction is positive
        Point testOrigin = new Point(1, 1, 1);
        Vector testDirection = new Vector(1, 1, 1);
        Vector testNormal = new Vector(2, 0, 0);
        Ray testRay = new Ray(testOrigin, testDirection, testNormal);
        Point expectedOrigin = new Point(1.2, 1, 1);
        Vector expectedDirection = testDirection.normalize();
        
        assertEquals(expectedOrigin, testRay.origin(), CONSTRUCTOR_TEST_ORIGIN_FAILURE_MESSAGE);
        assertEquals(expectedDirection, testRay.direction(), CONSTRUCTOR_TEST_DIRECTION_FAILURE_MESSAGE);
        
        // EP02: normal * direction is negative
        testDirection = new Vector(-1, -1, -1);
        testNormal = new Vector(0, 0, 1);
        testRay = new Ray(testOrigin, testDirection, testNormal);
        expectedOrigin = new Point(1, 1, 0.9);
        expectedDirection = testDirection.normalize();
        
        assertEquals(expectedOrigin, testRay.origin(), CONSTRUCTOR_TEST_ORIGIN_FAILURE_MESSAGE);
        assertEquals(expectedDirection, testRay.direction(), CONSTRUCTOR_TEST_DIRECTION_FAILURE_MESSAGE);
        
        // =============== Boundary Values Tests ==================
        // BV01: normal and direction is orthogonal
        testDirection = new Vector(-1, -1, 0);
        testNormal = new Vector(0, 0, 1);
        testRay = new Ray(testOrigin, testDirection, testNormal);
        expectedDirection = testDirection.normalize();
        
        assertEquals(testOrigin, testRay.origin(), CONSTRUCTOR_TEST_ORIGIN_FAILURE_MESSAGE);
        assertEquals(expectedDirection, testRay.direction(), CONSTRUCTOR_TEST_DIRECTION_FAILURE_MESSAGE);
    }
    
    /**
     * Test method for {@link Ray#Ray(Point, Point)}
     */
    @Test
    void testConstructor3() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: General origin and target
        Point testOrigin = new Point(1, 1, 1);
        Point testTarget = new Point(2, 2, 2);
        Ray testRay = new Ray(testOrigin, testTarget);
        Vector expectedDirection = new Vector(1, 1, 1).normalize();
        
        assertEquals(testOrigin, testRay.origin(), CONSTRUCTOR_TEST_ORIGIN_FAILURE_MESSAGE);
        assertEquals(expectedDirection, testRay.direction(), CONSTRUCTOR_TEST_DIRECTION_FAILURE_MESSAGE);
        
        // =============== Boundary Values Tests ==================
        // BV01: Origin and target are the same point
        assertThrows(IllegalArgumentException.class, () -> new Ray(testOrigin, testOrigin),
                CONSTRUCTOR_TARGET_AND_ORIGIN_SAME);
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
        assertEquals(point1, testRay.findClosestPoint(testPoints),
                FIND_CLOSEST_INTERSECTION_WRONG_POINT_FAILURE_MESSAGE);
        
        // =============== Boundary Values Tests ==================
        // BV01: The list of points is null
        assertNull(testRay.findClosestPoint(null), FIND_CLOSEST_INTERSECTION_NULL_FAILURE_MESSAGE);
        
        // BV02: List of at least 3 points and the first one is the closest
        testPoints = List.of(point1, point2, point3);
        assertEquals(point1, testRay.findClosestPoint(testPoints),
                FIND_CLOSEST_INTERSECTION_WRONG_POINT_FAILURE_MESSAGE);
        
        // BV03: List of at least 3 points and the last one is the closest
        testPoints = List.of(point3, point2, point1);
        assertEquals(point1, testRay.findClosestPoint(testPoints),
                FIND_CLOSEST_INTERSECTION_WRONG_POINT_FAILURE_MESSAGE);
        
        // BV04: List of at least 3 points and there is two point that are the same and the same points are the closest
        testPoints = List.of(point3, point1, point1);
        assertEquals(point1, testRay.findClosestPoint(testPoints),
                FIND_CLOSEST_INTERSECTION_WRONG_POINT_FAILURE_MESSAGE);
        
        // BV05: List of at least 3 points and there is two point that are the same
        // and the same points are not the closest
        testPoints = List.of(point3, point3, point1);
        assertEquals(point1, testRay.findClosestPoint(testPoints),
                FIND_CLOSEST_INTERSECTION_WRONG_POINT_FAILURE_MESSAGE);
    }
    
    /**
     * Test method for {@link Ray#findClosestIntersection(List)}
     */
    @Test
    void testFindClosestIntersection() {
        // ============ Equivalence Partition Tests ==============
        // EP01: List of at least 3 intersections and the middle one is the closest
        List<Intersection> testIntersections = List.of(intersection2, intersection1, intersection3);
        assertEquals(intersection1, testRay.findClosestIntersection(testIntersections),
                FIND_CLOSEST_INTERSECTION_WRONG_POINT_FAILURE_MESSAGE);
        
        // =============== Boundary Values Tests ==================
        // BV01: The list of intersections is null
        assertNull(testRay.findClosestIntersection(null), FIND_CLOSEST_INTERSECTION_NULL_FAILURE_MESSAGE);
        
        // BV02: List of at least 3 intersections and the first one is the closest
        testIntersections = List.of(intersection1, intersection2, intersection3);
        assertEquals(intersection1, testRay.findClosestIntersection(testIntersections),
                FIND_CLOSEST_INTERSECTION_WRONG_POINT_FAILURE_MESSAGE);
        
        // BV03: List of at least 3 intersections and the last one is the closest
        testIntersections = List.of(intersection3, intersection2, intersection1);
        assertEquals(intersection1, testRay.findClosestIntersection(testIntersections),
                FIND_CLOSEST_INTERSECTION_WRONG_POINT_FAILURE_MESSAGE);
        
        // BV04: List of at least 3 intersections and there is two intersections that are the same and the same
        // intersections are the closest
        testIntersections = List.of(intersection3, intersection1, intersection1);
        assertEquals(intersection1, testRay.findClosestIntersection(testIntersections),
                FIND_CLOSEST_INTERSECTION_WRONG_POINT_FAILURE_MESSAGE);
        
        // BV05: List of at least 3 intersections and there is two intersections that are the same and the same
        // intersections are not the closest
        testIntersections = List.of(intersection3, intersection3, intersection1);
        assertEquals(intersection1, testRay.findClosestIntersection(testIntersections),
                FIND_CLOSEST_INTERSECTION_WRONG_POINT_FAILURE_MESSAGE);
    }
}
