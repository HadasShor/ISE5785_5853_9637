package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Plane} class.
 */
class PlaneTests {

    private final Point p1 = new Point(0, 1, 1);
    private final Point p2 = new Point(0, 0, 2);
    private final Point p3 = new Point(0, 0, 1);
    private final Vector v1 = new Vector(0, 1, 1);
    private final Vector v2 = new Vector(0, 0, 1);
    private final Vector v3 = new Vector(0, 1, 0);
    private final Vector v4 = new Vector(0, 0, 1);
    private final Plane planY= new Plane(p3, v4);

    /**
     * Default constructor for {@code Plane}.
     * Initializes the Plane with default values.
     */
    public  PlaneTests() {
        // Default constructor, no initialization or actions
    }

    /**
     * A point in 3D space with coordinates (1, 2, 3).
     */
    private final Point point1 = new Point(1, 2, 3);

    /**
     * A point in 3D space with coordinates (2, 4, 6).
     */
    private final Point point2 = new Point(2, 4, 6);

    /**
     * A point in 3D space with coordinates (3, 6, 9).
     */
    private final Point point3 = new Point(3, 6, 9);

    /**
     * A point in 3D space with coordinates (1, 1, 1).
     */
    private final Point point4 = new Point(1, 1, 1);

    /**
     * A plane defined by three non-collinear points: point1, point2, and point4.
     */
    private final Plane plane = new Plane(point1, point2, point4);

    /**
     * Test method for {@link Plane#getNormal(Point)}.
     *
     * This test verifies that the normal vector of the plane is calculated correctly.
     * It compares the computed normal with the expected normal obtained using
     * the cross product of two vectors in the plane.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // Test that the normal is calculated correctly
        Vector expectedNormal = point2.subtract(point1).crossProduct(point4.subtract(point1)).normalize();
        assertEquals(expectedNormal, plane.getNormal(new Point(8, 2, 3)));
    }

    /**
     * Test method for {@link Plane#Plane(Point, Point, Point)}.
     *
     * This test verifies the correctness of the plane constructor:
     * - Ensures it throws an exception when two or more points are identical.
     * - Ensures it throws an exception when the three points are collinear.
     * - Verifies that a valid plane is created when using three non-collinear points.
     */
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
    @Test
    void testGetIntersection() {

        // ============ Equivalence Partitions Tests ==============

        // TC01: Test that the ray does not intersect the plane
        assertNull(planY.findIntersections(new Ray(p2, v1)), "Failed to find the intersection point when the ray does not intersect the plane");

        // TC02: Test that the ray intersect the plane
        assertEquals(List.of(p1), planY.findIntersections(new Ray(p2, new Vector(0, 1, -1))), "Failed to find the intersection point when the ray intersect the plane");

        // =============== Boundary Values Tests =================

        // TC03: Test that the ray is parallel to the plane
        assertNull(planY.findIntersections(new Ray(p2, v3)), "Failed to find the intersection point when the ray is parallel to the plane");

        // TC04: Test that the ray is parallel to the plane and included in the plane
        assertNull(planY.findIntersections(new Ray(p1, v3)), "Failed to find the intersection point when the ray is parallel to the plane and included in the plane");

        // TC05: T that the ray is orthogonal to the plane
        assertEquals(List.of(p3), planY.findIntersections(new Ray(new Point(0, 0, -1), v2)), "Failed to find the intersection point when the ray is orthogonal to the plane");

        // TC06: Test that the ray is orthogonal to the plane and start in the plane
        assertNull(planY.findIntersections(new Ray(p3, v2)), "Failed to find the intersection point when the ray is orthogonal to the plane and start in the plane");

        // TC07: Test that the ray is orthogonal to the plane and start outside the plane
        assertNull(planY.findIntersections(new Ray(p2, v2)), "Failed to find the intersection point when the ray is orthogonal to the plane and start outside the plane");

        // TC08: Test that the ray start at the plane
        assertNull(planY.findIntersections(new Ray(p1, v1)), "Failed to find the intersection point when the ray start at the plane");

        // TC09: Test that the ray start at the plane at the point that sent to the constructor
        assertNull(planY.findIntersections(new Ray(p3, v1)), "Failed to find the intersection point when the ray start at the plane at the point that sent to the constructor");
    }

}
