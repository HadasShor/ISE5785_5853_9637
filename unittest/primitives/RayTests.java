package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Ray} class.
 * <p>
 * This class contains tests for verifying the behavior of the {@link Ray} class,
 * including the correct initialization of the ray with a head point and direction vector,
 * as well as testing the functionality of the {@link Ray#getPoint(double)} method.
 * </p>
 */
class RayTests {
    /**
     * Default constructor for {@code RayTests}.
     * Initializes the test class for testing the {@link Ray} class.
     */
    public RayTests() {
        // Default constructor, no initialization required
    }

    /**
     * A point used for initializing rays.
     */
    Point p= new Point(1, 2, 3);
    /**
     * A ray with a head point and a direction vector.
     */
    Ray ray1= new Ray(p, new Vector(4, 5, 6));
    /**
     * A ray with a head point and a direction vector.
     */
    Ray ray2= new Ray(new Point(1, 0, 0), new Vector(0, 0, 1));
    /**
     * Test method for {@link Ray#Ray(Point, Vector)}.
     * <p>
     * Verifies that the constructor correctly initializes the ray with the specified head point and direction vector.
     * </p>
     */
    @Test
    void testGetPoint() {
        assertEquals(new Point(1, 2, 3), ray1.getPoint(0));  // תקין
        assertEquals(new Point(1, 0, 1), ray2.getPoint(1));
        assertEquals(new Point(1, 0, -1), ray2.getPoint(-1));
    }

}