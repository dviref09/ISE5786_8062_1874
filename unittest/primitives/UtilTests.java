package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static primitives.Util.powerInt;

/**
 * Unit test class for {@link Util}
 */
public class UtilTests {
    /**
     * Test data
     */
    private final double base = 3.5;
    
    /**
     * Delta value for accuracy when comparing double values.
     */
    private static final double DELTA = 1e-10;
    
    /**
     * Failure messages
     */
    private static final String POWER_INT_FAILURE_MESSAGE = "The result of the exponentiation is incorrect.";
    
    /**
     * Test method for {@link Util#powerInt(double, int)}
     */
    @Test
    void testPowerInt() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: The exponent is odd
        int exponent = 7;
        double expectedResult = 6433.9296875;
        assertEquals(expectedResult, powerInt(base, exponent), POWER_INT_FAILURE_MESSAGE);
        
        // EP02: The exponent is even
        exponent = 12;
        expectedResult = 3379220.508056640625;
        assertEquals(expectedResult, powerInt(base, exponent), POWER_INT_FAILURE_MESSAGE);
        
        // EP03: The exponent is negative
        assertThrows(IllegalArgumentException.class, () -> powerInt(base, -4), POWER_INT_FAILURE_MESSAGE);
        
        // =============== Boundary Values Tests ==================
        // BV01: The exponent is 1
        exponent = 1;
        expectedResult = 3.5;
        assertEquals(expectedResult, powerInt(base, exponent), POWER_INT_FAILURE_MESSAGE);
        
        // BV02: The exponent is 0
        exponent = 0;
        expectedResult = 1;
        assertEquals(expectedResult, powerInt(base, exponent), POWER_INT_FAILURE_MESSAGE);
        
        // BV03: The exponent is -1
        assertThrows(IllegalArgumentException.class, () -> powerInt(base, -1), POWER_INT_FAILURE_MESSAGE);
    }
}
