package lighting;

import primitives.Color;

abstract class Light {

    protected final Color intensity;

    Light(Color intensity) {
        this.intensity = intensity;
    }

    public Color getIntensity() {
        return intensity;
    }
}
