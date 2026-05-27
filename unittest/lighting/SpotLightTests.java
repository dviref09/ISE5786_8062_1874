package lighting;

import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit test class for {@link SpotLight}
 * @author Dvir Farkash
 */
public class SpotLightTests {
    /**
     * Test data
     */
    private final Point testPosition = new Point(1, 1, 1);
    private final Vector testDirection = new Vector(1, 0, 0);
    private final Color testIntensity = new Color(55, 165, 110);
    private final LightSource testLight = new SpotLight(testIntensity, testPosition, testDirection)
            .setKc(0).setKl(2).setKq(4);
    
    /**
     * Failure strings
     */
    private static final String GET_L_FAILURE_MESSAGE = "The vector from the light source to the point is incorrect";
    private static final String GET_INTENSITY_FAILURE_MESSAGE = "The intensity at the point is incorrect";
    
    /**
     * Test method for {@link SpotLight#getL(Point)}
     */
    @Test
    void testGetL() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Target point is not at the light position
        Point testPoint = new Point(4, 2, 6);
        Vector expectedResult = (new Vector(3, 1, 5)).normalize();
        assertEquals(expectedResult, testLight.getL(testPoint), GET_L_FAILURE_MESSAGE);
        
        // =============== Boundary Values Tests ==================
        // BV01: Target point is at the light position
        assertThrows(IllegalArgumentException.class, () -> testLight.getL(testPosition), GET_L_FAILURE_MESSAGE);
    }
    
    /**
     * Test method for {@link SpotLight#getIntensity(Point)}
     */
    @Test
    void testGetIntensity() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Point in front of the spotlight
        Point testPoint = new Point(5, 4, 1);
        Color expectedIntensity = new Color(0.4, 1.2, 0.8);
        assertEquals(expectedIntensity, testLight.getIntensity(testPoint), GET_INTENSITY_FAILURE_MESSAGE);
        
        // EP02: Point in behind the spotlight (no lighting)
        testPoint = new Point(-5, 1, 1);
        assertEquals(Color.BLACK, testLight.getIntensity(testPoint), GET_INTENSITY_FAILURE_MESSAGE);
        
        // =============== Boundary Values Tests ==================
        // BV01: The target point is in the spotlight position
        assertEquals(testIntensity, testLight.getIntensity(testPosition), GET_INTENSITY_FAILURE_MESSAGE);
        
        // BV02: The target point is in 90° to the spotlight direction
        testPoint = new Point(1, 3, 5);
        assertEquals(Color.BLACK, testLight.getIntensity(testPoint), GET_INTENSITY_FAILURE_MESSAGE);
        
        // BV03: The target point is exactly in front of the spotlight (the angel between L and Direction is zero)
        testPoint = new Point(6, 1, 1);
        expectedIntensity = new Color(0.5, 1.5, 1);
        assertEquals(expectedIntensity, testLight.getIntensity(testPoint), GET_INTENSITY_FAILURE_MESSAGE);
    }
    
}
