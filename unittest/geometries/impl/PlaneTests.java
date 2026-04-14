package geometries.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for {@link geometries.impl.Plane}.
 * @author Amichai Feigelson
 */

public class PlaneTests {
	/**
	 * Delta value for accuracy when comparing double values.
	 */
	private final static double DELTA = 1e-6;

	private final static Plane plane1 = new Plane(new Point(0, 0, 1), new Vector(9, 0, 0));
	private final static Plane plane2 = new Plane(new Point(0, 0, 1), new Point(0, 10, 8), new Point(0, 9, 2));
	private final static Vector orthogonalVector1 = new Vector(0, 9, 5);
	private final static Vector orthogonalVector2 = new Vector(0, 37, 19);

	private final static String GETNORMAL_LENGTH_EP_FAILURE_MESSAGE = "The normal should be of length 1.";
	private final static String GETNORMAL_DIRECTION_EP_FAILURE_MESSAGE = "The dot product of the normal and a vector made from 2 points in the plane should be 0.";

	/**
	 * Test method for {@link geometries.impl.Plane#getNormal(Point point)}
	 */
	@Test
	void testGetNormal() {
		// ============ Equivalence Partitions Tests ==============
		// EP01: Getting a normal for point on the plane
		Vector result = plane1.getNormal(new Point(0, 2, 1));
		assertEquals(1, result.length(), DELTA, GETNORMAL_LENGTH_EP_FAILURE_MESSAGE);
		assertEquals(0, result.dotProduct(orthogonalVector1), DELTA, GETNORMAL_DIRECTION_EP_FAILURE_MESSAGE);
		assertEquals(0, result.dotProduct(orthogonalVector2), DELTA, GETNORMAL_DIRECTION_EP_FAILURE_MESSAGE);

		// =============== Boundary Values Tests ==================
		// BV01: Getting a normal for the point used in the plane constructor
		result = plane1.getNormal(new Point(0, 0, 1));
		assertEquals(1, result.length(), DELTA, GETNORMAL_LENGTH_EP_FAILURE_MESSAGE);
		assertEquals(0, result.dotProduct(orthogonalVector1), DELTA, GETNORMAL_DIRECTION_EP_FAILURE_MESSAGE);
		assertEquals(0, result.dotProduct(orthogonalVector2), DELTA, GETNORMAL_DIRECTION_EP_FAILURE_MESSAGE);
	}

	/**
	 * Test method for {@link geometries.impl.Plane#Plane(Point, Vector)}
	 */
	@Test
	void testConstructorNormal() {
		// ============ Equivalence Partitions Tests ==============
		// EP01: Getting a normal for point on the plane

	}

	/**
	 * Test method for {@link geometries.impl.Plane#Plane(Point, Point, Point)}
	 */
	@Test
	void testConstructorPoints() {

	}
}