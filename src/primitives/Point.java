package primitives;
import java.util.Objects;
/**
 * The `Point` class represents a point in 3D space.
 * It provides methods for basic geometric operations such as addition, subtraction, and distance calculation.
 */
public class Point {
    /** A constant representing the origin point (0,0,0). */
    public static final Object ZERO = new Point(0, 0, 0);

    /** The coordinates of the point stored as a `Double3` object. */
    final protected Double3 xyz;

    /**
     * Constructs a `Point` with the specified coordinates.
     *
     * @param xyz the coordinates of the point as a `Double3` object
     */
    public Point(Double3 xyz) {
        this.xyz = xyz;
    }

    /**
     * Constructs a `Point` with the specified coordinates.
     *
     * @param x the x-coordinate of the point
     * @param y the y-coordinate of the point
     * @param z the z-coordinate of the point
     */
    public Point(double x, double y, double z) {
        this(new Double3(x, y, z));
    }

    /**
     * Checks if this point is equal to another object.
     *
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return (o instanceof Point point)
                && this.xyz.equals(point.xyz);
    }



    /**
     * Returns the hash code of this point.
     *
     * @return the hash code of this point
     */

    @Override
    public int hashCode()
    {
        return Objects.hash(xyz);
    }

    /**
     * Returns a string representation of this point.
     *
     * @return a string representation of this point
     */
    @Override
    public String toString() {
        return "" + xyz;
    }

    /**
     * Subtracts another point from this point and returns the resulting vector.
     *
     * @param point the point to subtract
     * @return the resulting vector
     */
    public Vector subtract(Point point) {
        return new Vector(xyz.subtract(point.xyz));
    }

    /**
     * Adds a vector to this point and returns the resulting point.
     *
     * @param vector the vector to add
     * @return the resulting point
     */
//   public Point add(Vector vector) {
//        return new Point(xyz.add(vector.xyz));
//    }
    public Point add(Vector vector) {
        if (vector == null) {
            throw new IllegalArgumentException("Vector cannot be null");
        }
        return new Point(xyz.add(vector.xyz));
    }
    /**
     * Calculates the squared distance to another point.
     *
     * @param point the point to calculate the distance to
     * @return the squared distance to the point
     */
    public double distanceSquared(Point point) {
        double x2x1 = point.xyz.d1() - xyz.d1();
        double y2y1 = point.xyz.d2() - xyz.d2();
        double z2z1 = point.xyz.d3() - xyz.d3();
        return x2x1 * x2x1 + y2y1 * y2y1 + z2z1 * z2z1;
    }

    /**
     * Calculates the distance to another point.
     *
     * @param p1 the point to calculate the distance to
     * @return the distance to the point
     */
    public double distance(Point p1) {
        return Math.sqrt(distanceSquared(p1));
    }
}