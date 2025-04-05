package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link geometries.Sphere} class.
 * <p>
 * This class contains various test cases for the methods {@link geometries.Sphere#getNormal(Point)} and
 * {@link geometries.Sphere#findIntersections(Ray)}. These tests validate the functionality of the sphere, including
 * normal calculation and intersection checks with rays.
 * </p>
 */
class SphereTests {

    /**
     * Default constructor for {@code SphereTests}.
     * Initializes the test class with default values.
     */
    public SphereTests() {
        // Default constructor, no initialization or actions
    }

    /**
     * Constant used for calculating points on the sphere.
     */
    private final double sqrt075 = Math.sqrt(0.75);

    // ====== Vectors used in tests ======

    /**
     * A sample vector (1, 0, 1).
     */
    private final Vector v1 = new Vector(1, 0, 1);

    /**
     * A sample vector (0, -1, 0), points down along Y-axis.
     */
    private final Vector v2 = new Vector(0, -1, 0);

    /**
     * A sample vector (0, 1, 0), points up along Y-axis.
     */
    private final Vector v3 = new Vector(0, 1, 0);

    /**
     * A sample vector (1, 1, 1), diagonal direction.
     */
    private final Vector v4 = new Vector(1, 1, 1);

    // ====== Points used in tests ======

    /**
     * A point on the X-axis used for normal testing.
     */
    private final Point p1 = new Point(1, 0, 0);

    /**
     * A point on the sphere surface used for intersection testing.
     */
    private final Point p2 = new Point(0, 1, 1);

    /**
     * A point located above the sphere.
     */
    private final Point p3 = new Point(0, 2, 1);

    /**
     * A point located below the sphere.
     */
    private final Point p4 = new Point(0, -1, 1);

    /**
     * A point inside the sphere.
     */
    private final Point p5 = new Point(0, 0.5, 1);

    /**
     * A point on the top surface of the sphere (used for intersection testing).
     */
    private final Point p7 = new Point(0, sqrt075, 1.5);

    /**
     * The center of the sphere.
     */
    private final Point p8 = new Point(0, 0, 1);

    /**
     * A unit sphere centered at p8.
     */
    private final Sphere sphere = new Sphere(1, p8);

    /**
     * Test method for {@link geometries.Sphere#getNormal(Point)}.
     * Validates that the normal vector returned is correct.
     *
     * TC01: Check if normal vector is calculated correctly on a point on the sphere's surface.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        assertEquals(p1, new Sphere(1, p1).getNormal(new Point(2, 0, 0)),
                "Failed to get the normal vector of the sphere");
    }

    /**
     * Test method for {@link geometries.Sphere#findIntersections(Ray)}.
     * Validates intersection points between a ray and the sphere.
     *
     * This includes various test cases:
     * TC01: Ray starts inside the sphere.
     * TC02: Ray does not intersect the sphere.
     * TC03: Ray intersects the sphere at two points.
     * TC04: Ray misses the sphere.
     */
    @Test
    void testFindIntersections() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray starts inside the sphere
        assertEquals(List.of(p7),
                sphere.findIntersections(new Ray(new Point(0, 0, 1.5), v3)),
                "Failed to find intersection when ray starts inside the sphere");

        // TC02: Ray does not intersect the sphere
        assertNull(sphere.findIntersections(new Ray(new Point(0, 0, 3), v4)),
                "Ray should not intersect the sphere");

        // TC03: Ray intersects the sphere in two points
        assertEquals(List.of(p7, new Point(0, -sqrt075, 1.5)),
                sphere.findIntersections(new Ray(new Point(0, 2, 1.5), v2)),
                "Ray should intersect sphere in two points");

        // TC04: Ray misses the sphere
        assertNull(sphere.findIntersections(new Ray(new Point(0, -2, 1.5), v2)),
                "Ray should miss the sphere");

        // =============== Boundary Values Tests =================

        // TC05: Ray is orthogonal and starts before the sphere
        assertNull(sphere.findIntersections(new Ray(p3, new Vector(0, 0, 1))),
                "Orthogonal ray before sphere should miss");

        // TC06: Ray is orthogonal and starts inside the sphere
        assertEquals(List.of(new Point(0, 0.5, 1 - sqrt075)),
                sphere.findIntersections(new Ray(p5, new Vector(0, 0, -1))),
                "Orthogonal ray inside sphere should intersect once");

        // Tangent Rays

        // TC07: Ray tangent before the sphere
        assertNull(sphere.findIntersections(new Ray(new Point(-1, 1, 0), v1)),
                "Tangent ray before sphere should miss");

        // TC08: Ray tangent on the sphere
        assertNull(sphere.findIntersections(new Ray(p2, v1)),
                "Tangent ray on the sphere should miss");

        // TC09: Ray tangent after the sphere
        assertNull(sphere.findIntersections(new Ray(new Point(1, 1, 2), v1)),
                "Tangent ray after the sphere should miss");

        // Non-orthogonal/non-tangent rays

        // TC10: Ray starts on the sphere and intersects
        assertEquals(List.of(new Point(-2.0 / 3, 1.0 / 3, 1.0 / 3)),
                sphere.findIntersections(new Ray(p2, new Vector(-1, -1, -1))),
                "Ray on sphere should intersect once");

        // TC11: Ray starts on the sphere and misses
        assertNull(sphere.findIntersections(new Ray(p2, v4)),
                "Ray on sphere should miss if pointing out");

        // Rays through center

        // TC12: Ray starts on the sphere and goes through center
        assertEquals(List.of(p4),
                sphere.findIntersections(new Ray(p2, v2)),
                "Ray on sphere through center should intersect once");

        // TC13: Ray starts before sphere and goes through center
        assertEquals(List.of(p2, p4),
                sphere.findIntersections(new Ray(p3, v2))
                        .stream()
                        .sorted(Comparator.comparingDouble(p -> p.distance(new Point(-1, 0, 0))))
                        .toList(),
                "Ray through center should intersect twice");

        // TC14: Ray starts at center
        assertEquals(List.of(p2),
                sphere.findIntersections(new Ray(p8, v3)),
                "Ray from center should intersect once");

        // TC15: Ray on sphere, opposite direction of center
        assertNull(sphere.findIntersections(new Ray(p2, v3)),
                "Ray should miss if going away from center");

        // TC16: Ray starts after sphere, away from center
        assertNull(sphere.findIntersections(new Ray(p3, v3)),
                "Ray after sphere should miss");

        // TC17: Ray inside sphere, away from center
        assertEquals(List.of(p2),
                sphere.findIntersections(new Ray(p5, v3)),
                "Ray inside sphere going outward should intersect once");
    }
}