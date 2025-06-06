package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.alignZero;


import java.util.Comparator;


import static primitives.Util.alignZero;

/**
 * The `Cylinder` class represents a cylinder in 3D space.
 * It extends the `Tube` class and adds a height parameter.
 */
public class Cylinder extends Tube {



    /**
     * To represent bottomPlane of the cylinder.
     */
    private final Plane bottomPlane;

    /**
     * To represent topPlane of the cylinder.
     */
    private final Plane topPlane;
    /**
     * The height of the cylinder.
     */
    private final double height;

    /**
     * Constructs a `Cylinder` with the specified axis ray, radius, and height.
     *
     * @param axis   the axis ray of the cylinder
     * @param radius the radius of the cylinder
     * @param height the height of the cylinder
     */
    public Cylinder(Ray axis, double radius, double height) {
        super(axis, radius);
        this.height = height;

        this.bottomPlane = new Plane(axis.getHead(), axis.getDirection());
        this.topPlane = new Plane(axis.getPoint(height), axis.getDirection());
    }

    /**
     * Returns the normal vector to the cylinder at the given point.
     *
     * @param p0 the point on the surface of the cylinder
     * @return the normal vector at the given point
     */


    @Override
    public Vector getNormal(Point p0) {
        Point head = axis.getHead();
        Vector dir = axis.getDirection();
        Point point = axis.getPoint();
        // Check if the point is at the base center
        if (p0.equals(axis.getHead())) {
            return dir.scale(-1);
        }
        // Check if the point is at the top base center
        if (p0.equals(axis.getPoint(height))) {
            return dir;
        }

        if (p0.subtract(axis.getHead()).dotProduct(dir) == 0.0 &&
                p0.distanceSquared(axis.getHead()) <= radius * radius) {
            return dir.scale(-1);
        }

        if (p0.subtract(axis.getPoint(height)).dotProduct(dir) == 0.0 &&
                p0.distanceSquared(axis.getPoint(height)) <= radius * radius) {
            return dir;
        }


        return super.getNormal(p0);
    }

//    @Override
//    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
//        final List<Intersection> intersections = new LinkedList<>();
//        final Vector axisDir = axis.getDirection();
//        final Point baseCenter = axis.getHead();
//        final Point topCenter = axis.getPoint(height);
//        final Point rayOrigin = ray.getHead();
//
//        // 1. Tube intersections
//        List<Intersection> tubeIntersections = super.calculateIntersectionsHelper(ray);
//        if (tubeIntersections != null) {
//            for (Intersection p : tubeIntersections) {
//                double axisProjection = axisDir.dotProduct(p.point.subtract(baseCenter));
//                if (alignZero(axisProjection) >= 0 && alignZero(axisProjection - height) <= 0) {
//                    intersections.add(new Intersection(this, p.point));
//                }
//            }
//        }
//
//        // 2. Bottom cap
//        List<Point> bottom = bottomPlane.findIntersections(ray);
//        if (bottom != null) {
//            Point p = bottom.getFirst();
//            if (alignZero(p.distanceSquared(baseCenter) - radius*radius) < 0) {
//                intersections.add(new Intersection(this, p));
//            }
//        }
//
//        // 3. Top cap
//        List<Point> top = topPlane.findIntersections(ray);
//        if (top != null) {
//            Point p = top.getFirst();
//            if (alignZero(p.distanceSquared(topCenter) - radius*radius) < 0) {
//                intersections.add(new Intersection(this, p));
//            }
//        }
//
//        // 4. Sort by distance
//        intersections.sort(Comparator.comparingDouble(p ->
//                p.point.subtract(rayOrigin).dotProduct(ray.getDirection())));
//
//        return intersections.isEmpty() ? null : intersections;
//    }
    double radiusSquared= radius * radius;
    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
    final List<Intersection> intersections = new LinkedList<>();
    final Vector axisDir = axis.getDirection();
    final Point baseCenter = axis.getHead();
    final Point topCenter = axis.getPoint(height);
    final Point rayOrigin = ray.getHead();

        // 1. Tube intersections
        List<Intersection> tubeIntersections = super.calculateIntersectionsHelper(ray);
        if (tubeIntersections != null) {
            for (Intersection p : tubeIntersections) {
                double axisProjection = axisDir.dotProduct(p.point.subtract(baseCenter));
                if (alignZero(axisProjection) >= 0 && alignZero(axisProjection - height) <= 0) {
                    intersections.add(new Intersection(this, p.point));
                }
            }
        }


        // 2. Bottom cap
        List<Point> bottom = bottomPlane.findIntersections(ray);
        if (bottom != null) {
            Point p = bottom.getFirst();
            if (alignZero(p.distanceSquared(baseCenter) - radius*radius) < 0) {
                intersections.add(new Intersection(this, p));
            }
        }
//
        // 3. Top cap
        List<Point> top = topPlane.findIntersections(ray);
        if (top != null) {
            Point p = top.getFirst();
            if (alignZero(p.distanceSquared(topCenter) - radius*radius) < 0) {
                intersections.add(new Intersection(this, p));
            }
        }







    // 4. Sort by distance
    intersections.sort(Comparator.comparingDouble(p ->
            p.point.subtract(rayOrigin).dotProduct(ray.getDirection())));

    return intersections.isEmpty() ? null : intersections;
}
}

