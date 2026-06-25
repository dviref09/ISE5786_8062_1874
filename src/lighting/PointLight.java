package lighting;

import java.util.List;

import primitives.Color;
import primitives.Point;
import primitives.Vector;
import sampling.Blackboard;
import sampling.SamplerType;

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
    protected double _width = 0;
    protected double _height = 0;
    
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
    
    /**
     * Calculates the blackboard properties for the light source pointing towards the point
     * (assumes soft shadows enabled)
     * @param p The point the blackboard is pointing toward
     * @param resolution The amount of rays per side in the blackboard
     * @param samplerType The sampling pattern to use
     * @return The calculated blackboard
     */
    public Blackboard getBlackboard(Point p, int resolution, SamplerType samplerType) {
        List<Vector> directions = calculateOrthogonalVectors(vToCalc(p));
        
        return Blackboard.getBuilder()
                         .setSize(_width, _height)
                         .setDirection(directions.get(0), directions.get(1))
                         .setCenter(_position)
                         .setResolution(resolution, resolution)
                         .setSampler(samplerType)
                         .build();
    }
    
    /**
     * private method for calculating the direction of the blackboard towards certain point
     * @param p The point the blackboard is "looking" towards
     * @return The direction of the blackboard
     */
    protected Vector vToCalc(Point p) {
        return getL(p);
    }
    
    /**
     * Helper method to calculate orthogonal vUp and vRight vectors from a primary direction vector.
     * Tries to use AXIS_Y as vUp, and if it's parallel to the primary vector, uses AXIS_X instead.
     * @return A list of two vectors: (vUp, vRight)
     */
    protected List<Vector> calculateOrthogonalVectors(Vector vTo) {
        Vector vUp = Vector.AXIS_Y;
        Vector vRight;
        try {
            vRight = vTo.crossProduct(vUp).normalize();
        } catch (IllegalArgumentException e) {
            vUp = Vector.AXIS_X;
            vRight = vTo.crossProduct(vUp).normalize();
        }
        vUp = vRight.crossProduct(vTo).normalize();
        
        return List.of(vUp, vRight);
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
