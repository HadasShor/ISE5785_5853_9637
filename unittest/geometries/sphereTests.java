package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link sphere} class.
 * <p>
 * This class contains a series of tests for the methods implemented in the {@link sphere} class,
 * ensuring that the sphere operations behave as expected. The tests include operations such as
 * normal calculation.
 * </p>
 */
class SphereTests {

    /**
     * Default constructor for {@code SphereTests}.
     * <p>
     * Initializes the test class without any specific setup or initialization.
     * </p>
     */
    public SphereTests() {
        // Default constructor, no initialization or actions
    }

    /**
     * A point in 3D space with coordinates (1, 1, 1).
     */
    final Point point1 = new Point(1, 1, 1);

    /**
     * A point in 3D space with coordinates (2, 2, 2).
     */
    final Point point2 = new Point(2, 2, 2);

    /**
     * Test method for {@link sphere#getNormal(Point)}.
     * <p>
     * Verifies that the {@code getNormal()} method correctly computes the normal vector at a point on the surface of the sphere.
     * </p>
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // Test to ensure the normal is correctly calculated at a point on the surface of the sphere
        Vector expectedNormal = new Vector(1, 1, 1).normalize();
        assertEquals(expectedNormal, new sphere(point1, 1).getNormal(point2), "getNormal() did not return the correct normal vector");
    }
}