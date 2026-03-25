package primitives;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for {@link primitives.Ray}.
 * @author Dvir Farkash
 */
class RayTests {
	/**
	 * Messages for assertion failures.
	 */
	final static String CONSTRUCTOR_TEST_ORIGIN_FAILURE_MESSAGE = "Constructor didn't set the origin properly";
	final static String CONSTRUCTOR_TEST_DIRECTION_FAILURE_MESSAGE = "Constructor didn't set the direction properly";

	/**
	 * Test method for {@link primitives.Ray#Ray(primitives.Point, primitives.Vector)}.
	 */
	@Test
	void testConstructor() {
		// ============ Equivalence Partitions Tests ==============
		// EP01: Constructing a ray with a valid origin and direction
		Point origin = new Point(1, 2, 3);
		Vector direction = new Vector(0, 4, 0);
		Ray testRay = new Ray(origin, direction);
		Vector expectedDirection = new Vector(0, 1, 0); // The direction should be normalized

		assertEquals(origin, testRay.origin(), CONSTRUCTOR_TEST_ORIGIN_FAILURE_MESSAGE);
		assertEquals(expectedDirection, testRay.direction(), CONSTRUCTOR_TEST_DIRECTION_FAILURE_MESSAGE);

	}
}
