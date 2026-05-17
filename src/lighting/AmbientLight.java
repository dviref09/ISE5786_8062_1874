package lighting;

import primitives.Color;

/**
 * Class for representing ambient light in 3D scene
 * @author Dvir Farkash
 */
public final class AmbientLight {
    /**
     * The color of the ambient light
     */
    private final Color _intensity;
    /**
     * Constant for representing no ambient light in the scene
     */
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK);

    /**
     * Constructor
     * @param intensity the color of the ambient light
     */
    public AmbientLight(Color intensity) {
        _intensity = intensity;
    }

    /**
     * Getter for the intensity of the ambient lighting
     * @return The intensity of the ambient lighting
     */
    public Color getIntensity() {
        return _intensity;
    }
}
