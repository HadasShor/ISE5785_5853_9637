package scene;

import geometries.Geometries;
import lighting.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import primitives.*;
import geometries.*;
import org.json.simple.parser.JSONParser;


import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**

 The JsonScene class provides functionality to import 3D scenes from JSON files.
 It parses scene definitions including geometries, materials, lights and other properties,
 converting them into Scene objects that can be rendered by the ray tracing engine.
 */
public class JsonScene {

    /**

     Imports a scene from a JSON file.
     Reads the file at the given path, parses the JSON structure, and constructs
     a Scene object with all defined properties, geometries, and light sources.
     */
    public static Scene importScene(String path) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(path));
        JSONObject sceneObj = (JSONObject) jsonObject.get("scene");

        String name = (String) sceneObj.get("name");
        Scene scene = new Scene(name);
        if(sceneObj.containsKey("background-color"))
            scene.setBackground(parseColor((String) sceneObj.get("background-color")));
        if(sceneObj.containsKey("ambient-light"))
        {
            JSONObject ambientLightObj = (JSONObject) sceneObj.get("ambient-light");
            Color ambientLight = parseColor((String) ambientLightObj.get("color"));
            scene.setAmbientLight(new AmbientLight(ambientLight));
        }
        if(sceneObj.containsKey("geometries")){
            JSONArray materials = (JSONArray) sceneObj.get("materials");
            scene.setGeometries(parseGeometries((JSONArray) sceneObj.get("geometries"), materials));
        }

        if(sceneObj.containsKey("lights"))
            scene.setLight(parseLights((JSONArray) sceneObj.get("lights")));

        return scene;
    }

    /**

     Parses an array of light source definitions from JSON.
     Identifies the type of each light (point, directional, or spot) and
     delegates to the appropriate parsing method.
     */
    private static List<LightSource> parseLights(JSONArray lights) {
        List<LightSource> lightSources = new LinkedList<>();
        for (Object obj : lights) {
            JSONObject lightObj = (JSONObject) obj;
            if (lightObj.containsKey("point")) {
                lightSources.add(parsePointLight((JSONObject) lightObj.get("point")));
            } else if (lightObj.containsKey("directional")) {
                lightSources.add(parseDirectionalLight((JSONObject) lightObj.get("directional")));
            } else if (lightObj.containsKey("spot")) {
                lightSources.add(parseSpotLight((JSONObject) lightObj.get("spot")));
            } else {
                throw new IllegalArgumentException("Unknown light type");
            }
        }
        return lightSources;
    }

    /**

     Parses a spot light from a JSON object.
     Creates a SpotLight with the specified intensity, position, and direction,
     and applies optional attenuation factors and narrow beam settings if defined.
     */
    private static LightSource parseSpotLight(JSONObject lightObj) {
        Color color = parseColor((String) lightObj.get("intensity"));
        Point position = parsePoint((String) lightObj.get("position"));
        Vector direction = parseVector((String) lightObj.get("direction"));
        SpotLight spotLight = new SpotLight(color, position, direction);
        if (lightObj.containsKey("kc")) {
            spotLight.setKc(((Number) lightObj.get("kc")).doubleValue());
        }
        if (lightObj.containsKey("kl")) {
            spotLight.setKl(((Number) lightObj.get("kl")).doubleValue());
        }
        if (lightObj.containsKey("kq")) {
            spotLight.setKq(((Number) lightObj.get("kq")).doubleValue());
        }
        if (lightObj.containsKey("narrow-beam")) {
            spotLight.setNarrowBeam(((Number) lightObj.get("narrow-beam")).doubleValue());
        }
        return spotLight;
    }

    /**

     Parses a directional light from a JSON object.
     Creates a DirectionalLight with the specified intensity and direction.
     */
    private static LightSource parseDirectionalLight(JSONObject lightObj) {
        Color color = parseColor((String) lightObj.get("intensity"));
        Vector direction = parseVector((String) lightObj.get("direction"));
        return new DirectionalLight(color, direction);
    }

    /**

     Parses a point light from a JSON object.
     Creates a PointLight with the specified intensity and position,
     and applies optional attenuation factors if defined.
     */
    private static LightSource parsePointLight(JSONObject lightObj) {
        Color color = parseColor((String) lightObj.get("intensity"));
        Point position = parsePoint((String) lightObj.get("position"));
        PointLight pointLight = new PointLight(color, position);
        if (lightObj.containsKey("kc")) {
            pointLight.setKc(((Number) lightObj.get("kc")).doubleValue());
        }
        if (lightObj.containsKey("kl")) {
            pointLight.setKl(((Number) lightObj.get("kl")).doubleValue());
        }
        if (lightObj.containsKey("kq")) {
            pointLight.setKq(((Number) lightObj.get("kq")).doubleValue());
        }

        return pointLight;
    }

    /**

     Parses geometry objects from a JSON array.
     For each geometry definition, identifies its type and delegates to the appropriate
     parsing method, then applies material and emission properties if specified.
     */
    private static Geometries parseGeometries(JSONArray geometriesArray, JSONArray materials) {
        Geometries geometries = new Geometries();
        for (Object obj : geometriesArray) {
            JSONObject geometryObj = (JSONObject) obj;
            Geometry geometry;
            if (geometryObj.containsKey("sphere")) {
                geometry = parseSphere((JSONObject) geometryObj.get("sphere"));
            } else if (geometryObj.containsKey("triangle")) {
                geometry = parseTriangle((JSONArray) geometryObj.get("triangle"));
            } else if (geometryObj.containsKey("plane")) {
                geometry = parsePlane((JSONObject) geometryObj.get("plane"));
            } else if (geometryObj.containsKey("polygon")) {
                geometry = parsePolygon((JSONArray) geometryObj.get("polygon"));
            } else if (geometryObj.containsKey("cylinder")) {
                geometry = parseCylinder((JSONObject) geometryObj.get("cylinder"));
            } else if (geometryObj.containsKey("tube")) {
                geometry = parseTube((JSONObject) geometryObj.get("tube"));
            } else {
                throw new IllegalArgumentException("Unknown geometry type");
            }
            if(geometry == null) continue;
            if (geometryObj.containsKey("material"))
                parseMaterial(geometryObj, geometry, materials);

            if(geometryObj.containsKey("emission"))
                geometry.setEmission(parseColor((String) geometryObj.get("emission")));

            geometries.add(geometry);
        }
        return geometries;
    }

    /**

     Parses material properties from a JSON object and applies them to a geometry.
     Handles both inline material definitions and references to predefined materials.
     */
    private static void parseMaterial(JSONObject geometryObj, Geometry geometry, JSONArray materials) {

        Object objCheck = geometryObj.get("material");
        JSONObject materialObj = null;
        if (objCheck instanceof String) {
            int materialIndex = Integer.parseInt((String) objCheck);
            materialObj = (JSONObject) materials.get(materialIndex);
        } else {
            materialObj = (JSONObject) objCheck;
        }

        Material material = new Material();
        if (materialObj.containsKey("ka")) {
            if(materialObj.get("ka") instanceof Number)
                material.setKD(((Number) materialObj.get("ka")).doubleValue());
            else{
                String[] ka = ((String) materialObj.get("ka")).split(" ");
                Double3 kaColor = new Double3(Double.parseDouble(ka[0]), Double.parseDouble(ka[1]), Double.parseDouble(ka[2]));
                material.setKA(kaColor);
            }
        }
        if (materialObj.containsKey("kd")) {
            if(materialObj.get("kd") instanceof Number)
                material.setKD(((Number) materialObj.get("kd")).doubleValue());
            else{
                String[] kd = ((String) materialObj.get("kd")).split(" ");
                Double3 kdColor = new Double3(Double.parseDouble(kd[0]), Double.parseDouble(kd[1]), Double.parseDouble(kd[2]));
                material.setKD(kdColor);
            }
        }
        if (materialObj.containsKey("ks")) {
            if(materialObj.get("ks") instanceof Number)
                material.setKD(((Number) materialObj.get("ks")).doubleValue());
            else{
                String[] ks = ((String) materialObj.get("ks")).split(" ");
                Double3 ksColor = new Double3(Double.parseDouble(ks[0]), Double.parseDouble(ks[1]), Double.parseDouble(ks[2]));
                material.setKD(ksColor);
            }
        }
        if (materialObj.containsKey("ns")) {
            material.setShininess(((Number) materialObj.get("ns")).intValue());
        }
        if(materialObj.containsKey("kr"))
            material.setKR(((Number) materialObj.get("kr")).doubleValue());
        if (materialObj.containsKey("kt")) {
            material.setKT(((Number) materialObj.get("kt")).doubleValue());
        }
        geometry.setMaterial(material);
    }

    /**

     Parses a tube geometry from a JSON object.
     Creates a Tube with the specified radius and axis.
     */
    private static Geometry parseTube(JSONObject tube) {
        double radius = ((Number) tube.get("radius")).doubleValue();
        Ray axis = parseRay((JSONObject) tube.get("axis"));
        return new Tube( axis,radius);
    }

    /**

     Parses a cylinder geometry from a JSON object.
     Creates a Cylinder with the specified radius, height, and axis.
     */
    private static Geometry parseCylinder(JSONObject cylinder) {
        double radius = ((Number) cylinder.get("radius")).doubleValue();
        double height = ((Number) cylinder.get("height")).doubleValue();
        Ray axis = parseRay((JSONObject) cylinder.get("axis"));
        return new Cylinder(axis,radius, height);
    }

    /**

     Parses a ray from a JSON object.
     Creates a Ray with the specified origin point and direction vector.
     */
    private static Ray parseRay(JSONObject axis) {
        Point point = parsePoint((String) axis.get("origin"));
        Vector direction = parseVector((String) axis.get("direction"));
        return new Ray(point, direction);
    }

    /**

     Parses a polygon geometry from a JSON array of vertices.
     Creates a Polygon from the specified points.
     */
    private static Geometry parsePolygon(JSONArray polygon) {
        return new Polygon(parseVertices(polygon));
    }

    /**

     Parses a sphere geometry from a JSON object.
     Creates a Sphere with the specified center point and radius.
     */
    private static Geometry parseSphere(JSONObject sphereObj) {
        Point center = parsePoint((String) sphereObj.get("center"));
        double radius = ((Number) sphereObj.get("radius")).doubleValue();
        return new Sphere( radius, center);
    }

    /**

     Parses a triangle geometry from a JSON array of vertices.
     Creates a Triangle from the specified three points,
     or returns null if the points are invalid.
     */
    private static Geometry parseTriangle(JSONArray triangleObj) {
        Point[] points = parseVertices(triangleObj);
        try{
            Triangle res = new Triangle(points[0], points[1], points[2]);
            return res;
        }catch (IllegalArgumentException e){
            return null; // or handle the exception as needed
        }
    }

    /**

     Parses a plane geometry from a JSON object.
     Creates a Plane from the specified point and normal vector.
     */
    private static Geometry parsePlane(JSONObject planeObj) {
        Point point = parsePoint((String) planeObj.get("point"));
        Vector normal = parseVector((String) planeObj.get("normal"));
        return new Plane(point, normal);
    }

    /**

     Parses an array of vertices from a JSON array.
     Converts each string representation into a Point object.
     */
    private static Point[] parseVertices(JSONArray vertices) {
        Point[] points = new Point[vertices.size()];
        for (int i = 0; i < vertices.size(); i++) {
            points[i] = parsePoint((String) vertices.get(i));
        }
        return points;
    }

    /**

     Parses a string of space-separated coordinates into an array of doubles.
     Used as a helper method for parsing points, vectors, and colors.
     */
    private static double[] parseCoordinates(String coordStr) {
        return Arrays.stream(coordStr.split(" "))
                .mapToDouble(Double::parseDouble)
                .toArray();
    }

    /**

     Parses a color from a string of space-separated RGB values.
     Creates a Color object from the parsed RGB components.
     */
    private static Color parseColor(String rgb) {
        double[] colors = parseCoordinates(rgb);
        return new Color(colors[0], colors[1], colors[2]);
    }

    /**

     Parses a vector from a string of space-separated coordinates.
     Creates a Vector object from the parsed X, Y, Z components.
     */
    private static Vector parseVector(String vector) {
        double[] coords = parseCoordinates(vector);
        return new Vector(coords[0], coords[1], coords[2]);
    }

    /**

     Parses a point from a string of space-separated coordinates.
     Creates a Point object from the parsed X, Y, Z components.
     */
    private static Point parsePoint(String pointStr) {
        double[] coords = parseCoordinates(pointStr);
        return new Point(coords[0], coords[1], coords[2]);
    }
}