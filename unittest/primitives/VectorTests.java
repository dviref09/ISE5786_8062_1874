package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for {@link primitives.Vector}.
 * @author Dvir Farkash
 */
public class VectorTests {
	/**
	 * Delta value for accuracy when comparing double values.
	 */
	private final static double DELTA = 1e-6;

	/**
	 * General vector for testing.
	 */
	private final static Vector vector1 = new Vector(1, 2, 3);
	private final static Vector vector1Opp = new Vector(-1, -2, -3);
	private final static Vector vector2 = new Vector(7, 8, 7);
	private final static Vector orthogonalVector1 = new Vector(-2, 1, 0);

	/**
	 * Messages for assertion failures.
	 */
	private final static String CONSTRUCTOR_FAILURE_MESSAGE = "Constructor should throw an exception when trying to create zero vector.";
	private final static String ADDITION_EP_FAILURE_MESSAGE = "Adding two vectors did not produce the expected result.";
	private final static String ADDITION_BVA_FAILURE_MESSAGE = "Adding the opposite vector should throw an exception";
	private final static String SUBTRACTION_EP_FAILURE_MESSAGE = "Subtracting two vectors did not produce the expected result.";
	private final static String SUBTRACTION_BVA_FAILURE_MESSAGE = "Subtracting a vector from itself should throw an exception.";
	private final static String SCALING_EP_FAILURE_MESSAGE = "Scaling a vector by a scalar did not produce the expected result.";
	private final static String SCALING_BVA_FAILURE_MESSAGE = "Scaling a vector by zero should throw an exception.";
	private final static String DOT_PRODUCT_EP_FAILURE_MESSAGE = "Dot product of two vectors did not produce the expected result.";
	private final static String DOT_PRODUCT_BVA1_FAILURE_MESSAGE = "Dot product of orthogonal vectors should be zero.";
	private final static String DOT_PRODUCT_BVA2_FAILURE_MESSAGE = "Dot product of a vector with itself should return the square of its length.";
	private final static String CROSS_PRODUCT_EP_FAILURE_MESSAGE = "Cross product of two vectors did not produce the expected result.";
	private final static String CROSS_PRODUCT_BVA_FAILURE_MESSAGE = "Cross product of parallel vectors should throw an exception.";
	private final static String LENGTH_FAILURE_MESSAGE = "Length of the vector did not produce the expected result.";
	private final static String LENGTH_SQUARED_FAILURE_MESSAGE = "Length squared of the vector did not produce the expected result.";
	private final static String NORMALIZATION_LENGTH_FAILURE_MESSAGE = "Normalizing a vector did not produce a vector with length 1.";
	private final static String NORMALIZATION_DIRECTION_FAILURE_MESSAGE = "Normalizing a vector did not produce a vector in the same direction.";
	private final static String NORMALIZATION_OPPOSITE_DIRECTION_FAILURE_MESSAGE = "Normalizing a vector produced a vector in the opposite direction.";

	/**
	 * Test method for {@link primitives.Vector#Vector(double, double, double)}
	 */
	@Test
	void testConstructorDouble() {
		// =============== Boundary Values Tests ==================
		// BV01: three zero values at the parameters
		assertThrows(IllegalArgumentException.class, () -> new Vector(0, 0, 0), CONSTRUCTOR_FAILURE_MESSAGE);
	}

	/**
	 * Test method for {@link primitives.Vector#Vector(Double3)}
	 */
	@Test
	void testConstructorDouble3() {
		// =============== Boundary Values Tests ==================
		// BV01: three zero values in the Double3 parameter
		assertThrows(IllegalArgumentException.class, () -> new Vector(new Double3(0, 0, 0)), CONSTRUCTOR_FAILURE_MESSAGE);
	}

	/**
	 * Test method for {@link primitives.Vector#add(Vector)}
	 */
	@Test
	void testAdd() {
		// ============ Equivalence Partitions Tests ==============
		// EP01: Adding two vectors
		Vector result = vector1.add(vector2);
		Vector expected = new Vector(8, 10, 10);
		assertEquals(expected, result, ADDITION_EP_FAILURE_MESSAGE);

		// =============== Boundary Values Tests ==================
		// BV01: Adding a vector to its opposite
		assertThrows(IllegalArgumentException.class, () -> vector1.add(vector1Opp), ADDITION_BVA_FAILURE_MESSAGE);
	}

