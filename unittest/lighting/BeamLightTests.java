package lighting;

import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit test class for {@link BeamLight}
 * @author Dvir Farkash
 */
public class BeamLightTests {
    /**
     * Test data
     */
    private final Point testPosition = new Point(1, 1, 1);
    private final Vector testDirection = new Vector(1, 0, 0);
    private final Color testIntensity = new Color(100000, 100000, 100000);
    private final LightSource testLight = new BeamLight(testIntensity, testPosition, testDirection)
            .setKc(0).setKl(2).setKq(4).setNarrowBeam(10);
    
    /**
     * Failure strings
     */
    private static final String GET_L_FAILURE_MESSAGE = "The vector from the light source to the point is incorrect";
    private static final String GET_INTENSITY_FAILURE_MESSAGE = "The intensity at the point is incorrect";
    
    /**
     * Test method for {@link BeamLight#getL(Point)}
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
     * Test method for {@link BeamLight#getIntensity(Point)}
     */
    @Test
    void testGetIntensity() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Point in front of the beam light
        Point testPoint = new Point(2, 1 + Math.sqrt(3), 1);
        Color expectedIntensity = new Color(4.8828125, 4.8828125, 4.8828125);
        assertEquals(expectedIntensity, testLight.getIntensity(testPoint), GET_INTENSITY_FAILURE_MESSAGE);
        
        // EP02: Point in behind the beam light (no lighting)
        testPoint = new Point(-5, 1, 1);
        assertEquals(Color.BLACK, testLight.getIntensity(testPoint), GET_INTENSITY_FAILURE_MESSAGE);
        
        // =============== Boundary Values Tests ==================
        // BV01: The target point is in the beam light position
        assertEquals(testIntensity, testLight.getIntensity(testPosition), GET_INTENSITY_FAILURE_MESSAGE);
        
        // BV02: The target point is in 90° to the beam light direction
        testPoint = new Point(1, 3, 5);
        assertEquals(Color.BLACK, testLight.getIntensity(testPoint), GET_INTENSITY_FAILURE_MESSAGE);
        
        // BV03: The target point is exactly in front of the beam light (the angel between L and Direction is zero)
        testPoint = new Point(3, 1, 1);
        expectedIntensity = new Color(5000, 5000, 5000);
        assertEquals(expectedIntensity, testLight.getIntensity(testPoint), GET_INTENSITY_FAILURE_MESSAGE);
        
        // BV04: The beam power (the exponent is 1) - should behave like spotlight, checking in the general case with
        // lighting
        LightSource testLight = new BeamLight(testIntensity, testPosition, testDirection)
                .setKc(0).setKl(2).setKq(4).setNarrowBeam(1);
        testPoint = new Point(2, 1 + Math.sqrt(3), 1);
        expectedIntensity = new Color(2500, 2500, 2500);
        assertEquals(expectedIntensity, testLight.getIntensity(testPoint), GET_INTENSITY_FAILURE_MESSAGE);
    }
    
}
