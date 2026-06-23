package sampling;

import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;

import primitives.Point;
import primitives.Point2D;
import primitives.Ray;
import primitives.Vector;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * A class for representing a screen that rays will be traced to using a super-sampling pattern.
 * @author Amichai Feigelson
 */
public class Blackboard implements Cloneable {
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
    private int _nX;
    /**
     * The blackboard height resolution.
     */
    private int _nY;
    /**
     * The center point of the blackboard.
     */
    private Point _center;
    /**
     * The normalized up direction of the blackboard
     */
    private Vector _vUp;
    /**
     * The normalized right direction of the blackboard
     */
    private Vector _vRight;
    /**
     * The width of individual pixel
     */
    private double _pixelWidth;
    /**
     * The height of individual pixel
     */
    private double _pixelHeight;
    /**
     * The sampler in use
     */
    private SamplerType _sampler = SamplerType.JITTERED;
    
    /**
     * Private constructor so the only one who is capable of creating black board is the builder
     */
    private Blackboard(){}
    
    /**
     * Getter for the builder of the blackboard
     * @return The builder of the blackboard
     */
    public static Builder getBuilder(){
        return new Builder();
    }
    
    /**
     * Generates all the 3D points from the offsets given from the sampler
     * @return List of all the 3D points generated
     */
    public List<Point> generatePoints() {
        List<Point> points = new LinkedList<>();
        
        List<Point2D> offsets = _sampler.samplePoints(_nX, _nY);
        
        for (Point2D offset : offsets) {
            points.add(generatePoint(offset.x(), offset.y()));
        }
        return points;
    }
    
    /**
     * Generates a 3D point from the given x and y indices
     * @param xIndex The horizontal value
     * @param yIndex The vertical value
     * @return The 3D point 
     */
    public Point generatePoint(double xIndex, double yIndex) {
        xIndex = alignZero(xIndex);
        yIndex = alignZero(yIndex);
        // The +-0.5 is for the randomization in the jittered
        if (xIndex > (_nX - 1) + 0.5 || yIndex > (_nY - 1) + 0.5 || xIndex < -0.5 || yIndex < -0.5) {
            throw new IllegalArgumentException
                    ("x and y indices must be within the range of the blackboard resolution.");
        }
        
        double x = (xIndex - (double) (_nX - 1) / 2) * _pixelWidth;
        double y = -(yIndex - (double) (_nY - 1) / 2) * _pixelHeight;
        Point pIJ = _center;
        
        if (!isZero(x)) {
            pIJ = pIJ.add(_vRight.scale(x));
        }
        if (!isZero(y)) {
            pIJ = pIJ.add(_vUp.scale(y));
        }
        
        return pIJ;
    }
    
    /**
     * Inner builder class for building the Blackboard object
     */
    public static class Builder {
        /**
         * The blackboard object the class builds
         */
        private final Blackboard _blackboard = new Blackboard();
        
        /**
         * Constructor for the builder
         */
        public Builder() {
        }
        
        /**
         * Sets the size of the blackboard
         * @param width The width of the blackboard
         * @param height The height of the blackboard
         * @return The same builder for chaining methods
         */
        public Builder setSize(double width, double height) {
            _blackboard._width = width;
            _blackboard._height = height;
            return this;
        }
        
        /**
         * Sets the resolution of the blackboard
         * @param nX The number of cells in the x-axis
         * @param nY The number of cells in the y-axis
         * @return The same builder for chaining methods
         */
        public Builder setResolution(int nX, int nY) {
            _blackboard._nX = nX;
            _blackboard._nY = nY;
            return this;
        }
        
        /**
         * Sets the center point of the blackboard
         * @param center The center point
         * @return The same builder for chaining methods
         */
        public Builder setCenter(Point center) {
            _blackboard._center = center;
            return this;
        }
        
        /**
         * Sets the orientation vectors of the blackboard
         * @param vUp The up direction vector
         * @param vRight The right direction vector
         * @return The same builder for chaining methods
         */
        public Builder setDirection(Vector vUp, Vector vRight) {
            _blackboard._vUp = vUp;
            _blackboard._vRight = vRight;
            return this;
        }
        
        /**
         * Sets the sampler type for the blackboard
         * @param sampler The sampler type
         * @return The same builder for chaining methods
         */
        public Builder setSampler(SamplerType sampler) {
            _blackboard._sampler = sampler;
            return this;
        }
        
        /**
         * Checks that all values are valid and generates the blackboard object
         * @return The finished Blackboard object
         * @throws IllegalArgumentException If parameters are out of bounds or vectors are not orthogonal
         * @throws MissingResourceException If mandatory fields are missing
         */
        public Blackboard build() {
            if (_blackboard._width <= 0 || _blackboard._height <= 0) {
                throw new IllegalArgumentException("Blackboard dimensions must be greater than 0.");
            }
            if (_blackboard._nX <= 0 || _blackboard._nY <= 0) {
                throw new IllegalArgumentException("Resolution in either axes should be greater than 0.");
            }
            if (_blackboard._center == null) {
                throw new MissingResourceException("Blackboard must have a center point", "Builder", "_center");
            }
            if (_blackboard._vUp == null || _blackboard._vRight == null) {
                throw new MissingResourceException("Blackboard must have orientation vectors", "Builder", "_vUp or _vRight");
            }
            
            // Check orthogonality using dot product
            if (!isZero(_blackboard._vUp.dotProduct(_blackboard._vRight))) {
                throw new IllegalArgumentException("vUp and vRight vectors must be orthogonal.");
            }
            
            // Normalize orientation vectors to ensure mathematical correctness
            _blackboard._vUp = _blackboard._vUp.normalize();
            _blackboard._vRight = _blackboard._vRight.normalize();
            
            // calculate the pixel's width and height
            _blackboard._pixelWidth = _blackboard._width / _blackboard._nX;
            _blackboard._pixelHeight = _blackboard._height / _blackboard._nY;
            
            try {
                return (Blackboard) _blackboard.clone();
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }
    }
}
