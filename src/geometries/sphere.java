package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;

/**
 * The `Sphere` class represents a sphere in 3D space.
 * It is defined by a center point and a radius.
 */
public class sphere extends RadialGeometry{
    /** The center point of the sphere. */
   final protected Point center;
    /**
     * Returns the normal vector to the sphere at the given point.
     *
     * @param p the point on the surface of the sphere
     * @return the normal vector at the given point
     */
   @Override
   public Vector getNormal(Point p) {
       return p.subtract(center).normalize();
   }
    /**
     * Constructs a `Sphere` with the specified center point and radius.
     *
     * @param center the center point of the sphere
     * @param radius the radius of the sphere
     */
    public sphere(Point center, double radius) {
        super(radius);
        this.center = center;
    }

    @Override
    public List<Point> findIntersections(Ray ray) {

        Vector v = ray.getDirection();
        Point p0 = ray.getP0();
        if(p0.equals(center)) {//if the ray start at the center of the sphere
            return List.of(p0.add(v.scale(radius)));
        }

        Vector u = center.subtract(p0);
        double tm =alignZero( v.dotProduct(u));
        double dd = alignZero((u.lengthSquared() - tm * tm));
        if (alignZero(dd- radius*radius) > 0) {//if the ray not intersect the sphere
            return null;
        }
        double th = alignZero(Math.sqrt(radius * radius - dd));
        if (th == 0) {//if the ray is tangent to the sphere
            return null;
        }
        double t1 = alignZero(tm + th);
        double t2 = alignZero(tm - th);
        if (t1 > 0 && t2 > 0) {//if the ray intersect the sphere in two points
            return List.of(ray.getPoint(t1), ray.getPoint(t2));
        }
        if (t1 > 0) {//if the ray intersect the sphere in one point
            return List.of(ray.getPoint(t1));
        }
        if (t2 > 0) {//if the ray intersect the sphere in one point
            return List.of(ray.getPoint(t2));
        }
        return null;
    }
}
