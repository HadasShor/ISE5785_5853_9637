package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Vector} class.
 * <p>
 * This class contains a series of tests for the methods implemented in the {@link Vector} class,
 * ensuring that the vector operations behave as expected. The tests include operations such as addition,
 * length calculation, normalization, dot product, cross product, and scaling.
 * </p>
 */
class VectorTests {

    /**
     * Default constructor for {@code VectorTests}.
     * <p>
     * Initializes the test class without any specific setup or initialization.
     * </p>
     */
    public VectorTests() {
        // Default constructor, no initialization or actions
    }

    // Vectors used for testing
    /**
     * A vector with coordinates (1, 2, 3).
     */
    private final Vector vector1 = new Vector(1, 2, 3);

    /**
     * A vector with coordinates (2, 4, 6), which is a scaled version of {@link #vector1}.
     */
    private final Vector vector2 = new Vector(2, 4, 6);

    /**
     * A vector with coordinates (-1, -2, -3), the negative of {@link #vector1}.
     */
    private final Vector vector3 = new Vector(-1, -2, -3);

    /**
     * A vector with coordinates (2, 1, 2).
     */
    private final Vector vector4 = new Vector(2, 1, 2);

    /**
     * A vector with coordinates (-2, -1, -2), the negative of {@link #vector4}.
     */
    private final Vector vector5 = new Vector(-2, -1, -2);

    /**
     * A vector with coordinates (2, 2, 2).
     */
    private final Vector vector6 = new Vector(2, 2, 2);

    /**
     * A vector with coordinates (1, 2, -3).
     */
    private final Vector vector7 = new Vector(1, 2, -3);

    /**
     * A unit vector along the Y-axis (0, 1, 0).
     */
    private final Vector vector8 = new Vector(0, 1, 0);

    /**
     * A unit vector along the Z-axis (0, 0, 1).
     */
    private final Vector vector9 = new Vector(0, 0, 1);

    /**
     * A vector with coordinates (4, 4, 4).
     */
    private final Vector vector10 = new Vector(4, 4, 4);

    /**
     * A unit vector along the X-axis (1, 0, 0).
     */
    private final Vector vector11 = new Vector(1, 0, 0);

    /**
     * A vector with coordinates (1, 4, -3).
     */
    private final Vector vector12 = new Vector(1, 4, -3);


    /**
     * Test method for {@link Vector#add(Vector)}.
     * <p>
     * Verifies that the {@code add()} method correctly computes the sum of two vectors.
     * Also checks that an exception is thrown when adding a zero vector.
     * </p>
     */
    @Test
    void testAdd() {
        assertEquals(vector2, vector1.add(new Vector(1, 2, 3)), "add() wrong result");
        assertThrows(IllegalArgumentException.class, () -> vector1.add(vector3), "add() should throw exception for zero vector");
    }

    /**
     * Test method for {@link Vector#lengthSquared()}.
     * <p>
     * Verifies that the {@code lengthSquared()} method correctly computes the squared length of a vector.
     * </p>
     */
    @Test
    void testLengthSquared() {
        assertEquals(14, vector1.lengthSquared(), 0.00001, "lengthSquared() wrong result");
    }

    /**
     * Test method for {@link Vector#length()}.
     * <p>
     * Verifies that the {@code length()} method correctly computes the length of a vector.
     * </p>
     */
    @Test
    void testLength() {
        assertEquals(Math.sqrt(9), vector4.length(), 0.00001, "length() wrong result");
        assertEquals(Math.sqrt(9), vector5.length(), 0.00001, "length() wrong result");
    }

    /**
     * Test method for {@link Vector#normalize()}.
     * <p>
     * Verifies that the {@code normalize()} method correctly normalizes a vector, making it a unit vector.
     * </p>
     */
    @Test
    void testNormalize() {
        assertEquals(new Vector(2.0 / 3, 1.0 / 3, 2.0 / 3), vector4.normalize(), "normalize() wrong result");
        assertEquals(new Vector(-2.0 / 3, -1.0 / 3, -2.0 / 3), vector5.normalize(), "normalize() wrong result");
    }

    /**
     * Test method for {@link Vector#dotProduct(Vector)}.
     * <p>
     * Verifies that the {@code dotProduct()} method correctly computes the dot product of two vectors.
     * </p>
     */
    @Test
    void testDotProduct() {
        assertEquals(0, vector6.dotProduct(vector7), 0.00001, "dotProduct() wrong result with zero");
        assertEquals(14, vector1.dotProduct(vector1), 0.00001, "dotProduct() wrong result");
        assertEquals(-14, vector1.dotProduct(vector3), 0.00001, "dotProduct() wrong result");
        assertEquals(0, vector8.dotProduct(vector9), 0.00001, "dotProduct() wrong result for perpendicular vectors");
    }

    /**
     * Test method for {@link Vector#crossProduct(Vector)}.
     * <p>
     * Verifies that the {@code crossProduct()} method correctly computes the cross product of two vectors.
     * It also checks that an exception is thrown when the vectors are parallel.
     * </p>
     */
    @Test
    void testCrossProduct() {
        assertEquals(new Vector(1, 4, -3), vector1.crossProduct(vector4), "Error from cross product of vectors");
        assertThrows(IllegalArgumentException.class, () -> vector6.crossProduct(vector10), "Exception should be thrown for parallel vectors");
        assertEquals(new Vector(0, 0, 1), vector11.crossProduct(vector8), "Error from cross product of perpendicular vectors");
    }

    /**
     * Test method for {@link Vector#scale(double)}.
     * <p>
     * Verifies that the {@code scale()} method correctly scales a vector by a scalar value.
     * It also checks that an exception is thrown when scaling by zero.
     * </p>
     */
    @Test
    void testScale() {
        assertEquals(vector2, vector1.scale(2), "Error from scale of vector");
        assertThrows(IllegalArgumentException.class, () -> vector1.scale(0), "Error from scaling vector by zero");
    }
}