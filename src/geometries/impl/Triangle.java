package geometries.impl;

import java.util.List;
import java.util.Objects;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Class representing a triangle in 3D space.
 * @author Amichai Feigelson
 */
public final class Triangle extends Polygon {
    /**
     * Constructs a triangle given three vertices.
     * @param p1 The first vertex of the triangle.
     * @param p2 The second vertex of the triangle.
     * @param p3 The third vertex of the triangle.
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3);
    }
    
    @Override
    protected List<Intersection> calcIntersectionsHelper(Ray ray) {
		/*
		* We are using Möller–Trumbore algorithm
		Explanation of the algorithm and naming of variables:
		A, B, C = The vertices of the triangle
		P = The origin point of the ray
		D = The direction vector of the ray
		t = The parameter for the ray equation: P + tD
		u, v = The barycentric coordinates
		E1, E2 = Two edges of the triangle (E1 = B - A, E2 = C - A)
		T = P - A
		P = D x E2
		Q = T x E1
		The formula:
		[t]      1  [Q•E2]
		|u| = ------|P•T |
		[v]    P•E1 [Q•D ]
		P•E1 is the determinant of the matrix [-D, E1, E2] so I will name it 'det'.
		If P•E1 is zero then the ray and triangle are parallel so there isn't an intersection.
		Else, if u > 0 and v > 0 and (u + v) < 1 and t > 0 then there is an intersection.
		Else there isn't and intersection.
		 */
        Vector Edge1 = _vertices.get(1).subtract(_vertices.get(0));
        Vector Edge2 = _vertices.get(2).subtract(_vertices.get(0));
        Vector normal = getNormal(_vertices.get(0));
        
        if (isZero(ray.direction().dotProduct(normal))) {
            // this means the ray is parallel to the triangle
            return null;
        }
        
        if (_vertices.get(0).equals(ray.origin())) {
            return null;
        }
        
        Vector P = ray.direction().crossProduct(Edge2);
        double det = P.dotProduct(Edge1);
        
        if (isZero(det)) {
            return null;
        }
        
        Vector T = ray.origin().subtract(_vertices.get(0));
        
        if (isZero(T.dotProduct(normal))) {
            return null;
        }
        
        Vector Q;
        try {
            Q = T.crossProduct(Edge1);
        } catch (IllegalArgumentException e) {
            return null;
        }
        
        double u = alignZero(P.dotProduct(T) / det);
        if (u <= 0 || u >= 1) {
            return null;
        }
        
        double v = alignZero(Q.dotProduct(ray.direction()) / det);
        if (v <= 0 || u + v >= 1) {
            return null;
        }
        
        double t = alignZero(Q.dotProduct(Edge2) / det);
        if (t <= 0) {
            return null;
        }
        
        return List.of(new Intersection(this, ray.getPoint(t)));
    }
    
    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null || getClass() != other.getClass())
            return false;
        return super.equals(other);
    }
    
    @Override
    public String toString() {
        return "Triangle: Vertex 1: " + _vertices.get(0) +
                " Vertex 2: " + _vertices.get(1) +
                " Vertex 3: " + _vertices.get(2);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(_vertices);
    }
}