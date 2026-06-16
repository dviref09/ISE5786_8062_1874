package renderer;

import primitives.Point;

/**
 * A class for representing a screen that rays will be traced to using a super-sampling pattern.
 * @author Amichai Feigelson
 */
public class Blackboard {
	/**
	 * The width of the blackboard.
	 */
	private double _width;
	/**
	 * The height of the blackboard.
	 */
	private double _height;
	/**
	 * The blackboard width resolution.
	 */
	private double _nX;
	/**
	 * The blackboard height resolution.
	 */
	private double _nY;
	/**
	 * The center point of the blackboard.
	 */
	private Point _center;
}
