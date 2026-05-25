package lighting;

import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit test class for {@link PointLight}
 * @author Dvir Farkash
 */
class PointLightTests {
    /**
     * Test data
     */
    final Point testPosition = new Point(1, 1, 1);
    final Color testIntensity = new Color(105, 210, 42);
    final LightSource testLight = new PointLight(testIntensity, testPosition)
            .setKc(0).setKl(2).setKq(4);

    /**
     * Failure strings
     */
    private static final String GET_L_FAILURE_MESSAGE = "The vector from the light source to the point is incorrect";
    private static final String GET_INTENSITY_FAILURE_MESSAGE = "The intensity at the point is incorrect";

    /**
     * Test method for {@link PointLight#getL(Point)}
     */
    @Test
    void testGetL() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Target point is not at the light position
        Point testPoint = new Point (4, 2, 6);
        Vector expectedResult = new Vector(3, 1, 5);
        assertEquals(expectedResult, testLight.getL(testPoint), GET_L_FAILURE_MESSAGE);

        // =============== Boundary Values Tests ==================
        // BV01: Target point is at the light position
        assertThrows(IllegalArgumentException.class, () -> testLight.getL(testPosition), GET_L_FAILURE_MESSAGE);
    }

    /**
     * Test method for {@link PointLight#getIntensity(Point)}
     */
    @Test
    void testGetIntensity() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Target point is not at the light position
        Point testPoint = new Point (3, 4, 7);
        Color expectedIntensity = new Color(0.5, 1, 0.2);
        assertEquals(expectedIntensity, testLight.getIntensity(testPoint), GET_INTENSITY_FAILURE_MESSAGE);

        // =============== Boundary Values Tests ==================
        // BV01: Target point is at the light position
        assertEquals(testIntensity, testLight.getIntensity(testPosition), GET_INTENSITY_FAILURE_MESSAGE);
    }
}
