package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class TubeTests {
 final Point point1= new Point(1,1,1);
    final Point point2= new Point(-2,-1,0);
 final Vector vector1= new Vector(1,2,3);
    final Vector vector2= new Vector(-3,1,4);
    @Test
    void testGetNormal() {
        assertEquals(0,new Tube( new Ray(point1, vector1),1).getNormal(point2), "ERROR: Tube getNormal() does not work correctly");
        assertEquals(vector2.normalize(),new Tube( new Ray(point1, vector1),1).getNormal(new Point(1,0,0)), "ERROR: Tube getNormal() does not work correctly");
    }
}