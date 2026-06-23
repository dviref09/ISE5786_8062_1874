package renderer;

import lighting.LightSource;
import primitives.Color;
import primitives.Ray;
import primitives.Vector;
import sampling.SamplerType;
import scene.Scene;

import static geometries.api.Intersectable.Intersection;
import static primitives.Util.alignZero;

/**
 * An abstract class for representing the ray tracer of the scene
 * @author Amichai Feiglson
 */
abstract class RayTracerBase {
    /**
     * The scene the ray tracer works on
     */
    protected final Scene _scene;
    
    /**
     * The amount of rays per side in soft shadows
     * If it one or less than soft shadows is disabled
     */
    protected int _softShadowNumRays = 1;
    /**
     * Sampling pattern for soft shadows
     */
    protected SamplerType _softShadowSampler = SamplerType.JITTERED;
    
    /**
     * Constructor
     * @param scene The scene the ray tracer works on
     */
    RayTracerBase(Scene scene) {
        _scene = scene;
    }
    
    /**
     * Setter for the number of rays in soft shadows
     * @param numRays The new value for the num or rays per side
     * @return The current RayTracerBase object for chaining setters
     */
    public RayTracerBase setSoftShadowNumRays(int numRays) {
        _softShadowNumRays = numRays;
        return this;
    }
    
    /**
     * Setter for the sampler of soft shadows
     * @param sampler The new sampler for soft shadows
     * @return The current RayTracerBase object for chaining setters
     */
    public RayTracerBase setSoftShadowSampler(SamplerType sampler) {
        _softShadowSampler = sampler;
        return this;
    }
    
    
    
    /**
     * Method for tracing a ray and determining the color of the ray
     * @param ray The ray to trace
     * @return The calculated color of the ray
     */
    abstract Color traceRay(Ray ray);
    
    /**
     * Private helper method for calculating reflected vector from intersection surface of a vector
     * @param intersection The intersection surface the vector is reflected from
     * @param original The original vector
     * @param vNormal the dotProduct between the vector and the intersection normal
     * (here for optimization reasons, because it is already being calculated in the intersection, we just don't know
     * if original is light vector of intersecting ray vector)
     * @return the reflected vector
     * @throws IllegalArgumentException if the vNormal is zero
     */
    private Vector reflectedVector(Intersection intersection, Vector original, double vNormal) {
        return original.subtract(intersection.normal.scale(2 * vNormal));
    }
    
    /**
     * Processes the geometrical data for the Phong reflection model and saves it in the intersection for caching
     * @param intersection The intersection to save tha data in
     * @param v The vector from the camera to the intersection
     * @return True if the v is not orthogonal to the normal at the intersection else returns false
     */
    protected boolean preprocessIntersection(Intersection intersection, Vector v) {
        intersection.v = v;
        intersection.normal = intersection.geometry.getNormal(intersection.point);
        intersection.vNormal = alignZero(intersection.v.dotProduct(intersection.normal));
        intersection.r = reflectedVector(intersection, v, intersection.vNormal);
        return intersection.vNormal != 0;
    }
    
    /**
     * Processes the light source data for the Phong reflection model and saves it in the intersection for caching
     * @param intersection The intersection to save tha data in
     * @param light The light source for the data processing
     * @return True if the vector from the light and the vector from the camera is in the same side of the object
     */
    protected boolean preprocessLightSource(Intersection intersection, LightSource light) {
        intersection.light = light;
        intersection.l = light.getL(intersection.point);
        intersection.lNormal = alignZero(intersection.l.dotProduct(intersection.normal));
        if (intersection.lNormal == 0) {
            return false;
        } else {
            // the condition is to prevent generating zero vector inside here
            intersection.rLight = reflectedVector(intersection, intersection.l, intersection.lNormal);
        }
        return intersection.lNormal * intersection.vNormal > 0;
    }
}
