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


    private static final double DELTA = 0.1; // Maximum recursion depth for color calculation
    private static final int MAX_CALC_COLOR_LEVEL =10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final Double3 INITIAL_K = Double3.ONE;
    /**
     * Constructor for {@code SimpleRayTracer}.
     *
     * @param scene the scene to be rendered, containing geometries and lighting
     *              //* @param  a placeholder parameter representing the ray tracer type (currently unused)
     */
    public SimpleRayTracer(Scene scene, RayTracerType simple) {
        super(scene);
    }

    public SimpleRayTracer(Scene scene) {
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
     * Calculates the color at the intersection point.
     *
     * @param intersection the intersection object
     * @param ray          the ray that hit the object
     * @return the color at the intersection point
     */
    private Color calcColor(Intersection intersection, Ray ray) {

//        if (!preprocessIntersection(intersection, ray.getDirection()))
//            return Color.BLACK; // No local effects → return black
//
//        Color color = scene.ambientLight.getIntensity()
//                .scale(intersection.material.Ka)
//                .add(calcColorLocalEffects(intersection));
//        return color;
        return preprocessIntersection(intersection, ray.getDirection())
                ? calcColor(intersection, MAX_CALC_COLOR_LEVEL, INITIAL_K)
                .add(scene.ambientLight.getIntensity().scale(intersection.geometry.getMaterial().Ka))
                : Color.BLACK;
    }


    /// need check
    public boolean preprocessIntersection(Intersection intersection, Vector rayDir) {
        intersection.rayDir = rayDir;
        intersection.normal = intersection.geometry.getNormal(intersection.point);

        intersection.scaleNR = alignZero(intersection.rayDir.dotProduct(intersection.normal));
        if (intersection.scaleNR == 0)
            return false;
        return true;
    }

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
            {
                if (!setLightSource(intersection, lightSource)||!unshaded(intersection, lightSource)) {
                    continue;
                }


                Color iL = lightSource.getIntensity(intersection.point);
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


    private boolean unshaded(Intersection intersection, LightSource lightSource) {

        Vector pointToLight = lightSource.getL(intersection.point).scale(-1);
        Vector delta = intersection.normal.scale(intersection.scaleNL < 0 ? DELTA : -DELTA);
        Ray shadowRay = new Ray(intersection.point.add(delta), pointToLight);
        // Check if the shadow ray intersects with any geometry in the scene
        List<Intersection> intersections = scene.geometries.calculateIntersectionsHelper(shadowRay);
        if (intersections == null||intersections.isEmpty()) {
            return true; // No intersections, shaded
        }
        double minDistance = lightSource.getDistance(intersection.point);
        for (Intersection shadowIntersection: intersections) {

            if (shadowIntersection.geometry == intersection.geometry) {
                continue; // Ignore the intersection with the same geometry
            }
            double dis = lightSource.getDistance(shadowIntersection.point);//.distance(intersection.point);
            double distance = shadowIntersection.point.distance(intersection.point);
            if (dis > minDistance) {
                return true; // Shaded by another geometry
            }
        }
        return false; // Not shaded
    }

    private Color calcColor (Intersection intersection, int level, Double3 k) {

    }
}
