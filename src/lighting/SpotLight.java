package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class SpotLight extends PointLight {
    private Double narrowBeam = 1d;
    private final Vector direction;
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }
    @Override
    public SpotLight setKc(double kC) {
        super.setKc(kC);
        return this;
    }
    @Override
    public SpotLight setKL(double kL) {
       super.setKL(kL);
        return this;
    }
    @Override
    public SpotLight setKQ(double kQ) {
        super.setKQ(kQ);
        return this;
    }

    @Override
    public Color getIntensity(Point point) {
        Vector l = getL(point);
        double dir = l.dotProduct(direction.scale(-1));
        double intensity = Math.max(0, dir);
        intensity = Math.pow(intensity, narrowBeam);
        return super.getIntensity(point).scale(intensity);
    }
    /**
     * set the narrow beam of the light
     * @param narrowBeam the narrow beam of the light
     * @return the light source
     */
    public SpotLight setNarrowBeam(double narrowBeam) {
        this.narrowBeam = narrowBeam;
        return this;
    }
}
