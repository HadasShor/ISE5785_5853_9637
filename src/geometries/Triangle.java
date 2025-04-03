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
     * Returns the normal vector to the triangle at the given point.
     *
     * @param p the point on the surface of the triangle
     * @return the normal vector at the given point
     */
    @Override
    public Vector getNormal(Point p) {
        return super.getNormal(p);
    }

    /**
     * Finds the intersections of the triangle with the specified ray.
     * @param ray the ray to intersect with
     * @return findIntersections
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        // barycentric coordinates
        /**
         * The barycentric coordinates of a point P with respect to a triangle ABC are a set of three numbers α, β, γ
         */
        Vector edge1 = vertices.get(1).subtract(vertices.get(0));
        /**
         * The vector from the first vertex to the second vertex
         */
        Vector edge2 = vertices.get(2).subtract(vertices.get(0));
        /**
         * The vector from the first vertex to the third vertex
         */
        Vector h = ray.getDirection().crossProduct(edge2);
        /**
         * The cross product of the ray direction and the vector from the first vertex to the third vertex
         */
        double a = edge1.dotProduct(h);
        /**
         * The dot product of the vector from the first vertex to the second vertex and h
         */
        if (a == 0) {
            return null;
        }
        /**
         * If a is equal to 0, the ray is parallel to the triangle
         */
        Vector s = ray.getP0().subtract(vertices.get(0));
        /**
         * The vector from the first vertex to the ray's starting point
         */
        double f = 1 / a;
        /**
         * The reciprocal of a
         */
        double u = f * s.dotProduct(h);
        /**
         * The dot product of s and h
         */
        if (u < 0 || u > 1) {
            return null;
        }
        /**
         * If u is less than 0 or greater than 1, the intersection point is outside the triangle
         */
        Vector q = s.crossProduct(edge1);
        /**
         * The cross product of s and the vector from the first vertex to the second vertex
         */
        double v = f * ray.getDirection().dotProduct(q);
        /**
         * The dot product of the ray direction and q
         */
        if (v < 0 || u + v > 1) {
            return null;
        }
        /**
         * If v is less than 0 or u + v is greater than 1, the intersection point is outside the triangle
         */
        double t = f * edge2.dotProduct(q);
        /**
         * The dot product of the vector from the first vertex to the third vertex and q
         */
        if (t > 0) {
            return List.of(ray.getP0(t));
        }
        return null;

    }
}