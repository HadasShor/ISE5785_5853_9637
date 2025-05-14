package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;
import primitives.Util;

public class PointLight extends Light implements LightSource{
    protected Point position;
    private double kC=1;
    private double kL=0;
    private double kQ=0;

    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }



    public PointLight setKc(double kC) {
        this.kC = kC;
        return this;
    }
    public PointLight setKL(double kL) {
        this.kL = kL;
        return this;
    }
    public PointLight setKQ(double kQ) {
        this.kQ = kQ;
        return this;
    }


    public Color getIntensity(Point point) {
        double d = position.distance(point);
        double factor = kC + kL * d + kQ * d * d;
        if(Util.isZero(factor))
            return intensity.scale(Double.POSITIVE_INFINITY);

        return intensity.scale(1d/factor);
    }

    @Override
    public Vector getL(Point point) {
        return point.subtract(position).normalize();
    }
}
