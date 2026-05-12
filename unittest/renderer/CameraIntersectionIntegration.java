package renderer;

import geometries.api.Intersectable;
import geometries.impl.Plane;
import geometries.impl.Sphere;
import geometries.impl.Triangle;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * A class for integration testing between the camera class and the geometries.
 * @author Amichai Feigelson
 */
class CameraIntersectionIntegration {
	/**
	 * Camera data
	 */
	private final static Point LOCATION = Point.ZERO;
	private final static Vector V_TO = new Vector(-1, 0, 0);
	private final static Vector V_UP = new Vector(0, 0, 1);
	private final static double DISTANCE = 1d;
	private final static double WIDTH = 3d;
	private final static double HEIGHT = 3d;
	private final static int NX = 3;
	private final static int NY = 3;

	private final static Camera testCamera= Camera.getBuilder()
			.setLocation(LOCATION)
			.setDirection(V_TO, V_UP)
			.setVpDistance(DISTANCE)
			.setVpSize(WIDTH, HEIGHT)
			.setResolution(NX, NY)
			.build();

	/**
	 * A helper method for checking that the number of intersections between the rays constructed by the camera and the given geometry is as expected.
	 * @param camera The camera constructing the rays.
	 * @param intersectable The geometric body getting intersected by the rays.
	 * @param expectedCount Number of expected intersections.
	 * @param testName The name of the test.
	 */
	private void assertIntersectionsCount(Camera camera, Intersectable intersectable, int expectedCount, String testName) {
		int totalIntersections = 0;

		// Iterate through all pixels in a 3x3 resolution
		for (int i = 0; i < NY; ++i) {
			for (int j = 0; j < NX; ++j) {
				var ray = camera.constructRay(j, i);
				var intersections = intersectable.findIntersections(ray);
				if (intersections != null) {
					totalIntersections += intersections.size();
				}
			}
		}

		assertEquals(expectedCount, totalIntersections, "Failed integration test: " + testName);
	}

	/**
	 * Integration tests for Camera with Sphere.
	 */
	@Test
	void testCameraRaySphereIntegration() {
		// ============ Equivalence Partitions Tests ==============

		// EP01: Sphere r=1 (2 intersections)
		Sphere testSphere = new Sphere(new Point(-3, 0, 0), 1);
		assertIntersectionsCount(testCamera, testSphere, 2, "Sphere 2 intersections");

		// EP02: Sphere r=2.5 (18 intersections)
		testSphere = new Sphere(new Point(-3, 0, 0), 2.5);
		assertIntersectionsCount(testCamera, testSphere, 18, "Sphere 18 intersections");

		// EP03: Sphere r=2 (10 intersections)
		testSphere = new Sphere(new Point(-2.5, 0, 0), 2);
		assertIntersectionsCount(testCamera, testSphere, 10, "Sphere 10 intersections");

		// EP04: Camera inside Sphere r=4 (9 intersections)
		testSphere = new Sphere(new Point(-1, 0, 0), 4);
		assertIntersectionsCount(testCamera, testSphere, 9, "Sphere 9 intersections");

		// EP05: Sphere behind Camera (0 intersections)
		testSphere = new Sphere(new Point(2, 0, 0), 0.5);
		assertIntersectionsCount(testCamera, testSphere, 0, "Sphere behind camera (0 intersections)");

		// =============== Boundary Values Tests ==================

		// BV01: Camera touches sphere, rays towards sphere (9 intersections).
		testSphere = new Sphere(new Point(-10, 0, 0), 10);
		assertIntersectionsCount(testCamera, testSphere, 9, "Sphere touches camera (9 intersections)");

		// BV02: Camera touches sphere from behind (0 intersections).
		testSphere = new Sphere(new Point(10, 0, 0), 10);
		assertIntersectionsCount(testCamera, testSphere, 0, "Sphere touches camera from behind (0 intersections)");
	}

	/**
	 * Integration tests for Camera with Plane.
	 */
	@Test
	void testCameraRayPlaneIntegration() {
		// ============ Equivalence Partitions Tests ==============

		// EP01: Plane parallel to View Plane (9 intersections)
		Plane testPlane = new Plane(new Point(-2, 0, 0), new Vector(1, 0, 0));
		assertIntersectionsCount(testCamera, testPlane, 9, "Plane is orthogonal to vTo (9 intersections)");

		// EP02: Plane tilted (9 intersections)
		testPlane = new Plane(new Point(-1.5, 0, 0), new Vector(1, 0, -0.5));
		assertIntersectionsCount(testCamera, testPlane, 9, "Plane slightly titled towards the camera (9 intersections)");

		// EP03: Plane tilted, misses some pixels (6 intersections)
		testPlane = new Plane(new Point(-5, 0, 0), new Vector(1, 0, 1));
		assertIntersectionsCount(testCamera, testPlane, 6, "Plane titled away from the camera, missing some pixels (9 intersections)");

		// EP04: Plane is behind the camera (0 intersections)
		testPlane = new Plane(new Point(1, 0, 0), new Vector(1, 0, 0));
		assertIntersectionsCount(testCamera, testPlane, 0, "Plane is behind the camera (0 intersections)");

		// =============== Boundary Values Tests ==================

		// BV01: The plane is exactly on the camera (0 intersections).
		testPlane = new Plane(LOCATION, V_TO);
		assertIntersectionsCount(testCamera, testPlane, 0, "Plane is exactly on the camera (0 intersections)");
	}

	/**
	 * Integration tests for Camera with Triangle.
	 */
	@Test
	void testCameraRayTriangleIntegration() {
		// EP01: Small triangle (1 intersection)
		Triangle testTriangle = new Triangle(new Point(-2, 0, 1), new Point(-2, 1, -1), new Point(-2, -1, -1));
		assertIntersectionsCount(testCamera, testTriangle, 1, "Small triangle (1 intersection)");

		// EP02: Tall triangle (2 intersections)
		testTriangle = new Triangle(new Point(-2, 0, 20), new Point(-2, 1, -1), new Point(-2, -1, -1));
		assertIntersectionsCount(testCamera, testTriangle, 2, "Tall triangle (2 intersections)");

		// EP03: Large triangle (9 intersections)
		testTriangle = new Triangle(new Point(-2, 0, 20), new Point(-2, -20, -20), new Point(-2, 20, -20));
		assertIntersectionsCount(testCamera, testTriangle, 9, "Large triangle (9 intersections)");

		// EP04: Triangle behind camera (0 intersections)
		testTriangle = new Triangle(new Point(2, 0, 1), new Point(2, 1, -1), new Point(2, -1, -1));
		assertIntersectionsCount(testCamera, testTriangle, 0, "Triangle behind camera (9 intersections)");

		// =============== Boundary Values Tests ==================

		// BV01: Triangle is exactly on the camera (0 intersections).
		testTriangle = new Triangle(new Point(0, 0, 1), new Point(0, 1, -1), new Point(0, -1, -1));
		assertIntersectionsCount(testCamera, testTriangle, 0, "Triangle is exactly on the camera (0 intersections)");
	}
}
