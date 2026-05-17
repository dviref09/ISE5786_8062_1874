package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit test class for {@link primitives.Point}.
 * @author Dvir Farkash
 */
class PointTests {
	/**
	 * Delta value for accuracy when comparing double values.
	 */
	private static final double DELTA = 1e-6;

	/**
	 * General point for testing.
	 */
	private static final Point testPoint = new Point(1, 1, 1);

	/**
	 * Point used for distance tests.
	 */
	private static final Point distancePoint = new Point(3, 3, 2);

	/**
	 * Messages for assertion failures.
	 */
	static final String ADDITION_FAILURE_MESSAGE = "Adding a vector to a point did not return the expected new point.";
	static final String SUBTRACTION_EP_FAILURE_MESSAGE = "Subtracting a point from another point did not return the expected vector.";
	static final String SUBTRACTION_BV_FAILURE_MESSAGE = "Subtracting a point from itself did not throw the expected exception.";
	static final String DISTANCE_SQUARED_EP_FAILURE_MESSAGE = "Distance squared between two different points did not return the expected value.";
	static final String DISTANCE_SQUARED_BV_FAILURE_MESSAGE = "Distance squared between a point and itself did not return 0.";
	static final String DISTANCE_EP_FAILURE_MESSAGE = "Distance between two different points did not return the expected value.";
	static final String DISTANCE_BV_FAILURE_MESSAGE = "Distance between a point and itself did not return 0.";

	/**
	 * Test method for {@link primitives.Point#add(primitives.Vector)}.
	 */
	@Test
	void testAdd() {
		// ============ Equivalence Partitions Tests ==============
		// EP01: Adding a vector to a point should return the correct new point
		Vector addVector = new Vector(1, 2, 3);
		Point expectedPoint = new Point(2, 3, 4);

		assertEquals(expectedPoint, testPoint.add(addVector), ADDITION_FAILURE_MESSAGE);
	}

	/**
	 * Test method for {@link primitives.Point#subtract(primitives.Point)}.
	 */
	@Test
	void testSubtract() {
		// ============ Equivalence Partitions Tests ==============
		// EP01: Subtracting a different point should return the correct vector
		Point subtractPoint = new Point(2, 3, 4);
		Vector expectedVector = new Vector(-1, -2, -3);

		assertEquals(expectedVector, testPoint.subtract(subtractPoint), SUBTRACTION_EP_FAILURE_MESSAGE);

		// =============== Boundary Values Tests ==================
		// BV01: Subtracting a point from itself should throw an IllegalArgumentException
		assertThrows(IllegalArgumentException.class, () -> testPoint.subtract(testPoint), SUBTRACTION_BV_FAILURE_MESSAGE);
	}

	/**
	 * Test method for {@link primitives.Point#distanceSquared(primitives.Point)}
	 */
	@Test
	void testDistanceSquared() {
		// ============ Equivalence Partitions Tests ==============
		// EP01: Distance squared between two different points should return the correct value
		double expectedDistanceSquared = 9;

		assertEquals(expectedDistanceSquared, testPoint.distanceSquared(distancePoint), DELTA, DISTANCE_SQUARED_EP_FAILURE_MESSAGE);

		// =============== Boundary Values Tests ==================
		// BV01: Distance squared between a point and itself
		expectedDistanceSquared = 0;

		assertEquals(expectedDistanceSquared, testPoint.distanceSquared(testPoint), DELTA, DISTANCE_SQUARED_BV_FAILURE_MESSAGE);
	}

	/**
	 * Test method for {@link primitives.Point#distance(primitives.Point)}
	 */
	@Test
	void testDistance() {
		// ============ Equivalence Partitions Tests ==============
		// EP01: Distance between two different points should return the correct value
		double expectedDistance = 3;

		assertEquals(expectedDistance, testPoint.distance(distancePoint), DELTA, DISTANCE_EP_FAILURE_MESSAGE);

		// =============== Boundary Values Tests ==================
		// BV01: Distance between a point and itself should return 0
		expectedDistance = 0;

		assertEquals(expectedDistance, testPoint.distance(testPoint), DELTA, DISTANCE_BV_FAILURE_MESSAGE);
	}
}
