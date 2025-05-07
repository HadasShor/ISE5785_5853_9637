package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

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
     * <p>
     * This method calculates the intersection points of the given ray with the triangle. If the ray intersects the triangle,
     * the points of intersection are returned. Otherwise, the method returns {@code null}.
     * </p>
     *
     * @param ray the ray to intersect with
     * @return a list of intersection points, or {@code null} if no intersection occurs
     */
    @Override
    public List<Point> findIntersections(Ray ray) {

        // Vector calculations for the triangle edges
        Vector edge1 = vertices.get(1).subtract(vertices.get(0));
        Vector edge2 = vertices.get(2).subtract(vertices.get(0));

        // Begin Möller–Trumbore algorithm
        Vector h = ray.getDirection().crossProduct(edge2);
        double a = alignZero(edge1.dotProduct(h));

        // Ray is parallel to the triangle
        if (isZero(a)) {
            return null;
        }

        double f = 1 / a;
        Vector s = ray.getHead().subtract(vertices.get(0));
        double u = f * alignZero(s.dotProduct(h));

        // Intersection point is outside the triangle
        if (u <= 0 || u >= 1) {
            return null;
        }

        Vector q = s.crossProduct(edge1);
        double v = f * ray.getDirection().dotProduct(q);

        // Intersection point is outside the triangle
        if (v <= 0 || u + v >= 1) {
            return null;
        }

        // Compute intersection distance along the ray
        double t = alignZero(f * edge2.dotProduct(q));

        // Intersection is behind the ray's origin
        if (t <= 0) {
            return null;
        }

        // Return the intersection point
        return List.of(ray.getHead().add(ray.getDirection().scale(t)));
    }

}