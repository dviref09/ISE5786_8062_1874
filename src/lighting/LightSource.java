package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;
import sampling.Blackboard;

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
    
    /**
     * Method for checking if the light source enabled soft shadows
     * @return True if soft shadow is enabled for this light source
     */
    boolean isSoftShadows();
    
    /**
     * Calculates the blackboard properties for the light source pointing towards the point
     * (assumes soft shadows enabled)
     * @param p The point the blackboard is pointing toward
     * @param resolution The amount of rays per side in the blackboard
     * @return The calculated blackboard
     */
    public Blackboard getBlackboard(Point p, int resolution);
    
}
