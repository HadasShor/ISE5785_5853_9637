package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.ArrayList;
import java.util.List;

import static primitives.Util.isZero;

/**
 * Represents an infinite tube in 3D space.
 * A tube is defined by a central axis (represented by a ray) and a constant radius.
 * The tube extends infinitely in both directions along its axis.
 */
public class Tube extends RadialGeometry {
    /**
     * The central axis ray of the tube.
     */
    protected final Ray axis;

    /**
     * Constructs a Tube object with the specified axis ray and radius.
     *
     * @param axis   the axis ray that defines the direction and center of the tube
     * @param radius the radius of the tube
     * @throws IllegalArgumentException if the radius is not positive
     */
    public Tube(Ray axis, double radius) {
        super(radius);
        this.axis = axis;
    }

    /**
     * Returns the normal vector to the surface of the tube at a given point.
     *
     * @param p0 a point on the surface of the tube
     * @return the normalized normal vector at the given point
     * @throws IllegalArgumentException if the given point is not on the tube's surface
     */
    @Override
    public Vector getNormal(Point p0) {
        return p0.subtract(
                axis.getHead().add(
                        axis.getDirection().scale(
                                axis.getDirection().dotProduct(p0.subtract(axis.getHead()))
                        )
                )
        ).normalize();
    }

    /**
     * Finds the intersection points between the tube and a given ray.
     *
     * @param ray the ray to check for intersections with the tube
     * @return a list of intersection points, or {@code null} if there are no intersections
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        Point P = ray.getHead();
        Vector d = ray.getDirection();

        Point C = axis.getHead();
        Vector v = axis.getDirection();

        Vector delta = P.subtract(C);

        double dDotV = d.dotProduct(v);
        Vector dPerp = d.subtract(v.scale(dDotV));

        double deltaDotV = delta.dotProduct(v);
        Vector deltaPerp = delta.subtract(v.scale(deltaDotV));

        double A = dPerp.lengthSquared();
        double B = 2 * deltaPerp.dotProduct(dPerp);
        double C_coeff = deltaPerp.lengthSquared() - radius * radius;

        if (isZero(A))
            return null;

        double disc = B * B - 4 * A * C_coeff;
        if (disc < 0)
            return null;

        double sqrtDisc = Math.sqrt(disc);
        double t1 = (-B + sqrtDisc) / (2 * A);
        double t2 = (-B - sqrtDisc) / (2 * A);

        List<Point> intersections = new ArrayList<>();
        if (t1 > Util.ZERO) {
            intersections.add(ray.getPoint(t1));
        }
        if (t2 > Util.ZERO) {
            intersections.add(ray.getPoint(t2));
        }

        return intersections.isEmpty() ? null : intersections;
    }
}
