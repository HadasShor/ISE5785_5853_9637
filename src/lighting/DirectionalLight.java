package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Represents a directional light source in a 3D scene.
 * A directional light has a uniform intensity and direction,
 * simulating a distant light source like the sun.
 */
public class DirectionalLight extends Light implements LightSource {

    /**
     * The direction of the light.
     */
    private final Vector direction;

    /**
     * Constructs a new DirectionalLight with the specified intensity and direction.
     *
     * @param intensity The color intensity of the light.
     * @param direction The direction of the light.
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction;
    }

    /**
     * Gets the intensity of the directional light at a given point.
     * Since directional light has uniform intensity, the point does not affect the returned intensity.
     *
     * @param p The point for which to get the intensity (ignored for directional light).
     * @return The color intensity of the light.
     */
    @Override
    public Color getIntensity(Point p) {
        return intensity;
    }

    /**
     * Gets the direction vector from the light source to a given point.
     * For a directional light, this vector is constant and represents the light's direction.
     *
     * @param p The point for which to get the direction vector (ignored for directional light).
     * @return The direction vector of the light.
     */
    @Override
    public Vector getL(Point p) {
        return direction;
    }

    /**
     * Gets the distance from the light source to a given point.
     * For a directional light, the distance is considered infinite.
     *
     * @param point The point to which to calculate the distance.
     * @return Positive infinity, representing an infinite distance.
     */
    @Override
    public double getDistance(Point point) {
        return Double.POSITIVE_INFINITY;
    }
}