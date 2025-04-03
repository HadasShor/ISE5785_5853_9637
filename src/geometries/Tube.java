package geometries;

import primitives.*;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

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
     * @param p0 the point on the surface of the tube
     * @return the normal vector at the given point
     */
    @Override
    public Vector getNormal(Point p0) {

            //  חישוב ההיטל של P_0 על וקטור הכיוון של הקרן
         //   double t = axis.getDirection().dotProduct(p0.subtract(axis.getPoint()));

            // 2️ מוצאים את נקודת המרכז O על הציר
           // Point o = axis.getPoint().add(axis.getDirection().scale(t));

            // 3 מחשבים את וקטור הנורמל
           // Vector n = p0.subtract(o);

            // 4 מנרמלים את הווקטור ומחזירים אותו
            return p0.subtract(axis.getP0().add(axis.getDirection().scale(axis.getDirection().dotProduct(p0.subtract(axis.getP0()))))).normalize();

    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}