package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

/**
 * An abstract class for representing the ray tracer of the scene
 */
abstract class RayTracerBase {
    /**
     * The scene the ray tracer works on
     */
    protected final Scene _scene;

    /**
     * Constructor
     * @param scene The scene the ray tracer works on
     */
    RayTracerBase(Scene scene) {
        _scene = scene;
    }

    /**
     * Method for tracing a ray and determining the color of the ray
     * @param ray The ray to trace
     * @return The calculated color of the ray
     */
    abstract Color traceRay(Ray ray);
}
