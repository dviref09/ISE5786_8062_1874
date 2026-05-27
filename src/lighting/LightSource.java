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
    public Vector getL(Point p);
    
    /**
     * The intensity of the light that comes from the light source in the point
     * @param p The point which the intensity is calculated in
     * @return The intensity in the point
     */
    public Color getIntensity(Point p);
}
