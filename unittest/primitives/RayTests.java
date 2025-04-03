package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class RayTests {

    /**
     * Default constructor for {@code RayTests}.
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