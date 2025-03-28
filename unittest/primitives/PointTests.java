package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointTests {
    private final Point point1 = new Point(1, 2, 3);
    private final Point point2 = new Point(4, 5, 6);
    private final Vector vector1 = new Vector(1, 2, 3);

    @Test
    void testSubtract() {
        assertEquals(vector1, new Point(2, 4, 6).subtract(point1), "ERROR: Point - Point does not work correctly");
        assertThrows(IllegalArgumentException.class, () -> point1.subtract(point1), "ERROR: Point - Point subtraction does not throw an exception when subtracting the same point");
    }

    @Test
    void testAdd() {
        assertEquals(new Point(2, 4, 6), point1.add(vector1), "ERROR: Point + Vector does not work correctly");
        assertEquals(Point.ZERO, point1.add(new Vector(-1, -2, -3)), "ERROR: Point + Vector does not work correctly");
    }

    @Test
    void testDistanceSquared() {
        assertEquals(27, point1.distanceSquared(point2), "ERROR: Distance Point does not work correctly");
        assertEquals(0, point1.distanceSquared(point1), 0.00001, "ERROR: Distance Point does not work correctly");
    }

@Test
void testDistance() {
    assertEquals(3, point1.distance(new Point(0, 4, 5)), 0.00001, "ERROR: Distance Point does not work correctly");
    assertEquals(0, point1.distance(point1), "ERROR: Distance Point does not work correctly");
}
}