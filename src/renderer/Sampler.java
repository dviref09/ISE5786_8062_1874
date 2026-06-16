package renderer;

import java.util.ArrayList;
import java.util.List;

import primitives.Point;

/**
 * An interface for creating offsets for each of the points that the
 * rays will be traced to using a super-sampling pattern.
 * @author Amichai Feigelson
 */
public interface Sampler {
	/**
	 * Method for calculating the offsets for the points.
	 * @param nX The width resolution
	 * @param nY The height resolution
	 * @return A list of lists containing the offsets for each point
	 */
	public List<List<Double>> sameplePoints(int nX, int nY);
}
