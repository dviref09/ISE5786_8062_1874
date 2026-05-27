package lighting;

import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests class for {@link DirectionalLight}
 * @author Dvir Farkash
 */
class DirectionalLightTests {
    /**
     * Test data
     */
    private final Color testIntensity = new Color(143, 54, 73);
    private final Vector testDirection = new Vector(0.6, 0.8, 0);
    private final LightSource testLight = new DirectionalLight(testIntensity, testDirection);
    private final Point testPoint = new Point(4, 3, 76);
    
    /**
     * Failure messages
     */
    private static final String GET_L_FAILURE_MESSAGE = "The vector from the light source to the point is incorrect";
    private static final String GET_INTENSITY_FAILURE_MESSAGE = "The intensity at the point is incorrect";
    
    /**
     * Test method for {@link DirectionalLight#getL(Point)}
     */
    @Test
    void testGetL() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: any point
        assertEquals(testDirection, testLight.getL(testPoint), GET_L_FAILURE_MESSAGE);
    }
    
    /**
     * Test method for {@link DirectionalLight#getIntensity()}
     */
    @Test
    void testGetIntensity() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: any point
        assertEquals(testIntensity, testLight.getIntensity(testPoint), GET_INTENSITY_FAILURE_MESSAGE);
    }
}
