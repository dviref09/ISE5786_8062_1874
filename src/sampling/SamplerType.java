package sampling;

import java.util.List;

import primitives.Point2D;

/**
 * Enum for choosing between different sampling patterns
 * @author Dvir Farkash
 */
public enum SamplerType {
    /**
     * Jittered sampling pattern
     */
    JITTERED {
        public List<Point2D> samplePoints(int nX, int nY) {
            return Sampler.sampleJitteredPoints(nX, nY);
        }
    };
    
    public abstract List<Point2D> samplePoints(int nX, int nY);
}
