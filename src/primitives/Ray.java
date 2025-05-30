package primitives;

import geometries.Intersectable.*;

import java.util.List;
import java.util.Objects;

/**
 * The {@code Ray} class represents a ray in 3D space.
 * A ray is defined by a starting point (head) and a direction vector.
 */
public class Ray {
    /**
     * The starting point of the ray.
     */
    private final Point head;

    /**
     * The normalized direction vector of the ray.
     */
    private final Vector direction;

    /**
     * Constructs a {@code Ray} with the specified head point and direction vector.
     * The direction is normalized upon creation.
     *
     * @param head      the starting point of the ray
     * @param direction the direction vector of the ray
     */
    public Ray(Point head, Vector direction) {
        this.head = head;
        this.direction = direction.normalize();
    }

    /**
     * Returns the normalized direction vector of the ray.
     *
     * @return the normalized direction vector.
     */
    public Vector getDirection() {
        // The direction is already normalized in the constructor, so we can just return it.
        return direction;
    }

    /**
     * Returns a point on the ray at a given distance from the head point using parameter {@code t}.
     * If the distance is approximately zero, the head point itself is returned.
     *
     * @param t the distance from the ray's origin.
     * @return the point located {@code t} units in the direction of the ray.
     */
    public Point getHead(double t) {
        if (Util.isZero(t))
            return head;
        return head.add(direction.scale(t));
    }

    /**
     * Returns a point on the ray at a given distance from the head point.
     * If the distance is approximately zero, the head point itself is returned.
     *
     * @param distance the distance from the ray's origin.
     * @return the point located {@code distance} units in the direction of the ray.
     */
    public Point getPoint(double distance) {
        if (Util.isZero(distance))
            return head;
        return head.add(direction.scale(distance));
    }

    /**
     * Returns the head (origin) point of the ray.
     *
     * @return the starting point of the ray.
     */
    public Point getPoint() {
        return head;
    }

    /**
     * Returns the head (origin) point of the ray.
     * This method is identical to {@link #getPoint()}.
     *
     * @return the starting point of the ray.
     */
    public Point getHead() {
        return head;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param o the reference object with which to compare.
     * @return {@code true} if this ray is equal to the specified object; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // The 'direction' is already normalized in the constructor, so we can directly compare.
        return (o instanceof Ray ray)
                && this.direction.equals(ray.direction)
                && this.head.equals(ray.head);
    }

    /**
     * Returns a hash code value for this ray.
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(head, direction);
    }

    /**
     * Returns a string representation of the ray.
     *
     * @return a string describing the ray.
     */
    @Override
    public String toString() {
        return "Ray{" +
                "head=" + head +
                ", direction=" + direction +
                '}';
    }

    /**
     * Finds the closest point to the ray's origin from a given list of points.
     *
     * @param points The list of points to check.
     * @return The closest point to the ray's origin, or {@code null} if the list is empty or {@code null}.
     */
    public Point findClosedPoint(List<Point> points) {
        if (points == null || points.isEmpty()) {
            return null;
        }
        // Map points to Intersection objects (geometry is null as it's not relevant here)
        // Then find the closest intersection and return its point.
        return findClosestIntersection(points.stream().map(p -> new Intersection(null, p)).toList()).point;
    }

    /**
     * Finds the closest intersection point to the ray's origin from a given list of intersections.
     *
     * @param intersections The list of intersections to check.
     * @return The closest intersection to the ray's origin, or {@code null} if the list is empty or {@code null}.
     */
    public Intersection findClosestIntersection(List<Intersection> intersections) {
        if (intersections == null || intersections.isEmpty()) {
            return null;
        }
        double minDistanceSquared = Double.MAX_VALUE;
        Intersection closestIntersection = null;

        for (Intersection i : intersections) {
            double distanceSquared = i.point.distanceSquared(head);
            if (distanceSquared < minDistanceSquared) {
                minDistanceSquared = distanceSquared;
                closestIntersection = i;
            }
        }
        return closestIntersection;
    }
}