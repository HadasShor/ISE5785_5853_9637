package renderer;

import geometries.Geometries;
import geometries.Intersectable.*;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.alignZero;

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
        return calcColor(closestintersection,ray);
    }

    /**
     * Calculates the color at the intersection point.
     *
     * @param intersection the intersection object
     * @param ray          the ray that hit the object
     * @return the color at the intersection point
     */
    private Color calcColor(Intersection intersection, Ray ray) {
        // Preprocess data: set normal, ray direction, dot product
        if (!preprocessIntersection(intersection, ray.getDirection()))
            return Color.BLACK; // No local effects → return black

        //Double3 kA = intersection.geometry.getMaterial().Ka;
        //Color c = scene.ambientLight.getIntensity().scale(kA);

        //c = c.add(calcColorLocalEffects(intersection));
        Color color = scene.ambientLight.getIntensity()
                .scale(intersection.material.Ka)
                .add(calcColorLocalEffects(intersection));
        return color;

    }


/// need check
    public boolean preprocessIntersection(Intersection intersection, Vector rayDir) {
        intersection.rayDir = rayDir.scale(-1);
        intersection.normal = intersection.geometry.getNormal(intersection.point);
        //intersection.scaleNR = intersection.normal.dotProduct(rayDir);
        intersection.scaleNR = alignZero(intersection.rayDir.dotProduct(intersection.normal));
        if (intersection.scaleNR == 0)
            return false;
        return true;
    }

    public boolean setLightSource(Intersection intersection, LightSource lightSource) {
        intersection.lightSource = lightSource;
        intersection.lightDir = lightSource.getL(intersection.point).normalize();
        intersection.scaleNL = alignZero(intersection.lightDir.dotProduct(intersection.normal));
        return (intersection.scaleNL * intersection.scaleNR > 0);
    }

    /**
     * Calculates the local lighting effects (diffusive + specular) at the intersection point.
     *
     * @param intersection the intersection object
     * @return the local lighting color contribution
     */
    private Color calcColorLocalEffects(Intersection intersection) {
        if (intersection == null) {
            return scene.backgroundColor;
        }
        Material material = intersection.material;
        Color color = intersection.geometry.getEmission();

        //color = color.add(scene.ambientLight.getIntensity().scale(material.Ka));

        for (LightSource lightSource : scene.light) {
            {
                if(!setLightSource(intersection, lightSource)) {
                    continue;
                }

                // Compute light intensity at the intersection point
                Color iL = lightSource.getIntensity(intersection.point);

                // Add contribution from diffusive and specular effects
                color = color.add(iL.scale(calcDiffusive(intersection))).add(iL.scale(calcSpecular(intersection)));
            }
        }
        return color;
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
        Vector r = l.subtract(n.scale(2 * intersection.scaleNL));
        double rv = Math.max(0, (r.dotProduct(v) * -1));
        double specularFactor = Math.pow(rv, intersection.material.nSh);

        return intersection.material.Ks.scale(specularFactor);
    }
    /**
     * Calculates the diffusive component of the light at the intersection point.
     *
     * @param intersection the intersection data
     * @return the diffusive component as Double3
     */
    private Double3 calcDiffusive(Intersection intersection) {
        if (intersection.scaleNL < 0) {
            return intersection.material.Kd.scale(intersection.scaleNL * -1);
        }
        return intersection.material.Kd.scale(intersection.scaleNL);
    }




}
