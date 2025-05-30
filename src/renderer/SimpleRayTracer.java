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
 * This class extends {@link RayTracerBase} and provides methods for tracing rays and calculating colors based on
 * local and global illumination models.
 * @author Yitzchak_Rabinowitz, Gilad_Ganz
 */
public class SimpleRayTracer extends RayTracerBase {

    /**
     * Maximum recursion depth for color calculation.
     */
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    /**
     * Minimum coefficient for color calculation, used to prevent unnecessary recursions.
     */
    private static final double MIN_CALC_COLOR_K = 0.001;
    /**
     * Initial transparency and reflectivity coefficient.
     */
    private static final Double3 INITIAL_K = Double3.ONE;
    /**
     * A small constant used for offsetting points to avoid self-intersection issues.
     */
    private static final double DELTA = 0.1; // Maximum recursion depth for color calculation


    /**
     * Constructor for {@code SimpleRayTracer}.
     *
     * @param scene the scene to be rendered, containing geometries and lighting
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Constructor for {@code SimpleRayTracer}.
     *
     * @param scene the scene to be rendered, containing geometries and lighting
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
        return calcColor(closestintersection, ray);
    }

    /**
     * Calculates the color at the intersection point, considering local and global illumination.
     *
     * @param intersection the intersection object
     * @param ray          the ray that hit the object
     * @return the color at the intersection point
     */
    private Color calcColor(Intersection intersection, Ray ray) {
        Color c;
        if (preprocessIntersection(intersection, ray.getDirection())) {
            c= calcColor(intersection, MAX_CALC_COLOR_LEVEL, INITIAL_K)
                    .add(scene.ambientLight.getIntensity().scale(intersection.geometry.getMaterial().Ka));
        } else {
            return Color.BLACK;
        }
        return c;
    }


    /**
     * Preprocesses the intersection by setting the ray direction and normal vector,
     * and calculating the dot product of the ray direction and normal.
     *
     * @param intersection the intersection object
     * @param rayDir the direction of the ray
     * @return true if the dot product of ray direction and normal is not zero, false otherwise
     */
    public boolean preprocessIntersection(Intersection intersection, Vector rayDir) {
        intersection.rayDir = rayDir;
        intersection.normal = intersection.geometry.getNormal(intersection.point);

        intersection.scaleNR = alignZero(intersection.rayDir.dotProduct(intersection.normal));
        if (intersection.scaleNR == 0)
            return false;
        return true;
    }

