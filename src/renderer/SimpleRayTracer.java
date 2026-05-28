package renderer;

import java.util.List;

import lighting.LightSource;
import primitives.Color;
import primitives.Double3;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import static geometries.api.Intersectable.Intersection;
import static primitives.Util.powerInt;

class SimpleRayTracer extends RayTracerBase {
    /**
     * A shift constant for shading checks
     */
    private static final double DELTA = 0.1;
    
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
        return closestIntersection != null ? calcColor(closestIntersection, ray.direction()) : _scene.background;
    }
    
    /**
     * Checks whether an intersection is shaded from the current light source
     * @param intersection The intersection the check is being performed on
     * @return True if the intersection is not shaded
     */
    private boolean unshaded(Intersection intersection) {
        Vector pointToLight = intersection.l.scale(-1);
        Vector delta = intersection.normal.scale(intersection.vNormal < 0 ? DELTA : -DELTA);
        Ray shadowRay = new Ray(intersection.point.add(delta), pointToLight);
        double lightDistance = intersection.light.getDistance(intersection.point);
        return _scene.geometries.calcIntersections(shadowRay, lightDistance) == null;
    }
    
    /**
     * Private helper method for calculating the color at a single point
     * @param intersection The point to calculate the color in it
     * @return The color of the point
     */
    private Color calcColor(Intersection intersection, Vector v) {
        return !preprocessIntersection(intersection, v) ? _scene.background :
                _scene.ambientLight.getIntensity().scale(intersection.material.kA).add(calcLocalEffects(intersection));
    }
    
    /**
     * Sums the effect of all the light sources on the intersection with phong reflection model
     * @param intersection The intersection the effects are calculated on
     * @return The sum of all light sources colors
     */
    private Color calcLocalEffects(Intersection intersection) {
        Color color = intersection.geometry.getEmission();
        for (LightSource lightSource : _scene.lights) {
            if (preprocessLightSource(intersection, lightSource) && unshaded(intersection)) {
                color = color.add(lightSource.getIntensity(intersection.point).scale(calcDiffuse(intersection).add(calcSpecular(intersection))));
            }
        }
        return color;
    }
    
    /**
     * Calculates the diffusive part in the phong reflection model on the intersection from the current light source
     * @param intersection The intersection the effects are calculated on
     * @return The total of the diffusive part from all light sources
     */
    private Double3 calcDiffuse(Intersection intersection) {
        return intersection.material.kD.scale(Math.abs(intersection.lNormal));
    }
    
    /**
     * Calculates the specular part in the phong reflection model on the intersection from the current light source
     * @param intersection The intersection the effects are calculated on
     * @return The total of the specular part from all light sources
     */
    private Double3 calcSpecular(Intersection intersection) {
        return intersection.material.kS.scale(powerInt(Math.max(0,
                intersection.r.dotProduct(intersection.v.scale(-1))), intersection.material.nShininess));
    }
}
