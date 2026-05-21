package lighting;

import primitives.Color;

/**
 * An abstract class for representing different types of light in the scene
 * @author Dvir Farkash
 */
abstract class Light {
    /**
     * The intensity of the light (the color of the lighting)
     */
    protected Color _intensity;

    /**
     * Constructor
     * @param intensity The intensity for the new light
     */
    protected Light(Color intensity) {
        _intensity = intensity;
    }

    /**
     * Getter for the intensity
     * @return The intensity of the light
     */
    public Color getIntensity() {
        return _intensity;
    }
}
