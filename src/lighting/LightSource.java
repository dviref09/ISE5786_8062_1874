package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * An interface for representing an external light source
 * @author Dvir Farkash
 */
public interface LightSource {
    /**
     * Calculates the normalized vector from the light source to the point
     * @param p The destination point of the vector
     * @return The normalized vector from the light source to the point
     */
    Vector getL(Point p);
    
    /**
     * Calculates the intensity of the light that comes from the light source in the point
     * @param p The point which the intensity is calculated in
     * @return The intensity in the point
     */
    Color getIntensity(Point p);
    
    /**
     * Calculates the distance between the light source and the point
     * @param p The point to check the distance from the light source
     * @return The distance between the point and the light source
     */
    double getDistance(Point p);
}
