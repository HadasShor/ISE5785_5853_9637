package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.isZero;

/**
 * Polygon class represents a two-dimensional polygon in a 3D Cartesian coordinate system.
 * The polygon is defined by a list of vertices and lies in a specific plane.
 * It provides methods for various polygon-related operations, such as checking if a point lies on an edge
 * and finding intersections with a ray.
 *
 * @author Hadas_Shor, Nurit_Ezra
 */
public class Polygon extends Geometry {

    /**
     * List of the polygon's vertices.
     */
    protected final List<Point> vertices;

    /**
     * Associated plane in which the polygon lies.
     */
    protected final Plane plane;

    /**
     * The size of the polygon - the number of vertices in the polygon.
     */
    private final int size;

    /**
     * Constructs a Polygon object from a list of vertices.
     * The list must be ordered by edge path and the polygon must be convex.
     *
     * @param vertices The list of vertices ordered by edge path.
     *                 The polygon must be convex and the vertices must lie in the same plane.
     * @throws IllegalArgumentException if any of the following conditions are violated:
     *                                  <ul>
     *                                  <li>Less than 3 vertices</li>
     *                                  <li>Consecutive vertices are the same point</li>
     *                                  <li>The vertices do not lie in the same plane</li>
     *                                  <li>The vertices are not ordered by edge path</li>
     *                                  <li>Three consecutive vertices are collinear (180° angle between two edges)</li>
     *                                  <li>The polygon is concave (not convex)</li>
     *                                  </ul>
     */
    public Polygon(Point... vertices) {
        if (vertices.length < 3)
            throw new IllegalArgumentException("A polygon can't have less than 3 vertices");
        this.vertices = List.of(vertices);
        size = vertices.length;
        // Generate the plane according to the first three vertices and associate the polygon with this plane.
        // The plane holds the invariant normal (orthogonal unit) vector to the polygon
        plane = new Plane(vertices[0], vertices[1], vertices[2]);
        if (size == 3) return; // no need for more tests for a Triangle
        Vector n = plane.getNormal(null);
        // Subtracting any subsequent points will throw an IllegalArgumentException
        // because of Zero Vector if they are in the same point
        Vector edge1 = vertices[size - 1].subtract(vertices[size - 2]);
        Vector edge2 = vertices[0].subtract(vertices[size - 1]);

        // Cross Product of any subsequent edges will throw an IllegalArgumentException
        // because of Zero Vector if they connect three vertices that lie in the same line.
        // Generate the direction of the polygon according to the angle between last and
        // first edge being less than 180deg. It is held by the sign of its dot product
        // with the normal. If all the rest consequent edges will generate the same sign
        // - the polygon is convex ("kamur" in Hebrew).
        boolean positive = edge1.crossProduct(edge2).dotProduct(n) > 0;
        for (var i = 1; i < size; ++i) {
            // Test that the point is in the same plane as calculated originally
            if (!isZero(vertices[i].subtract(vertices[0]).dotProduct(n)))
                throw new IllegalArgumentException("All vertices of a polygon must lay in the same plane");
            // Test the consequent edges have the same orientation
            edge1 = edge2;
            edge2 = vertices[i].subtract(vertices[i - 1]);
            if (positive != (edge1.crossProduct(edge2).dotProduct(n) > 0))
                throw new IllegalArgumentException("All vertices must be ordered and the polygon must be convex");
        }
    }

    /**
     * Returns the normal vector to the polygon.
     *
     * @param point The point to calculate the normal for.
     * @return The normal vector to the polygon.
     */
    @Override
    public Vector getNormal(Point point) {
        return plane.getNormal(point);
    }

    /**
     * Checks if a given point is on the edge of the polygon defined by two other points.
     *
     * @param p The point to check.
     * @param a One end of the edge.
     * @param b The other end of the edge.
     * @return true if the point lies on the edge, false otherwise.
     */
    public boolean isPointOnEdge(Point p, Point a, Point b) {
        // Vector from A to P
        Vector v1 = p.subtract(a);

        // Vector from A to B
        Vector v2 = b.subtract(a);

        // If the vectors are not parallel, the point is not on the edge
        if (!v1.crossProduct(v2).equals(Vector.ZERO)) {
            return false;
        }

        // Calculate the squared length of the edge AB
        double edgeLengthSquared = v2.dotProduct(v2);

        // Calculate the dot product of the vector from P to A with the vector from A to B
        double dotProduct = v1.dotProduct(v2);

        // If the dot product is between 0 and the squared length of the edge, the point is on the edge
        return dotProduct >= 0 && dotProduct <= edgeLengthSquared;
    }

    /**
     * Finds the intersection points of the polygon with a given ray.
     *
     * @param ray The ray to check for intersections.
     * @return A list of intersection points, or null if no intersection is found.
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        // Initialize a list to hold the normals of the edges of the polygonal base
        List<Vector> normals = new LinkedList<>();

        Point first = vertices.getFirst();
        // Get the starting point and direction of the ray
        final Point startPoint = ray.getPoint(0);
        final Vector dir = ray.getDirection();

        // Calculate the normal vector for each edge of the polygonal base
        Vector v1 = first.subtract(startPoint);
        var reshima = vertices.subList(1, size);
        for (Point p : reshima ) {
            Vector v2 = p.subtract(startPoint);
            normals.add(v1.crossProduct(v2).normalize());
            v1 = v2;
        }
        // Add the normal for the edge connecting the last vertex to the first vertex
        normals.add(vertices.get(size - 1).subtract(startPoint).crossProduct(first.subtract(startPoint)).normalize());

        // Determine if the ray direction is consistently on one side of all the polygon's edges
        boolean allPositive = dir.dotProduct(normals.get(0)) > 0;
        for (Vector normal : normals) {
            double s = dir.dotProduct(normal);
            // If the dot product is zero or if it changes sign, the ray does not intersect the polygon's base
            if (Util.isZero(s) || (s > 0 != allPositive)) {
                return null;
            }
        }

        // Find and return the intersection points of the ray with the plane
        return plane.findIntersections(ray);
    }
}
