package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;

/**
 * The `Intersectable` interface represents a geometric object that can be intersected by a ray.
 * It provides a method to find the intersections of the object with a ray.
 */
public abstract class Intersectable {
    /**
     * Finds the intersections of the object with the specified ray.
     *
     * @param ray the ray to intersect with
     * @return a list of intersection points, or {@code null} if there are no intersections
     */
    //public abstract List<Point> findIntersections(Ray ray);

    public  List<Point> findIntersections(Ray ray) {
        var list = calculateIntersections(ray);
        return list == null ? null : list.stream().map(intersection -> intersection.point).toList();
    }

    protected abstract List<Intersection> calculateIntersectionsHelper(Ray ray);

    public final List<Intersection> calculateIntersections(Ray ray) {
        return calculateIntersectionsHelper(ray);
    }

    public static class Intersection {
        public final Geometry geometry;
        public final Point point;

        public Intersection(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true; // אם זה אותו אובייקט בדיוק – שווה
            if (obj == null || getClass() != obj.getClass()) return false; // אם null או מסוג אחר – לא שווה
            Intersection other = (Intersection) obj;
            return this.geometry == other.geometry // בדיקת זהות של האובייקט הגיאומטרי (==)
                    && this.point.equals(other.point); // בדיקת שוויון הנקודה (equals)
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
