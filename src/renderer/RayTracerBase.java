package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

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
