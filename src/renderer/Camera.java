package renderer;

import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import sampling.Blackboard;
import sampling.SamplerType;
import scene.Scene;

import static renderer.PixelManager.Pixel;

/**
 * Class for representing the camera in 3D scene
 * @author Dvir Farkash
 */
public final class Camera implements Cloneable {
    /**
     * The position of the camera
     */
    private Point _p0;
    /**
     * The resolution of the view plane
     */
    private int _nX = 1, _nY = 1;
    /**
     * The view plane of the camera
     */
    private Blackboard _viewPlane;
    /**
     * The ImageWriter instance to write with him the image
     */
    private ImageWriter _imageWriter;
    /**
     * The ray tracer to be in use in the camera
     */
    private RayTracerBase _rayTracer;
    /**
     * The amount of threads to be used in the rendering process:
     * 0 - no multithreading
     * -1 - parallel stream
     * 1+ - raw threads
     */
    private int _threadCount = 0;
    /**
     * The interval of the printing of the progress of the rendering
     */
    private double _printInterval = 0;
    /**
     * The pixel manager of the rendering
     */
    private PixelManager _pixelManager;
    /**
     * Spare threads that won't be used in auto raw threads
     */
    private static final int SPARE_THREADS = 2;
    
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
     * Calculated the color of all the pixels and paints them in the writeImage
     * @return The same camera object for chaining methods
     */
    public Camera renderImage() {
        _pixelManager = new PixelManager(_nY, _nX, _printInterval);
        return switch (_threadCount) {
            case 0 -> renderImageNoThreads();
            case -1 -> renderImageStream();
            default -> renderImageRawThreads();
        };
    }
    
    private Camera renderImageNoThreads() {
        for (int x = 0; x < _nX; x++) {
            for (int y = 0; y < _nY; y++) {
                castRay(x, y);
            }
        }
        return this;
    }
    
    private Camera renderImageRawThreads() {
        List<Thread> threads = new LinkedList<>();
        int count = _threadCount;
        CountDownLatch latch = new CountDownLatch(count);
        
        while (count-- > 0) {
            threads.add(new Thread(() -> {
                Pixel pixel;
                while ((pixel = _pixelManager.nextPixel()) != null) {
                    castRay(pixel.col(), pixel.row());
                }
                latch.countDown();
            }));
        }
        
        for (Thread thread : threads) {
            thread.start();
        }
        try {
            latch.await();
        } catch (InterruptedException ignored) {
        }
        return this;
    }
    
    private Camera renderImageStream() {
        IntStream.range(0, _nX).parallel()
                 .forEach(yIndex -> IntStream.range(0, _nY).parallel()
                                             .forEach(xIndex -> castRay(xIndex, yIndex)));
        return this;
    }
    
    /**
     * Adding a grid on top of the current image, for debugging usage
     * @param interval The size of each grid cell in pixels
     * @param color The color of the grid
     * @return The same camera for chaining methods
     */
    public Camera printGrid(int interval, Color color) {
        for (int x = 0; x < _nX; x++) {
            // the y increase is based on which column we are now
            // if the column is divisible by interval this means it should be fully colored so y is going up by one
            // each time
            // if the column is not divisible by interval this means it should be colored only in the interval gaps
            // so y is going up by interval each time
            for (int y = 0; y < _nY; y += (x % interval == 0 ? 1 : interval)) {
                _imageWriter.writePixel(x, y, color);
            }
        }
        
        return this;
    }
    
    /**
     * Writes the current image to a PNG file
     * @param name The name of the output PNG file
     */
    public void writeToImage(String name) {
        _imageWriter.writeToImage(name);
    }
    
    /**
     * Creates a ray through the center of a pixel in the view plane
     * For the indexes the center is considered (0,0)
     * @param xIndex The x index of the pixel
     * @param yIndex The y index of the pixel
     * @return A ray through the pixel
     */
    public Ray constructRay(int xIndex, int yIndex) {
        return new Ray(_p0, _viewPlane.generatePoint(xIndex, yIndex));
    }
    
