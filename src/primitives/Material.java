package primitives;

/**
 * Represents the material properties of a geometric object, influencing how it interacts with light.
 * This includes coefficients for ambient, diffuse, specular, shininess, transparency, and reflectivity.
 */
public class Material {

    /**
     * The ambient reflection coefficient. This determines how much ambient light is reflected by the material.
     * Default value is {@link Double3#ONE}.
     */
    public Double3 Ka = Double3.ONE;
    /**
     * The diffuse reflection coefficient. This determines how much diffuse light is reflected by the material.
     * Default value is {@link Double3#Zero}.
     */
    public Double3 Kd = Double3.Zero;
    /**
     * The specular reflection coefficient. This determines how much specular light is reflected by the material.
     * Default value is {@link Double3#Zero}.
     */
    public Double3 Ks = Double3.Zero;
    /**
     * The shininess exponent for specular reflection. A higher value results in a smaller, more intense specular highlight.
     * Default value is 0.
     */
    public int nSh = 0;
    /**
     * The transparency coefficient. This determines how much light passes through the material.
     * Default value is {@link Double3#ZERO}.
     */
    public Double3 KT = Double3.ZERO;
    /**
     * The reflectivity coefficient. This determines how much light is reflected off the material's surface.
     * Default value is {@link Double3#ZERO}.
     */
    public Double3 KR = Double3.ZERO;

    /**
     * Sets the ambient reflection coefficient (Ka) of the material.
     *
     * @param Ka The {@link Double3} value for the ambient reflection coefficient.
     * @return The current Material object, allowing for method chaining.
     */
    public Material setKA(Double3 Ka) {
        this.Ka = Ka;
        return this;
    }

    /**
     * Sets the ambient reflection coefficient (Ka) of the material with a single scalar value,
     * which will be applied to all three color components.
     *
     * @param Ka The scalar value for the ambient reflection coefficient.
     * @return The current Material object, allowing for method chaining.
     */
    public Material setKA(double Ka) {
        this.Ka = new Double3(Ka);
        return this;
    }

    /**
     * Sets the diffuse reflection coefficient (Kd) of the material.
     *
     * @param Kd The {@link Double3} value for the diffuse reflection coefficient.
     * @return The current Material object, allowing for method chaining.
     */
    public Material setKD(Double3 Kd) {
        this.Kd = Kd;
        return this;
    }

    /**
     * Sets the diffuse reflection coefficient (Kd) of the material with a single scalar value,
     * which will be applied to all three color components.
     *
     * @param Kd The scalar value for the diffuse reflection coefficient.
     * @return The current Material object, allowing for method chaining.
     */
    public Material setKD(double Kd) {
        this.Kd = new Double3(Kd);
        return this;
    }

    /**
     * Sets the specular reflection coefficient (Ks) of the material.
     *
     * @param Ks The {@link Double3} value for the specular reflection coefficient.
     * @return The current Material object, allowing for method chaining.
     */
    public Material setKS(Double3 Ks) {
        this.Ks = Ks;
        return this;
    }

    /**
     * Sets the specular reflection coefficient (Ks) of the material with a single scalar value,
     * which will be applied to all three color components.
     *
     * @param Ks The scalar value for the specular reflection coefficient.
     * @return The current Material object, allowing for method chaining.
     */
    public Material setKS(double Ks) {
        this.Ks = new Double3(Ks);
        return this;
    }

    /**
     * Sets the shininess exponent (nSh) of the material.
     *
     * @param nSh The integer value for the shininess exponent.
     * @return The current Material object, allowing for method chaining.
     */
    public Material setShininess(int nSh) {
        this.nSh = nSh;
        return this;
    }

    /**
     * Sets the transparency coefficient (KT) of the material.
     *
     * @param KT The {@link Double3} value for the transparency coefficient.
     * @return The current Material object, allowing for method chaining.
     */
    public Material setKT(Double3 KT) {
        this.KT = KT;
        return this;
    }

    /**
     * Sets the transparency coefficient (KT) of the material with a single scalar value,
     * which will be applied to all three color components.
     *
     * @param KT The scalar value for the transparency coefficient.
     * @return The current Material object, allowing for method chaining.
     */
    public Material setKT(double KT) {
        this.KT = new Double3(KT);
        return this;
    }

    /**
     * Sets the reflectivity coefficient (KR) of the material.
     *
     * @param KR The {@link Double3} value for the reflectivity coefficient.
     * @return The current Material object, allowing for method chaining.
     */
    public Material setKR(Double3 KR) {
        this.KR = KR;
        return this;
    }

    /**
     * Sets the reflectivity coefficient (KR) of the material with a single scalar value,
     * which will be applied to all three color components.
     *
     * @param KR The scalar value for the reflectivity coefficient.
     * @return The current Material object, allowing for method chaining.
     */
    public Material setKR(double KR) {
        this.KR = new Double3(KR);
        return this;
    }
}