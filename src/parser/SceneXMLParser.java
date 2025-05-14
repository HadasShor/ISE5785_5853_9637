package parser;

/**
 * A parser for reading and initializing a {@link SceneDescriptor} object from an XML file.
 * This class uses the {@link SceneDescriptor#InitializeFromXMLstring(String)} method to parse
 * the XML file and populate the scene data.
 */
public class SceneXMLParser {
    private SceneDescriptor data;

    /**
     * Initializes the {@link SceneDescriptor} by parsing the specified XML file.
     *
     * @param xmlFilePath the path to the XML file
     */
    public void initializeFromXML(String xmlFilePath) {
        data = new SceneDescriptor().InitializeFromXMLstring(xmlFilePath);
    }

    /**
     * Gets the {@link SceneDescriptor} containing the parsed scene data.
     *
     * @return the {@link SceneDescriptor} object
     */
    public SceneDescriptor getData() {
        return data;
    }

    /**
     * Main method for testing the XML parsing functionality.
     * Parses a sample XML file and prints the parsed scene data.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        SceneXMLParser parser = new SceneXMLParser();
        parser.initializeFromXML("xml/renderTestTwoColors (1).xml");

        System.out.println("Scene Attributes: " + parser.getData().getSceneAttributes());
        System.out.println("Ambient Light: " + parser.getData().getAmbientLightAttributes());
        System.out.println("Spheres: " + parser.getData().getSpheres());
        System.out.println("Triangles: " + parser.getData().getTriangles());
    }
}