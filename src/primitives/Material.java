package primitives;

public final class Material {
    /**
     * The ambient lighting constant of the material
     */
    public Double3 kA = Double3.ONE;

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
}

