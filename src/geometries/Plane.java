package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * The `Plane` class represents a plane in 3D space.
 * It is defined by a point on the plane and a normal vector.
 */
public class Plane extends Geometry {

    /** A point on the plane. */
    protected final Point point;
    /** The normal vector to the plane. */
    protected final Vector normal;

    /**
     * Constructs a `Plane` with the specified point and normal vector.
     *
     * @param point the point on the plane
     * @param normal the normal vector to the plane
     */
    public Plane(Point point, Vector normal) {
        this.point = point;
        this.normal = normal.normalize();
    }

    /**
     * Constructs a `Plane` with three points on the plane.
     * The normal vector is calculated from these points.
     *
     * @param point1 the first point on the plane
     * @param point2 the second point on the plane
     * @param point3 the third point on the plane
     */
    public Plane(Point point1, Point point2,Point point3 ) {
        normal=null;
        point=point1;
    }

    /**
     * Returns the normal vector to the plane at the given point.
     *
     * @param p the point on the plane
     * @return the normal vector at the given point
     */
    @Override
    public Vector getNormal(Point p) {
        return normal;
    }
}