package geometries;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * The `Geometry` class is an abstract base class for all geometric objects.
 * It provides a method to get the normal vector at a given point on the surface of the geometry.
 */
public abstract class Geometry extends Intersectable {


    protected Color emission = Color.BLACK; // Default emission color

    public Color getEmission() {
        return emission;
    }

    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }
    /**
     * Returns the normal vector to the geometry at the given point.
     *
     * @param p the point on the surface of the geometry
     * @return the normal vector at the given point
     */
    public abstract Vector getNormal(Point p);
}