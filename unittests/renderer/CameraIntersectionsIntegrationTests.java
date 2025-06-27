package renderer;

import geometries.Geometry;
import geometries.Sphere;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Integration tests for verifying the number of intersection points between
 * rays constructed by the {@link Camera} and different {@link Geometry} types.
 * <p>
 * Includes tests with {@link Sphere}, {@link geometries.Plane}, and {@link geometries.Triangle}.
 */
public class CameraIntersectionsIntegrationTests {
    /**
     * Constructs a new {@code CameraIntersectionsIntegrationTests} instance.
     * This default constructor is used for running the integration tests.
     */
    public CameraIntersectionsIntegrationTests() {
        // default constructor
    }

    /**
     * Camera's up direction vector (negative Y-axis).
     */
    private final Vector yAxis = new Vector(0, -1, 0);

    /**
     * Camera's view direction vector (negative Z-axis).
     */
    private final Vector zAxis = new Vector(0, 0, -1);

    /**
     * Builder for constructing camera with predefined direction and view plane settings.
     */
    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setDirection(zAxis, yAxis)
            .setVpDistance(1)
            .setVpSize(3, 3);

    /**
     * Default camera instance used in most tests.
     */
    private final Camera camera = cameraBuilder.setLocation(new Point(0, 0, 0.5)).build();

    /**
     * Helper function to test the number of intersection points between rays
     * constructed by the camera and a given geometry.
     *
     * @param camera         the camera from which rays are constructed
     * @param geometry       the geometry to test intersections with
     * @param expectedAmount the expected number of intersection points
     */
    private void amountOfIntersections(Camera camera, Geometry geometry, int expectedAmount) {
        int intersections = 0;
        for (int j = 0; j < 3; j++)
            for (int i = 0; i < 3; i++) {
                List<Point> intersectionsList = geometry.findIntersections(camera.constructRay(3, 3, j, i));
                intersections += intersectionsList != null ? intersectionsList.size() : 0;
            }

        assertEquals(expectedAmount, intersections, "Wrong amount of intersections");
    }

    /**
     * Integration test for {@link Sphere} intersections with rays from the camera.
     * <ul>
     *     <li>TC01: Sphere in front of camera, radius 1 — expects 2 intersections</li>
     *     <li>TC02: Sphere around view plane — expects 18 intersections</li>
     *     <li>TC03: Smaller sphere in front — expects 10 intersections</li>
     *     <li>TC04: Sphere covers view plane — expects 9 intersections</li>
     *     <li>TC05: Sphere behind camera — expects 0 intersections</li>
     * </ul>
     */
    @Test
    void testSphereIntersection() {
        amountOfIntersections(cameraBuilder.setLocation((Point) Point.ZERO).build(), new Sphere(1, new Point(0, 0, -3)), 2);
        amountOfIntersections(camera, new Sphere(2.5, new Point(0, 0, -2.5)), 18);
        amountOfIntersections(camera, new Sphere(2, new Point(0, 0, -2)), 10);
        amountOfIntersections(camera, new Sphere(4, new Point(0, 0, -1)), 9);
        amountOfIntersections(camera, new Sphere(0.5, new Point(0, 0, 1)), 0);
    }

    /**
     * Integration test for {@link geometries.Plane} intersections with rays from the camera.
     * <ul>
     *     <li>TC01: Plane perpendicular to view direction — expects 9 intersections</li>
     *     <li>TC02: Plane with slight angle to view direction — expects 9 intersections</li>
     *     <li>TC03: Plane angled such that only part of rays intersect — expects 6 intersections</li>
     * </ul>
     */
    @Test
    void testPlaneIntersection() {
        amountOfIntersections(camera, new geometries.Plane(new Point(0, 0, -1), new Vector(0, 0, -1)), 9);
        amountOfIntersections(camera, new geometries.Plane(new Point(0, 0, -1), new Vector(0, 1, -10)), 9);
        amountOfIntersections(camera, new geometries.Plane(new Point(0, 0, -1), new Vector(0, -1, -1)), 6);
    }

    /**
     * Integration test for {@link geometries.Triangle} intersections with rays from the camera.
     * <ul>
     *     <li>TC01: Narrow triangle — expects 1 intersection</li>
     *     <li>TC02: Wider triangle — expects 2 intersections</li>
     * </ul>
     */
    @Test
    void testTriangleIntersection() {
        amountOfIntersections(camera, new geometries.Triangle(
                new Point(0, 1, -2),
                new Point(1, -1, -2),
                new Point(-1, -1, -2)), 1);

        amountOfIntersections(camera, new geometries.Triangle(
                new Point(0, 20, -2),
                new Point(1, -1, -2),
                new Point(-1, -1, -2)), 2);
    }
}
