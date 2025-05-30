package parser;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a descriptor for a scene, containing attributes for the scene itself,
 * ambient light settings, and lists of attributes for all spheres and triangles within the scene.
 * This class facilitates parsing scene data from an XML file into structured maps and lists.
 */
public class SceneDescriptor {
    /**
     * A map storing key-value string pairs representing the attributes of the scene.
     * For example, "background-color".
     */
    private Map<String, String> sceneAttributes = new HashMap<>();
    /**
     * A map storing key-value string pairs representing the attributes of the ambient light.
     * For example, "color".
     */
    private Map<String, String> ambientLightAttributes = new HashMap<>();
    /**
     * A list of maps, where each inner map contains key-value string pairs
     * representing the attributes of a single sphere geometry (e.g., "center", "radius").
     */
    private List<Map<String, String>> spheres = new ArrayList<>();
    /**
     * A list of maps, where each inner map contains key-value string pairs
     * representing the attributes of a single triangle geometry (e.g., "p0", "p1", "p2").
     */
    private List<Map<String, String>> triangles = new ArrayList<>();

    /**
     * Constructs a new {@code SceneDescriptor} instance.
     */
    public SceneDescriptor() {
        // Default constructor, initializes internal data structures.
    }

    /**
     * Gets the attributes of the scene.
     *
     * @return A map containing the scene attributes (e.g., background color).
     */
    public Map<String, String> getSceneAttributes() {
        return sceneAttributes;
    }

    /**
     * Gets the attributes of the ambient light.
     *
     * @return A map containing the ambient light attributes (e.g., color).
     */
    public Map<String, String> getAmbientLightAttributes() {
        return ambientLightAttributes;
    }

    /**
     * Gets the list of spheres in the scene.
     * Each sphere is represented by a map of its attributes.
     *
     * @return A list of maps, where each map contains the attributes of a sphere.
     */
    public List<Map<String, String>> getSpheres() {
        return spheres;
    }

    /**
     * Gets the list of triangles in the scene.
     * Each triangle is represented by a map of its attributes.
     *
     * @return A list of maps, where each map contains the attributes of a triangle.
     */
    public List<Map<String, String>> getTriangles() {
        return triangles;
    }

    /**
     * Initializes the scene descriptor by parsing an XML file from the given path.
     * This method creates a SAX parser and uses a {@link SAXHandler} to populate
     * the descriptor's internal data structures.
     *
     * @param xmlFilePath The path to the XML file to be parsed.
     * @return The populated {@link SceneDescriptor} object if parsing is successful,
     * or {@code null} if an error occurs during parsing (e.g., file not found, XML parsing issues).
     * @throws ParserConfigurationException if a parser cannot be created which satisfies the requested configuration.
     * @throws SAXException if any SAX errors occur during parsing.
     * @throws IOException if any I/O errors occur.
     */
    public SceneDescriptor InitializeFromXMLstring(String xmlFilePath) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        // A new descriptor is created and passed to the handler, which populates it.
        // The current instance of SceneDescriptor (this) is not directly populated by the handler in this method.
        SceneDescriptor descriptor = new SceneDescriptor();
        SAXHandler handler = new SAXHandler(descriptor);
        saxParser.parse(xmlFilePath, handler);

        return handler.getSceneDescriptor();
    }
}