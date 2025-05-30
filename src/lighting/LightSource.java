package lighting;

import primitives.*;

/**
 * Represents a generic light source in a 3D scene.
 * This interface defines the basic contract for any object that emits light,
 * providing methods to get the light's intensity, direction, and distance to a point.
 */
public interface LightSource {
    /**
     * Gets the intensity (color) of the light at a specific point.
     * The intensity might vary depending on the type of light source and its distance from the point.
     *
     * @param p The point for which to get the light intensity.
     * @return The color intensity of the light at the given point.
     */
    Color getIntensity(Point p);

    /**
     * Gets the normalized vector from the light source to a given point.
     * This vector is used to calculate the angle of incidence for lighting calculations.
     *
     * @param p The point towards which the light vector is directed.
     * @return A normalized vector pointing from the light source to the given point.
     */
    Vector getL(Point p);

    /**
     * Gets the distance from the light source to a given point.
     * This distance is used in attenuation calculations to determine how much the light intensity diminishes over distance.
     *
     * @param point The point to which the distance is calculated.
     * @return The distance from the light source to the specified point.
     */
    double getDistance(Point point);
}