package geometries.impl;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test class for {@link geometries.impl.Triangle}.
 * @author Dvir Farkash
 */
class TriangleTests {
	/**
	 * Delta value for accuracy when comparing double values.
	 */
	private final static double DELTA = 1e-6;

	/**
	 * Test data.
	 */
	private final static Point p1 = new Point(0, 0, 0);
	private final static Point p2 = new Point(1, 0, 0);
	private final static Point p3 = new Point(0, 1, 0);
	private final static Point testPoint = new Point(0.25, 0.25, 0);
	private final static Triangle triangle = new Triangle(p1, p2, p3);

	private final static Vector orthogonalVector1 = p1.subtract(p2);
	private final static Vector orthogonalVector2 = p1.subtract(p3);

	/**
	 * Failure messages for the tests
	 */
	private final static String LENGTH_FAILURE_MESSAGE = "The normal should be of length 1.";
	private final static String DIRECTION_FAILURE_MESSAGE = "The dot product of the normal and a vector made from 2 points in the triangle should be 0.";

	/**
	 * Test method for {@link geometries.impl.Triangle#getNormal(Point)}
	 */
	@Test
	void testGetNormal() {
		// ============ Equivalence Partitions Tests ==============
		// EP01: Getting a normal for point on the triangle
		Vector result = triangle.getNormal(testPoint);

		assertEquals(1, result.length(), DELTA, LENGTH_FAILURE_MESSAGE);
		assertEquals(0, result.dotProduct(orthogonalVector1), DELTA, DIRECTION_FAILURE_MESSAGE);
		assertEquals(0, result.dotProduct(orthogonalVector2), DELTA, DIRECTION_FAILURE_MESSAGE);
	}
}