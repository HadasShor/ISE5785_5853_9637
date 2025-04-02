package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * The `Geometry` class is an abstract base class for all geometric objects.
 * It provides a method to get the normal vector at a given point on the surface of the geometry.
 */
public abstract class Geometry implements Intersectable {
    /**
     * Default constructor for {@code Geometry}.
     * Initializes the geometry with default values.
     * This constructor does not perform any specific initialization actions.
     */
    public Geometry() {
        // Default constructor, no initialization or actions
    }

    /**
     * Returns the normal vector to the geometry at the given point.
     *
     * @param p the point on the surface of the geometry
     * @return the normal vector at the given point
     */
    public abstract Vector getNormal(Point p);
}