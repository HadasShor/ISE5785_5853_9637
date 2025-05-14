package renderer;

import geometries.Geometries;
import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.List;

/**
 * A basic ray tracer that computes the color of the closest intersection point of a ray with scene geometries.
 */
public class SimpleRayTracer extends RayTracerBase {

    /**
     * Constructor for {@code SimpleRayTracer}.
     *
     * @param scene  the scene to be rendered, containing geometries and lighting
     * @param simple a placeholder parameter representing the ray tracer type (currently unused)
     */
    public SimpleRayTracer(Scene scene, RayTracerType simple) {
        super(scene);
    }

    /**
     * Traces a ray through the scene and determines the resulting color.
     * If no intersection is found, returns the background color.
     *
     * @param ray the ray to trace through the scene
     * @return the calculated color at the closest intersection point, or background color if no intersection occurs
     */
    @Override
    public Color traceRay(Ray ray) {
        /** List of intersection points between the ray and the scene geometries */
        List<Point> intersections = scene.geometries.findIntersections(ray);

        // If there are no intersections, return the background color
        if (intersections == null) {
            return scene.backgroundColor;
        }

        /** The closest intersection point to the ray's origin */
        Point closestPoint = ray.findClosedPoint(intersections);

        // Return the color at the closest intersection point
        return calcColor(closestPoint);
    }

    /**
     * Calculates the color at a given point.
     * Currently returns only the ambient light intensity as a placeholder.
     *
     * @param point the point at which to calculate color
     * @return the ambient light color at the given point
     */
    private Color calcColor(Point point) {
        return scene.ambientLight.getIntensity();
    }
}
