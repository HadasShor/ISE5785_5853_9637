//package geometries;
//
//import primitives.Point;
//import primitives.Ray;
//import primitives.Util;
//import primitives.Vector;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static primitives.Util.alignZero;
//import static primitives.Util.isZero;
//
///**
// * Represents an infinite tube in 3D space.
// * A tube is defined by a central axis (represented by a ray) and a constant radius.
// * The tube extends infinitely in both directions along its axis.
// */
//public class Tube extends RadialGeometry {
//    /**
//     * The central axis ray of the tube.
//     */
//    protected final Ray axis;
//
//    /**
//     * Constructs a Tube object with the specified axis ray and radius.
//     *
//     * @param axis   the axis ray that defines the direction and center of the tube
//     * @param radius the radius of the tube
//     * @throws IllegalArgumentException if the radius is not positive
//     */
//    public Tube(Ray axis, double radius) {
//        super(radius);
//        this.axis = axis;
//    }
//
//    /**
//     * Returns the normal vector to the surface of the tube at a given point.
//     *
//     * @param p0 a point on the surface of the tube
//     * @return the normalized normal vector at the given point
//     * @throws IllegalArgumentException if the given point is not on the tube's surface
//     */
//    @Override
//    public Vector getNormal(Point p0) {
//        return p0.subtract(
//                axis.getHead().add(
//                        axis.getDirection().scale(
//                                axis.getDirection().dotProduct(p0.subtract(axis.getHead()))
//                        )
//                )
//        ).normalize();
//    }
//
//    @Override
//    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
//        // protected List<Intersection> calculateIntersectionsHelper(Ray ray)
//        // Get ray origin and direction
//        final Point rayOrigin = ray.getHead();
//        final Point axisPoint = axis.getHead(); // Cylinder axis starting point
//        final Vector axisDir = axis.getDirection(); // Cylinder axis direction
//        final Vector rayDir = ray.getDirection();
//
//        Vector deltaP;
//        boolean isDeltaPZero;
//        try {
//            // Vector from cylinder axis point to ray origin
//            deltaP = rayOrigin.subtract(axisPoint);
//            isDeltaPZero = false;
//        } catch (IllegalArgumentException e) {
//            // Special case: ray origin lies exactly on the axis line
//            deltaP = null;
//            isDeltaPZero = true;
//        }
//
//        double rayDirDotAxis = rayDir.dotProduct(axisDir);
//        // Compute quadratic coefficients for intersection equation
//        double a = rayDir.dotProduct(rayDir) - rayDirDotAxis * rayDirDotAxis;
//        double b, c;
//
//        if (isDeltaPZero) {
//            // Special case handling if deltaP is zero
//            b = 0;
//            c = -radius*radius;
//        } else {
//            // General case
//            double deltaPDotAxis = deltaP.dotProduct(axisDir);
//
//            // Coefficient b of quadratic equation
//            b = 2 * (rayDir.dotProduct(deltaP) - rayDirDotAxis * deltaPDotAxis);
//
//            // Coefficient c of quadratic equation
//            c = deltaP.dotProduct(deltaP) - deltaPDotAxis * deltaPDotAxis - radius*radius;
//        }
//
//        // Calculate discriminant to determine intersection existence
//        double discriminant = alignZero(b * b - 4 * a * c);
//        if (discriminant <= 0) return null; // No real solutions → no intersection
//
//        double sqrtDiscriminant = Math.sqrt(discriminant);
//        double denominator = 2 * a;
//
//        // quadratic parameter 'a' is always positive in our equation, therefore t2 is always greater than t1
//        // Calculate the intersection parameters (t values)
//        double t2 = alignZero((-b + sqrtDiscriminant) / denominator);
//        if (t2 <= 0) return null; // No valid intersection
//
//        double t1 = alignZero((-b - sqrtDiscriminant) / denominator);
//        return t1 <= 0 ?
//
//                List.of(new Intersection(this, ray.getPoint(t2))) :
//                List.of(new Intersection(this, ray.getPoint(t1)),
//                        new Intersection(this, ray.getPoint(t2)));
//    }
//}


package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.ArrayList;
import java.util.List;

import static primitives.Util.alignZero;
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
     *
     * @return the normalized normal vector at the given point
     * @throws IllegalArgumentException if the given point is not on the tube's surface
     */
    @Override
    public Vector getNormal(Point point) {
        Point head = axis.getHead();
        Vector direction = axis.getDirection();

        Vector v = point.subtract(head);
        double t = v.dotProduct(direction);
        return point.subtract(axis.getPoint(t)).normalize();
    }

    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
        // protected List<Intersection> calculateIntersectionsHelper(Ray ray)
        // Get ray origin and direction
        final Point rayOrigin = ray.getHead();
        final Point axisPoint = axis.getHead(); // Cylinder axis starting point
        final Vector axisDir = axis.getDirection(); // Cylinder axis direction
        final Vector rayDir = ray.getDirection();

        Vector deltaP;
        boolean isDeltaPZero;
        try {
            // Vector from cylinder axis point to ray origin
            deltaP = rayOrigin.subtract(axisPoint);
            isDeltaPZero = false;
        } catch (IllegalArgumentException e) {
            // Special case: ray origin lies exactly on the axis line
            deltaP = null;
            isDeltaPZero = true;
        }

        double rayDirDotAxis = rayDir.dotProduct(axisDir);
        // Compute quadratic coefficients for intersection equation
        double a = rayDir.dotProduct(rayDir) - rayDirDotAxis * rayDirDotAxis;
        double b, c;

        if (isDeltaPZero) {
            // Special case handling if deltaP is zero
            b = 0;
            c = -radius*radius;
        } else {
            // General case
            double deltaPDotAxis = deltaP.dotProduct(axisDir);

            // Coefficient b of quadratic equation
            b = 2 * (rayDir.dotProduct(deltaP) - rayDirDotAxis * deltaPDotAxis);

            // Coefficient c of quadratic equation
            c = deltaP.dotProduct(deltaP) - deltaPDotAxis * deltaPDotAxis - radius*radius;
        }

        // Calculate discriminant to determine intersection existence
        double discriminant = alignZero(b * b - 4 * a * c);
        if (discriminant <= 0) return null; // No real solutions → no intersection

        double sqrtDiscriminant = Math.sqrt(discriminant);
        double denominator = 2 * a;

        // quadratic parameter 'a' is always positive in our equation, therefore t2 is always greater than t1
        // Calculate the intersection parameters (t values)
        double t2 = alignZero((-b + sqrtDiscriminant) / denominator);
        if (t2 <= 0) return null; // No valid intersection

        double t1 = alignZero((-b - sqrtDiscriminant) / denominator);
        return t1 <= 0 ?

                List.of(new Intersection(this, ray.getPoint(t2))) :
                List.of(new Intersection(this, ray.getPoint(t1)),
                        new Intersection(this, ray.getPoint(t2)));
    }
}