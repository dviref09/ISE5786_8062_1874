package primitives;

import org.junit.jupiter.api.Test;

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
     * Messages for assertion failures.
     */
    final static String CONSTRUCTOR_TEST_ORIGIN_FAILURE_MESSAGE = "Constructor didn't set the origin properly";
    final static String CONSTRUCTOR_TEST_DIRECTION_FAILURE_MESSAGE = "Constructor didn't set the direction properly";
    final static String GET_POINT_FAILURE_MESSAGE = "The result point is not correct";

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
}
