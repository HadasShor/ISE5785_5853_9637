package lighting;

import primitives.Color;

/**
 * The {@code AmbientLight} class represents ambient light in a scene.
 * It is characterized by its intensity, which is a color value.
 * Ambient light is non-directional and illuminates all objects equally.
 * @author Hadas_Shor, Nurit_Ezra
 */
public class AmbientLight {
    /**
     * Represents no ambient light (black color).
     */
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK) ;
    /**
     * The intensity of the ambient light.
     * It is represented as a {@link Color} object.
     */
    private final Color intensity;
    /**
     * Constructor for AmbientLight with intensity.
     *
     * @param IA The intensity of the ambient light.
     */
    public AmbientLight(Color IA) {
        this.intensity = IA;
    }

    /**
     * Gets the intensity of the ambient light.
     *
     * @return The color intensity of the ambient light.
     */
    public Color getIntensity() {
        return intensity;
    }
}