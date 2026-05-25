package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * A class representing a spotlight - a point light that light only in one direction
 */
public class SpotLight extends PointLight {
    /**
     * The direction of the light
     */
    protected final Vector _direction;

    /**
     * Constructor
     * @param intensity The intensity of the light
     * @param position  The position of the spotlight
     * @param direction The direction of the spotlight
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        _direction = direction;
    }

    @Override
    public Color getIntensity(Point p) {
        return null;
    }

    // setters for the attenuation coefficients
    @Override
    SpotLight setKc(double kC) {
        return (SpotLight) super.setKc(kC);
    }

    @Override
    SpotLight setKl(double kL) {
        return (SpotLight) super.setKl(kL);
    }

    @Override
    SpotLight setKq(double kQ) {
        return (SpotLight) super.setKq(kQ);
    }
}
