package geometries.impl;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test class for {@link geometries.impl.AABB} class.
 * @author Dvir Farkash
 */
public class AABBTests {
    /**
     * Test data
     */
    private static final AABB testAABB = new AABB(new Point(-1, -1, -1), new Point(1, 1, 1));
    
    /**
     * Failure messages
     */
    private static final String FAIL_MSG_INTERSECTS = "Ray should intersect the AABB";
    private static final String FAIL_MSG_NO_INTERSECT = "Ray should not intersect the AABB";
    
    /**
     * Test method for {@link AABB#isIntersects(primitives.Ray)}
     */
    @Test
    void testIsIntersects() {
        // ============ Equivalence Partitions Tests ==============
        // EP01: general ray intersects the AABB
        Ray testRay = new Ray(new Point(-5, -2, -0.5), new Vector(1.25, 0.5, 0.25));
        assertTrue(testAABB.isIntersects(testRay), FAIL_MSG_INTERSECTS);
        
        // EP02: general ray doesn't intersect the AABB
        testRay = new Ray(new Point(-5, -2, -0.5), new Vector(-1.25, 0.5, 0.25));
        assertFalse(testAABB.isIntersects(testRay), FAIL_MSG_NO_INTERSECT);
        
        // EP03: The ray intersects the AABB from behind (the intersection is behind the starting point of the ray)
        testRay = new Ray(new Point(1.5, 0, 0), new Vector(1, 1, 0.5));
        assertFalse(testAABB.isIntersects(testRay), FAIL_MSG_NO_INTERSECT);
        
        // =============== Boundary Values Tests ==================
        // BV01: ray starts inside the AABB
        testRay = new Ray(new Point(0, 0, 0), new Vector(1, 1, 1));
        assertTrue(testAABB.isIntersects(testRay), FAIL_MSG_INTERSECTS);
        
        // BV02: ray starts on the surface of the AABB pointing inwards
        testRay = new Ray(new Point(1, 0, 0), new Vector(-1, -1, 0.5));
        assertTrue(testAABB.isIntersects(testRay), FAIL_MSG_INTERSECTS);
        
        // BV03: ray starts on the surface of the AABB pointing outwards
        testRay = new Ray(new Point(1, 0, 0), new Vector(1, 1, 0.5));
        assertFalse(testAABB.isIntersects(testRay), FAIL_MSG_NO_INTERSECT);
        
        // BV04: Ray is parallel to one of the axes and intersects the AABB
        testRay = new Ray(new Point(2, 0.5, -0.25), new Vector(-1, 0, 0));
        assertTrue(testAABB.isIntersects(testRay), FAIL_MSG_INTERSECTS);
        
        // BV05: Ray is parallel to one of the axes and doesn't intersect the AABB
        testRay = new Ray(new Point(2, 0.5, -0.25), new Vector(1, 0, 0));
        assertFalse(testAABB.isIntersects(testRay), FAIL_MSG_NO_INTERSECT);
        
        // BV06: The Ray intersects the AABB in the edge (doesn't count as intersection)
        testRay = new Ray(new Point(2, 0, 0), new Vector(-1, -1, 0.25));
        assertFalse(testAABB.isIntersects(testRay), FAIL_MSG_NO_INTERSECT);
        
        // BV07: The ray intersects the AABB in the vertex
        testRay = new Ray(new Point(2, 0, 0), new Vector(1, 1, 1));
        assertFalse(testAABB.isIntersects(testRay), FAIL_MSG_NO_INTERSECT);
        
        // BV08: Ray starts on the edge of the AABB pointing inward
        testRay = new Ray(new Point(1, 1, 0), new Vector(-1, -1, 1));
        assertTrue(testAABB.isIntersects(testRay), FAIL_MSG_INTERSECTS);
        
        // BV09: Ray starts on the edge of the AABB pointing outward
        testRay = new Ray(new Point(1, 1, 0), new Vector(1, 1, 1));
        assertFalse(testAABB.isIntersects(testRay), FAIL_MSG_NO_INTERSECT);
        
        // BV10: Ray starts on the vertex and pointing inward
        testRay = new Ray(new Point(1, 1, -1), new Vector(-1, -0.5, 0.5));
        assertTrue(testAABB.isIntersects(testRay), FAIL_MSG_INTERSECTS);
        
        // BV11: Ray starts on the vertex and pointing outward
        testRay = new Ray(new Point(1, 1, -1), new Vector(1, 0.5, -0.5));
        assertFalse(testAABB.isIntersects(testRay), FAIL_MSG_NO_INTERSECT);
    }
}
