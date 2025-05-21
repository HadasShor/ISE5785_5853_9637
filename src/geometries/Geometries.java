package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * The {@code Geometries} class represents a collection of {@link Intersectable} objects,
 * such as spheres, planes, triangles, etc. It serves as a composite design pattern,
 * allowing the management and intersection calculations of a group of geometries.
 * <p>
 * This class provides methods to add geometries to the collection and to find
 * intersection points of a given {@link Ray} with all the geometries in the collection.
 * </p>
 */
public class Geometries extends Intersectable {

    /**
     * The list of geometries contained in this collection.
     * <p>
     * This list holds all the {@link Intersectable} objects that are part of this collection.
     * </p>
     */
    private final List<Intersectable> geometries = new LinkedList<>();

    /**
     * Constructs an empty {@code Geometries} object.
     * <p>
     * Initializes the internal list of geometries to an empty list, allowing geometries
     * to be added later using the {@link #add(Intersectable...)} method.
     * </p>
     */
    public Geometries() {
    }

    /**
     * Constructs a {@code Geometries} object and adds the given {@link Intersectable} geometries to it.
     * <p>
     * This constructor allows initializing the collection with one or more geometries.
     * </p>
     *
     * @param geometries one or more {@link Intersectable} objects to add to the collection.
     *                   If no geometries are provided, the collection will remain empty.
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    /**
     * Adds one or more {@link Intersectable} geometries to the current collection.
     * <p>
     * This method allows adding multiple geometries at once using varargs. Each geometry
     * passed as an argument will be added to the internal list of geometries managed by this object.
     * </p>
     *
     * @param geometries one or more {@link Intersectable} objects to be added.
     *                   Can pass zero or more geometries.
     */
    public void add(Intersectable... geometries) {
        Collections.addAll(this.geometries, geometries);
    }

    /**
     * Finds the intersections of a ray with all geometries in the collection.
     * <p>
     * This method iterates through all the geometries in the collection and calculates
     * the intersection points of the given {@link Ray} with each geometry. If there are
     * no intersections, the method returns {@code null}.
     * </p>
     *
     * @param ray the {@link Ray} to find intersections with.
     * @return a list of intersection points, or {@code null} if no intersections are found.
     */
    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray) {
        List<Intersection> intersections = null;
        for (Intersectable geometry : geometries) {
            List<Intersection> geoIntersections = geometry.calculateIntersectionsHelper(ray);
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