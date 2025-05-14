package renderer;

import geometries.Intersectable;
import primitives.Color;
import primitives.Ray;
import scene.Scene;
import geometries.Intersectable.Intersection;
/**
 * The `RayTracerBase` class is an abstract base class for ray tracing algorithms.
 * It provides a method to trace rays through a scene and obtain the color at the intersection point.
 */
public abstract class RayTracerBase {

    /**
     * The scene to be rendered.
     */
    protected final Scene scene;
    /**
     * Constructor for RayTracerBase.
     *
     * @param scene The scene to be rendered.
     */
    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }
    /**
     * Traces a ray through the scene and returns the color at the intersection point.
     *
     * @param ray The ray to be traced.
     * @return The color at the intersection point.
     */
    public abstract Color traceRay(Ray ray) ;
}
