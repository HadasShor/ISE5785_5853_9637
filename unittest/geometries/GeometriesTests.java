package geometries;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import primitives.*;

/**
 * Unit tests for the {@link Geometries} class.
 * <p>
 *     This class contains a series of tests for the methods implemented in the {@link Geometries} class,
 *     ensuring that the geometries operations behave as expected.
 *     </p>
 */
class GeometriesTests {

    /**
     * A composite geometry containing:
     * - A sphere centered at (0, 0, 1) with radius 1
     * - A triangle in the XY plane with corners at (1,0,0), (1,1,0), and (0,1,0)
     * - A plane located at z = 3 with a normal vector in the positive z direction
     */
    private final Geometries geometries = new Geometries(
            new Sphere(1, new Point(0, 0, 1)),
            new Triangle(new Point(1, 0, 0), new Point(1, 1, 0), new Point(0, 1, 0)),
            new Plane(new Point(0, 0, 3), new Vector(0, 0, 1))
    );

    /**
     * Test method for {@link Geometries#add(Intersectable...)}.
     * This is currently a placeholder for testing the addition of geometries.
     */
    @Test
    void testAdd() {
        // TODO: Implement test logic for the add() method.
    }

    /**
     * Test method for {@link Geometries#findIntersections(Ray)}.
     * Tests the behavior of finding intersections between a ray and a group of geometries.
     */
    @Test
    void testFindIntersections() {
        // ================= Boundary Values Tests =================

        // TC01: empty geometries list
        // Expect null when no geometries exist
        assertNull(
                new Geometries().findIntersections(new Ray(new Point(1,1,1), new Vector(1,1,1))),
                "empty geometries list"
        );

        // TC02: no geometry is intersected
        // Ray does not intersect any geometry
        assertNull(
                geometries.findIntersections(new Ray(new Point(1,1,2.5), new Vector(1,0,0))),
                "no geometry is intersected"
        );

        // TC03: one geometry is intersected
        // Expect 2 intersection points with the sphere
        assertEquals(
                2,
                geometries.findIntersections(new Ray(new Point(0, -2, 1), new Vector(0, 1, 0))).size(),
                "one geometry is intersected"
        );

        // TC04: some geometries are intersected
        // Expect 3 intersection points with multiple geometries (e.g., sphere and triangle)
        assertEquals(
                3,
                geometries.findIntersections(new Ray(new Point(0, -2, 0), new Vector(0, 1, 1))).size(),
                "some geometries are intersected"
        );

        // TC05: all geometries are intersected
        // Expect 4 intersection points with all geometries
        assertEquals(
                4,
                geometries.findIntersections(new Ray(new Point(0.6, 0.6, -2), new Vector(0, 0, 1))).size(),
                "all geometries are intersected"
        );
    }
}
