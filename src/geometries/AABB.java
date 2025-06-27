package geometries;

import primitives.*;

/**
 * Represents an Axis-Aligned Bounding Box (AABB) in 3D space.
 * This class provides methods to check for ray intersections, point containment,
 * and bounding box combinations.
 */
public class AABB {

    /**
     * The minimum corner point of the bounding box.
     */
    private final Point min;

    /**
     * The maximum corner point of the bounding box.
     */
    private final Point max;

    /**
     * The center point of the bounding box (cached for performance).
     */
    private Point center;

    /**
     * Constructs a bounding box with the specified minimum and maximum corner points.
     *
     * @param min the minimum corner point of the bounding box
     * @param max the maximum corner point of the bounding box
     */
    public AABB(Point min, Point max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Copy constructor to create a new AABB from an existing one.
     *
     * @param other the AABB to copy
     */
    public AABB(AABB other) {
        this.min = new Point(other.min.getX(), other.min.getY(), other.min.getZ());
        this.max = new Point(other.max.getX(), other.max.getY(), other.max.getZ());
    }

    /**
     * Checks if a ray intersects the bounding box using an optimized slab method.
     *
     * @param ray         the ray to test for intersection
     * @return true if the ray intersects the bounding box, false otherwise
     */
    public boolean intersect(Ray ray) {
        Point head = ray.getHead();
        Vector dir = ray.getDirection();

        double tMin = Double.NEGATIVE_INFINITY;
        double tMax = Double.POSITIVE_INFINITY;

        for (int axis = 0; axis < 3; axis++) {
            double axisDir = dir.get(axis);
            double axisOrigin = head.get(axis);
            double axisMin = min.get(axis);
            double axisMax = max.get(axis);

            // If ray is (almost) parallel to the axis
            if (Math.abs(axisDir) < 1e-15) {
                if (axisOrigin < axisMin || axisOrigin > axisMax) {
                    return false;
                }
                continue;
            }

            double t1 = (axisMin - axisOrigin) / axisDir;
            double t2 = (axisMax - axisOrigin) / axisDir;

            if (t1 > t2) {
                double temp = t1;
                t1 = t2;
                t2 = temp;
            }

            tMin = Math.max(tMin, t1);
            tMax = Math.min(tMax, t2);

            if (tMin > tMax + 1e-10) {
                return false;
            }
        }

        // We intersected the box; check if intersection is in front of ray origin
        return tMax >= 0;
    }

    /**
     * Checks if a point is inside the bounding box.
     *
     * @return true if the point is inside the bounding box, false otherwise
     */
    public Point getMin() {
        return min;
    }

    /**
     * Returns the maximum corner point of the bounding box.
     *
     * @return the maximum corner point
     */
    public Point getMax() {
        return max;
    }

    /**
     * Gets the center point of the bounding box (cached for performance).
     *
     * @return the center point of the bounding box
     */
    public Point getCenter() {
        if (center == null) {
            double centerX = (min.getX() + max.getX()) * 0.5;
            double centerY = (min.getY() + max.getY()) * 0.5;
            double centerZ = (min.getZ() + max.getZ()) * 0.5;
            center = new Point(centerX, centerY, centerZ);
        }
        return center;
    }

    /**
     * Combines two bounding boxes into one that encompasses both.
     *
     * @param b1 the first bounding box
     * @param b2 the second bounding box
     * @return a new AABB that encompasses both b1 and b2, or null if both are null
     */
    public static AABB combine(AABB b1, AABB b2) {
        if (b1 == null) return b2;
        if (b2 == null) return b1;

        Point min1 = b1.getMin(), max1 = b1.getMax();
        Point min2 = b2.getMin(), max2 = b2.getMax();

        Point min = new Point(
                Math.min(min1.getX(), min2.getX()),
                Math.min(min1.getY(), min2.getY()),
                Math.min(min1.getZ(), min2.getZ())
        );

        Point max = new Point(
                Math.max(max1.getX(), max2.getX()),
                Math.max(max1.getY(), max2.getY()),
                Math.max(max1.getZ(), max2.getZ())
        );

        return new AABB(min, max);
    }

    /**
     * Combines this bounding box with another bounding box.
     *
     * @param other the other bounding box to combine with
     * @return a new AABB that encompasses both this and the other bounding box
     */
    public AABB combine(AABB other) {
        Point newMin = new Point(
                Math.min(this.min.getX(), other.min.getX()),
                Math.min(this.min.getY(), other.min.getY()),
                Math.min(this.min.getZ(), other.min.getZ())
        );
        Point newMax = new Point(
                Math.max(this.max.getX(), other.max.getX()),
                Math.max(this.max.getY(), other.max.getY()),
                Math.max(this.max.getZ(), other.max.getZ())
        );
        return new AABB(newMin, newMax);
    }

    /**
     * Calculates the volume of the bounding box.
     *
     * @return the volume of the bounding box
     */
    public double surfaceArea() {
        double dx = max.getX() - min.getX();
        double dy = max.getY() - min.getY();
        double dz = max.getZ() - min.getZ();
        return 2 * (dx * dy + dx * dz + dy * dz);
    }

    /**
     * Creates an infinite bounding box that covers the entire space.
     *
     * @return an AABB representing an infinite bounding box
     */
    public static AABB createInfiniteBoundingBox() {
        return new AABB(
                new Point(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY),
                new Point(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)
        );
    }
}