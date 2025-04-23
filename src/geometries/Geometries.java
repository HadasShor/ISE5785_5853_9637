package geometries;

import primitives.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Geometries} class represents a collection of {@link Intersectable} objects,
 * such as spheres, planes, triangles, etc. It allows managing and performing intersection
 * calculations with a group of geometries.
 */
public class Geometries implements Intersectable {

    /**
     * The list of geometries contained in this collection.
     */
    private final List<Intersectable> geometries = new ArrayList<>();

    /**
     * Constructs an empty {@code Geometries} object.
     * <p>
     * Initializes the internal list of geometries to an empty list.
     * </p>
     */
    public Geometries() {}

    /**
     * Constructs a {@code Geometries} object and adds the given {@link Intersectable} geometries to it.
     *
     * @param geometries one or more {@link Intersectable} objects to add to the collection.
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    /**
     * Adds one or more {@link Intersectable} geometries to the current collection.
     *
     * <p>This method allows adding multiple geometries at once using varargs. Each geometry
     * passed as an argument will be added to the internal list of geometries managed by this object.</p>
     *
     * @param geometries one or more {@link Intersectable} objects to be added.
     *                   Can pass zero or more geometries.
     */
    public void add(Intersectable... geometries) {
        for (Intersectable geometry : geometries) {
            this.geometries.add(geometry);
        }
    }

    /**
     * Finds the intersection points between a given {@link Ray} and all the geometries in this collection.
     *
     * @param ray the {@link Ray} to intersect with the geometries.
     * @return a list of {@link Point} objects where the ray intersects the geometries,
     *         or {@code null} if there are no intersections.
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        return null; // To be implemented
    }
}