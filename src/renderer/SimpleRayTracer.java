package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.List;

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
        List<Point> intersections = _scene.geometries.findIntersections(ray);
        Point closestIntersection = ray.findClosestPoint(intersections);
        return (closestIntersection != null ? calcColor(closestIntersection) : _scene.background);
    }

    /**
     * Private helper method for calculating the color at a single point
     * @param intersection The point to calculate the color in it
     * @return The color of the point
     */
    private Color calcColor(Point intersection) {
        return _scene.ambientLight.getIntensity();
    }
}
