package renderer;

import java.util.MissingResourceException;

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
         * Constructor for the builder, sets default values to camera's fields
         */
        public Builder() {
            _camera._vUp = Vector.AXIS_Y;
            _camera._vTo = null;
        }
        /**
         * Helper variable for the setDirection method
         */
        private Point _targetPoint = null;

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
            _camera._width = width;
            _camera._height = height;
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
         * Checks that all values are valid and generates the missing data
         * @return The finished camera object
         */
        public Camera build() {
            checkResolution();
            checkLocationAndDirection();
            checkViewPlane();

            try {
                return (Camera)_camera.clone();
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }
        /**
         * Checks whether the resolution values are valid or not
         * @throws IllegalArgumentException if the resolution values are not valid
         */
        private void checkResolution() {
            if (_camera._nX < 1 || _camera._nY < 1) {
                throw new IllegalArgumentException("Resolution in either axes should be greater than 0.");
            }
        }

        /**
         * Checks if there is enough data about the camera position
         * Enough data is:
         * Mandatory: camera's position and at least one of below:
         * 1) Target point
         * 2) vTo
         * After that calculates the remaining data using {@link Builder#calcVectors()}
         * @throws MissingResourceException If the camera's position is missing or both target point an vTo are missing
         * @throws IllegalArgumentException If the target point is in the camera's position and we don't have vTo already
         */
        private void checkLocationAndDirection() {
            if (_camera._p0 == null) {
                throw new MissingResourceException("The camera must have a location", "Builder", "_po");
            }
            if (_targetPoint == null && _camera._vTo == null) {
                throw new MissingResourceException("The camera must have vTo vector or target point", "Builder", "_vTo or _targetPoint");
            }

            if (_camera._vTo == null) {
                try {
                    _camera._vTo = _targetPoint.subtract(_camera._p0);
                }
                catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("The camera's target point can't be in the camera's position");
                }
            }
            calcVectors();
        }

        /**
         * Checks if the view plane data is valid
         * And then calculates the center point of the view plane and the pixel dimensions using {@link Builder#calcViewPlane()}
         */
        private void checkViewPlane() {
            if (_camera._width <= 0 || _camera._height <= 0 || _camera._distance <= 0) {
                throw new IllegalArgumentException("The camera's view plane dimensions must be greater than 0.");
            }
            calcViewPlane();
        }

        /**
         * Calculates the vUp, vRight vectors.
         * All of the vectors will be normalized
         * @throws IllegalArgumentException If the vTo and vUp are parallel
         */
        private void calcVectors() {
            _camera._vTo = _camera._vTo.normalize();
            try {
                _camera._vRight = _camera._vTo.crossProduct(_camera._vUp).normalize();
            }
            catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("vTo and vUp can't be parallel.");
            }

            _camera._vUp = _camera._vRight.crossProduct(_camera._vTo).normalize();
        }

        /**
         * Calculates the missing data of the view plane:
         * The center point and the pixel dimensions
         */
        private void calcViewPlane() {
            _camera._vpCenter = _camera._vTo.scale(_camera._distance);
            _camera._pixelWidth = _camera._width / _camera._nX;
            _camera._pixelHeight = _camera._height / _camera._nY;
        }
    }
}
