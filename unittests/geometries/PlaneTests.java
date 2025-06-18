package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Plane} class.
 */
class PlaneTests {

    /** Sample point used for intersection tests */
    private final Point p1 = new Point(0, 1, 1);
    /** Sample point used for intersection tests */
    private final Point p2 = new Point(0, 0, 2);
    /** Sample point used for intersection tests */
    private final Point p3 = new Point(0, 0, 1);

    /** Vector for intersection testing */
    private final Vector v1 = new Vector(0, 1, 1);
    /** Vector for orthogonal ray */
    private final Vector v2 = new Vector(0, 0, 1);
    /** Vector parallel to the plane */
    private final Vector v3 = new Vector(0, 1, 0);
    /** Vector defining the plane's normal */
    private final Vector v4 = new Vector(0, 0, 1);

    /** A sample plane defined by a point and a normal vector */
    private final Plane planY = new Plane(p3, v4);

    /**
     * Default constructor for {@code PlaneTests}.
     * Used for initialization if needed.
     */
    public PlaneTests() {
        // No initialization required
    }

    /** Non-collinear point used to define a valid plane */
    private final Point point1 = new Point(1, 2, 3);
    /** Non-collinear point used to define a valid plane */
    private final Point point2 = new Point(2, 4, 6);
    /** Collinear point used to check invalid plane construction */
    private final Point point3 = new Point(3, 6, 9);
    /** Non-collinear point used to define a valid plane */
    private final Point point4 = new Point(1, 1, 1);

    /** A valid plane defined by three non-collinear points */
    private final Plane plane = new Plane(point1, point2, point4);

    /**
     * Test method for {@link Plane#getNormal(Point)}.
     *
     * <p>This test verifies that the normal vector of the plane is calculated correctly.
     * It compares the computed normal with the expected normal obtained using
     * the cross product of two vectors in the plane.</p>
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
     * <p>This test verifies the correctness of the plane constructor:</p>
     * <ul>
     *     <li>Ensures it throws an exception when two or more points are identical.</li>
     *     <li>Ensures it throws an exception when the three points are collinear.</li>
     *     <li>Verifies that a valid plane is created when using three non-collinear points.</li>
     * </ul>
     */
    @Test
    void testConstructor() {
        // =============== Boundary Values Tests ==================
        assertThrows(IllegalArgumentException.class, () -> new Plane(point1, point1, point2));
        assertThrows(IllegalArgumentException.class, () -> new Plane(point2, point1, point1));
        assertThrows(IllegalArgumentException.class, () -> new Plane(point1, point2, point1));
        assertThrows(IllegalArgumentException.class, () -> new Plane(point1, point1, point1));

        // Test with collinear points
        assertThrows(IllegalArgumentException.class, () -> new Plane(point1, point2, point3));

        // ============ Equivalence Partitions Tests ==============
        Vector expectedNormal = point2.subtract(point1).crossProduct(point4.subtract(point1)).normalize();
        assertEquals(expectedNormal, plane.getNormal(point4), "The normal is not correct");
        assertEquals(1, plane.getNormal(point4).length(), "The normal is not normalized");
        assertEquals(0, plane.getNormal(point4).dotProduct(point2.subtract(point1)), "The normal is not orthogonal to the plane");
        assertEquals(0, plane.getNormal(point4).dotProduct(point3.subtract(point1)), "The normal is not orthogonal to the plane");
    }

    /**
     * Test method for {@link Plane#findIntersections(Ray)}.
     *
     * <p>This test checks various intersection scenarios between a ray and a plane:</p>
     * <ul>
     *     <li>Ray does not intersect the plane</li>
     *     <li>Ray intersects the plane</li>
     *     <li>Ray is parallel to the plane (and lies on it or not)</li>
     *     <li>Ray is orthogonal to the plane</li>
     *     <li>Ray starts at the plane</li>
     *     <li>Ray starts outside the plane</li>
     * </ul>
     */
    @Test
    void testGetIntersection() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray does not intersect the plane
        assertNull(planY.findIntersections(new Ray(p2, v1)),
                "Failed to find the intersection point when the ray does not intersect the plane");

        // TC02: Ray intersects the plane
        assertEquals(List.of(p1), planY.findIntersections(new Ray(p2, new Vector(0, 1, -1))),
                "Failed to find the intersection point when the ray intersect the plane");

        // =============== Boundary Values Tests =================

        // TC03: Ray is parallel to the plane
        assertNull(planY.findIntersections(new Ray(p2, v3)),
                "Failed to find the intersection point when the ray is parallel to the plane");

        // TC04: Ray is parallel to the plane and lies in it
        assertNull(planY.findIntersections(new Ray(p1, v3)),
                "Failed to find the intersection point when the ray is parallel to the plane and included in the plane");

        // TC05: Ray is orthogonal and intersects
        assertEquals(List.of(p3), planY.findIntersections(new Ray(new Point(0, 0, -1), v2)),
                "Failed to find the intersection point when the ray is orthogonal to the plane");

        // TC06: Ray is orthogonal and starts in the plane
        assertNull(planY.findIntersections(new Ray(p3, v2)),
                "Failed to find the intersection point when the ray is orthogonal to the plane and starts in the plane");

        // TC07: Ray is orthogonal and starts outside the plane
        assertNull(planY.findIntersections(new Ray(p2, v2)),
                "Failed to find the intersection point when the ray is orthogonal to the plane and starts outside the plane");

        // TC08: Ray starts at the plane but goes out
        assertNull(planY.findIntersections(new Ray(p1, v1)),
                "Failed to find the intersection point when the ray starts at the plane");

        // TC09: Ray starts at the plane's base point
        assertNull(planY.findIntersections(new Ray(p3, v1)),
                "Failed to find the intersection point when the ray starts at the point used to define the plane");
    }
}
