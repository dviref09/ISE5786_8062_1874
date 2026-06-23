package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

import static primitives.Util.alignZero;

/**
 * A class representing a spotlight - a point light that light only in one direction
 * @author Dvir Farkash
 */
public class SpotLight extends PointLight {
    /**
     * The normalized direction of the light
     */
    protected final Vector _direction;
    
    /**
     * Constructor
     * @param intensity The intensity of the light
     * @param position The position of the spotlight
     * @param direction The direction of the spotlight
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        _direction = direction.normalize();
    }
    
    @Override
    public Color getIntensity(Point p) {
        if (p.equals(_position)) {
            return _intensity;
        }
        return super.getIntensity(p).scale(Math.max(0, alignZero(getL(p).dotProduct(_direction))));
    }
    
    // setters for the attenuation coefficients
    @Override
    public SpotLight setKc(double kC) {return (SpotLight) super.setKc(kC);}
    
    @Override
    public SpotLight setKl(double kL) {return (SpotLight) super.setKl(kL);}
    
    @Override
    public SpotLight setKq(double kQ) {return (SpotLight) super.setKq(kQ);}
    
    @Override
    public SpotLight setWidth(double width) {return (SpotLight) super.setWidth(width);}
    
    @Override
    public SpotLight setHeight(double height) {return (SpotLight) super.setHeight(height);}
}
