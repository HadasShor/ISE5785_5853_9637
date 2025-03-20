package primitives;

import java.util.Objects;

/**
 * The `Ray` class represents a ray in 3D space.
 * A ray is defined by a starting point (head) and a direction vector.
 */
public class Ray {
    /** The starting point of the ray. */
    private final Point head;
    /** The direction vector of the ray. */
    private final Vector direction;

    /**
     * Constructs a `Ray` with the specified head point and direction vector.
     *
     * @param head the starting point of the ray
     * @param direction the direction vector of the ray
     */
    public Ray(Point head, Vector direction) {
        this.head = head;
        this.direction = direction.normalize();
    }

    /**
     * Checks if this ray is equal to another object.
     *
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return (o instanceof Ray ray)
                && this.direction.equals(ray.direction)
                && this.head.equals(ray.head);
    }

    /**
     * Returns the hash code value for this ray.
     *
     * @return the hash code value for this ray
     */
    @Override
    public int hashCode() {
        return Objects.hash(head, direction);
    }

    /**
     * Returns a string representation of this ray.
     *
     * @return a string representation of this ray
     */
    @Override
    public String toString() {
        return "Ray{" +
                "head=" + head +
                ", direction=" + direction +
                '}';
    }
}