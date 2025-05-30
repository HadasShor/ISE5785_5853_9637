package lighting;

import primitives.Color;

/**
 * Abstract base class for all light sources.
 * This class provides the fundamental property of light: its intensity (color).
 * Concrete light types (e.g., ambient, directional, point) will extend this class
 * and implement specific behaviors.
 */
abstract class Light {

    /**
     * The color intensity of the light source.
     */
    protected final Color intensity;

    /**
     * Constructs a new Light object with the specified intensity.
     *
     * @param intensity The {@link Color} representing the intensity of the light.
     */
    Light(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * Gets the intensity (color) of the light source.
     *
     * @return The {@link Color} representing the intensity of the light.
     */
    public Color getIntensity() {
        return intensity;
    }
}