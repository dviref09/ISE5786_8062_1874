package renderer;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.LinkedList;
import org.junit.jupiter.api.Test;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Unit tests for {@link BeamGenerator} class.
 * * @author Your Name
 */
class BeamGeneratorTests {
    /** Error message for incorrect number of rays generated */
    private static final String RAY_COUNT_FAILURE = "Generated beam has an incorrect number of rays";
    
    /** Error message for incorrect ray origin */
    private static final String RAY_ORIGIN_FAILURE = "Ray in the beam has an incorrect origin point";
    
    /** Error message for incorrect ray direction */
    private static final String RAY_DIRECTION_FAILURE = "Ray in the beam has an incorrect direction vector";
    private static final String GENERATED_BEAM_NOT_NULL_FAILURE = "Generated beam list should not be null";
    private static final String BEAM_EMPTY_TARGETS_FAILURE = "Beam from empty targets list should contain 0 rays";
    private static final String TARGET_EQUALS_ORIGIN_FAILURE =
            "Generating a ray where target equals origin should throw an exception";
    private static final String NULL_TARGETS_FAILURE =
            "Generating a beam with a null targets list should throw an exception";
    
    /**
     * Test method for {@link BeamGenerator#generateBeam(Point, List, boolean)}.
     */
    @Test
    void testGenerateBeam() {
        Point origin = new Point(0, 0, 0);
        
        // Target points for testing
        List<Point> targets = new LinkedList<>();
        targets.add(new Point(1, 0, 0));  // Along X axis
        targets.add(new Point(0, 2, 0));  // Along Y axis
        targets.add(new Point(0, 0, 3));  // Along Z axis
        
        // ============ Equivalence Partitions Tests =================
        // EP01: Test beam generation from origin to target points (switchDirection = false)
        List<Ray> testBeam = BeamGenerator.generateBeam(origin, targets, false);
        
        assertEquals(3, testBeam.size(), RAY_COUNT_FAILURE);
        
        // Check first ray (0,0,0) -> (1,0,0) => Direction vector (1,0,0)
        Ray testRay = testBeam.get(0);
        assertEquals(origin, testRay.origin(), RAY_ORIGIN_FAILURE);
        assertEquals(new Vector(1, 0, 0), testRay.direction(), RAY_DIRECTION_FAILURE);
        
        // Check first ray (0,0,0) -> (1,0,0) => Direction vector (0,1,0)
        testRay = testBeam.get(1);
        assertEquals(origin, testRay.origin(), RAY_ORIGIN_FAILURE);
        assertEquals(new Vector(0, 1, 0), testRay.direction(), RAY_DIRECTION_FAILURE);
        
        // Check first ray (0,0,0) -> (1,0,0) => Direction vector (0,0,1)
        testRay = testBeam.get(2);
        assertEquals(origin, testRay.origin(), RAY_ORIGIN_FAILURE);
        assertEquals(new Vector(0, 0, 1), testRay.direction(), RAY_DIRECTION_FAILURE);
        
        // EP02: Test beam generation from target points to origin (switchDirection = true)
        testBeam = BeamGenerator.generateBeam(origin, targets, true);
        
        assertEquals(3, testBeam.size(), RAY_COUNT_FAILURE);
        
        // Check first ray (1,0,0) -> (0,0,0) => Direction vector (-1,0,0)
        testRay = testBeam.get(0);
        assertEquals(origin, testRay.origin(), RAY_ORIGIN_FAILURE);
        assertEquals(new Vector(-1, 0, 0), testRay.direction(), RAY_DIRECTION_FAILURE);
        
        // Check first ray (0,2,0) -> (0,0,0) => Direction vector (0,-1,0)
        testRay = testBeam.get(1);
        assertEquals(origin, testRay.origin(), RAY_ORIGIN_FAILURE);
        assertEquals(new Vector(0, -1, 0), testRay.direction(), RAY_DIRECTION_FAILURE);
        
        // Check first ray (0,0,3) -> (0,0,0) => Direction vector (0,0,-1)
        testRay = testBeam.get(2);
        assertEquals(origin, testRay.origin(), RAY_ORIGIN_FAILURE);
        assertEquals(new Vector(0, 0, -1), testRay.direction(), RAY_DIRECTION_FAILURE);
        
        // ============ Boundary Values Tests ========================
        // BV01: Test with an empty target points list
        List<Point> emptyTargets = new LinkedList<>();
        List<Ray> emptyBeam = BeamGenerator.generateBeam(origin, emptyTargets, false);
        assertNotNull(emptyBeam, GENERATED_BEAM_NOT_NULL_FAILURE);
        assertEquals(0, emptyBeam.size(), BEAM_EMPTY_TARGETS_FAILURE);
        
        // BV02: Test when a target point is exactly equal to the origin point (Vector(0,0,0) is illegal)
        List<Point> invalidTargets = new LinkedList<>();
        invalidTargets.add(new Point(0, 0, 0));
        
        assertThrows(IllegalArgumentException.class,
                () -> BeamGenerator.generateBeam(origin, invalidTargets, false),
                TARGET_EQUALS_ORIGIN_FAILURE);
        
        // BV03: The targets list is null
        List<Point> nullTargets = null;
        assertThrows(NullPointerException.class,
                () -> BeamGenerator.generateBeam(origin, null, false),
                NULL_TARGETS_FAILURE);
    }
}