    /**
     * Private helper method for creating a ray through a pixel, calculating its color and then paints the
     * corresponding pixel in imageWriter
     * @param xIndex The x index of the pixel to paint
     * @param yIndex The y index of the pixel to paint
     */
    private void castRay(int xIndex, int yIndex) {
        Ray ray = constructRay(xIndex, yIndex);
        Color pixelColor = _rayTracer.traceRay(ray);
        _imageWriter.writePixel(xIndex, yIndex, pixelColor);
        _pixelManager.pixelDone();
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
         * The builder of the view plane
         */
        private final Blackboard.Builder _viewPlaneBuilder = Blackboard.getBuilder();
        // private helper parameter for setting up the view plane
        /**
         * Vector that represent the direction of the camera
         */
        private Vector _vTo, _vUp, _vRight;
        /**
         * The width and height of the view plane
         */
        private double _width, _height;
        /**
         * The distance of the camera from the view plane
         */
        private double _distance;
        /**
         * Helper variable for the setDirection method
         */
        private Point _targetPoint = null;
        
        /**
         * Constructor for the builder, sets default values to camera's fields
         */
        public Builder() {
            _vUp = Vector.AXIS_Y;
            _vTo = null;
        }
        
        /**
         * Sets the location of the camera
         * @param location The location of the camera
         * @return The same builder for chaining methods
         */
        public Builder setLocation(Point location) {
            _camera._p0 = location;
            return this;
        }
        
        /**
         * Sets the direction by two vectors:
         * @param to Where the camera is looking
         * @param up The direction of the top of the camera (affects rotation)
         * @return The same builder for chaining methods
         */
        public Builder setDirection(Vector to, Vector up) {
            _vTo = to;
            _vUp = up;
            return this;
        }
        
        /**
         * Sets the direction by a vector and point:
         * @param target A point in the direction of the camera, by this point the direction vector of the camera
         * will be calculated
         * @param up The direction of the top of the camera (affects rotation)
         * @return The same builder for chaining methods
         */
        public Builder setDirection(Point target, Vector up) {
            _targetPoint = target;
            _vUp = up;
            return this;
        }
        
        /**
         * Sets the direction by a target point (and the up vector will be the y-axis)
         * @param target A point in the direction of the camera, by this point the direction vector of the camera
         * will be calculated
         * @return The same builder for chaining methods
         */
        public Builder setDirection(Point target) {
            _targetPoint = target;
            return this;
        }
        
        /**
         * Sets the size of the view plane
         * @param width The width of the view plane
         * @param height The height of the view plane
         * @return The same builder for chaining methods
         */
        public Builder setVpSize(double width, double height) {
            _width = width;
            _height = height;
            return this;
        }
        
        /**
         * Sets the distance of the view plane from the camera
         * @param distance The distance of the view plane form the camera
         * @return The same builder for chaining methods
         */
        public Builder setVpDistance(double distance) {
            _distance = distance;
            return this;
        }
        
        /**
         * Sets the resolution of the camera
         * @param nX The number of pixels in the x-axis
         * @param nY The number of pixels in the y-axis
         * @return The same builder for chaining methods
         */
        public Builder setResolution(int nX, int nY) {
            _camera._nX = nX;
            _camera._nY = nY;
            return this;
        }
        
        /**
         * Sets the ray tracer of the camera
         * @param scene The scene the ray tracer works on
         * @param type The type of ray tracer to be used in camera
         * @return The same builder for chaining methods
         * @throws IllegalArgumentException If the ray tracer type is invalid
         */
        public Builder setRayTracer(Scene scene, RayTracerType type) {
            if (type == RayTracerType.SIMPLE) {
                _camera._rayTracer = new SimpleRayTracer(scene);
            } else {
                throw new IllegalArgumentException("Must enter a valid ray tracer");
            }
            return this;
        }
        
        /**
         * Sets the amount of rays per side in soft shadows super sampling
         * @param numOfRays The number of rays per side in soft shadows
         * @return The same builder for chaining methods
         */
        public Builder setSSRays(int numOfRays) {
            if (_camera._rayTracer != null) {
                _camera._rayTracer.setSoftShadowNumRays(numOfRays);
            }
            return this;
        }
        
        /**
         * Sets the sample pattern in soft shadows super sampling
         * @param samplerType The type of sample pattern to use in soft shadows
         * @return The same builder for chaining methods
         */
        public Builder setSSSampler(SamplerType samplerType) {
            if (_camera._rayTracer != null) {
                _camera._rayTracer.setSoftShadowSampler(samplerType);
            }
            return this;
        }
        
        /**
         * Sets the amount of threads to be used in the rendering process
         * @param threads The amount of threads to be used:
         * 0 - no multithreading
         * -1 - parallel stream
         * -2 - auto raw threads
         * 1+ - raw threads
         * @return The same builder for chaining methods
         */
        public Builder setMultithreading(int threads) {
            if (threads < -2) {
                throw new IllegalArgumentException("Threads parameter must be equal or higher than -2");
            }
            if (threads >= -1) {
                _camera._threadCount = threads;
            } else {
                int cores = Runtime.getRuntime().availableProcessors() - SPARE_THREADS;
                _camera._threadCount = (cores <= 2 ? 1 : cores);
            }
            return this;
        }
        
        public Builder setDebugPrint(double interval) {
            if (interval < 0) {
                throw new IllegalArgumentException("Interval parameter must be a non-negative number");
            }
            _camera._printInterval = interval;
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
            checkRayTracer();
            
            try {
                return (Camera) _camera.clone();
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }
        
        /**
         * Checks whether the resolution values are valid or not
         * Adds the imageWriter with the resolution data
         * @throws IllegalArgumentException if the resolution values are not valid
         */
        private void checkResolution() {
            if (_camera._nX < 1 || _camera._nY < 1) {
                throw new IllegalArgumentException("Resolution in either axes should be greater than 0.");
            }
            _camera._imageWriter = new ImageWriter(_camera._nX, _camera._nY);
            _viewPlaneBuilder.setResolution(_camera._nX, _camera._nY);
        }
        
        /**
         * Checks if there is enough data about the camera position
         * Enough data is:
         * Mandatory: camera's position and at least one of below:
         * 1) Target point
         * 2) vTo
         * After that calculates the remaining data
         * @throws MissingResourceException If the camera's position is missing or both target point an vTo are missing
         * @throws IllegalArgumentException If the target point is in the camera's position, and we don't have vTo
         * already
         */
        private void checkLocationAndDirection() {
            if (_camera._p0 == null) {
                throw new MissingResourceException("The camera must have a location", "Builder", "_po");
            }
            if (_targetPoint == null && _vTo == null) {
                throw new MissingResourceException("The camera must have vTo vector or target point", "Builder",
                        "_vTo or _targetPoint");
            }
            
            if (_vTo == null) {
                if (_targetPoint.equals(_camera._p0)) {
                    throw new IllegalArgumentException("The camera's target point can't be in the camera's position");
                }
                _vTo = _targetPoint.subtract(_camera._p0);
            }
            calcVectors();
        }
        
        /**
         * Checks if the view plane data is valid
         * And then calculates the center point of the view plane and the pixel dimensions
         * @throws IllegalArgumentException If the width or the height or the distance of the view plane are not
         * positive
         */
        private void checkViewPlane() {
            if (_width <= 0 || _height <= 0 || _distance <= 0) {
                throw new IllegalArgumentException("The camera's view plane dimensions must be greater than 0.");
            }
            calcViewPlane();
        }
        
        /**
         * Checks if there is a ray tracer and if there isn't then sets a default ray tracer: simple and scene called
         * "test"
         */
        private void checkRayTracer() {
            if (_camera._rayTracer == null) {
                setRayTracer(new Scene("test"), RayTracerType.SIMPLE);
            }
        }
        
        /**
         * Calculates the vUp, vRight vectors.
         * All the vectors will be normalized
         * @throws IllegalArgumentException If the vTo and vUp are parallel
         */
        private void calcVectors() {
            _vTo = _vTo.normalize();
            try {
                _vRight = _vTo.crossProduct(_vUp);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("vTo and vUp can't be parallel.");
            }
            
            _vUp = _vRight.crossProduct(_vTo);
        }
        
        /**
         * Calculates the missing data of the view plane:
         * The center point and the pixel dimensions
         */
        private void calcViewPlane() {
            Point _vpCenter = _camera._p0.add(_vTo.scale(_distance));
            
            _viewPlaneBuilder.setCenter(_vpCenter)
                             .setSize(_width, _height)
                             .setDirection(_vUp, _vRight);
            _camera._viewPlane = _viewPlaneBuilder.build();
        }
    }
}
