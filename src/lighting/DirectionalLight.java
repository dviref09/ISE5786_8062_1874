package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * A class representing a light that comes to any place in the scene in constant angle and intensity (like a light
 * source very far away like the sun)
 * @author Dvir Farkash
 */
public class DirectionalLight extends Light implements LightSource {
    /**
     * The normalized direction vector of the light
     */
    private final Vector _direction;
    
    /**
     * Constructor
     * @param intensity The intensity of the new directional light
     * @param direction The direction of the new directional light
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        _direction = direction.normalize();
    }
    
    @Override
    public Vector getL(Point p) {
        return _direction;
    }
    
    @Override
    public Color getIntensity(Point P) {
        return _intensity;
    }
}
