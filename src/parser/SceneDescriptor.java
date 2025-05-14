package parser;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a descriptor for a scene, containing attributes for the scene, ambient light,
 * spheres, and triangles. This class provides methods to parse and initialize its data
 * from an XML file.
 */
public class SceneDescriptor {
    private Map<String, String> sceneAttributes = new HashMap<>();
    private Map<String, String> ambientLightAttributes = new HashMap<>();
    private List<Map<String, String>> spheres = new ArrayList<>();
    private List<Map<String, String>> triangles = new ArrayList<>();

    /**
     * Gets the attributes of the scene.
     *
     * @return a map containing the scene attributes
     */
    public Map<String, String> getSceneAttributes() {
        return sceneAttributes;
    }

    /**
     * Gets the attributes of the ambient light.
     *
     * @return a map containing the ambient light attributes
     */
    public Map<String, String> getAmbientLightAttributes() {
        return ambientLightAttributes;
    }

    /**
     * Gets the list of spheres in the scene.
     *
     * @return a list of maps, each containing the attributes of a sphere
     */
    public List<Map<String, String>> getSpheres() {
        return spheres;
    }

    /**
     * Gets the list of triangles in the scene.
     *
     * @return a list of maps, each containing the attributes of a triangle
     */
    public List<Map<String, String>> getTriangles() {
        return triangles;
    }

    /**
     * Initializes the scene descriptor by parsing an XML file.
     *
     * @param xmlFilePath the path to the XML file
     * @return the populated {@link SceneDescriptor} object, or null if an error occurs
     */
    public SceneDescriptor InitializeFromXMLstring(String xmlFilePath) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            SceneDescriptor descriptor = new SceneDescriptor();
            SAXHandler handler = new SAXHandler(descriptor);
            saxParser.parse(xmlFilePath, handler);

            return handler.getSceneDescriptor();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}