package geometries.impl;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
import primitives.Ray;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit test class for {@link geometries.impl.Sphere}.
 * @author Dvir Farkash
 */
class SphereTests {
	/**
	 * Test data.
	 */
	private static final Sphere testSphere = new Sphere(new Point(1, 0, 0), 1);

	/**
	 * Failure messages for the tests.
	 */
	private static final String NORMAL_FAILURE_MESSAGE = "Sphere normal is incorrect.";
	private static final String INTERSECTION_LINE_OUTSIDE_FAILURE_MESSAGE = "Ray's line out of sphere";
	private static final String INTERSECTION_START_BEFORE_FAILURE_MESSAGE = "Wrong intersection points for the ray";
	private static final String INTERSECTION_START_INSIDE_FAILURE_MESSAGE = "Wrong intersection point for the ray";
	private static final String INTERSECTION_START_AFTER_FAILURE_MESSAGE = "Ray starts after sphere";
	private static final String INTERSECTION_START_ON_SURFACE_FAILURE_MESSAGE = "Should not count starting point";
	private static final String INTERSECTION_GOES_THROUGH_CENTER_FAILURE_MESSAGE = "Wrong intersection points for ray through center";
	private static final String INTERSECTION_TANGENT_FAILURE_MESSAGE = "Tangent ray should not have intersection points";
	private static final String INTERSECTION_ORTHOGONAL_FAILURE_MESSAGE = "Wrong intersection point for ray orthogonal to center-starting point vector";


	/**
	 * Test method for {@link geometries.impl.Sphere#getNormal(Point)}.
	 */
	@Test
	void testGetNormal() {
		// ============ Equivalence Partitions Tests ==============
		// EP01: A point on the surface of the sphere
		Vector result = testSphere.getNormal(new Point(1, 0, 1));
		Vector expected = new Vector(0, 0, 1);
		assertEquals(expected, result, NORMAL_FAILURE_MESSAGE);
	}

