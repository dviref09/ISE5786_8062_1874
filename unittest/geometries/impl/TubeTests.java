package geometries.impl;

import org.junit.jupiter.api.Test;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test class for {@link geometries.impl.Tube}.
 * @author Dvir Farkash
 */
public class TubeTests {
    /**
     * Test data
     */
    // Simple tube along the Z axis (from z=1 upwards), radius 1
    Ray axis = new Ray(new Point(0, 0, 1), new Vector(0, 0, 1));
    Tube testTube = new Tube(1, axis);

    /**
     * Failure messages for the tests
     */
    private static final String GET_NORMAL_FAILURE_MESSAGE = "Tube normal is incorrect.";

    /**
     * Test method for {@link geometries.impl.Tube#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests =================
        // EP01: Point is "before" the start of the ray
        Point testPoint = new Point(1, 0, 0);
        Vector normal = testTube.getNormal(testPoint);
        Vector expectedNormal = new Vector(1, 0, 0);
        assertEquals(expectedNormal, normal, GET_NORMAL_FAILURE_MESSAGE);

        // EP02: Point is "after" the start of the ray
        testPoint = new Point(1, 0, 2);
        normal = testTube.getNormal(testPoint);
        expectedNormal = new Vector(1, 0, 0);
        assertEquals(expectedNormal, normal, GET_NORMAL_FAILURE_MESSAGE);

        // =============== Boundary Values Tests ==================
        // BV01: The point on the surface is exactly across the ray's starting point
        testPoint = new Point(1, 0, 1);
        normal = testTube.getNormal(testPoint);
        expectedNormal = new Vector(1, 0, 0);
        assertEquals(expectedNormal, normal, GET_NORMAL_FAILURE_MESSAGE);

    }
}
