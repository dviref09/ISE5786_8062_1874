package renderer;

import java.util.List;

import lighting.LightSource;
import lighting.PointLight;
import primitives.Color;
import primitives.Double3;
import primitives.Ray;
import primitives.Vector;
import sampling.Blackboard;
import scene.Scene;

import static geometries.api.Intersectable.Intersection;
import static primitives.Util.powerInt;

final class SimpleRayTracer extends RayTracerBase {
    /**
     * The maximum level of recursion in the calculation of global effects
     * (the level is how many recursive calls left)
     */
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    /**
     * The minimum level of effect on the scene to be counted:
     * stops the calculation of global effects if the effect is less than this
     */
    private static final double MIN_CALC_COLOR_K = 0.001;
    /**
     * The initial amount of influence on the global effect at the first level of calculation
     */
    private static final Double3 INITIAL_K = Double3.ONE;
    
    /**
     * /**
     * Constructor
     * @param scene The scene the ray tracer works on
     */
    SimpleRayTracer(Scene scene) {
        super(scene);
    }
    
    @Override
    Color traceRay(Ray ray) {
        Intersection closestIntersection = findClosestIntersection(ray);
        return closestIntersection != null ?
                calcColor(closestIntersection, ray.direction())
                : _scene.background;
    }
    
    /**
     * Checks whether an intersection is shaded from the current light source
     * @param intersection The intersection the check is being performed on
     * @return True if the intersection is not shaded
     */
    private boolean unshaded(Intersection intersection) {
        Vector pointToLight = intersection.l.scale(-1);
        Ray shadowRay = new Ray(intersection.point, pointToLight, intersection.normal);
        double lightDistance = intersection.light.getDistance(intersection.point);
        List<Intersection> shadowIntersections =
                _scene.geometries.calcIntersections(shadowRay, lightDistance);
        if (shadowIntersections == null) {
            return true;
        }
        for (Intersection shadowIntersection : shadowIntersections) {
            if (shadowIntersection.material.kT.isLowerThan(MIN_CALC_COLOR_K)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Checks the blocking bodies between the intersection and the light source and calculates the amount of
     * influence the light have on the intersection (depends on the transparency of the blocking bodies)
     * @param intersection The intersection the calculation is performed on
     * @return The amount of influence the light source has on the intersection
     */
    private Double3 transparency(Intersection intersection) {
        if (intersection.light.isSoftShadows() && _softShadowNumRays > 1) {
            // This means soft shadow is enabled
            // Cast to PointLight since only PointLight (and subclasses) support getBlackboard()
            Blackboard blackboard = ((PointLight) intersection.light)
                    .getBlackboard(intersection.point, _softShadowNumRays, _softShadowSampler);
            List<Ray> shadowBeam =
                    BeamGenerator.generateBeam(
                            intersection.point, blackboard.generatePoints(), intersection.normal, false
                    );
            
            Double3 ktrSum = Double3.ZERO;
            for (Ray shadowRay : shadowBeam) {
                ktrSum = ktrSum.add(singleRayTransparency(intersection, shadowRay));
            }
            return ktrSum.divide(shadowBeam.size());
        } else {
            Vector pointToLight = intersection.l.scale(-1);
            Ray shadowRay = new Ray(intersection.point, pointToLight, intersection.normal);
            
            return singleRayTransparency(intersection, shadowRay);
        }
    }
    
    private Double3 singleRayTransparency(Intersection intersection, Ray shadowRay) {
        double lightDistance = intersection.light.getDistance(intersection.point);
        List<Intersection> shadowIntersections =
                _scene.geometries.calcIntersections(shadowRay, lightDistance);
        if (shadowIntersections == null) {
            return Double3.ONE;
        }
        
        Double3 ktr = Double3.ONE;
        for (Intersection shadowIntersection : shadowIntersections) {
            ktr = ktr.product(shadowIntersection.material.kT);
            if (ktr.isLowerThan(MIN_CALC_COLOR_K)) {
                return Double3.ZERO;
            }
        }
        return ktr;
    }
    
    /**
     * Private helper method for calculating the color at a single point
     * @param intersection The point to calculate the color in it
     * @return The color of the point
     */
    private Color calcColor(Intersection intersection, Vector v) {
        return !preprocessIntersection(intersection, v) ?
                _scene.background
                : _scene.ambientLight.getIntensity().scale(intersection.material.kA)
                                     .add(calcColor(intersection, MAX_CALC_COLOR_LEVEL, INITIAL_K));
    }
    
    /**
     * private helper method for calculating the color at a single point with certain effect
     * on the final result in a recursive call
     * @param intersection The intersection at which we calculate the color
     * @param level The level of recursion
     * @param k The amount of effect on the final result
     * @return The color in the intersection with according to the amount of effect
     */
    private Color calcColor(Intersection intersection, int level, Double3 k) {
        Color color = calcLocalEffects(intersection, k);
        return level <= 1 ? color
                : color.add(calcGlobalEffects(intersection, level, k));
    }
    
    /**
     * Calculates the sum of all the global effects at an intersection with recursion
     * @param intersection The intersection the global effects are calculated in
     * @param level The level of recursion
     * @param k The amount of influence on the final result
     * @return The global effects at the intersection with respect to the k parameter
     */
    private Color calcGlobalEffects(Intersection intersection, int level, Double3 k) {
        return calcGlobalEffect(constructTransparencyRay(intersection),
                level, k, intersection.material.kT)
                .add(calcGlobalEffect(constructReflectionRay(intersection),
                        level, k, intersection.material.kR));
    }
    
    /**
     * Calculates the global effect on a ray with recursion
     * @param ray The ray the global effect will be calculated with
     * @param level The level of recursion
     * @param k The amount of influence the current ray color will have on the final result
     * @param kx The coefficient of the global effect being calculated
     */
    private Color calcGlobalEffect(Ray ray, int level, Double3 k, Double3 kx) {
        Double3 kkx = k.product(kx);
        
        if (kkx.isLowerThan(MIN_CALC_COLOR_K)) {
            return Color.BLACK;
        }
        
        Intersection intersection = findClosestIntersection(ray);
        if (intersection == null) {
            return _scene.background.scale(kx);
        }
        return preprocessIntersection(intersection, ray.direction())
                ? calcColor(intersection, level - 1, kkx).scale(kx)
                : Color.BLACK;
    }
    
    /**
     * Construct a transparency ray from the intersection
     * @param intersection The intersection the ray is created from
     * @return The transparency ray
     */
    private Ray constructTransparencyRay(Intersection intersection) {
        return new Ray(intersection.point, intersection.v, intersection.normal);
    }
    
    /**
     * Construct a reflection ray from the intersection
     * @param intersection The intersection the ray is created from
     * @return The reflection ray
     */
    private Ray constructReflectionRay(Intersection intersection) {
        return new Ray(intersection.point, intersection.r, intersection.normal);
    }
    
    /**
     * /**
     * Sums the effect of all the light sources on the intersection with Phong reflection model
     * @param intersection The intersection the effects are calculated on
     * @return The sum of all light sources colors
     */
    private Color calcLocalEffects(Intersection intersection, Double3 k) {
        Color color = intersection.geometry.getEmission();
        for (LightSource lightSource : _scene.lights) {
            if (preprocessLightSource(intersection, lightSource)) {
                Double3 ktr = transparency(intersection);
                if (ktr.product(k).isGreaterThan(MIN_CALC_COLOR_K)) {
                    color = color.add(lightSource.getIntensity(intersection.point).scale(ktr)
                                                 .scale(calcDiffuse(intersection).add(calcSpecular(intersection))));
                }
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
     * Calculates the specular part in the Phong reflection model on the intersection from the current light source
     * @param intersection The intersection the effects are calculated on
     * @return The total of the specular part from all light sources
     */
    private Double3 calcSpecular(Intersection intersection) {
        return intersection.material.kS.scale(powerInt(Math.max(0,
                intersection.rLight.dotProduct(intersection.v.scale(-1))), intersection.material.nShininess));
    }
    
    /**
     * Method for calculating the closest intersection to the start of the ray
     * from the list of the intersections of the ray
     * @param ray The ray the intersection are calculated with
     * @return The closest intersection from the ray's intersections
     */
    private Intersection findClosestIntersection(Ray ray) {
        return ray.findClosestIntersection(_scene.geometries.calcIntersections(ray));
    }
}
