package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * The `RadialGeometry` class represents a radial geometry in 3D space.
 * It is defined by a radius.
 */
public abstract class RadialGeometry extends Geometry {
    /** The radius of the radial geometry. */
    protected final double radius;

    /**
     * Constructs a `RadialGeometry` with the specified radius.
     *
     * @param radius the radius of the radial geometry
     */
    public RadialGeometry(double radius) {
        this.radius = radius;
    }
}