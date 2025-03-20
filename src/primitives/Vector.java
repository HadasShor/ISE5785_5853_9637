package primitives;

/**
 * The `Vector` class represents a vector in 3D space.
 * It extends the `Point` class and provides additional methods for vector operations.
 */
public class Vector extends Point {

    /**
     * Constructs a `Vector` with the specified coordinates.
     *
     * @param x the x-coordinate of the vector
     * @param y the y-coordinate of the vector
     * @param z the z-coordinate of the vector
     * @throws IllegalArgumentException if the vector is a zero vector
     */
    public Vector(double x, double y, double z) {
        super(x, y, z);
        if (xyz.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Zero vector is not allowed");
        }
    }



    /**
     * Constructs a `Vector` with the specified coordinates.
     *
     * @param xyz the coordinates of the vector as a `Double3` object
     * @throws IllegalArgumentException if the vector is a zero vector
     */
    public Vector(Double3 xyz) {
        super(xyz);
        if (xyz.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Zero vector is not allowed");
        }
    }

    /**
     * Checks if this vector is equal to another object.
     *
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return (o instanceof Vector vector)
                && this.xyz.equals(vector.xyz);
    }


    /**
     * Returns a string representation of this vector.
     *
     * @return a string representation of this vector
     */
    @Override
    public String toString() {
        return "Vector" + xyz;
    }

    /**
     * Adds another vector to this vector and returns the resulting vector.
     *
     * @param vector the vector to add
     * @return the resulting vector
     */
    public Vector add(Vector vector) {
        return new Vector(xyz.add(vector.xyz));
    }

    /**
     * Calculates the squared length of this vector.
     *
     * @return the squared length of this vector
     */
    public double lengthSquared() {
        return xyz.d1() * xyz.d1() + xyz.d2() * xyz.d2() + xyz.d3() * xyz.d3();
    }

    /**
     * Calculates the length of this vector.
     *
     * @return the length of this vector
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * Normalizes this vector to a unit vector.
     *
     * @return the normalized vector
     */
    public Vector normalize() {
        double length = length();
        if (length == 0) {
            throw new ArithmeticException("Cannot normalize zero vector");
        }
        return new Vector(xyz.reduce(length));
    }

    /**
     * Calculates the dot product with another vector.
     *
     * @param vector the vector to calculate the dot product with
     * @return the dot product
     */
    public double dotProduct(Vector vector) {
        return xyz.d1() * vector.xyz.d1() + xyz.d2() * vector.xyz.d2() + xyz.d3() * vector.xyz.d3();
    }

    /**
     * Calculates the cross product with another vector.
     *
     * @param vector the vector to calculate the cross product with
     * @return the cross product vector
     */
    public Vector crossProduct(Vector vector) {
        double x = xyz.d2() * vector.xyz.d3() - xyz.d3() * vector.xyz.d2();
        double y = xyz.d3() * vector.xyz.d1() - xyz.d1() * vector.xyz.d3();
        double z = xyz.d1() * vector.xyz.d2() - xyz.d2() * vector.xyz.d1();
        return new Vector(x, y, z);
    }
    public Vector scale(double scalar) {
        return new Vector(xyz.scale(scalar));
    }

}