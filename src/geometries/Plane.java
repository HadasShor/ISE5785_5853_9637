package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.isZero;

/**
 * The {@code Plane} class represents a geometric plane in 3D space.
 * A plane is defined either by a point and a normal vector or by three points.
 */
public class Plane extends Geometry {

    /**
     * A reference point on the plane.
     */
    protected final Point q;

    /**
     * The normal vector of the plane.
     */
    protected final Vector normal;

    /**
     * Constructs a {@code Plane} using a given point on the plane and its normal vector.
     *
     * @param point  a point on the plane
     * @param normal the normal vector to the plane (will be normalized)
     */
    public Plane(Point point, Vector normal) {
        this.q = point;
        this.normal = normal.normalize();
    }

    /**
     * Constructs a {@code Plane} using three points in 3D space.
     * The normal is computed as the cross product of the vectors formed by the points.
     *
     * @param point1 the first point on the plane
     * @param point2 the second point on the plane
     * @param point3 the third point on the plane
     */
    public Plane(Point point1, Point point2, Point point3) {
        this.normal = point2.subtract(point1).crossProduct(point3.subtract(point1)).normalize();
        this.q = point1;
    }

    /**
     * Returns the normal vector to the plane at a specific point.
     * Since the normal is constant for a plane, the point is ignored.
     *
     * @param p the point (ignored)
     * @return the normal vector of the plane
     */
    @Override
    public Vector getNormal(Point p) {
        return normal;
    }

    /**
     * Finds the intersection point(s) between a given ray and this plane.
     *
     * @param ray the ray to test for intersection with the plane
     * @return a list containing the intersection point, or {@code null} if no intersection exists
     */
    public List<Point> findIntersections(Ray ray) {
        Vector v = ray.getDirection();
        Point p0 = ray.getHead();

        // Special case: the ray origin is on the plane
        if (p0.equals(q)) {
            return null;
        }

        Vector q_p0 = q.subtract(p0);

        // Special case: the vector from the ray origin to the plane point is orthogonal to the plane
        if (isZero(normal.dotProduct(q_p0))) {
            return null;
        }

        double nv = normal.dotProduct(v);

        // If the ray is parallel to the plane (dot product is zero), no intersection
        if (isZero(nv)) {
            return null;
        }

        // Compute the intersection scalar 't'
        double t = normal.dotProduct(q_p0) / nv;

        // If the intersection is behind the ray's origin or at the origin, it's not considered
        if (t <= 0) {
            return null;
        }

        // Return the intersection point
        return List.of(ray.getPoint(t));
    }
}
