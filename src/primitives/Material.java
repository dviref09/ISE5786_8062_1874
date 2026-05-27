package primitives;

public final class Material {
    /**
     * The ambient lighting coefficient of the material
     */
    public Double3 kA = Double3.ONE;
    /**
     * The diffusive coefficient of the material
     */
    public Double3 kD = Double3.ZERO;
    /**
     * The specular coefficient of the material
     */
    public Double3 kS = Double3.ZERO;
    /**
     * The shininess exponent in the specular part in phong reflection model
     */
    public int nShininess = 0;
    
    /**
     * Setter for kA from Double3
     * @param kA The new value for kA
     * @return The same Material class for chaining setters
     */
    public Material setKA(Double3 kA) {
        this.kA = kA;
        return this;
    }
    
    /**
     * Setter for kA from one double (all the values in the Double3 will be the same)
     * @param kA The new value for kA
     * @return The same Material class for chaining setters
     */
    public Material setKA(double kA) {
        this.kA = new Double3(kA);
        return this;
    }
    
    /**
     * Setter for kS from Double 3
     * @param kS The new value for kS
     * @return The same Material class for chaining setters
     */
    public Material setKS(Double3 kS) {
        this.kS = kS;
        return this;
    }
    
    /**
     * Setter for kS from one double (all the values in the Double3 will be the same)
     * @param kS The new value for kS
     * @return The same Material class for chaining setters
     */
    public Material setKS(double kS) {
        this.kS = new Double3(kS);
        return this;
    }
    
    /**
     * Setter for kD from Double 3
     * @param kD The new value for kD
     * @return The same Material class for chaining setters
     */
    public Material setKD(Double3 kD) {
        this.kD = kD;
        return this;
    }
    
    /**
     * Setter for kD from one double (all the values in the Double3 will be the same)
     * @param kD The new value for kD
     * @return The same Material class for chaining setters
     */
    public Material setKD(double kD) {
        this.kD = new Double3(kD);
        return this;
    }
    
    /**
     * Setter for nShininess
     * @param nShininess The new value for nShininess
     * @return The same Material class for chaining setters
     */
    public Material setShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }
}

