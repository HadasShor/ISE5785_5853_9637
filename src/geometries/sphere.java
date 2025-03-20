package geometries;

import primitives.Point;
import primitives.Vector;
/**
 * The `Sphere` class represents a sphere in 3D space.
 * It is defined by a center point and a radius.
 */
public class sphere extends RadialGeometry{
    /** The center point of the sphere. */
   final private Point center;
    /**
     * Returns the normal vector to the sphere at the given point.
     *
     * @param p the point on the surface of the sphere
     * @return the normal vector at the given point
     */
   @Override
   public Vector getNormal(Point p) {
       return null;
   }
    /**
     * Constructs a `Sphere` with the specified center point and radius.
     *
     * @param center the center point of the sphere
     * @param radius the radius of the sphere
     */
    public sphere(Point _center, double _radius) {
        super(_radius);
        center = _center;
    }
}
