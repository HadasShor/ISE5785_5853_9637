package geometries;

import primitives.*;
import primitives.Ray;
import primitives.Vector;

import java.util.ArrayList;
import java.util.List;

import static primitives.Util.isZero;

/**
 * The `Tube` class represents a tube in 3D space.
 * It is defined by a ray (axis) and a radius.
 */
public class Tube extends RadialGeometry {
    /** The axis ray of the tube. */
    protected final Ray axis;

    /**
     * Constructs a `Tube` with the specified axis ray and radius.
     *
     * @param axis the axis ray of the tube
     * @param radius the radius of the tube
     */
    public Tube(Ray axis, double radius) {
        super(radius);
        this.axis = axis;
    }

    /**
     * Returns the normal vector to the tube at the given point.
     *
     * @param p0 the point on the surface of the tube
     * @return the normal vector at the given point
     */
    @Override
    public Vector getNormal(Point p0) {

            //  חישוב ההיטל של P_0 על וקטור הכיוון של הקרן
         //   double t = axis.getDirection().dotProduct(p0.subtract(axis.getPoint()));

            // 2️ מוצאים את נקודת המרכז O על הציר
           // Point o = axis.getPoint().add(axis.getDirection().scale(t));

            // 3 מחשבים את וקטור הנורמל
           // Vector n = p0.subtract(o);

            // 4 מנרמלים את הווקטור ומחזירים אותו
            return p0.subtract(axis.getP0().add(axis.getDirection().scale(axis.getDirection().dotProduct(p0.subtract(axis.getP0()))))).normalize();

    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        // קבלת נתוני הקרן
        Point P = ray.getP0();
        Vector d = ray.getDirection();

        // קבלת נתוני ציר הגליל
        Point C = axis.getP0();
        Vector v = axis.getDirection();  // מנורמל

        // מחשבים את הווקטור מהנקודה P לנקודה C
        Vector delta = P.subtract(C);

        // מחשבים את הרכיב של d המאונך לציר v: d_perp = d - (d dot v) * v
        double dDotV = d.dotProduct(v);
        Vector dPerp = d.subtract(v.scale(dDotV));

        // מחשבים את הרכיב של delta המאונך לציר v: delta_perp = delta - (delta dot v) * v
        double deltaDotV = delta.dotProduct(v);
        Vector deltaPerp = delta.subtract(v.scale(deltaDotV));

        // חישוב המקדמים במשוואה הריבועית
        double A = dPerp.lengthSquared();
        double B = 2 * deltaPerp.dotProduct(dPerp);
        double C_coeff = deltaPerp.lengthSquared() - radius * radius;

        // אם A הוא אפס, הקרן מקבילה לציר הגליל - לא נחשב חיתוך תקין
        if (isZero(A))
            return null;

        double disc = B * B - 4 * A * C_coeff;
        if (disc < 0)
            return null; // אין פתרון אמיתי

        double sqrtDisc = Math.sqrt(disc);
        double t1 = (-B + sqrtDisc) / (2 * A);
        double t2 = (-B - sqrtDisc) / (2 * A);

        List<Point> intersections = new ArrayList<>();
        // בודקים רק ערכים חיוביים
        if (t1 > Util.ZERO) {
            intersections.add(ray.getPoint(t1));
        }
        if (t2 > Util.ZERO) {
            intersections.add(ray.getPoint(t2));
        }

        // אם לא נמצאו חיתוכים חיוביים, מחזירים null
        if (intersections.isEmpty())
            return null;

        return intersections;
    }
}