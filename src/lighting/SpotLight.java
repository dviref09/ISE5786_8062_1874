package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

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
	 * @param position  The position of the spotlight
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
        return super.getIntensity(p).scale(Math.max(0, getL(p).dotProduct(_direction)));
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
