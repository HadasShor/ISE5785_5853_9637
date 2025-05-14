package parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;
import java.util.Map;


/**
 * A custom SAX handler for parsing XML files into a {@link SceneDescriptor} object.
 * This handler processes elements such as "scene", "ambient-light", "sphere", and "triangle"
 * and maps their attributes to the corresponding fields in the {@link SceneDescriptor}.
 */
public class SAXHandler extends DefaultHandler {

    private SceneDescriptor sceneDesc = new SceneDescriptor();


    /**
     * Constructs a new SAXHandler with the given {@link SceneDescriptor}.
     *
     * @param sceneDesc the {@link SceneDescriptor} object to populate with parsed data
     */
    public SAXHandler(SceneDescriptor sceneDesc) {
        this.sceneDesc = sceneDesc;
    }
    /**
     * Returns the {@link SceneDescriptor} populated with the parsed XML data.
     *
     * @return the populated {@link SceneDescriptor}
     */
    public SceneDescriptor getSceneDescriptor() {
        return sceneDesc;
    }

    /**
     * Called at the start of an XML element. This method processes the element's attributes
     * and maps them to the appropriate fields in the {@link SceneDescriptor}.
     *
     * @param uri        the Namespace URI, or the empty string if the element has no Namespace URI
     * @param localName  the local name (without prefix), or the empty string if not namespace processing
     * @param qName      the qualified name (with prefix), or the empty string if qualified names are not available
     * @param attributes the attributes attached to the element
     * @throws SAXException any SAX exception, possibly wrapping another exception
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        Map<String, String> attrMap = new HashMap<>();
        for (int i = 0; i < attributes.getLength(); i++) {
            attrMap.put(attributes.getQName(i), attributes.getValue(i));
        }

        switch (qName) {
            case "scene":
                sceneDesc.getSceneAttributes().putAll(attrMap);
                break;
            case "ambient-light":
                sceneDesc.getAmbientLightAttributes().putAll(attrMap);
                break;
            case "sphere":
                sceneDesc.getSpheres().add(attrMap);
                break;
            case "triangle":
                sceneDesc.getTriangles().add(attrMap);
                break;
        }
    }
}
