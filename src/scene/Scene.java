package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import primitives.Color;

public class Scene {
    public final String sceneName;
    public Color backgroundColor=Color.BLACK;
    public AmbientLight ambientLight= AmbientLight.NONE;
    public final Geometries geometries=new Geometries();


    public Scene(String sceneName) {
        this.sceneName = sceneName;
    }

    public Scene setBackground(Color backgroundColor) {
        this.backgroundColor=backgroundColor;
        return this;
    }

    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight=ambientLight;
        // Set the ambient light for the scene
        return this;
    }
}
