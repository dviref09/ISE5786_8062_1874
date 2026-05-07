package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Class for representing the camera in 3D scene
 * @author Dvir Farkash
 */
public class Camera implements Cloneable {
    /**
     * The position of the camera
     */
    private Point _p0;
    /**
     * Vector that represent the direction of the camera
     */
    private Vector _vTo, _vUp, _vRight;
    /**
     * The center of the view plane
     */
    private Point _vpCenter;
    /**
     * The width and height of the view plane
     */
    private double _width, _height;
    /**
     * The distance of the camera from the view plane
     */
    private double _distance;
    /**
     * The resolution of the view plane
     */
    private int _nX = 1, _nY = 1;
    /**
     * The width and height of individual pixel
     */
    private double _pixelWidth, _pixelHeight;

    /**
     * Private constructor, its is private because the camera should be created using the inner builder class
     */
    private Camera() {
    }

    /**
     * Method for getting an object of builder
     * @return Builder class object
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * Creates a ray through the center of a pixel in the view plane
     * For the indexes the center is considered (0,0)
     * @param xIndex The x index of the pixel
     * @param yIndex The y index of the pixel
     * @return A ray through the pixel
     */
    public Ray constructRay(int xIndex, int yIndex) {
        return null;
    }

    /**
     * Inner builder class for building the camera object
     */
    public static class Builder {
        /**
         * The camera object the class builds
         */
        private final Camera _camera = new Camera();

        /**
         * Helper variable for the setDirection method
         */
        private Point _targetPoint = null;

        /**
         * More helper variable to help in the building process
         */
        private Vector _vToHelper = null;
        private Vector _vUpHelper = Vector.AXIS_Y;

        /**
         * Sets the location of the camera
         * @param location The location of the camera
         * @return The same builder for chaining
         */
        public Builder setLocation(Point location) {
            _camera._p0 = location;
            return this;
        }

        /**
         * Sets the direction by two vectors:
         * @param to Where the camera is looking
         * @param up The direction of the top of the camera (affects rotation)
         * @return The same builder for chaining
         */
        public Builder setDirection(Vector to, Vector up) {
            _camera._vTo = to;
            _camera._vUp = up;
            _vToHelper = to;
            _vUpHelper = up;
            return this;
        }

        /**
         * Sets the direction by a vector and point:
         * @param target A point in the direction of the camera, by this point the direction vector of the camera will be calculated
         * @param up The direction of the top of the camera (affects rotation)
         * @return The same builder for chaining
         */
        public Builder setDirection(Point target, Vector up) {
            _targetPoint = target;
            _camera._vUp = up;
            _vUpHelper = up;
            return this;
        }

        /**
         * Sets the direction by a target point (and the up vector will be the y-axis)
         * @param target A point in the direction of the camera, by this point the direction vector of the camera will be calculated
         * @return The same builder for chaining
         */
        public Builder setDirection(Point target) {
            _targetPoint = target;
            return this;
        }

        /**
         * Sets the size of the view plane
         * @param width The width of the view plane
         * @param height The height of the view plane
         * @return The same builder for chaining
         */
        public Builder setVpSize(double width, double height) {
            _camera._pixelWidth = width;
            _camera._pixelHeight = height;
            return this;
        }

        /**
         * Sets the distance of the view plane from the camera
         * @param distance The distance of the view plane form the camera
         * @return The same builder for chaining
         */
        public Builder setVpDistance(double distance) {
            _camera._distance = distance;
            return this;
        }

        /**
         * Sets the resolution of the camera
         * @param nX The number of pixels in the x-axis
         * @param nY The number of pixels in the y-axis
         * @return The same builder for chaining
         */
        public Builder setResolution(int nX, int nY) {
            _camera._nX = nX;
            _camera._nY = nY;
            return this;
        }

        /**
         * Calculates the vUp, vRight vectors.
         * All of the vectors will be normalized
         */
        private void calcVector() {} // TODO: implementing the method
    }
}
