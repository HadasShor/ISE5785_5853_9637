package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class sphereTests {
    final Point point1 = new Point(1, 1, 1);
    final Point point2 = new Point(2, 2, 2);

    @Test
    void testGetNormal() {
        Vector expectedNormal = new Vector(1, 1, 1).normalize();
        assertEquals(expectedNormal, new sphere(point1, 1).getNormal(point2), "getNormal() did not return the correct normal vector");
    }
}