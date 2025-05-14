package renderer;

import geometries.Geometries;
import geometries.Intersectable.*;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.LinkedList;
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
/// need check
    public boolean preprocessIntersection(Intersection intersection, Vector rayDir) {
        intersection.rayDir = rayDir;
        intersection.normal = intersection.geometry.getNormal(intersection.point);
        intersection.scaleNR = intersection.normal.dotProduct(rayDir);
        if (Util.isZero( intersection.scaleNR ))
            return false;
      return true;
    }

    public boolean setLightSource(Intersection intersection, LightSource lightSource) {
        intersection.lightSource = lightSource;
        intersection.lightDir = lightSource.getL(intersection.point).normalize();
        intersection.scaleNL=intersection.lightDir.dotProduct(intersection.normal);
        if (Util.isZero(intersection.scaleNL))
            return false;
        return true;
    }

    /**
     * Calculates the local lighting effects (diffusive + specular) at the intersection point.
     *
     * @param intersection the intersection object
     * @return the local lighting color contribution
     */
    private Color calcColorLocalEffects(Intersection intersection) {
        Color result = intersection.geometry.getEmission();

        for (var l : scene.light) {
            if (!setLightSource(intersection, l))
                continue;

            Color lightIntensity = l.getIntensity (intersection.point);
            Double3 diffusive = calcDiffusive(intersection);
            Double3 specular = calcSpecular(intersection);
            Double3 totalComponents = diffusive.add(specular);

            Color contribution = lightIntensity.scale(totalComponents);
            result = result.add(contribution);
        }

        return result;
    }
    /**
     * Calculates the specular component of the light at the intersection point.
     *
     * @param intersection the intersection data
     * @return the specular component as Double3
     */
    private Double3 calcSpecular(Intersection intersection) {
        Vector n = intersection.normal;
        Vector l = intersection.lightDir;
        Vector v = intersection.rayDir.normalize(); // inverse of ray direction

        // Calculate reflection vector R = L - 2 * (N·L) * N
        Vector r = l.subtract(n.scale(2 * intersection.scaleNL)).normalize();

        // Calculate R·V (viewer direction)
        double rv = r.dotProduct(v);
        if (rv <= 0)
            return Double3.ZERO; // no specular if angle > 90 degrees

        // Calculate specular component: kS * (R·V)^nShininess
        return intersection.material.Ks.scale(Math.pow(rv, intersection.material.nSh));
    }
    /**
     * Calculates the diffusive component of the light at the intersection point.
     *
     * @param intersection the intersection data
     * @return the diffusive component as Double3
     */
    private Double3 calcDiffusive(Intersection intersection) {
        // According to Phong model: kD * max(0, N·L)
        double nl = Math.max(0, intersection.scaleNL); // ensure non-negative
        return intersection.material.Kd.scale(nl);
    }
}
