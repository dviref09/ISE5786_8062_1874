package lighting;

import primitives.Color;

/**
 * Class for representing ambient light in 3D scene
 * @author Dvir Farkash
 */
public final class AmbientLight extends Light {
    /**
     * Constant for representing no ambient light in the scene
     */
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK);
    
    /**
     * Constructor
     * @param intensity the color of the ambient light
     */
    public AmbientLight(Color intensity) {
        super(intensity);
    }
}