	/**
	 * Test method for {@link geometries.impl.Sphere#findIntersections(primitives.Ray)}.
	 */
	@Test
	void testFindIntersections() {
		// ============ Equivalence Partitions Tests =============
		// EP01: Ray's line is outside the sphere (0 points)
		Ray testRay = new Ray(new Point(-1, 0, 0), new Vector(1, 1, 0));
		assertNull(testSphere.findIntersections(testRay), INTERSECTION_LINE_OUTSIDE_FAILURE_MESSAGE);

		// EP02: Ray starts before and crosses the sphere (2 points)
		testRay = new Ray(new Point(-1, 0, 0), new Vector(3, 1, 0));
		Point p1 = new Point(0.0651530771650466, 0.355051025721682, 0);
		Point p2 = new Point(1.53484692283495, 0.844948974278318, 0);
		List<Point> expectedIntersectionList = List.of(p1, p2);
		assertEquals(expectedIntersectionList, testSphere.findIntersections(testRay), INTERSECTION_START_BEFORE_FAILURE_MESSAGE);

		// EP03: Ray starts inside the sphere (1 point)
		testRay = new Ray(new Point(0.5, 0, 0), new Vector(0.5, 0, 1));
		p1 = new Point(1, 0, 1);
		expectedIntersectionList = List.of(p1);
		assertEquals(expectedIntersectionList, testSphere.findIntersections(testRay), INTERSECTION_START_INSIDE_FAILURE_MESSAGE);

		// EP04: Ray starts after the sphere (0 points)
		testRay = new Ray(new Point(3, 0.5, 0), new Vector(1, 0, 0));
		assertNull(testSphere.findIntersections(testRay), INTERSECTION_START_AFTER_FAILURE_MESSAGE);

		// =============== Boundary Values Tests ==================
		// **** Group: Ray's line starts on the sphere's surface
		// BV01: Ray starts at sphere and goes inside (1 points)
		testRay = new Ray(new Point(2, 0, 0), new Vector(-1, -1, 0));
		p1 = new Point(1, -1, 0);
		expectedIntersectionList = List.of(p1);
		assertEquals(expectedIntersectionList, testSphere.findIntersections(testRay), INTERSECTION_START_ON_SURFACE_FAILURE_MESSAGE);

		// BV02: Ray starts at sphere and goes outside (0 points)
		testRay = new Ray(new Point(2, 0, 0), new Vector(1, 1, 0));
		assertNull(testSphere.findIntersections(testRay), INTERSECTION_START_ON_SURFACE_FAILURE_MESSAGE);

		// **** Group: Ray's line goes through the center
		// BV03: Ray starts before the sphere (2 points)
		testRay = new Ray(new Point(1, -2, 0), new Vector(0, 1, 0));
		p1 = new Point(1, -1, 0);
		p2 = new Point(1,1,0);
		expectedIntersectionList = List.of(p1, p2);
		assertEquals(expectedIntersectionList, testSphere.findIntersections(testRay), INTERSECTION_GOES_THROUGH_CENTER_FAILURE_MESSAGE);

		// BV04: Ray starts at sphere and goes inside (1 points)
		testRay = new Ray(new Point(1, -1, 0), new Vector(0, 1, 0));
		p1 = new Point(1, 1, 0);
		expectedIntersectionList = List.of(p1);
		assertEquals(expectedIntersectionList, testSphere.findIntersections(testRay), INTERSECTION_GOES_THROUGH_CENTER_FAILURE_MESSAGE);

		// BV05: Ray starts at the center (1 points)
		testRay = new Ray(new Point(1, 0, 0), new Vector(0, 1, 0));
		p1 = new Point(1, 1, 0);
		expectedIntersectionList = List.of(p1);
		assertEquals(expectedIntersectionList, testSphere.findIntersections(testRay), INTERSECTION_GOES_THROUGH_CENTER_FAILURE_MESSAGE);

		// BV06: Ray starts at sphere and back goes to center (0 points)
		testRay = new Ray(new Point(1, 1, 0), new Vector(0, 1, 0));
		assertNull(testSphere.findIntersections(testRay), INTERSECTION_GOES_THROUGH_CENTER_FAILURE_MESSAGE);

		// BV07: Ray starts inside and back goes to center (1 points)
		testRay = new Ray(new Point(1.5, 0, 0), new Vector(1, 0, 0));
		p1 = new Point(2, 0, 0);
		expectedIntersectionList = List.of(p1);
		assertEquals(expectedIntersectionList, testSphere.findIntersections(testRay), INTERSECTION_GOES_THROUGH_CENTER_FAILURE_MESSAGE);

		// BV08: Ray starts outside and back goes to center (0 points)
		testRay = new Ray(new Point(-0.5, 0, 0), new Vector(-1, 0, 0));
		assertNull(testSphere.findIntersections(testRay), INTERSECTION_GOES_THROUGH_CENTER_FAILURE_MESSAGE);

		// **** Group: Ray's line is tangent to the sphere
		// BV09: Ray starts before the tangent point (0 points)
		testRay = new Ray(new Point(0, 1, 0), new Vector(1, 0, 0));
		assertNull(testSphere.findIntersections(testRay), INTERSECTION_TANGENT_FAILURE_MESSAGE);

		// BV10: Ray starts at the tangent point (0 points)
		testRay = new Ray(new Point(1, 1, 0), new Vector(1, 0, 0));
		assertNull(testSphere.findIntersections(testRay), INTERSECTION_TANGENT_FAILURE_MESSAGE);

		// BV11: Ray starts after the tangent point (0 points)
		testRay = new Ray(new Point(2, 1, 0), new Vector(1, 0, 0));
		assertNull(testSphere.findIntersections(testRay), INTERSECTION_TANGENT_FAILURE_MESSAGE);

		// **** Group: Ray is orthogonal to the vector from center to starting point
		// BV12: Ray starts inside the sphere (1 points)
		testRay = new Ray(new Point(1.5, 0.5, 0), new Vector(0, 0, 1));
		p1 = new Point(1.5, 0.5, 0.70710678118654752);
		expectedIntersectionList = List.of(p1);
		assertEquals(expectedIntersectionList, testSphere.findIntersections(testRay), INTERSECTION_ORTHOGONAL_FAILURE_MESSAGE);

		// BV11: Ray starts after the tangent point (0 points)
		testRay = new Ray(new Point(3, 0, 0), new Vector(0, 1, 0));
		assertNull(testSphere.findIntersections(testRay), INTERSECTION_ORTHOGONAL_FAILURE_MESSAGE);
	}
}