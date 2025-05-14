package renderer;

import geometries.Geometries;
import geometries.Intersectable.*;
import primitives.Color;
import primitives.Double3;
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
        List<Intersection> intersections = scene.geometries.calculateIntersectionsHelper(ray);

        // If there are no intersections, return the background color
        if (intersections == null) {
            return scene.backgroundColor;
        }

        /** The closest intersection point to the ray's origin */
        Intersection closestintersection = ray.findClosestIntersection(intersections);

        // Return the color at the closest intersection point
        return calcColor(closestintersection);
    }


    /**
     * Calculates the color at a given point.
     * Currently returns only the ambient light intensity as a placeholder.
     *
     * @param intersection the point at which to calculate color
     * @return the ambient light color at the given point
     */
    private Color calcColor(Intersection intersection) {
        Double3 k=intersection.geometry.getMaterial().Ka;
        return scene.ambientLight.getIntensity().scale(k).add(intersection.geometry.getEmission());
    }
}
