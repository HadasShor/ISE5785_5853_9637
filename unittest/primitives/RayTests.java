package primitives;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class RayTests {

    /**
     * Default constructor for {@code RayTests}.
     */
    Point p = new Point(1, 2, 3);
    /**
     * A ray with a head point and a direction vector.
     */
    Ray ray1 = new Ray(p, new Vector(4, 5, 6));
    /**
     * A ray with a head point and a direction vector.
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
        assertEquals(new Point(1, 2, 3), ray1.getPoint(0));  // תקין
        assertEquals(new Point(1, 0, 1), ray2.getPoint(1));
        assertEquals(new Point(1, 0, -1), ray2.getPoint(-1));
    }

    @Test
    void testFindClosedPoint() {
        List<Point> points = List.of( new Point(10, 20, 30),new Point(1, 2, 3), new Point(100, 0, -1));
        List<Point> points1 = null;
        List<Point> points2 = List.of(new Point(1, 2, 3), new Point(10, 20, 30), new Point(100, 0, -1));
        List<Point> points3 = List.of( new Point(10, 20, 30),new Point(1, 2, 3));
        assertEquals( ray1.findClosedPoint(points2), new Point(1, 2, 3)); // תקין
        assertEquals(ray1.findClosedPoint(points1), null); // תקין
        assertEquals(ray1.findClosedPoint(points2), new Point(1, 2, 3)); // תקין
        assertEquals(ray1.findClosedPoint(points3), new Point(1, 2, 3)); // תקין

    }
}