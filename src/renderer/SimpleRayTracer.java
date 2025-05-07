package renderer;

import geometries.Geometries;
import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.List;

public class SimpleRayTracer extends RayTracerBase{
    /**
     * Constructor for SimpleRayTracer
     *
     * @param scene  the scene to be rendered
     * @param simple
     */
    public SimpleRayTracer(Scene scene, RayTracerType simple) {
        super(scene);
    }

    /**
     * Traces a ray through the scene and returns the color at the intersection point.
     * @param ray the ray to be traced
     * @return the color at the intersection point
     */
    @Override
    public Color traceRay(Ray ray) {
        List<Point> intersections=  scene.geometries.findIntersections(ray);
        if (intersections==null||intersections.isEmpty()) {
            return scene.backgroundColor;
        }
        Point closestPoint = ray.findClosedPoint(intersections);
        return calcColor(closestPoint);
    }

    private Color calcColor(Point point) {
       return scene.ambientLight.getIntensity();
    }
}
