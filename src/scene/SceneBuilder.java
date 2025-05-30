package scene;

import geometries.Geometries;
import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import parser.SAXHandler;
import parser.SceneDescriptor;
import primitives.Color;
import primitives.Point;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException; // Added for specific exception
import org.xml.sax.SAXException;                     // Added for specific exception

import java.io.File;
import java.io.IOException;                          // Added for specific exception
import java.util.Map;

/**
 * The {@code SceneBuilder} class is responsible for constructing a {@link Scene} object
 * from data parsed from an XML file. It uses a {@link SceneDescriptor} to hold the parsed
 * attributes and geometries before building the final {@link Scene}.
 */
public class SceneBuilder {

    /**
     * The descriptor holding the raw parsed data from the XML file.
     */
    private SceneDescriptor descriptor;
    /**
     * The {@link Scene} object being built.
     */
    private Scene scene;
    /**
     * The file path to the XML scene definition.
     */
    private static String filePath;

    /**
     * Constructs a new {@code SceneBuilder} with a default {@link SceneDescriptor}
     * and a predefined default file path for XML scene loading.
     */
    public SceneBuilder() {
        descriptor = new SceneDescriptor();
        filePath = "xml/renderTestTwoColors (1).xml";
    }

    /**
     * Gets the default file path for the XML scene file.
     * Checks if the file exists before returning the path.
     *
     * @return The file path as a string, or {@code null} if the file path is not set or the file does not exist.
     */
    public static String getFilePath() {
        if (filePath == null) {
            return null;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            // In a production scenario, consider throwing an IOException or returning Optional<String>
            return null;
        }
        return filePath;
    }

    /**
     * Loads scene data from the given XML file using a SAX parser and stores it in the internal {@link SceneDescriptor}.
     *
     * @param filePath The path to the XML file containing the scene definition.
     * @return This {@code SceneBuilder} instance, allowing for method chaining.
     * @throws ParserConfigurationException if a parser cannot be created which satisfies the requested configuration.
     * @throws SAXException                 if any SAX errors occur during parsing.
     * @throws IOException                  if any I/O errors occur.
     */
    /**
     * Loads scene data from the given XML file using a SAX parser and stores it in the internal {@link SceneDescriptor}.
     * This method handles potential parsing and I/O exceptions internally, printing their stack traces if they occur.
     *
     * @param filePath The path to the XML file containing the scene definition.
     * @return This {@code SceneBuilder} instance, allowing for method chaining.
     */
    public SceneBuilder loadSceneFromFile(String filePath) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            SAXHandler handler = new SAXHandler(descriptor);
            saxParser.parse(new File(filePath), handler);
        } catch (ParserConfigurationException | SAXException | IOException e) { // Catching specific exceptions
            e.printStackTrace(); // Log the exception or re-throw a custom one in production
        }
        return this;
    }

    /**
     * Builds the {@link Scene} object using the parsed data stored in the {@link SceneDescriptor}.
     * This method initializes scene properties like background color and ambient light,
     * and adds all defined geometries (spheres and triangles) to the scene.
     *
     * @return The constructed {@link Scene} object.
     */
    public Scene build() {
        scene = new Scene("XML Scene");

        // Set background color
        String bgColorStr = descriptor.getSceneAttributes().get("background-color");
        if (bgColorStr != null) {
            scene.setBackground(parseColor(bgColorStr));
        }

        // Set ambient light
        String ambientColorStr = descriptor.getAmbientLightAttributes().get("color");
        if (ambientColorStr != null) {
            // Note: Ambient light intensity (kA) is currently hardcoded to 1.0.
            // If the XML schema supports 'kA', it should be parsed from
            // descriptor.getAmbientLightAttributes() for accurate scene representation.
            scene.setAmbientLight(new AmbientLight(parseColor(ambientColorStr), 1.0));
        }

        // Add geometries
        Geometries geometries = new Geometries();

        for (Map<String, String> sphereAttrs : descriptor.getSpheres()) {
            Point center = parsePoint(sphereAttrs.get("center"));
            double radius = Double.parseDouble(sphereAttrs.get("radius"));
            geometries.add(new Sphere(radius, center));
        }

        for (Map<String, String> triangleAttrs : descriptor.getTriangles()) {
            Point p0 = parsePoint(triangleAttrs.get("p0"));
            Point p1 = parsePoint(triangleAttrs.get("p1"));
            Point p2 = parsePoint(triangleAttrs.get("p2"));
            geometries.add(new Triangle(p0, p1, p2));
        }

        scene.setGeometries(geometries);
        return scene;
    }

    /**
     * Parses a color string in the format "R G B" into a {@link Color} object.
     *
     * @param colorStr The string representation of the color (e.g., "255 0 128").
     * @return The parsed {@link Color} object.
     * @throws NumberFormatException if any part of the color string is not a valid integer.
     */
    private Color parseColor(String colorStr) {
        String[] parts = colorStr.trim().split(" ");
        return new Color(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
    }

    /**
     * Parses a point string in the format "X Y Z" into a {@link Point} object.
     *
     * @param pointStr The string representation of the point (e.g., "1.0 2.5 -3.0").
     * @return The parsed {@link Point} object.
     * @throws NumberFormatException if any part of the point string is not a valid double.
     */
    private Point parsePoint(String pointStr) {
        String[] parts = pointStr.trim().split(" ");
        return new Point(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
    }
}