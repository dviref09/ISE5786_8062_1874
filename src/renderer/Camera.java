package renderer;

import primitives.Point;
import primitives.Vector;

/**
 * Class for representing the camera in 3D scene
 */
public class Camera implements Cloneable {
    /**
     * The position of the camera
     */
    private Point _p0;
    /**
     * Vector that represent the direction of the camera
     */
    private Vector vTo, vUp, vRight;
    /**
     * The center of the view plane
     */
    private Point vpCenter;
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
    private int _nX = 1, nY = 1;
    /**
     * The width and height of individual pixel
     */
    private double _pixelWidth, _pixelHeight;

    /**
     * Private constructor, its is private because the camera should be created using the inner builder class
     */
    private Camera() {}

    /**
     * Method for getting a object of builder
     * @return Builder class object
     */
    public static Builder getBuilder() {
        return new Builder();
    }
    /**
     * Inner builder class for building the camera object
     */
    public static class Builder {}
}
