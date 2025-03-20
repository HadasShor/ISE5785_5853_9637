package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * The `Cylinder` class represents a cylinder in 3D space.
 * It is defined by a ray (axis), a radius, and a height.
 */
public class Cylinder extends Tube {
    /** The height of the cylinder. */
    private double height;

    /**
     * Constructs a `Cylinder` with the specified axis ray, radius, and height.
     *
     * @param axis the axis ray of the cylinder
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
     * @param p the point on the surface of the cylinder
     * @return the normal vector at the given point
     */
    @Override
    public Vector getNormal(Point p) {
      return null;
    }
}