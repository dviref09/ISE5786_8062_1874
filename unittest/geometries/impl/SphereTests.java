package geometries.impl;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test class for {@link geometries.impl.Sphere}.
 * @author Dvir Farkash
 */
public class SphereTests {
	/** Delta value for accuracy when comparing double values. */
	private final static double DELTA = 1e-6;

	private final static Sphere sphere = new Sphere(new Point(0, 0, 0), 1);
	private final static String NORMAL_FAILURE_MESSAGE = "Sphere normal is incorrect.";

	/**
	 * Test method for {@link geometries.impl.Sphere#getNormal(Point)}.
	 */
	@Test
	void testGetNormal() {
		// ============ Equivalence Partitions Tests ==============
		// EP01: A point on the surface of the sphere
		Vector result = sphere.getNormal(new Point(0, 0, 1));
		Vector expected = new Vector(0, 0, 1);
		assertEquals(expected, result, NORMAL_FAILURE_MESSAGE);
	}
}