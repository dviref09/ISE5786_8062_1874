package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

import static primitives.Util.powerInt;

public class BeamLight extends SpotLight {
    /**
     * The amount of beam concentration, must be positive
     */
    private int _beamPower;
    
    /**
     * Constructor
     * @param intensity The intensity of the light
     * @param position The position of the beam light
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
        return super.getIntensity(p).scale(powerInt((Math.max(0, getL(p).dotProduct(_direction))), _beamPower - 1));
    }
    
    // setters for the attenuation coefficients
    @Override
    public BeamLight setKc(double kC) {
        return (BeamLight) super.setKc(kC);
    }
    
    @Override
    public BeamLight setKl(double kL) {
        return (BeamLight) super.setKl(kL);
    }
    
    @Override
    public BeamLight setKq(double kQ) {
        return (BeamLight) super.setKq(kQ);
    }
    
    /**
     * Setter for beamPower coefficient
     * @param beamPower The new value for the coefficient
     * @return The same instance for setters chaining
     */
    public BeamLight setNarrowBeam(int beamPower) {
        if (beamPower < 1) {
            throw new IllegalArgumentException("Beam power must be positive");
        }
        _beamPower = beamPower;
        return this;
    }
}
