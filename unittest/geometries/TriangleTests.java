package geometries;
import primitives.Point;

import org.junit.jupiter.api.Test;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Triangle} class.
 * <p>
 * This class contains a series of tests for the methods implemented in the {@link Triangle} class,
 * ensuring that the triangle operations behave as expected. The tests include operations such as
 * normal calculation.
 * </p>
 */
class TriangleTests {

    /**
     * Default constructor for {@code TriangleTests}.
     * <p>
     * Initializes the test class without any specific setup or initialization.
     * </p>
     */
    public TriangleTests() {
        // Default constructor, no initialization or actions
    }

    /**
     * Test method for {@link Triangle#getNormal(Point)}.
     * <p>
     * Verifies that the {@code getNormal()} method correctly computes the normal vector at a point on the surface of the triangle.
     * </p>
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // Checking if the normal is correctly calculated for a valid triangle
        Vector expectedNormal = new Vector(1, 1, 1).normalize();
        assertEquals(expectedNormal, new Triangle(new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0, 1)).getNormal(new Point(0, 0, 0)), "ERROR: Triangle getNormal() does not work correctly");
    }
}