package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;

/**
 * Sphere class represents a sphere in 3D Cartesian coordinate system.
 */
public class Sphere extends RadialGeometry {
    /**
     * The center point of the object.
     */
    protected final Point center;


    /**
     * Constructor for Sphere.
     *
     * @param p      the center point of the sphere
     * @param radius the radius of the sphere
     */
    public Sphere(double radius, Point p) {
        super(radius);
        this.center = p;
    }

    @Override
    public Vector getNormal(Point point) {
        return point.subtract(center).normalize();
    }

    @Override
    //public List<Point> findIntersections(Ray ray) {
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {

        Vector v = ray.getDirection();
        Point p0 = ray.getHead();

        // if the ray starts at the center of the sphere

        if (p0.equals(center))
            return List.of(new Intersection(this,p0.add(v.scale(radius)))); // return List.of(center.add(v.scale(radius)));
            //return List.of(p0.add(v.scale(radius)));

        Vector u = center.subtract(p0);
        double tm = alignZero(v.dotProduct(u));
        double d2 = alignZero(u.lengthSquared() - tm * tm);

        if (alignZero(d2 - radius * radius) >= 0)
            return null;

        double th = Math.sqrt(alignZero(radius * radius - d2));
        double t1 = alignZero(tm - th);
        double t2 = alignZero(tm + th);

        if (t1 > 0 && t2 > 0)
            return List.of(new Intersection(this,ray.getPoint(t1)),new Intersection(this, ray.getPoint(t2)));

        if (t1 > 0)
            return List.of(new Intersection(this,ray.getPoint(t1)));

        if (t2 > 0)
            return List.of(new Intersection(this,ray.getPoint(t2)));

        return null;

    }
}