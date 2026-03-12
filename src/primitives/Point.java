class Point {
    /**
     * The coordinates of the point.
     */
    private final Double3 _coordinates;

    /**
     * A constant representing the axes origin.
     */
    private final Double3 ZERO = new Double3(0,0,0);

    /**
     * Constructs a new point from the coordinates given in the parameters.
     *
     * @param x The x coordinate of the point.
     * @param y The y coordinate of the point.
     * @param z the z coordinate of the point.
     */
    public Point(double x, double y, double z) {
        _coordinates = new Double3(x, y, z);
    }

    /**
     * Constructs a new points from the coordinate given in the parameters.
     *
     * @param coordinates The cooridantes of the point.
     */
    public Point(Double3 coordinates) {
        _coordinates = coordinates;
    }


    /**
     * Subtracts another point from our point.
     * @param other The point to subtract from our point.
     * @return The result vector.
     * @throw IllegalArgumentException When the two points are equal.
     */
    public Vector subtract(Point other) {
        if (other.equals(this)) {
            throw new IllegalArgumentException("subtraction of point on itself results in zero vector");
        }
        return new Vector(this._coordinates.subtract(other._coordinates));
    }

    /**
     * Adds a vector to this point, returning a new point.
     * @param vector The vector to add.
     * @return A new point after adding the vector to this point.
     */
    public Point add(Vector vector) {
        return new Point(_coordinates.add(vector.getCoordinates()));
    }

    /**
     * Calculates the squared distance between this point and another point.
     * @param other The other point.
     * @return The squared distance between the two points.
     */
    public double distanceSquared(Point other) {
        return _coordinates.subtract(other._coordinates).dotProduct(_coordinates.subtract(other._coordinates));
    }

    /**
     * Calculates the distance between this point and another point.
     * @param other The other point.
     * @return The distance between the two points.
     */
    public double distance(Point other) {
        return Math.sqrt(distanceSquared(other));
    }


    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        return _coordinates.equals(((Point) object)._coordinates);
    }

    @Override
    public String toString() {
        return "Point: " + _coordinates;
    }

    @Override
    public int hashCode() {
        return _coordinates.hashCode());
    }
}