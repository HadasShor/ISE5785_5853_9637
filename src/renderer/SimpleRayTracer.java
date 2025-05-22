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
        Color c;
        if (preprocessIntersection(intersection, ray.getDirection())) {
             c= calcColor(intersection, MAX_CALC_COLOR_LEVEL, INITIAL_K)
                    .add(scene.ambientLight.getIntensity().scale(intersection.geometry.getMaterial().Ka));
        } else {
            return Color.BLACK;
        }
        return c;
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

                if (!setLightSource(intersection, lightSource)||!unshaded(intersection,lightSource)) {
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
        Point origin = intersection.point.add(delta);
        Ray shadowRay = new Ray(origin, pointToLight);

        double maxDistance = lightSource.getDistance(intersection.point);

        List<Intersection> intersections = scene.geometries.calculateIntersectionsHelper(shadowRay);
        if (intersections == null || intersections.isEmpty()) {
            return true; // אין חסימה
        }

        for (Intersection shadowIntersection : intersections) {
            if (shadowIntersection.geometry == intersection.geometry) {
                continue; // מדלגים על הגוף עצמו
            }

            double distanceFromRayOrigin = shadowIntersection.point.distance(origin);
            if (distanceFromRayOrigin < maxDistance) {
                // בודקים אם יש שקיפות חלקית – אם לא שקוף → חסימה
                if (shadowIntersection.geometry.getMaterial().KT.lowerThan(MIN_CALC_COLOR_K)) {
                    return false; // חסום
                }
            }
        }

        return true;
    }



   private Color calcColor (Intersection intersection, int level, Double3 k) {

       Color color = calcColorLocalEffects(intersection);
        if (level == 1 || k.lowerThan(MIN_CALC_COLOR_K)) {
            return color;
        }
      return color.add(calcGlobalEffects(intersection, level-1, k));

    }

    private Color calcGlobalEffect(Ray ray, int level, Double3 k, Double3 kx) {Double3 kkx = k.product(kx);
        if (kkx.lowerThan(MIN_CALC_COLOR_K)) return Color.BLACK;
        Intersection intersection = findClosestIntersection(ray);
        if (intersection == null) return scene.backgroundColor.scale(kx);
        return preprocessIntersection(intersection, ray.getDirection())
                ? calcColor(intersection, level-1, kkx).scale(kx) : Color.BLACK;
    }
    private Color calcGlobalEffects(Intersection intersection, int level, Double3 k)
    {
        return calcGlobalEffect(constructRefractedRay(intersection),
            level, k, intersection.material.KT)
            .add(calcGlobalEffect(constructReflectedRay(intersection),
                    level, k, intersection.material.KR));
    }
    private Ray constructReflectedRay(Intersection intersection) {
        Vector v = intersection.rayDir;
        Vector n = intersection.normal;
        Vector r = v.subtract(n.scale(2 * intersection.scaleNR));
        Vector delta = n.scale(intersection.scaleNR > 0 ? -DELTA : DELTA);
        return new Ray(intersection.point.add(delta), r);
    }

    private Ray constructRefractedRay(Intersection intersection) {
        Vector v = intersection.rayDir;
        Vector n = intersection.normal;
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
