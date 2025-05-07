package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * The `Cylinder` class represents a cylinder in 3D space.
 * It extends the `Tube` class and adds a height parameter.
 */
public class Cylinder extends Tube {


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
    }

    /**
     * Returns the normal vector to the cylinder at the given point.
     *
     * @param p0 the point on the surface of the cylinder
     * @return the normal vector at the given point
     */


    @Override
    public Vector getNormal(Point p0) {
        // Check if the point is at the base center
        if (p0.equals(axis.getHead())) {
            return axis.getDirection().scale(-1);
        }

        // Compute the projection of the point onto the cylinder's axis
        double distance = axis.getDirection().dotProduct(p0.subtract(axis.getHead()));

        // Check if the point is on the bottom base
        if (distance == 0) {
            return axis.getDirection().scale(-1);
        }

        // Check if the point is on the top base
        if (distance == height) {
            return axis.getDirection();
        }

        // If the point is on the curved surface, use the normal from the Tube class
        return super.getNormal(p0);
    }

    /**
     * Finds the intersections of the cylinder with the specified ray.
     *
     * @param ray the ray to intersect with
     * @return a list of intersection points, or {@code null} if there are no intersections
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}