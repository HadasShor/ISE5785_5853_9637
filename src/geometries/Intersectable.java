package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;

/**
 * The `Intersectable` interface represents a geometric object that can be intersected by a ray.
 * It provides a method to find the intersections of the object with a ray.
 */
public interface Intersectable {
    /**
     * Finds the intersections of the object with the specified ray.
     *
     * @param ray the ray to intersect with
     * @return a list of intersection points, or {@code null} if there are no intersections
     */
    List<Point> findIntersections(Ray ray);

}
