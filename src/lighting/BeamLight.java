package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class BeamLight extends SpotLight {
//    /**
//     * The beam angle in degrees
//     * It is the angle between the direction vector to the edge of the beam (not the angel between the two edges of
//     * the beam)
//     */
//    private double angle;

    /**
     * The amount of beam concentration
     */
    private double _beamPower;

    /**
     * Constructor
     * @param intensity The intensity of the light
     * @param position  The position of the beam light
     * @param direction The direction of the beam light
     */
    public BeamLight(Color intensity, Point position, Vector direction) {
        super(intensity, position, direction);
    }


    @Override
    public Color getIntensity(Point p) {
        if (p.equals(_position)) {
            return _intensity;
        }
        return super.getIntensity(p).scale(Math.pow((Math.max(0, getL(p).dotProduct(_direction))), _beamPower - 1));
    }

    // setters for the attenuation coefficients
    @Override
    BeamLight setKc(double kC) {
        return (BeamLight) super.setKc(kC);
    }

    @Override
    BeamLight setKl(double kL) {
        return (BeamLight) super.setKl(kL);
    }

    @Override
    BeamLight setKq(double kQ) {
        return (BeamLight) super.setKq(kQ);
    }

    /**
     * Setter for beamPower coefficient
     * @param beamPower The new value for the coefficient
     * @return The same instance for setters chaining
     */
    BeamLight setNarrowBeam(double beamPower) {
        _beamPower = beamPower;
        return this;
    }
}
