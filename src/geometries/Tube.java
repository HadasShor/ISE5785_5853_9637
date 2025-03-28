package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import static primitives.Util.alignZero;
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
     * @param point the point on the surface of the tube
     * @return the normal vector at the given point
     */
    //@Override
//    public Vector getNormal(Point p) {
//        //Vector tv= axis.getNurmalVector().crossProduct(p.subtract(axis.getPoint())).crossProduct(axis.getNurmalVector());
//       //Point o=axis.getPoint().add(tv);
//       //Vector n=p.subtract(o).normalize();
//        return p.subtract(axis.getPoint().add(
//                axis.getNurmalVector().crossProduct(
//                        p.subtract(axis.getPoint())).crossProduct(
//                                axis.getNurmalVector()))).normalize();
//    }

    @Override
    public Vector getNormal(Point point) {
        //calculate the projection of the point on the axis
        double t = alignZero(this.axis.getNurmalVector().dotProduct(point.subtract(this.axis.getPoint(0d))));

        //find center of the tube
        //return the normalized vector from the center of the tube to the point
        return point.subtract(this.axis.getPoint(t)).normalize();
    }

}