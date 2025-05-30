package geometries;

import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;

/**
 * The `Geometry` class is an abstract base class for all geometric objects in a 3D scene.
 * It provides common properties such as emission color and material, and defines an abstract method
 * for obtaining the normal vector at any point on the object's surface.
 */
public abstract class Geometry extends Intersectable {

    /**
     * The emission color of the geometry. This color is emitted by the object itself,
     * regardless of external light sources.
     * The default emission color is black.
     */
    protected Color emission = Color.BLACK; // Default emission color

    /**
     * Gets the emission color of the geometry.
     *
     * @return The current emission color of the geometry.
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * Sets the emission color for the geometry.
     *
     * @param emission The new {@link Color} to set as the emission color.
     * @return The current Geometry object, allowing for method chaining.
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }

    /**
     * Returns the normal vector to the geometry at the given point.
     * This method must be implemented by concrete geometric subclasses.
     *
     * @param p The point on the surface of the geometry for which to calculate the normal vector.
     * @return The normal {@link Vector} at the given point on the geometry's surface.
     */
    public abstract Vector getNormal(Point p);

    /**
     * The material properties of the geometry, defining how it interacts with light.
     * This includes properties like diffuse, specular, and shininess.
     * The default material is an empty {@link Material} object.
     */
    private Material material = new Material();

    /**
     * Gets the material properties of the geometry.
     *
     * @return The {@link Material} object associated with this geometry.
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Sets the material properties for the geometry.
     *
     * @param material The new {@link Material} to set for the geometry.
     * @return The current Geometry object, allowing for method chaining.
     */
    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }
}