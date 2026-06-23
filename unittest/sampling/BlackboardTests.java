package sampling;

import java.util.List;
import java.util.MissingResourceException;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for class {@link Blackboard} and its Builder.
 * @author Amichai feigelson
 */
class BlackboardTests {
    /**
     * Failure messages
     */
    private static final String VALID_BUILD_FAILURE = "Failed to build a valid Blackboard instance";
    private static final String WIDTH_FAILURE = "Width must be greater than zero";
    private static final String HEIGHT_FAILURE = "Height must be greater than zero";
    private static final String NX_RESOLUTION_FAILURE = "nX resolution must be greater than zero";
    private static final String NY_RESOLUTION_FAILURE = "nY resolution must be greater than zero";
    private static final String MISSING_CENTER_FAILURE = "Should throw exception when center point is missing";
    private static final String MISSING_DIRECTION_FAILURE = "Should throw exception when direction vectors are missing";
    private static final String NON_ORTHOGONAL_FAILURE = "Vectors must be orthogonal to each other";
    private static final String GENERATE_POINT_FAILURE = "generatePoint returned incorrect 3D point coordinates";
    private static final String POINTS_LIST_SIZE_FAILURE = "generatePoints returned an incorrect number of points";
    private static final String POINTS_NOT_NULL_FAILURE = "generatePoints should not return a null list";
    private static final String NEGATIVE_OFFSET_FAILURE = "Should throw exception for negative offsets";
    
    /**
     * Test method for {@link Blackboard#generatePoint(double, double)}.
     */
    @Test
    void testGeneratePoint() {
        // Build a standard blackboard for testing: size 10x10, center at (0,0,10), looking forward
        Blackboard blackboard = Blackboard.getBuilder()
                                          .setSize(10, 10)
                                          .setResolution(5, 5)
                                          .setCenter(new Point(0, 0, 10))
                                          .setDirection(new Vector(0, 1, 0), new Vector(1, 0, 0))
                                          .build();
        
        // ============ Equivalence Partitions Tests ==================
        // EP01: Positive offsets strictly within valid bounds
        Point result = blackboard.generatePoint(2, 3);
        assertEquals(new Point(0, -2, 10), result, GENERATE_POINT_FAILURE);
        
        // ---- Invalid Partition ----
        // EP02: Negative x offset
        assertThrows(IllegalArgumentException.class, () -> blackboard.generatePoint(-1.5, 2.5), NEGATIVE_OFFSET_FAILURE);
        
        // EP03: Negative y offset
        assertThrows(IllegalArgumentException.class, () -> blackboard.generatePoint(2.5, -1.5), NEGATIVE_OFFSET_FAILURE);
        
        // EP04: Out of bounds:
        assertThrows(IllegalArgumentException.class, () -> blackboard.generatePoint(8, 7), NEGATIVE_OFFSET_FAILURE);
        
        // =============== Boundary Values Tests ==================
        
        // BV01: Offset is exactly at the lower bound (0,0) - start of the pixel/grid
        result = blackboard.generatePoint(-0.5, -0.5);
        assertEquals(new Point(-5, 5, 10), result, GENERATE_POINT_FAILURE);
        
        // BV02: Offsets at the exact upper boundary limits
        result = blackboard.generatePoint(4.5, 4.5);
        assertEquals(new Point(5, -5, 10), result, GENERATE_POINT_FAILURE);
    }
    
    /**
     * Test method for {@link Blackboard#generatePoints()}.
     */
    @Test
    void testGeneratePoints() {
        // Build a blackboard with 2x2 resolution (total 4 cells)
        Blackboard blackboard = Blackboard.getBuilder()
                                          .setSize(4, 4)
                                          .setResolution(2, 2)
                                          .setCenter(new Point(0, 0, 0))
                                          .setDirection(new Vector(0, 1, 0), new Vector(1, 0, 0))
                                          .build();
        
        // ============ Equivalence Partitions Tests ==================
        // EP01: Test successful generation of multiple points and check list properties
        List<Point> points = blackboard.generatePoints();
        
        assertNotNull(points, POINTS_NOT_NULL_FAILURE);
        // Check that the collection size matches the expected resolution grid (nX * nY = 2 * 2 = 4)
        assertEquals(4, points.size(), POINTS_LIST_SIZE_FAILURE);
    }
    
