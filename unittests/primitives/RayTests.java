package primitives;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@code Ray} class, containing methods to test the functionality
 * of ray operations such as getting points along the ray and finding the closest point
 * to a given list of points.
 * This class contains unit tests for the methods of the {@link Ray} class, specifically
 * verifying correct behavior when interacting with points and vectors.
 */
class RayTests {

    /**
     * A point representing the location of the ray's head in 3D space.
     */
    Point p = new Point(1, 2, 3);

    /**
     * A ray object with a specified head point {@code p} and a direction vector.
     */
    Ray ray1 = new Ray(p, new Vector(4, 5, 6));

    /**
     * A ray object with a head point at (1, 0, 0) and a direction vector along the Z-axis.
     */
    Ray ray2 = new Ray(new Point(1, 0, 0), new Vector(0, 0, 1));

    /**
     * Test method for {@link Ray#Ray(Point, Vector)}.
     * <p>
     * Verifies that the constructor correctly initializes the ray with the specified head point and direction vector.
     * </p>
     */
    @Test
    void testGetPoint() {
        // Verifying that the getPoint method returns the correct points along the ray for given scalars.
        assertEquals(new Point(1, 2, 3), ray1.getPoint(0));  // Expected point is the head of the ray
        assertEquals(new Point(1, 0, 1), ray2.getPoint(1));  // Expected point at distance 1 along the ray
        assertEquals(new Point(1, 0, -1), ray2.getPoint(-1)); // Expected point at distance -1 along the ray
    }

    /**
     * Test method for {@link Ray#findClosedPoint(List)}.
     * <p>
     * Verifies the functionality of finding the closest point to the ray from a list of points.
     * </p>
     */
    @Test
    void testFindClosedPoint() {
        // List of points to check against the ray
        List<Point> points = List.of(new Point(10, 20, 30), new Point(1, 2, 3), new Point(100, 0, -1));

        // Testing with different lists of points
        List<Point> points1 = null; // null list (edge case)
        List<Point> points2 = List.of(new Point(1, 2, 3), new Point(10, 20, 30), new Point(100, 0, -1)); // List with points, including ray's own point
        List<Point> points3 = List.of(new Point(10, 20, 30), new Point(1, 2, 3)); // List with points including the ray's own point

        // Asserting that the closest point to ray1 is the point (1, 2, 3)
        assertEquals(ray1.findClosedPoint(points2), new Point(1, 2, 3)); // Correct: the ray's own point is the closest
        assertEquals(ray1.findClosedPoint(points1), null); // Correct: no points in the list, so null is returned
        assertEquals(ray1.findClosedPoint(points2), new Point(1, 2, 3)); // Correct: again, the ray's own point is the closest
        assertEquals(ray1.findClosedPoint(points3), new Point(1, 2, 3)); // Correct: the ray's own point is the closest
    }
}
