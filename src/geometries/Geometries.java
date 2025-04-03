package geometries;

import primitives.*;
import primitives.*;

import java.util.List;

public class Geometries implements Intersectable {

    private final List<Intersectable> geometries = List.of();

    /**
     * Constructs a new `Geometries` object.
     * <p>
     * Initializes the list of geometries to an empty list.
     * </p>
     */
    public Geometries() {}
    /**
     * Constructs a new `Geometries` object.
     * @param geometries
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }
    /**
     * Adds the specified geometries to the list of geometries.
     * @param geometries the geometries to add
     */
    public void add(Intersectable... geometries) {
        for (Intersectable geometry : geometries) {
            this.geometries.add(geometry);
        }
    }


    /**
     * The list of geometries.
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}
