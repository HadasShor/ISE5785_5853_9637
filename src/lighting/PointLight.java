package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;
import primitives.Util;

/**
 * The {@code PointLight} class represents a light source with a specific position in space.
 * Its intensity diminishes with distance from the light source.
 */
public class PointLight extends Light implements LightSource {
    /**
     * The position of the point light in 3D space.
     */
    protected Point position;
    /**
     * Constant attenuation factor. Default is 1.
     */
    private double kC = 1;
    /**
     * Linear attenuation factor. Default is 0.
     */
    private double kL = 0;
    /**
     * Quadratic attenuation factor. Default is 0.
     */
    private double kQ = 0;

    /**
     * Constructs a {@code PointLight} object with the specified intensity and position.
     *
     * @param intensity The base intensity of the point light.
     * @param position  The position of the point light.
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }

    /**
     * Sets the constant attenuation factor (kC) for the point light.
     *
     * @param kC The constant attenuation factor to set.
     * @return The {@code PointLight} object itself, for chaining calls.
     */
    public PointLight setKc(double kC) {
        this.kC = kC;
        return this;
    }

    /**
     * Sets the linear attenuation factor (kL) for the point light.
     *
     * @param kL The linear attenuation factor to set.
     * @return The {@code PointLight} object itself, for chaining calls.
     */
    public PointLight setKl(double kL) {
        this.kL = kL;
        return this;
    }

    /**
     * Sets the quadratic attenuation factor (kQ) for the point light.
     *
     * @param kQ The quadratic attenuation factor to set.
     * @return The {@code PointLight} object itself, for chaining calls.
     */
    public PointLight setKq(double kQ) {
        this.kQ = kQ;
        return this;
    }

    /**
     * Calculates the intensity of the light at a given point in space, considering attenuation.
     * The intensity decreases with distance from the light source.
     *
     * @param point The point in space for which to calculate the light intensity.
     * @return The attenuated color intensity at the given point.
     */
    @Override
    public Color getIntensity(Point point) {
        double d = position.distance(point);
        double factor = kC + kL * d + kQ * d * d;

        if (Util.isZero(factor)) { // Check if factor is effectively zero to avoid division by zero
            return Color.BLACK;
        }

        return intensity.scale(1d / factor);
    }

    /**
     * Returns the direction vector from the light source to a given point.
     *
     * @param point The point to which the vector should be directed.
     * @return A normalized vector pointing from the light's position to the given point.
     */
    @Override
    public Vector getL(Point point) {
        return point.subtract(position).normalize();
    }

    /**
     * Returns the distance from the light source to a given point.
     *
     * @param point The point to which the distance is measured.
     * @return The distance between the light's position and the given point.
     */
    @Override
    public double getDistance(Point point) {
        return position.distance(point);
    }
}