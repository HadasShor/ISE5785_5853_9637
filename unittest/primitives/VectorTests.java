package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VectorTests {
    private final Vector vector1 = new Vector(1, 2, 3);
    private final Vector vector2 = new Vector(2, 4, 6);
    private final Vector vector3 = new Vector(-1, -2, -3);
    private final Vector vector4 = new Vector(2, 1, 2);
    private final Vector vector5 = new Vector(-2, -1, -2);
    private final Vector vector6 = new Vector(2, 2, 2);
    private final Vector vector7 = new Vector(1, 2, -3);
    private final Vector vector8 = new Vector(0, 1, 0);
    private final Vector vector9 = new Vector(0, 0, 1);
    private final Vector vector10 = new Vector(4, 4, 4);
    private final Vector vector11 = new Vector(1, 0, 0);
    private final Vector vector12 = new Vector(1, 4, -3);

    @Test
    void testAdd() {
        assertEquals(vector2, vector1.add(new Vector(1, 2, 3)), "add() wrong result");
        assertThrows(IllegalArgumentException.class, () -> vector1.add(vector3), "add() should throw exception for zero vector");
    }

    @Test
    void testLengthSquared() {
        assertEquals(14, vector1.lengthSquared(), 0.00001, "lengthSquared() wrong result");
    }

    @Test
    void testLength() {
        assertEquals(Math.sqrt(9), vector4.length(), 0.00001, "length() wrong result");
        assertEquals(Math.sqrt(9), vector5.length(), 0.00001, "length() wrong result");
    }

    @Test
    void testNormalize() {
        assertEquals(new Vector(2.0 / 3, 1.0 / 3, 2.0 / 3), vector4.normalize(), "normalize() wrong result");
        assertEquals(new Vector(-2.0 / 3, -1.0 / 3, -2.0 / 3), vector5.normalize(), "normalize() wrong result");
    }

    @Test
    void testDotProduct() {
        assertEquals(0, vector6.dotProduct(vector7), 0.00001, "dotProduct() wrong result with zero");
        assertEquals(14, vector1.dotProduct(vector1), 0.00001, "dotProduct() wrong result");
        assertEquals(-14, vector1.dotProduct(vector3), 0.00001, "dotProduct() wrong result");
        assertEquals(0, vector8.dotProduct(vector9), 0.00001, "dotProduct() wrong result vertical vectors");
    }

    @Test
    void testCrossProduct() {
        assertEquals(new Vector(1, 4, -3), vector1.crossProduct(vector4), "Error from cross product of vectors");
        assertThrows(IllegalArgumentException.class, () -> vector6.crossProduct(vector10), "Exception to the vector product of parallel vectors");
        assertEquals(new Vector(0, 0, 1), vector11.crossProduct(vector8), "Error from cross product of perpendicular vectors");
    }

    @Test
    void testScale() {
        assertEquals(vector2, vector1.scale(2), "Error from scale of vector");
        assertThrows(IllegalArgumentException.class, () -> vector1.scale(0), "Error from scale of vector zero");
    }
}