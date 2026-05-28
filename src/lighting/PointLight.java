package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * A class representing a light coming from a point in all directions
 * @author Dvir Farkash
 */
public class PointLight extends Light implements LightSource {
    /**
     * The position of the light source in the scene
     */
    protected final Point _position;
    
    //The light attenuation coefficients
    /**
     * The constant coefficient
     */
    private double _kC = 1;
    
    /**
     * The linear coefficient
     */
    private double _kL = 0;
    
    /**
     * The quadratic coefficient
     */
    private double _kQ = 0;
    
    /**
     * constructor
     * @param intensity The intensity of the light
     * @param position The position of the point light
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        _position = position;
    }
    
    @Override
    public Vector getL(Point p) {
        return p.subtract(_position).normalize();
    }
    
    @Override
    public Color getIntensity(Point p) {
        if (p.equals(_position)) {
            return _intensity;
        }
        double distance = p.distance(_position);
        return _intensity.scale(1 / (_kC + _kL * distance + _kQ * distance * distance));
    }
    
    @Override
    public double getDistance(Point p) {
        return p.distance(_position);
    }
    
    // setters for the attenuation coefficients
    
    /**
     * Setter for kC coefficient
     * @param kC The new value for the coefficient
     * @return The same instance for setters chaining
     */
    public PointLight setKc(double kC) {
        _kC = kC;
        return this;
    }
    
    /**
     * Setter for kL coefficient
     * @param kL The new value for the coefficient
     * @return The same instance for setters chaining
     */
    public PointLight setKl(double kL) {
        _kL = kL;
        return this;
    }
    
    /**
     * Setter for kQ coefficient
     * @param kQ The new value for the coefficient
     * @return The same instance for setters chaining
     */
    public PointLight setKq(double kQ) {
        _kQ = kQ;
        return this;
    }
}