    /**
     * Sets the light source for the intersection and calculates the light direction and dot product.
     *
     * @param intersection the intersection object
     * @param lightSource the light source to set
     * @return true if the product of scaleNL and scaleNR is positive, indicating the light is on the same side as the ray.
     */
    public boolean setLightSource(Intersection intersection, LightSource lightSource) {
        intersection.lightSource = lightSource;
        intersection.lightDir = lightSource.getL(intersection.point).normalize();
        intersection.scaleNL = alignZero(intersection.lightDir.dotProduct(intersection.normal));
        return (intersection.scaleNL * intersection.scaleNR) > 0;
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

        Color color = intersection.geometry.getEmission();

        for (LightSource lightSource : scene.light) {
            if (!setLightSource(intersection, lightSource)) {
                continue;
            }

            // Partial transparency - calculates soft shadow
            Double3 ktr = transparency(intersection);
            if (ktr.lowerThan(MIN_CALC_COLOR_K)) {
                continue; // Almost complete blocking - skip light
            }

            // Calculate light intensity at the point - after transparency effect
            Color iL = lightSource.getIntensity(intersection.point).scale(ktr);

            // Diffusive + Specular lighting
            color = color.add(iL.scale(calcDiffusive(intersection)))
                    .add(iL.scale(calcSpecular(intersection)));
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
        Vector n = intersection.normal.normalize();
        Vector l = intersection.lightDir.normalize();
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


    /**
     * Determines if a point is unshaded from a specific light source.
     * This method is deprecated and replaced by {@link #transparency(Intersection)}.
     *
     * @param intersection the intersection point to check
     * @param lightSource the light source to consider
     * @return true if the point is unshaded, false otherwise
     * @deprecated Use {@link #transparency(Intersection)} instead for more accurate shadow calculation.
     */
    @Deprecated
    private boolean unshaded(Intersection intersection, LightSource lightSource) {
        Vector pointToLight = lightSource.getL(intersection.point).scale(-1);
        Vector delta = intersection.normal.scale(intersection.scaleNL < 0 ? DELTA : -DELTA);
        Point origin = intersection.point.add(delta);
        Ray shadowRay = new Ray(origin, pointToLight);

        double maxDistance = lightSource.getDistance(intersection.point);

        List<Intersection> intersections = scene.geometries.calculateIntersectionsHelper(shadowRay);
        if (intersections == null || intersections.isEmpty()) {
            return true; // No blocking
        }

        for (Intersection shadowIntersection : intersections) {
            if (shadowIntersection.geometry == intersection.geometry) {
                continue; // Skip the object itself
            }

            double distanceFromRayOrigin = shadowIntersection.point.distance(origin);
            if (distanceFromRayOrigin < maxDistance) {
                // Check for partial transparency – if not transparent → blocking
                if (shadowIntersection.geometry.getMaterial().KT.lowerThan(MIN_CALC_COLOR_K)) {
                    return false; // Blocked
                }
            }
        }

        return true;
    }

    /**
     * Calculates the transparency coefficient for a given intersection point towards its light source.
     * This method accounts for soft shadows by considering the transparency of objects between
     * the intersection point and the light source.
     *
     * @param intersection the intersection point for which to calculate transparency
     * @return a {@link Double3} representing the accumulated transparency (ktr) from the intersection point to the light source.
     * A value of {@link Double3#ONE} means no obstruction, while {@link Double3#ZERO} means full obstruction.
     */
    private Double3 transparency(Intersection intersection) {
        // Retrieve the light source from the intersection
        LightSource lightSource = intersection.lightSource;
        if (lightSource == null) {
            return Double3.ONE; // No light source, no shading
        }

        // Direction of the shadow ray – from the point towards the light source (in reverse direction)
        Vector lightDirection = lightSource.getL(intersection.point).scale(-1);

        // Slight offset from the intersection point to prevent self-intersection
        Vector delta = intersection.normal.scale(intersection.scaleNL < 0 ? DELTA : -DELTA);
        Point origin = intersection.point.add(delta);

        // Create the shadow ray
        Ray shadowRay = new Ray(origin, lightDirection);

        // Distance to the light source
        double lightDistance = lightSource.getDistance(intersection.point);

        // Calculate intersections of the shadow ray with the scene
        List<Intersection> shadowIntersections = scene.geometries.calculateIntersectionsHelper(shadowRay);

        // Accumulated transparency – starts as 1 (no blocking)
        Double3 ktr = Double3.ONE;

        // If no intersections – no blocking at all
        if (shadowIntersections == null) {
            return ktr;
        }

        // Iterate over all intersections
        for (Intersection shadowIntersection : shadowIntersections) {
            // Skip the same object
            if (shadowIntersection.geometry == intersection.geometry) {
                continue;
            }

            // Calculate distance between the shadow ray origin and the intersection point
            double distance = shadowIntersection.point.distance(origin);

            // If the object is in the ray's path before the light source – it partially blocks
            if (distance < lightDistance) {
                // Multiply the accumulated transparency by the object's transparency
                ktr = ktr.product(shadowIntersection.geometry.getMaterial().KT);

                // If transparency is very low – early exit
                if (ktr.lowerThan(MIN_CALC_COLOR_K)) {
                    return Double3.ZERO;
                }
            }
        }

        // Return the ray's transparency – describing partial shadow
        return ktr;
    }


    /**
     * Recursively calculates the color at an intersection point, considering global illumination effects
     * like reflection and refraction, up to a specified recursion level.
     *
     * @param intersection the intersection point for which to calculate the color
     * @param level the current recursion level
     * @param k the accumulated attenuation coefficient for this ray
     * @return the calculated color at the intersection point, including local and global effects
     */
    private Color calcColor (Intersection intersection, int level, Double3 k) {

        Color color = calcColorLocalEffects(intersection);
        if (level == 1 || k.lowerThan(MIN_CALC_COLOR_K)) {
            return color;
        }
        return color.add(calcGlobalEffects(intersection, level-1, k));

    }

    /**
     * Calculates the contribution of global effects (reflection and refraction) for a given ray.
     *
     * @param ray the ray for which to calculate global effects
     * @param level the current recursion level
     * @param k the accumulated attenuation coefficient for this ray
     * @param kx the material's attenuation coefficient for reflection or refraction
     * @return the color contribution from global effects
     */
    private Color calcGlobalEffect(Ray ray, int level, Double3 k, Double3 kx) {
        Double3 kkx = k.product(kx);
        if (kkx.lowerThan(MIN_CALC_COLOR_K)) return Color.BLACK;
        Intersection intersection = findClosestIntersection(ray);
        if (intersection == null) return scene.backgroundColor.scale(kx);
        return preprocessIntersection(intersection, ray.getDirection())
                ? calcColor(intersection, level-1, kkx).scale(kx) : Color.BLACK;
    }

    /**
     * Calculates the sum of global effects (reflection and refraction) at the intersection point.
     *
     * @param intersection the intersection point
     * @param level the current recursion level
     * @param k the accumulated attenuation coefficient
     * @return the combined color from reflection and refraction
     */
    private Color calcGlobalEffects(Intersection intersection, int level, Double3 k)
    {
        return calcGlobalEffect(constructRefractedRay(intersection),
                level, k, intersection.material.KT)
                .add(calcGlobalEffect(constructReflectedRay(intersection),
                        level, k, intersection.material.KR));
    }

    /**
     * Constructs a reflected ray from the intersection point.
     *
     * @param intersection the intersection point from which to construct the reflected ray
     * @return the constructed reflected ray
     */
    private Ray constructReflectedRay(Intersection intersection) {
        Vector v = intersection.rayDir;
        Vector n = intersection.normal;
        Vector r = v.subtract(n.scale(2 * intersection.scaleNR));
        // Offset the origin of the reflected ray slightly to avoid self-intersection
        Vector delta = n.scale(intersection.scaleNR > 0 ? -DELTA : DELTA);
        return new Ray(intersection.point.add(delta), r);
    }

    /**
     * Constructs a refracted ray from the intersection point.
     *
     * @param intersection the intersection point from which to construct the refracted ray
     * @return the constructed refracted ray
     */
    private Ray constructRefractedRay(Intersection intersection) {
        Vector v = intersection.rayDir;
        Vector n = intersection.normal;
        // Offset the origin of the refracted ray slightly to avoid self-intersection
        Vector delta = n.scale(intersection.scaleNR > 0 ? DELTA : -DELTA);
        return new Ray(intersection.point.add(delta), v);
    }

    /**
     * Finds the closest intersection between a ray and the scene geometries.
     *
     * @param ray the ray to find intersections with
     * @return the closest intersection or null if none found
     */
    private Intersection findClosestIntersection(Ray ray) {
        var intersections = scene.geometries.calculateIntersections(ray);
        return intersections == null ? null : ray.findClosestIntersection(intersections);
    }

}