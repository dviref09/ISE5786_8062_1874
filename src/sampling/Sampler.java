package sampling;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.Random;

import primitives.Point2D;

/**
 * Class that implements all the sampling patterns
 * @author Amichai Feigelson
 */
public class Sampler {
    /**
     * Jittered sampling pattern
     * @param nX The number of points in the x-axis
     * @param nY The number of points in the y-axis
     * @return A list of 2D points of the offsets of the jittered pattern
     * @throws java.lang.IllegalArgumentException if nX or nY is not natural number
     */
    public static List<Point2D> sampleJitteredPoints(int nX, int nY) {
        // if there is only one point then just return (0, 0) without offset
        if (nX == 1 || nY == 1) {
            return List.of(new Point2D(0, 0));
        } else if (nX < 1 || nY < 1) {
            throw new IllegalArgumentException("The amount of points should be positive");
        }
        List<Point2D> points = new LinkedList<>();
        PrimitiveIterator.OfDouble randIt = (new Random()).doubles(nX * nY * 2, -0.5, 0.5).iterator();
        
        // for the current offset
        double xValue, yValue;
        
        for (int i = 0; i < nX; i++) {
            for (int j = 0; j < nY; j++) {
                xValue = i + randIt.nextDouble();
                yValue = j + randIt.nextDouble();
                
                points.add(new Point2D(xValue, yValue));
            }
        }
        
        return points;
    }
}
