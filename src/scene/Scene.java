package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import lighting.LightSource;
import primitives.Color;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a scene in a 3D environment, containing various properties such as the
 * scene's name, background color, ambient light, and geometries.
 */
public class Scene {

    /**
     * A list of light sources present in the scene.
     */
    public List <LightSource> light=new LinkedList<>();

    /**
     * The name of the scene.
     */
    public final String sceneName;

    /**
     * The background color of the scene. Defaults to {@link Color#BLACK}.
     */
    public Color backgroundColor = Color.BLACK;

    /**
     * The ambient light of the scene. Defaults to {@link AmbientLight#NONE}.
     */
    public AmbientLight ambientLight = AmbientLight.NONE;

    /**
     * The collection of geometries in the scene. Represented by the {@link Geometries} object.
     */
    public final Geometries geometries = new Geometries();


    /**
     * Constructs a new scene with a specified name.
     *
     * @param sceneName The name of the scene.
     */
    public Scene(String sceneName) {
        this.sceneName = sceneName;
    }

    /**
     * Sets the background color of the scene.
     *
     * @param backgroundColor The color to set as the background.
     * @return The current scene object, allowing for method chaining.
     */
    public Scene setBackground(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    /**
     * Sets the ambient light for the scene.
     *
     * @param ambientLight The ambient light to set for the scene.
     * @return The current scene object, allowing for method chaining.
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /**
     * Adds geometries to the scene's existing collection.
     *
     * @param geometries The {@link Geometries} object containing the geometries to add.
     * @return The current {@code Scene} instance, for method chaining.
     */
    public Scene setGeometries(Geometries geometries) {
        this.geometries.add(geometries);
        return this;
    }

    /**
     * Sets the list of light sources for the scene.
     *
     * @param light A {@link List} of {@link LightSource} objects to set as the scene's lights.
     * @return The current {@code Scene} instance, for method chaining.
     */
    public Scene setLight(List <LightSource> light) {
        this.light = light;
        return this;
    }
}