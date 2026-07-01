package geometries.impl;

import java.util.ArrayList;
import java.util.List;

import geometries.api.Intersectable;
import primitives.Point;
import primitives.Ray;

/**
 * Composite class for all geometric bodies
 * @author Dvir Farkash
 */
public final class Geometries extends Intersectable {
    /**
     * The minimum number of geometric items required to allow a tree node split.
     */
    private static final int MIN_ITEMS_TO_SPLIT = 5;
    
    /**
     * The list of the geometric bodies
     */
    private final List<Intersectable> geometries = new ArrayList<>();
    
    /**
     * Is the geometries has an infinite geometric body
     */
    private boolean hasInfiniteBody = false;
    
    /**
     * Constructs the object from all the geometries in the parameters
     * @param geometries List of geometries to have in this instance
     */
    public Geometries(Intersectable... geometries) {
        this.geometries.addAll(List.of(geometries));
    }
    
    /**
     * Constructs the object from the list object containing all the geometries in the parameters
     * @param geometries List of geometries to have in this instance
     */
    public Geometries(List<Intersectable> geometries) {
        this.geometries.addAll(geometries);
    }
    
    /**
     * Method for adding new geometric bodies to the list
     * @param geometries List of geometries to add to the geometries instance
     */
    public void add(Intersectable... geometries) {
        this.geometries.addAll(List.of(geometries));
    }
    
    /**
     * Method for adding new geometric bodies to the list
     * @param geometries List of geometries to add to the geometries instance
     */
    public void add(List<Intersectable> geometries) {
        this.geometries.addAll(geometries);
    }
    
    @Override
    public AABB getAABB() {
        if (hasInfiniteBody) return null;
        if (aabb == null) {
            for (Intersectable geometry : geometries) {
                AABB geometryAABB = geometry.getAABB();
                if (geometryAABB == null) {
                    // if one geometry has null AABB then this means it's must be infinite then the whole geometries
                    // is infinite (we ignore from the case of a body with no findIntersection implementation because
                    // he shouldn't be in the scene from the first place)
                    hasInfiniteBody = true;
                    aabb = null;
                    break;
                }
                if (aabb == null) {
                    aabb = geometryAABB;
                } else {
                    aabb = new AABB(
                            new Point(
                                    Math.min(aabb.getMin().x(), geometryAABB.getMin().x()),
                                    Math.min(aabb.getMin().y(), geometryAABB.getMin().y()),
                                    Math.min(aabb.getMin().z(), geometryAABB.getMin().z())
                            ),
                            new Point(
                                    Math.max(aabb.getMax().x(), geometryAABB.getMax().x()),
                                    Math.max(aabb.getMax().y(), geometryAABB.getMax().y()),
                                    Math.max(aabb.getMax().z(), geometryAABB.getMax().z())
                            )
                    );
                }
            }
        }
        return aabb;
    }
    
    /**
     * Private helper method to separate all the infinite bodies from the finite bodies.
     * The infinite bodies stays in the geometries object.
     * the finite bodies are added to sub-geometries object and then buildTree is applied on it.
     */
    private void separateInfiniteBodies() {
        List<Intersectable> finiteBodies = new ArrayList<>();
        List<Intersectable> infiniteBodies = new ArrayList<>();
        for (Intersectable geometry : geometries) {
            if (geometry.getAABB() != null) {
                finiteBodies.add(geometry);
            } else {
                infiniteBodies.add(geometry);
            }
        }
        geometries.clear();
        geometries.addAll(infiniteBodies);
        Geometries finiteBodiesGeometries = new Geometries(finiteBodies);
        finiteBodiesGeometries.buildTree();
        geometries.add(finiteBodiesGeometries);
    }
    
    /**
     * Automatically builds a BVH tree structure out of the current geometries
     * using a Midpoint Split along the longest axis.
     */
    public void buildTree() {
        if (geometries.size() < MIN_ITEMS_TO_SPLIT) {
            return;
        }
        AABB currentAABB = getAABB();
        if (hasInfiniteBody) {
            separateInfiniteBodies();
            return;
        }
        double widthX = currentAABB.getMax().x() - currentAABB.getMin().x();
        double widthY = currentAABB.getMax().y() - currentAABB.getMin().y();
        double widthZ = currentAABB.getMax().z() - currentAABB.getMin().z();
        double midpoint;
        
        int longestAxis; // 0 = X, 1 = Y, 2 = Z
        
        if (widthX >= widthY && widthX >= widthZ) {
            longestAxis = 0;
            midpoint = (currentAABB.getMin().x() + currentAABB.getMax().x()) / 2.0;
        } else if (widthY >= widthX && widthY >= widthZ) {
            longestAxis = 1;
            midpoint = (currentAABB.getMin().y() + currentAABB.getMax().y()) / 2.0;
        } else {
            longestAxis = 2;
            midpoint = (currentAABB.getMin().z() + currentAABB.getMax().z()) / 2.0;
        }
        List<Intersectable> leftList = new ArrayList<>();
        List<Intersectable> rightList = new ArrayList<>();
        
        for (Intersectable geometry : geometries) {
            AABB geometryAABB = geometry.getAABB();
            double center;
            if (longestAxis == 0) {
                center = (geometryAABB.getMin().x() + geometryAABB.getMax().x()) / 2.0;
            } else if (longestAxis == 1) {
                center = (geometryAABB.getMin().y() + geometryAABB.getMax().y()) / 2.0;
            } else {
                center = (geometryAABB.getMin().z() + geometryAABB.getMax().z()) / 2.0;
            }
            
            if (center < midpoint) {
                leftList.add(geometry);
            } else {
                rightList.add(geometry);
            }
        }
        
        // Edge Case Guard: If all objects fall on one side (if they have identical center points),
        // force an index-based split to avoid an infinite loop.
        if (leftList.isEmpty() || rightList.isEmpty()) {
            leftList.clear();
            rightList.clear();
            int midIndex = geometries.size() / 2;
            for (int i = 0; i < geometries.size(); i++) {
                if (i < midIndex) {
                    leftList.add(geometries.get(i));
                } else {
                    rightList.add(geometries.get(i));
                }
            }
        }
        
        Geometries leftSubTree = new Geometries();
        leftSubTree.geometries.addAll(leftList);
        leftSubTree.buildTree();
        
        Geometries rightSubTree = new Geometries();
        rightSubTree.geometries.addAll(rightList);
        rightSubTree.buildTree();
        
        this.geometries.clear();
        this.geometries.add(leftSubTree);
        this.geometries.add(rightSubTree);
    }
    
    @Override
    protected List<Intersection> calcIntersectionsHelper(Ray ray, double maxDistance) {
        List<Intersection> intersections = null;
        for (Intersectable geometry : geometries) {
            List<Intersection> geometryIntersections = geometry.calcIntersections(ray, maxDistance);
            if (geometryIntersections != null) {
                if (intersections == null) {
                    intersections = new ArrayList<>(geometryIntersections);
                } else {
                    intersections.addAll(geometryIntersections);
                }
            }
        }
        return intersections;
    }
}
