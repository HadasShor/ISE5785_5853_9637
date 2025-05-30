package lighting;

import primitives.Color;

/**
 * The {@code AmbientLight} class represents ambient light in a scene.
 * It is characterized by its intensity, which is a color value.
 * Ambient light is non-directional and illuminates all objects equally.
 * @author Hadas_Shor, Nurit_Ezra
 */
public class AmbientLight extends Light {
    /**
     * Represents no ambient light (black color).
     */
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK,1.0) ;

    /**
     * Constructs an {@code AmbientLight} object with the specified intensity and attenuation factor.
     *
     * @param IA The base intensity of the ambient light.
     * @param kA The attenuation factor for the ambient light's intensity.
     */
    public AmbientLight(Color IA, double kA) {
        super(IA.scale(kA));
    }

    /**
     * Constructs an {@code AmbientLight} object with the specified base intensity.
     * The attenuation factor is implicitly 1.0.
     *
     * @param IA The base intensity of the ambient light.
     */
    public AmbientLight(Color IA) {
        super(IA);
    }

    /**
     * Gets the intensity of the ambient light.
     *
     * @return The color intensity of the ambient light.
     */
    @Override
    public Color getIntensity() {
        return intensity;
    }
}