    /**
     * Test method for {@link Blackboard.Builder#build()}.
     */
    @Test
    void testBuild() {
        // ============ Equivalence Partitions Tests ==================
        // EP01: Test that a valid blackboard is built successfully
        assertDoesNotThrow(() -> Blackboard.getBuilder()
                                           .setSize(10, 10)
                                           .setResolution(5, 5)
                                           .setCenter(new Point(0, 0, 0))
                                           .setDirection(new Vector(0, 1, 0), new Vector(1, 0, 0))
                                           .build(),
                VALID_BUILD_FAILURE);
        
        // EP02: Width is negative
        assertThrows(IllegalArgumentException.class, () -> Blackboard.getBuilder()
                                                                     .setSize(-5, 10)
                                                                     .setResolution(5, 5)
                                                                     .setCenter(new Point(0, 0, 0))
                                                                     .setDirection(new Vector(0, 1, 0), new Vector(1,
                                                                             0, 0))
                                                                     .build(),
                WIDTH_FAILURE);
        
        // EP03: Height is negative
        assertThrows(IllegalArgumentException.class, () -> Blackboard.getBuilder()
                                                                     .setSize(10, -5)
                                                                     .setResolution(5, 5)
                                                                     .setCenter(new Point(0, 0, 0))
                                                                     .setDirection(new Vector(0, 1, 0), new Vector(1,
                                                                             0, 0))
                                                                     .build(),
                HEIGHT_FAILURE);
        
        // EP04: nX is negative
        assertThrows(IllegalArgumentException.class, () -> Blackboard.getBuilder()
                                                                     .setSize(10, 10)
                                                                     .setResolution(-5, 5)
                                                                     .setCenter(new Point(0, 0, 0))
                                                                     .setDirection(new Vector(0, 1, 0), new Vector(1,
                                                                             0, 0))
                                                                     .build(),
                NX_RESOLUTION_FAILURE);
        
        // EP05: nY is negative
        assertThrows(IllegalArgumentException.class, () -> Blackboard.getBuilder()
                                                                     .setSize(10, 10)
                                                                     .setResolution(5, -5)
                                                                     .setCenter(new Point(0, 0, 0))
                                                                     .setDirection(new Vector(0, 1, 0), new Vector(1,
                                                                             0, 0))
                                                                     .build(),
                NY_RESOLUTION_FAILURE);
        
        // EP06: Missing Center point
        assertThrows(MissingResourceException.class, () -> Blackboard.getBuilder()
                                                                     .setSize(10, 10)
                                                                     .setResolution(5, 5)
                                                                     .setDirection(new Vector(0, 1, 0), new Vector(1,
                                                                             0, 0))
                                                                     .build(),
                MISSING_CENTER_FAILURE);
        
        // EP07: Missing Direction vectors
        assertThrows(MissingResourceException.class, () -> Blackboard.getBuilder()
                                                                     .setSize(10, 10)
                                                                     .setResolution(5, 5)
                                                                     .setCenter(new Point(0, 0, 0))
                                                                     .build(),
                MISSING_DIRECTION_FAILURE);
        
        // EP08: Vectors are not orthogonal (angle is not 90 degrees)
        assertThrows(IllegalArgumentException.class, () -> Blackboard.getBuilder()
                                                                     .setSize(10, 10)
                                                                     .setResolution(5, 5)
                                                                     .setCenter(new Point(0, 0, 0))
                                                                     .setDirection(new Vector(0, 1, 0), new Vector(1,
                                                                             1, 0))
                                                                     .build(),
                NON_ORTHOGONAL_FAILURE);
        
        // =============== Boundary Values Tests ==================
        // BV01: Width is exactly zero
        assertThrows(IllegalArgumentException.class, () -> Blackboard.getBuilder()
                                                                     .setSize(0, 10)
                                                                     .setResolution(5, 5)
                                                                     .setCenter(new Point(0, 0, 0))
                                                                     .setDirection(new Vector(0, 1, 0), new Vector(1,
                                                                             0, 0))
                                                                     .build(),
                WIDTH_FAILURE);
        
        // BV02: Height is exactly zero
        assertThrows(IllegalArgumentException.class, () -> Blackboard.getBuilder()
                                                                     .setSize(10, 0)
                                                                     .setResolution(5, 5)
                                                                     .setCenter(new Point(0, 0, 0))
                                                                     .setDirection(new Vector(0, 1, 0), new Vector(1,
                                                                             0, 0))
                                                                     .build(),
                HEIGHT_FAILURE);
        
        // BV03: nX resolution is exactly zero
        assertThrows(IllegalArgumentException.class, () -> Blackboard.getBuilder()
                                                                     .setSize(10, 10)
                                                                     .setResolution(0, 5)
                                                                     .setCenter(new Point(0, 0, 0))
                                                                     .setDirection(new Vector(0, 1, 0), new Vector(1,
                                                                             0, 0))
                                                                     .build(),
                NX_RESOLUTION_FAILURE);
        
        // BV04: nY resolution is exactly zero
        assertThrows(IllegalArgumentException.class, () -> Blackboard.getBuilder()
                                                                     .setSize(10, 10)
                                                                     .setResolution(5, 0)
                                                                     .setCenter(new Point(0, 0, 0))
                                                                     .setDirection(new Vector(0, 1, 0), new Vector(1,
                                                                             0, 0))
                                                                     .build(),
                NY_RESOLUTION_FAILURE);
    }
}