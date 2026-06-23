package sampling;

import java.util.List;

import org.junit.jupiter.api.Test;
import primitives.Point2D;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static primitives.Util.alignZero;

/**
 * Unit test class for the 2D points sampling (in enum {@link SamplerType} and class {@link Sampler}
 * @author Amichai Feigelson
 */
public class SamplerTests {
    /**
     * Test data
     */
    SamplerType jittered  = SamplerType.JITTERED;
    
    /**
     * Failure messages
     */
    private static final String JITTERED_X_FAILURE_MESSAGE = "The x value of the point is out of range";
    private static final String JITTERED_Y_FAILURE_MESSAGE = "The y value of the point is out of range";
    private static final String JITTERED_00_FAILURE_MESSAGE = "The point should be (0, 0) without any offset";
    
    /**
     * Test method for jittered sampling pattern
     */
    @Test
    void jitteredTest() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: Jittered with general amount of x and y values
        List<Point2D> testPoints = jittered.samplePoints(13, 17);
        testJitteredPoints(testPoints, 17);
        
        // =============== Boundary Values Tests ==================
        // BV01: Jittered with x value of 1
        testPoints = jittered.samplePoints(1, 17);
        testJitteredPoints(testPoints, 17);
        
        // BV02: Jittered with y value of 1
        testPoints = jittered.samplePoints(13, 1);
        testJitteredPoints(testPoints, 1);
        
        // BV03: Jittered with x and y value of 1 (The point should be (0, 0) without any offset)
        testPoints = jittered.samplePoints(1, 1);
        assertEquals(new Point2D(0, 0), testPoints.get(0), JITTERED_00_FAILURE_MESSAGE);
    }
    
    /**
     * Private helper method that check that all points are in range
     */
    private void testJitteredPoints(List<Point2D> points, int nY) {
        // The current x, y in the point
        double x = 0, y = 0;
        // The offset in the point from the center of the (x,y)
        double xOffset, yOffset;
        for (Point2D point : points) {
            xOffset = alignZero(point.x() - x);
            yOffset = alignZero(point.y() - y);
            // Check that the point is in the correct range
            assertTrue(xOffset <= 0.5 && xOffset >= -0.5,
                    JITTERED_X_FAILURE_MESSAGE + " point x: " + point.x() + " should be x: " + x + " offset: " + xOffset);
            assertTrue(yOffset <= 0.5 && yOffset >= -0.5,
                    JITTERED_Y_FAILURE_MESSAGE+ " point y: " + point.y() + " should be y: " + y + " offset: " + yOffset);
            
            // Update the current offset
            if (y == nY - 1) {
                x++;
                y = 0;
            }
            else {
                y++;
            }
        }
    }
}
