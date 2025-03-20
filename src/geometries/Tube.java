package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * The `Tube` class represents a tube in 3D space.
 * It is defined by a ray (axis) and a radius.
 */
public class Tube extends RadialGeometry {
    /** The axis ray of the tube. */
    protected final Ray axis;

    /**
     * Constructs a `Tube` with the specified axis ray and radius.
     *
     * @param axis the axis ray of the tube
     * @param radius the radius of the tube
     */
    public Tube(Ray axis, double radius) {
        super(radius);
        this.axis = axis;
    }

    /**
     * Returns the normal vector to the tube at the given point.
     *
     * @param p the point on the surface of the tube
     * @return the normal vector at the given point
     */
    @Override
    public Vector getNormal(Point p) {
        return null;
    }
}