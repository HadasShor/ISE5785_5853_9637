package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
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
    public Geometries() {
    }

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
        Collections.addAll(this.geometries, geometries);
    }

    /**
     * Finds the intersections of a ray with all geometries in the list.
     *
     * @param ray the ray to find intersections with
     * @return a list of intersection points
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> intersections = null;
        for (Intersectable geometry : geometries) {
            List<Point> geoIntersections = geometry.findIntersections(ray);
            if (geoIntersections != null) {
                if (intersections == null) {
                    intersections = new LinkedList<>();
                }
                intersections.addAll(geoIntersections);
            }
        }
        return intersections;
    }
}