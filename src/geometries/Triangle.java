package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * The `Triangle` class represents a triangle in 3D space.
 * It is defined by three vertices and extends the `Polygon` class.
 */
public class Triangle extends Polygon {

    /**
     * Constructs a `Triangle` with the specified vertices.
     *
     * @param p1 the first vertex of the triangle
     * @param p2 the second vertex of the triangle
     * @param p3 the third vertex of the triangle
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3);
    }

    /**
     * Returns the normal vector to the triangle at the given point.
     *
     * @param p the point on the surface of the triangle
     * @return the normal vector at the given point
     */
    @Override
    public Vector getNormal(Point p) {
        return super.getNormal(p);
    }
}