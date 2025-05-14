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
import java.io.File;
import java.util.Map;


/**
 * Test method for {@link SceneBuilder#loadSceneFromFile(String)}.
 * Verifies that a scene is successfully loaded from a valid XML file
 * and that the file path matches the expected value.
 */
public class SceneBuilder {

    private SceneDescriptor descriptor;
    private Scene scene;
    private static String filePath;

    public SceneBuilder() {
        descriptor = new SceneDescriptor();
        filePath = "xml/renderTestTwoColors (1).xml";
    }
    /**
     * Gets the default file path for the XML file.
     *
     * @return the file path as a string, or null if the file does not exist
     */
    public static String getFilePath() {
        if (filePath == null) return null;
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File not found: " + filePath);
            return null;
        }
        return filePath;
    }

    /**
     * Loads scene data from the given XML file and stores it in the builder.
     * @param filePath path to the XML file
     * @return this builder instance
     */
    public SceneBuilder loadSceneFromFile(String filePath) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            SAXHandler handler = new SAXHandler(descriptor);
            saxParser.parse(new File(filePath), handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Builds the Scene object using the parsed SceneDescriptor.
     * @return the constructed Scene
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
     * @param colorStr the color string
     * @return the parsed {@link Color} object
     */
    // Utility methods
    private Color parseColor(String colorStr) {
        String[] parts = colorStr.trim().split(" ");
        return new Color(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
    }


    /**
     * Parses a point string in the format "X Y Z" into a {@link Point} object.
     *
     * @param pointStr the point string
     * @return the parsed {@link Point} object
     */
    private Point parsePoint(String pointStr) {
        String[] parts = pointStr.trim().split(" ");
        return new Point(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
    }
}
