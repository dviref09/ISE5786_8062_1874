package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;
import sampling.Blackboard;

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
     * The width and height of the light, used for soft shadowing
     * if the width / height is zero or less, than soft shadowing is turned off for this light source
     */
    private double _width = 0;
    private double _height = 0;
    
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
        double distanceSquared = p.distanceSquared(_position);
        return _intensity.scale(1 / (_kC + _kL * Math.sqrt(distanceSquared) + _kQ * distanceSquared));
    }
    
    @Override
    public double getDistance(Point p) {
        return p.distance(_position);
    }
    
    @Override
    public Blackboard getBlackboard(Point p, int resolution) {
        Blackboard.Builder blackboardBuilder = Blackboard.getBuilder();
        
        // vUp and vRight calculation
        Vector vTo = getL(p);
        Vector vUp = Vector.AXIS_Y;
        Vector vRight;
        try {
            vRight = vTo.crossProduct(vUp).normalize();
        } catch (IllegalArgumentException e) {
            vUp = Vector.AXIS_X;
            vRight = vTo.crossProduct(vUp).normalize();
        }
        vUp = vRight.crossProduct(vTo).normalize();
        
        blackboardBuilder.setSize(_width, _height)
                         .setDirection(vUp, vRight)
                         .setCenter(_position)
                         .setResolution(resolution, resolution);
        return blackboardBuilder.build();
    }
    
    @Override
    public boolean isSoftShadows() {
        return _width > 0 && _height > 0;
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
    
    /**
     * Setter for the width of the light
     * @param width The new value of the width
     * @return The same instance for setters chaining
     */
    public PointLight setWidth(double width) {
        _width = width;
        return this;
    }
    
    /**
     * Setter for the height of the light
     * @param height The new value of the height
     * @return The same instance for setters chaining
     */
    public PointLight setHeight(double height) {
        _height = height;
        return this;
    }
}
