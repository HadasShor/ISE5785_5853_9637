package primitives;

public class Material {

    public Double3 Ka=Double3.ONE;
    public Double3 Kd=Double3.Zero;
    public Double3 Ks=Double3.Zero;
    public int nSh=0;

    public Material setKA(Double3 ka) {
        this.Ka = ka ;
        return this;
    }
    public Material setKA(double ka) {
        this.Ka = new Double3(ka);
        return this;
    }
    public Material setKD(Double3 kd) {
        this.Kd = kd ;
        return this;
    }
    public Material setKD(double kd) {
        this.Kd = new Double3(kd);
        return this;
    }
    public Material setKS(Double3 ks) {
        this.Ks = ks ;
        return this;
    }
    public Material setKS (double ks) {
        this.Ks = new Double3(ks);
        return this;
    }
    public Material setShininess(int nSh) {
        this.nSh = nSh;
        return this;
    }
}