	/**
	 * Test method for {@link primitives.Vector#subtract(Vector)}
	 */
	@Test
	void testSubtract() {
		// ============ Equivalence Partitions Tests ==============
		// EP01: Subtracting two vectors
		Vector result = vector1.subtract(vector2);
		Vector expected = new Vector(-6, -6, -4);
		assertEquals(expected, result, SUBTRACTION_EP_FAILURE_MESSAGE);

		// =============== Boundary Values Tests ==================
		// BV01: Subtracting a vector from itself
		assertThrows(IllegalArgumentException.class, () -> vector1.subtract(vector1), SUBTRACTION_BVA_FAILURE_MESSAGE);
	}

	/**
	 * Test method for {@link primitives.Vector#scale(double)}
	 */
	@Test
	void testScale() {
		// ============ Equivalence Partitions Tests ==============
		// EP01: Scaling a vector by a scalar
		Vector result = vector1.scale(2);
		Vector expected = new Vector(2, 4, 6);
		assertEquals(expected, result, SCALING_EP_FAILURE_MESSAGE);

		// =============== Boundary Values Tests ==================
		// BV01: Scaling a vector by zero
		assertThrows(IllegalArgumentException.class, () -> vector1.scale(0), SCALING_BVA_FAILURE_MESSAGE);
	}

	/**
	 * Test method for {@link primitives.Vector#dotProduct(Vector)}
	 */
	@Test
	void testDotProduct() {
		// ============ Equivalence Partitions Tests ==============
		// EP01: Dot product of two vectors
		double result = vector1.dotProduct(vector2);
		double expected = 44;
		assertEquals(expected, result, DELTA, DOT_PRODUCT_EP_FAILURE_MESSAGE);

		// =============== Boundary Values Tests ==================
		// BV01: Dot product of orthogonal vectors should be zero
		result = vector1.dotProduct(orthogonalVector1);
		expected = 0;
		assertEquals(expected, result, DELTA, DOT_PRODUCT_BVA1_FAILURE_MESSAGE);

		// BV02: Dot product of a vector with itself
		result = vector1.dotProduct(vector1);
		expected = 14;
		assertEquals(expected, result, DELTA, DOT_PRODUCT_BVA2_FAILURE_MESSAGE);
	}

	/**
	 * Test method for {@link primitives.Vector#crossProduct(Vector)}
	 */
	@Test
	void testCrossProduct() {
		// ============ Equivalence Partitions Tests ==============
		// EP01: Cross product of two vectors
		Vector result = vector1.crossProduct(vector2);
		Vector expected = new Vector(-10, 14, -6);
		assertEquals(expected, result, CROSS_PRODUCT_EP_FAILURE_MESSAGE);

		// =============== Boundary Values Tests ==================
		// BV01: Cross product of parallel vectors
		assertThrows(IllegalArgumentException.class, () -> vector1.crossProduct(vector1Opp), CROSS_PRODUCT_BVA_FAILURE_MESSAGE);
	}

	/**
	 * Test method for {@link primitives.Vector#length()}
	 */
	@Test
	void testLength() {
		// ============ Equivalence Partitions Tests ==============
		// EP01: Length of a vector
		double result = vector1.length();
		double expected = Math.sqrt(14);
		assertEquals(expected, result, DELTA, LENGTH_FAILURE_MESSAGE);
	}

	/**
	 * Test method for {@link primitives.Vector#lengthSquared()}
	 */
	@Test
	void testLengthSquared() {
		// ============ Equivalence Partitions Tests ==============
		// EP01: Length squared of a vector
		double result = vector1.lengthSquared();
		double expected = 14;
		assertEquals(expected, result, DELTA, LENGTH_SQUARED_FAILURE_MESSAGE);
	}

	/**
	 * Test method for {@link primitives.Vector#normalize()}
	 */
	@Test
	void testNormalize() {
		// ============ Equivalence Partitions Tests ==============
		// EP01: Normalizing a vector
		Vector result = vector1.normalize();

		// length test
		double length = result.length();
		double expectedLength = 1;
		assertEquals(expectedLength, length, DELTA, NORMALIZATION_LENGTH_FAILURE_MESSAGE);

		// direction test
		// checking if the normalized vector is in the same direction as the original vector by checking if their cross product
		// is a zero vector and their dot product is positive
		assertThrows(IllegalArgumentException.class, () -> vector1.crossProduct(result), NORMALIZATION_DIRECTION_FAILURE_MESSAGE);
		assertTrue(vector1.dotProduct(result) > 0, NORMALIZATION_OPPOSITE_DIRECTION_FAILURE_MESSAGE);
	}
}
