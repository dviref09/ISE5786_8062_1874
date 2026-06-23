package renderer;

import java.util.LinkedList;
import java.util.List;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import primitives.Color;

/**
 * A class for static functionality in ray beam in super sampling
 * @author Dvir Farkash
 */
public class BeamGenerator {
    /**
     * Private constructor so there will not be any instance of the class
     */
    private BeamGenerator(){}
    
    /**
     * Static method for generating beam of rays
     * @param origin The usual origin point of the rays
     * @param target The usual target points of the rays
     * @param switchDirection the direction of the ray, false for from origin to target, true for from target to origin
     * @return A list of the rays
     */
    public static List<Ray> generateBeam(Point origin, List<Point> target, boolean switchDirection) {
        List<Ray> beam = new LinkedList<>();
        for (Point targetPoint : target) {
            Vector direction = (switchDirection ? origin.subtract(targetPoint) : targetPoint.subtract(origin));
            beam.add(new Ray(origin, direction));
        }
        
        return beam;
    }
    
    /**
     * Static method for calculating the average color of a beam
     * @param beam A list of rays
     * @return The average color of the beam
     */
    public static Color averageColor(List<Ray> beam, RayTracerBase tracer) {
        Color sumColors = Color.BLACK;
        int numberOfBeam = beam.size();
        
        for (Ray ray : beam) {
            sumColors = sumColors.add(tracer.traceRay(ray));
        }
        
        if (numberOfBeam == 0) {
            return sumColors;
        }
        return sumColors.reduce(numberOfBeam);
    }
}
