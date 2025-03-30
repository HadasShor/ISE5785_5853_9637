package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import static org.junit.jupiter.api.Assertions.*;

class PlaneTests {

    final private Point point1 = new Point(1, 2, 3);
    final private Point point2 = new Point(2, 4, 6);
    final private Point point3 = new Point(3, 6, 9);
    final private Point point4 = new Point(1, 1, 1);
    final private Plane plane = new Plane(point1, point2, point4);

    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // Test that the normal is calculated correctly
        Vector expectedNormal = point2.subtract(point1).crossProduct(point4.subtract(point1)).normalize();
        assertEquals(expectedNormal, plane.getNormal(new Point(8, 2, 3)));
    }

    @Test
    void testConstructor() {
        // =============== Boundary Values Tests ==================
        // Test when two or more points are the same
        assertThrows(IllegalArgumentException.class, () -> new Plane(point1, point1, point2));
        assertThrows(IllegalArgumentException.class, () -> new Plane(point2, point1, point1));
        assertThrows(IllegalArgumentException.class, () -> new Plane(point1, point2, point1));
        assertThrows(IllegalArgumentException.class, () -> new Plane(point1, point1, point1));

        // Test when all three points are collinear
        assertThrows(IllegalArgumentException.class, () -> new Plane(point1, point2, point3));

        // ============ Equivalence Partitions Tests ==============
        // Test that a valid plane is created and its normal is correct
        Vector expectedNormal = point2.subtract(point1).crossProduct(point4.subtract(point1)).normalize();
        assertEquals(expectedNormal, plane.getNormal(point4), "The normal is not correct");
        assertEquals(1, plane.getNormal(point4).length(), "The normal is not normalized");
        assertEquals(0, plane.getNormal(point4).dotProduct(point2.subtract(point1)), "The normal is not orthogonal to the plane");
        assertEquals(0, plane.getNormal(point4).dotProduct(point3.subtract(point1)), "The normal is not orthogonal to the plane");
    }
}
