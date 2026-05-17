package scene;

import geometries.impl.Geometries;
import lighting.AmbientLight;
import primitives.Color;

/**
 * A PDS (Passive Data Structure) for representing a scene for rendering, it includes:
 * <ul>
 * <li>Background Color</li>
 * <li>Ambient Color</li>
 * <li>Geometric Bodies</li>
 * </ul>
 */
public final class Scene {
    /**
     * The name of the scene
     */
    public String name;
    /**
     * The background color of the scene
     */
    public Color background = Color.BLACK;
    /**
     * The ambient light of the scene, by default set the none;
     */
    public AmbientLight ambientLight = AmbientLight.NONE;
    /**
     * The collection of geometric bodies in the scene, by default set to empty
     */
    public Geometries geometries = new Geometries();

    /**
     * Constructor of the scene
     * @param name The name of the scene
     */
    public Scene(String name) {
        this.name = name;
    }

    // setters
    // all the setter are returning this for setters chaining

    /**
     * Setter for the name of the scene
     * @param name The new name for the scene
     * @return The scene after the change for setters chaining
     */
    public Scene setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Setter for the background color of the scene
     * @param background The new background color
     * @return The scene after the change for setters chaining
     */
    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }

    /**
     * Setter for the ambient light of the scene
     * @param ambientLight The new ambient light
     * @return The scene after the change for setters chaining
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /**
     * Setter for the collection of geometric bodies in the scene
     * @param geometries The new collection of geometric bodies
     * @return The scene after the change for setters chaining
     */
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }
}
