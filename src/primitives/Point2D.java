package primitives;

import static primitives.Util.isZero;

public record Point2D(double x, double y) {
    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null || getClass() != other.getClass())
            return false;
        return isZero(x - ((Point2D) other).x) && isZero(y - ((Point2D) other).y);
    }
}