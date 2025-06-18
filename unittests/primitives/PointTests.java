package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Point} class.
 * <p>
 * This class contains a series of tests for the methods implemented in the {@link Point} class,
 * ensuring that the point operations behave as expected. The tests include operations such as subtraction,
 * addition, and distance calculations.
 * </p>
 */
class PointTests {
    /**
     * Default constructor for the PointTests class.
     * This constructor is intentionally empty.
     */
    public PointTests() {
        // Default constructor
    }
    /**
     * A point in 3D space with coordinates (1, 2, 3).
     */
    private final Point point1 = new Point(1, 2, 3);

    /**
     * A point in 3D space with coordinates (4, 5, 6).
     */
    private final Point point2 = new Point(4, 5, 6);

    /**
     * A vector with coordinates (1, 2, 3).
     */
    private final Vector vector1 = new Vector(1, 2, 3);

    /**
     * Test method for {@link Point#subtract(Point)}.
     * <p>
     * Verifies that the {@code subtract()} method correctly computes the vector resulting from subtracting
     * one point from another. Also checks that an exception is thrown when subtracting the same point.
     * </p>
     */
    @Test
    void testSubtract() {
        assertEquals(vector1, new Point(2, 4, 6).subtract(point1), "ERROR: Point - Point does not work correctly");
        assertThrows(IllegalArgumentException.class, () -> point1.subtract(point1), "ERROR: Point - Point subtraction does not throw an exception when subtracting the same point");
    }

    /**
     * Test method for {@link Point#add(Vector)}.
     * <p>
     * Verifies that the {@code add()} method correctly computes the point resulting from adding a vector to a point.
     * </p>
     */
    @Test
    void testAdd() {
        assertEquals(new Point(2, 4, 6), point1.add(vector1), "ERROR: Point + Vector does not work correctly");
        assertEquals(Point.ZERO, point1.add(new Vector(-1, -2, -3)), "ERROR: Point + Vector does not work correctly");
    }

    /**
     * Test method for {@link Point#distanceSquared(Point)}.
     * <p>
     * Verifies that the {@code distanceSquared()} method correctly computes the squared distance between two points.
     * </p>
     */
    @Test
    void testDistanceSquared() {
        assertEquals(27, point1.distanceSquared(point2), "ERROR: Distance Point does not work correctly");
        assertEquals(0, point1.distanceSquared(point1), 0.00001, "ERROR: Distance Point does not work correctly");
    }

    /**
     * Test method for {@link Point#distance(Point)}.
     * <p>
     * Verifies that the {@code distance()} method correctly computes the distance between two points.
     * </p>
     */
    @Test
    void testDistance() {
        assertEquals(3, point1.distance(new Point(0, 4, 5)), 0.00001, "ERROR: Distance Point does not work correctly");
        assertEquals(0, point1.distance(point1), "ERROR: Distance Point does not work correctly");
    }
}