package renderer;

import static java.awt.Color.*;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.AmbientLight;
import primitives.*;
import scene.Scene;

/**
 * Test class for rendering basic 3D images using various scene configurations.
 * Includes tests for rendering with color, ambient light, and file-based scene definitions.
 * Designed for validating rendering pipeline functionality.
 *
 * Author: Dan
 */
public class RenderTests {

   /**
    * Default constructor to satisfy JavaDoc generator.
    */
   public RenderTests() { /* to satisfy JavaDoc generator */ }

   /**
    * Camera builder used to configure the camera parameters for all tests,
    * including position, direction, view plane size, and distance.
    */
   private final Camera.Builder cameraBilder = Camera.getBuilder() //
           .setLocation((Point) Point.ZERO)
           .setDirection(new Point(0, 0, -1), Vector.AXIS_Y) //
           .setVpDistance(100) //
           .setVpSize(500, 500);

   /**
    * Test method that renders a basic 3D scene composed of a sphere and three triangles
    * using two-tone color (green background and ambient light).
    * The rendered image includes a yellow grid.
    */
   @Test
   public void renderTwoColorTest() {
      Scene scene = new Scene("Two color").setBackground(new Color(75, 127, 90))
              .setAmbientLight(new AmbientLight(new Color(255, 191, 191)));
      scene.geometries //
              .add(
                      // center
                      new Sphere(50d, new Point(0, 0, -100)),
                      // up left
                      new Triangle(new Point(-100, 0, -100), new Point(0, 100, -100), new Point(-100, 100, -100)),
                      // down left
                      new Triangle(new Point(-100, 0, -100), new Point(0, -100, -100), new Point(-100, -100, -100)),
                      // down right
                      new Triangle(new Point(100, 0, -100), new Point(0, -100, -100), new Point(100, -100, -100)));

      cameraBilder //
              .setRayTracer(scene, RayTracerType.SIMPLE) //
              .setResolution(1000, 1000) //
              .build() //
              .renderImage() //
              .printGrid(100, new Color(YELLOW)) //
              .writeToImage("Two color render test");
   }

   /**
    * (Commented out) Test method to render a scene with emission colors on different geometries.
    * Used in stage 6 for testing per-geometry lighting effects.
    */
   /*@Test
   public void renderMultiColorTest() {
      Scene scene = new Scene("Multi color").setAmbientLight(new AmbientLight(new Color(51, 51, 51)));
      scene.geometries //
         .add(
              // center
              new Sphere(new Point(0, 0, -100), 50),
              // up left
              new Triangle(new Point(-100, 0, -100), new Point(0, 100, -100), new Point(-100, 100, -100)) //
                 .setEmission(new Color(GREEN)),
              // down left
              new Triangle(new Point(-100, 0, -100), new Point(0, -100, -100), new Point(-100, -100, -100)) //
                 .setEmission(new Color(RED)),
              // down right
              new Triangle(new Point(100, 0, -100), new Point(0, -100, -100), new Point(100, -100, -100)) //
                 .setEmission(new Color(BLUE)));

      camera //
         .setRayTracer(scene, RayTracerType.SIMPLE) //
         .setResolution(1000, 1000) //
         .build() //
         .renderImage() //
         .printGrid(100, new Color(WHITE)) //
         .writeToImage("color render test");
   }*/

   /**
    * Test method that renders a basic scene loaded from XML.
    * Scene content should be populated using XML-parsing logic.
    * Currently contains only a placeholder and renders an empty scene.
    */
   @Test
   public void basicRenderXml() {
      Scene scene = new Scene("Using XML");
      // enter XML file name and parse from XML file into scene object
      // NB: unit tests is not the correct place to put XML parsing code

      cameraBilder //
              .setRayTracer(scene, RayTracerType.SIMPLE) //
              .setResolution(1000, 1000) //
              .build() //
              .renderImage() //
              .printGrid(100, new Color(YELLOW)) //
              .writeToImage("xml render test");
   }

   /**
    * Test method that renders a basic scene loaded from JSON.
    * Scene content should be populated using JSON-parsing logic.
    * Currently contains only a placeholder and renders an empty scene.
    */
   @Test
   public void basicRenderJson() {
      Scene scene = new Scene("Using Json");
      // enter JSON file name and parse from JSON file into scene object
      // NB: unit tests is not the correct place to put JSON parsing code

      cameraBilder //
              .setRayTracer(scene, RayTracerType.SIMPLE) //
              .setResolution(1000, 1000) //
              .build() //
              .renderImage() //
              .printGrid(100, new Color(YELLOW)) //
              .writeToImage("xml render test");
   }
}
