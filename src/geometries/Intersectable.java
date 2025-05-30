package geometries;

import lighting.LightSource;
import primitives.Material;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * The `Intersectable` interface represents a geometric object that can be intersected by a ray.
 * It provides a method to find the intersections of the object with a ray.
 */
public abstract class Intersectable {

    /**
     * Finds the intersection points of the object with the specified ray.
     * This method delegates to {@link #calculateIntersections(Ray)} and extracts only the point component of the intersections.
     *
     * @param ray The ray to intersect with the object.
     * @return A list of intersection points, or {@code null} if there are no intersections.
     */
    public List<Point> findIntersections(Ray ray) {
        var list = calculateIntersections(ray);
        return list == null ? null : list.stream().map(intersection -> intersection.point).toList();
    }

    /**
     * Helper method to calculate the detailed intersections of the object with the specified ray.
     * Subclasses must implement this method to provide specific intersection logic.
     *
     * @param ray The ray to intersect with.
     * @return A list of {@link Intersection} objects, each containing the geometry and the intersection point,
     * or {@code null} if no intersections are found.
     */
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
        return null;
    }

    /**
     * Calculates the detailed intersections of the object with the specified ray.
     * This method is final to ensure that the intersection calculation mechanism is consistent across all subclasses.
     *
     * @param ray The ray to intersect with.
     * @return A list of {@link Intersection} objects, each containing the geometry and the intersection point,
     * or {@code null} if no intersections are found.
     */
    public final List<Intersection> calculateIntersections(Ray ray) {
        return calculateIntersectionsHelper(ray);
    }

    /**
     * Represents a single intersection point between a ray and a geometric object.
     * This inner class holds information about the geometry, the intersection point itself,
     * and additional properties relevant for shading calculations.
     */
    public static class Intersection {

        /**
         * The geometry object that was intersected.
         */
        public final Geometry geometry;
        /**
         * The 3D point of intersection on the geometry's surface.
         */
        public final Point point;
        /**
         * The material properties of the geometry at the intersection point.
         */
        public final Material material;
        /**
         * The direction of the incident ray at the intersection point.
         */
        public Vector rayDir;
        /**
         * The normal vector to the geometry's surface at the intersection point.
         */
        public Vector normal;
        /**
         * The scalar product of the normal vector and the ray direction, used in lighting calculations.
         */
        public double scaleNR;
        /**
         * The light source illuminating the intersection point.
         */
        public LightSource lightSource;
        /**
         * The direction vector from the intersection point to the light source.
         */
        public Vector lightDir;
        /**
         * The scalar product of the normal vector and the light direction, used in lighting calculations.
         */
        public double scaleNL;

        /**
         * Constructs an Intersection object with the intersected geometry and the intersection point.
         * The material is derived from the provided geometry.
         *
         * @param geometry The {@link Geometry} object that was intersected.
         * @param point    The {@link Point} of intersection.
         */
        public Intersection(Geometry geometry, Point point) {
            if (geometry != null)
                this.material = geometry.getMaterial();
            else
                this.material = new Material(); // Default material if geometry is null
            this.geometry = geometry;
            this.point = point;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Intersection other = (Intersection) obj;
            return this.geometry == other.geometry && this.point.equals(other.point);
        }

        @Override
        public String toString() {
            return "Intersection{" +
                    "geometry=" + geometry +
                    ", point=" + point +
                    '}';
        }
    }
}