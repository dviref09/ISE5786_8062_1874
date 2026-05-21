package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

import java.util.List;

import static geometries.api.Intersectable.Intersection;

class SimpleRayTracer extends RayTracerBase {
	/**
	 * Constructor
	 * @param scene The scene the ray tracer works on
	 */
	SimpleRayTracer(Scene scene) {
		super(scene);
	}

	@Override
	Color traceRay(Ray ray) {
		List<Intersection> intersections = _scene.geometries.calcIntersections(ray);
		Intersection closestIntersection = ray.findClosestIntersection(intersections);
		return (closestIntersection != null ? calcColor(closestIntersection) : _scene.background);
	}

	/**
	 * Private helper method for calculating the color at a single point
	 * @param intersection The point to calculate the color in it
	 * @return The color of the point
	 */
	private Color calcColor(Intersection intersection) {
		return _scene.ambientLight.getIntensity().scale(intersection.material.kA)
				.add(intersection.geometry.getEmission());
	}
}
