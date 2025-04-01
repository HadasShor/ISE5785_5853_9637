package geometries;

import primitives.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Cylinder} class.
 */

    /**
     * Unit tests for the {@link Cylinder} class.
     */
    class CylinderTests {

        /**
         * Default constructor for CylinderTests.
         * JUnit automatically instantiates this class.
         */
        public CylinderTests() {
            // No initialization needed
        }



    /**
     * Test method for {@link Cylinder#getNormal(Point)}.
     *
     * This test verifies that the normal vector is correctly computed at different locations:
     * - The center of the base.
     * - The center of the top.
     * - The edge of the base.
     * - The edge of the top.
     * - A point on the curved surface.
     */
    @Test
    void testGetNormal() {
        // Create a cylinder along the Z-axis with radius 2 and height 5
        Cylinder cylinder = new Cylinder(new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)), 2, 5);

        // Test normal at the base center
        Vector normal = cylinder.getNormal(new Point(0, 0, 0));
        assertEquals(new Vector(0, 0, -1), normal, "Normal at base center should be -axis direction");

        // Test normal at the top center
        normal = cylinder.getNormal(new Point(0, 0, 5));
        assertEquals(new Vector(0, 0, 1), normal, "Normal at top center should be axis direction");

        // Test normal at the base edge
        normal = cylinder.getNormal(new Point(2, 0, 0));
        assertEquals(new Vector(0, 0, -1), normal, "Normal at base edge should be -axis direction");

        // Test normal at the top edge
        normal = cylinder.getNormal(new Point(2, 0, 5));
        assertEquals(new Vector(0, 0, 1), normal, "Normal at top edge should be axis direction");

        // Test normal on the curved surface
        normal = cylinder.getNormal(new Point(2, 0, 2));
        assertEquals(new Vector(1, 0, 0), normal, "Normal on curved surface should be radial");
    }
}
