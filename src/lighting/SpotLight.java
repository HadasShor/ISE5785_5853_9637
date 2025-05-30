package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Represents a spotlight, a type of {@link PointLight} with a defined direction,
 * resulting in a cone-shaped illumination. The light's intensity diminishes
 * based on the angle to its direction vector, allowing for a focused beam.
 */
public class SpotLight extends PointLight {
    /**
     * The narrowness factor of the spotlight beam. A higher value results in a
     * more concentrated and smaller beam. Defaults to 1.0.
     */
    private Double narrowBeam = 1d;
    /**
     * The normalized direction vector of the spotlight.
     */
    private final Vector direction;

    /**
     * Constructs a new `SpotLight` instance.
     *
     * @param intensity The base color intensity of the light.
     * @param position The {@link Point} representing the light's position in 3D space.
     * @param direction The {@link Vector} indicating the direction of the spotlight's beam.
     * It will be normalized upon construction.
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }

    /**
     * Sets the constant attenuation factor (Kc) for the spotlight.
     * Overrides the method in {@link PointLight} to return `SpotLight` type for chaining.
     *
     * @param kC The constant attenuation factor.
     * @return The current `SpotLight` object, allowing for method chaining.
     */
    @Override
    public SpotLight setKc(double kC) {
        super.setKc(kC);
        return this;
    }

    /**
     * Sets the linear attenuation factor (Kl) for the spotlight.
     * Overrides the method in {@link PointLight} to return `SpotLight` type for chaining.
     *
     * @param kL The linear attenuation factor.
     * @return The current `SpotLight` object, allowing for method chaining.
     */
    @Override
    public SpotLight setKl(double kL) {
        super.setKl(kL);
        return this;
    }

    /**
     * Sets the quadratic attenuation factor (Kq) for the spotlight.
     * Overrides the method in {@link PointLight} to return `SpotLight` type for chaining.
     *
     * @param kQ The quadratic attenuation factor.
     * @return The current `SpotLight` object, allowing for method chaining.
     */
    @Override
    public SpotLight setKq(double kQ) {
        super.setKq(kQ);
        return this;
    }

    /**
     * Calculates the intensity of the spotlight at a given point in space.
     * The intensity is determined by the base intensity (from `PointLight`),
     * the distance to the point, and the angle between the light's direction
     * and the vector from the light to the point.
     *
     * @param point The {@link Point} in 3D space for which to calculate the intensity.
     * @return The {@link Color} representing the intensity of the light at the specified point.
     */
    @Override
    public Color getIntensity(Point point) {
        Vector l = getL(point); // Vector from light to point
        double dir = l.dotProduct(direction); // Cosine of the angle between light vector and spotlight direction

        // Ensure intensity is non-negative and apply the narrow beam effect
        double intensity = Math.max(0, dir);
        intensity = Math.pow(intensity, narrowBeam);

        // Scale the base point light intensity by the calculated directional intensity
        return super.getIntensity(point).scale(intensity);
    }

    /**
     * Sets the narrow beam factor for the spotlight.
     * A higher `narrowBeam` value will result in a more focused and narrower light cone.
     *
     * @param narrowBeam The double value representing the narrowness of the beam.
     * @return The current `SpotLight` object, allowing for method chaining.
     */
    public SpotLight setNarrowBeam(double narrowBeam) {
        this.narrowBeam = narrowBeam;
        return this;
    }

    /**
     * Returns the normalized vector from the spotlight's position to the given point.
     * This method overrides the `getL` method in {@link PointLight} to ensure the
     * returned vector is always normalized.
     *
     * @param point The {@link Point} for which to calculate the light vector.
     * @return The normalized {@link Vector} from the light's position to the given point.
     */
    @Override
    public Vector getL(Point point) {
        // The super.getL(point) already calculates the vector from position to point.
        // We ensure it's normalized here, though PointLight's getL might already do it.
        return super.getL(point).normalize();
    }
}