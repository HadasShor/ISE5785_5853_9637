package parser;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import java.io.IOException;

/**
 * A parser for reading and initializing a {@link SceneDescriptor} object from an XML file.
 * This class uses the {@link SceneDescriptor#InitializeFromXMLstring(String)} method to parse
 * the XML file and populate the scene data.
 */
public class SceneXMLParser {
    /**
     * The {@link SceneDescriptor} object holding the parsed scene data.
     */
    private SceneDescriptor data;

    /**
     * Constructs a new {@code SceneXMLParser} instance.
     */
    public SceneXMLParser() {
        // Default constructor initializes the parser.
    }

    /**
     * Initializes the {@link SceneDescriptor} by parsing the specified XML file.
     * The parsed data will then be accessible via {@link #getData()}.
     *
     * @param xmlFilePath The path to the XML file to be parsed.
     * @throws ParserConfigurationException if a parser cannot be created which satisfies the requested configuration.
     * @throws SAXException                 if any SAX errors occur during parsing.
     * @throws IOException                  if any I/O errors occur (e.g., file not found).
     */
    public void initializeFromXML(String xmlFilePath) throws ParserConfigurationException, SAXException, IOException {
        data = new SceneDescriptor().InitializeFromXMLstring(xmlFilePath);
    }

    /**
     * Gets the {@link SceneDescriptor} containing the parsed scene data.
     * This method should be called after {@link #initializeFromXML(String)} has been successfully executed.
     *
     * @return The {@link SceneDescriptor} object populated with scene data, or {@code null} if parsing failed or hasn't occurred.
     */
    public SceneDescriptor getData() {
        return data;
    }

    /**
     * Main method for testing the XML parsing functionality.
     * This method parses a sample XML file and prints the parsed scene data to the console.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        SceneXMLParser parser = new SceneXMLParser();
        try {
            parser.initializeFromXML("unittest/xml/renderTestTwoColors.xml");

            System.out.println("Scene Attributes: " + parser.getData().getSceneAttributes());
            System.out.println("Ambient Light: " + parser.getData().getAmbientLightAttributes());
            System.out.println("Spheres: " + parser.getData().getSpheres());
            System.out.println("Triangles: " + parser.getData().getTriangles());
        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.err.println("Error parsing XML: " + e.getMessage());
            e.printStackTrace();
        }
    }
}