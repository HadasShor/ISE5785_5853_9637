package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

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
     * Finds the intersections of the triangle with the specified ray.
     * @param ray the ray to intersect with
     * @return findIntersections
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        // barycentric coordinates
        Vector edge1 = vertices.get(1).subtract(vertices.get(0));
        Vector edge2 = vertices.get(2).subtract(vertices.get(0));
        Vector h = ray.getDirection().crossProduct(edge2);
        double a = edge1.dotProduct(h);
        if (a == 0) {
            return null;
        }
        Vector s = ray.getP0().subtract(vertices.get(0));
        double f = 1 / a;
        double u = f * s.dotProduct(h);
        if (u < 0 || u > 1) {
            return null;
        }
        Vector q = s.crossProduct(edge1);
        double v = f * ray.getDirection().dotProduct(q);
        if (v < 0 || u + v > 1) {
            return null;
        }
        double t = f * edge2.dotProduct(q);
        if (t > 0) {
            return List.of(ray.getP0(t));
        }
        return null;

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