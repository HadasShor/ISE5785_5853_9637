package geometries;
import primitives.Point;

import org.junit.jupiter.api.Test;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class TriangleTests {

    @Test
    void testGetNormal() {
        assertEquals(new Vector(1,0,1).normalize(), new Triangle(new Point(1,0,0), new Point(0,1,0), new Point(0,0,1)).getNormal(new Point(0,0,0)), "ERROR: Triangle getNormal() does not work correctly");
    }
}