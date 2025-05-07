package lighting;

import primitives.Color;

public class AmbientLight {
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK) ;
    private final Color intensity;

    public AmbientLight(Color IA) {
        this.intensity = IA;
    }

    public Color getIntensity() {
        return intensity;
    }
}