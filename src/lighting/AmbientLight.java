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
     * Constructor for AmbientLight with intensity.
     *
     * @param IA The intensity of the ambient light.
     */
    public AmbientLight(Color IA, double kA) {
        super(IA);
    }
    public AmbientLight(Color IA) {
        super(IA);
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