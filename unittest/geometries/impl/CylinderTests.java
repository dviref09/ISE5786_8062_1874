package geometries.impl;

import org.junit.jupiter.api.Test;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test class for {@link geometries.impl.Cylinder}.
 * @author Dvir Farkash
 */
class CylinderTests {
    /**
     * Test data
     */
    private static final Ray axis = new Ray(new Point(0, 0, 1), new Vector(0, 0, 1));
    private static final Cylinder testCylinder = new Cylinder(1, axis, 2);

    /**
     * Failure messages for the tests
     */
    private static final String FAILURE_MESSAGE = "The normal is wrong";

    /**
     * Test method for {@link geometries.impl.Cylinder#getNormal(Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Point on the side surface
        Point testPoint = new Point(1, 0, 2);
        Vector expectedNormal = new Vector(1, 0, 0);
        assertEquals(expectedNormal, testCylinder.getNormal(testPoint), FAILURE_MESSAGE);

        // EP02: Point on the bottom base
        testPoint = new Point(0.5, 0, 1);
        expectedNormal = new Vector(0, 0, -1);
        assertEquals(expectedNormal, testCylinder.getNormal(testPoint), FAILURE_MESSAGE);

        // EP03: Point on the top base
        testPoint = new Point(0.5, 0, 3);
        expectedNormal = new Vector(0, 0, 1);
        assertEquals(expectedNormal, testCylinder.getNormal(testPoint), FAILURE_MESSAGE);

        // =============== Boundary Values Tests ==================
        // BV01: Center of the bottom base
        testPoint = new Point(0, 0, 1);
        expectedNormal = new Vector(0, 0, -1);
        assertEquals(expectedNormal, testCylinder.getNormal(testPoint), FAILURE_MESSAGE);

        // BV02: Center of the top base
        testPoint = new Point(0, 0, 3);
        expectedNormal = new Vector(0, 0, 1);
        assertEquals(expectedNormal, testCylinder.getNormal(testPoint), FAILURE_MESSAGE);

        // We chose that the normal to the point on the edge between the side and the base will get the normal of the base
        // BV03: Point on the bottom edge
        testPoint = new Point(1, 0, 1);
        expectedNormal = new Vector(0, 0, -1);
        assertEquals(expectedNormal, testCylinder.getNormal(testPoint), FAILURE_MESSAGE);

        // BV04: Point on the top edge
        testPoint = new Point(1, 0, 3);
        expectedNormal = new Vector(0, 0, 1);
        assertEquals(expectedNormal, testCylinder.getNormal(testPoint), FAILURE_MESSAGE);
    }
}
