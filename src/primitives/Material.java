package primitives;

public class Material {

    public Double3 Ka=Double3.ONE;
    public Double3 Kd=Double3.Zero;
    public Double3 Ks=Double3.Zero;
    public int nSh=0;
    public Double3 KT=Double3.ZERO;
    public Double3 KR=Double3.ZERO;

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
    public Material setKT(Double3 kt) {
        this.KT = kt ;
        return this;
    }
    public Material setKT (double kt) {
        this.KT = new Double3(kt);
        return this;
    }
    public Material setKR(Double3 kr) {
        this.KR = kr ;
        return this;
    }
    public Material setKR (double kr) {
        this.KR = new Double3(kr);
        return this;
    }
}
