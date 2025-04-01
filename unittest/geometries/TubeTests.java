package geometries;
import primitives.Point;
import org.junit.jupiter.api.Test;
import primitives.*;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class TubeTests {

    /**
     * Default constructor for {@code TubeTests}.
     * Initializes the cylinder with default values.
     */
    public TubeTests() {
        // Default constructor, no initialization or actions
    }
    /**
     * A point in 3D space with coordinates (1, 1, 1).
     */
    final Point point1 = new Point(1, 1, 1);

    /**
     * A point in 3D space with coordinates (2, 3, 4).
     */
    final Point point2 = new Point(2, 3, 4);

    /**
     * A vector in 3D space with components (1, 2, 3).
     */
    final Vector vector1 = new Vector(1, 2, 3);

    /**
     * A vector in 3D space with components (-3, 1, 4).
     */
    final Vector vector2 = new Vector(-3, 1, 4);

    @Test
    void testGetNormal() {
        // Tube tube = new Tube(new Ray(point1, vector1), 1);

        // נקודה על פני הגליל לבדיקה
        //  Point p0 = new Point(2, 3, 4); // בחר נקודה שמתאימה לך על הגליל

        // חישוב הנורמל לנקודה
        //Vector normal = tube.getNormal(p0);

        // בדיקה אם הנורמל מאונך לוקטור הכיוון של הקרן (ציר הגליל)
        assertTrue(Math.abs(new Tube(new Ray(point1, vector1), 1).getNormal(new Point(3, 2, 1)).dotProduct(vector1)) < 0.0001, "Erore in Tube getNormal(), the normal is not orthogonal to the direction vector of the tube");
        assertEquals(0, new Tube(new Ray(point1, vector1), 1).getNormal(new Point(2, 2, 3)).dotProduct(vector1), 0.00001, "Error in Tube getNormal(), the normal is not orthogonal to the direction vector of the tube");

        //assertEquals(0,new Tube( new Ray(point1, vector1),1).getNormal(point2).length(), "ERROR: Tube getNormal() does not work correctly with Zero");
        // assertEquals(vector2.normalize(),new Tube( new Ray(point1, vector1),1).getNormal(new Point(1,0,0)), "ERROR: Tube getNormal() does not work correctly");

        // Points for testing
        final Point point1 = new Point(1, 1, 1);
        final Point point2 = new Point(-2, -1, 0);

        // Vectors for testing
        final Vector vector1 = new Vector(1, 2, 3);
        final Vector vector2 = new Vector(-3, 1, 4);


    }
    }
