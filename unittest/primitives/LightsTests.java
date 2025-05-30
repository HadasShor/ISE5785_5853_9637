package primitives;

import static java.awt.Color.BLUE;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import renderer.Camera;
import renderer.RayTracerType;
import scene.Scene;

import javax.imageio.ImageWriter;

/**
 * Test rendering a basic image with various light sources.
 * @author Dan Zilberstein
 */
class LightsTests {
   /**
    * Default constructor to satisfy JavaDoc generator
    */
   LightsTests() { /* to satisfy JavaDoc generator */ }

   /**
    * First scene for some of tests.
    */
   private final Scene scene1 = new Scene("Test scene");
   /**
    * Second scene for some of tests with a predefined ambient light.
    */
   private final Scene scene2 = new Scene("Test scene")
           .setAmbientLight(new AmbientLight(new Color(38, 38, 38)));

   /**
    * Shininess value for most of the geometries in the tests.
    */
   private static final int SHININESS = 100;

   /**
    * First camera builder for some of tests.
    */
   private final Camera.Builder camera1 = Camera.getBuilder()                                          //
           .setRayTracer(scene1, RayTracerType.SIMPLE)                                                                      //
           .setLocation(new Point(0, 0, 1000))                                                                              //
           .setDirection(Point.ZERO, Vector.AXIS_Y)                                                                         //
           .setVpSize(150, 150).setVpDistance(1000);

   /**
    * Second camera builder for some of tests.
    */
   private final Camera.Builder camera2 = Camera.getBuilder()                                          //
           .setRayTracer(scene2, RayTracerType.SIMPLE)                                                                      //
           .setLocation(new Point(0, 0, 1000))                                                                              //
           .setDirection(Point.ZERO, Vector.AXIS_Y)                                                                         //
           .setVpSize(200, 200).setVpDistance(1000);

   /**
    * Diffusion attenuation factor for some of the geometries in the tests.
    */
   private static final double KD = 0.5;
   /**
    * Diffusion attenuation factor as a {@link Double3} object for some of the geometries in the tests.
    */
   private static final Double3 KD3 = new Double3(0.2, 0.6, 0.4);

   /**
    * Specular attenuation factor for some of the geometries in the tests.
    */
   private static final double KS = 0.5;
   /**
    * Specular attenuation factor as a {@link Double3} object for some of the geometries in the tests.
    */
   private static final Double3 KS3 = new Double3(0.2, 0.4, 0.3);

   /**
    * Material for some of the geometries in the tests.
    */
   private final Material material = new Material().setKD(KD3).setKS(KS3).setShininess(SHININESS);
   /**
    * Light color for tests with triangles.
    */
   private final Color trianglesLightColor = new Color(800, 500, 250);
   /**
    * Light color for tests with sphere.
    */
   private final Color sphereLightColor = new Color(800, 500, 0);
   /**
    * Color of the sphere.
    */
   private final Color sphereColor = new Color(BLUE).reduce(2);

   /**
    * Center of the sphere.
    */
   private final Point sphereCenter = new Point(0, 0, -50);
   /**
    * Radius of the sphere.
    */
   private static final double SPHERE_RADIUS = 50d;

   /**
    * The triangles' vertices for the tests with triangles.
    */
   private final Point[] vertices =
           {
                   // the shared left-bottom:
                   new Point(-110, -110, -150),
                   // the shared right-top:
                   new Point(95, 100, -150),
                   // the right-bottom
                   new Point(110, -110, -150),
                   // the left-top
                   new Point(-75, 78, 100)
           };
   /**
    * Position of the light in tests with sphere.
    */
   private final Point sphereLightPosition = new Point(-50, -50, 25);
   /**
    * Light direction (directional and spot) in tests with sphere.
    */
   private final Vector sphereLightDirection = new Vector(1, 1, -0.5);
   /**
    * Position of the light in tests with triangles.
    */
   private final Point trianglesLightPosition = new Point(30, 10, -100);
   /**
    * Light direction (directional and spot) in tests with triangles.
    */
   private final Vector trianglesLightDirection = new Vector(-2, -2, -2);

   /**
    * The sphere geometry used in appropriate tests.
    */
   private final Geometry sphere = new Sphere(SPHERE_RADIUS, sphereCenter)
           .setEmission(sphereColor).setMaterial(new Material().setKD(KD).setKS(KS).setShininess(SHININESS));
   /**
    * The first triangle geometry used in appropriate tests.
    */
   private final Geometry triangle1 = new Triangle(vertices[0], vertices[1], vertices[2])
           .setMaterial(material);
   /**
    * The second triangle geometry used in appropriate tests.
    */
   private final Geometry triangle2 = new Triangle(vertices[0], vertices[1], vertices[3])
           .setMaterial(material);

   /**
    * Produces a picture of a sphere lighted by a directional light.
    */
   @Test
   void sphereDirectional() {
      scene1.geometries.add(sphere);
      scene1.light.add(new DirectionalLight(sphereLightColor, sphereLightDirection));

      camera1 //
              .setResolution(500, 500) //
              .build() //
              .renderImage() //
              .writeToImage("lightSphereDirectional");
   }

   /**
    * Produces a picture of a sphere lighted by a point light.
    */
   @Test
   void spherePoint() {
      scene1.geometries.add(sphere);
      scene1.light.add(new PointLight(sphereLightColor, sphereLightPosition) //
              .setKl(0.001).setKq(0.0002));

      camera1 //
              .setResolution(500, 500) //
              .build() //
              .renderImage() //
              .writeToImage("lightSpherePoint");
   }

   /**
    * Produces a picture of a sphere lighted by a spotlight.
    */
   @Test
   void sphereSpot() {
      scene1.geometries.add(sphere);
      scene1.light.add(new SpotLight(sphereLightColor, sphereLightPosition, sphereLightDirection) //
              .setKl(0.001).setKq(0.0001));

      camera1 //
              .setResolution(500, 500) //
              .build() //
              .renderImage() //
              .writeToImage("lightSphereSpot");
   }

   /**
    * Produces a picture of two triangles lighted by a directional light.
    */
   @Test
   void trianglesDirectional() {
      scene2.geometries.add(triangle1, triangle2);
      scene2.light.add(new DirectionalLight(trianglesLightColor, trianglesLightDirection));

      camera2.setResolution(500, 500) //
              .build() //
              .renderImage() //
              .writeToImage("lightTrianglesDirectional");
   }

   /**
    * Produces a picture of two triangles lighted by a point light.
    */
   @Test
   void trianglesPoint() {
      scene2.geometries.add(triangle1, triangle2);
      scene2.light.add(new PointLight(trianglesLightColor, trianglesLightPosition) //
              .setKl(0.001).setKq(0.0002));

      camera2.setResolution(500, 500) //
              .build() //
              .renderImage() //
              .writeToImage("lightTrianglesPoint");
   }

   /**
    * Produces a picture of two triangles lighted by a spotlight.
    */
   @Test
   void trianglesSpot() {
      scene2.geometries.add(triangle1, triangle2);
      scene2.light.add(new SpotLight(trianglesLightColor, trianglesLightPosition, trianglesLightDirection) //
              .setKl(0.001).setKq(0.0001));

      camera2.setResolution(500, 500) //
              .build() //
              .renderImage() //
              .writeToImage("lightTrianglesSpot");
   }

   /**
    * Produces a picture of a sphere lighted by a narrow spotlight.
    */
   @Test
   void sphereSpotSharp() {
      scene1.geometries.add(sphere);
      scene1.light
              .add(new SpotLight(sphereLightColor, sphereLightPosition, new Vector(1, 1, -0.5))
                      .setKl(0.001).setKq(0.00004).setNarrowBeam(10.0));

      camera1.setResolution(500, 500) //
              .build() //
              .renderImage() //
              .writeToImage("lightSphereSpotSharp");
   }

   /**
    * Produces a picture of two triangles lighted by a narrow spotlight.
    */
   @Test
   void trianglesSpotSharp() {
      scene2.geometries.add(triangle1, triangle2);
      scene2.light.add(new SpotLight(trianglesLightColor, trianglesLightPosition, trianglesLightDirection)
              .setKl(0.001).setKq(0.00004).setNarrowBeam(10.0));

      camera2.setResolution(500, 500) //
              .build() //
              .renderImage() //
              .writeToImage("lightTrianglesSpotSharp");
   }


   /**
    * Produce a picture of a sphere lighted by multiple light sources.
    */
   @Test
   void sphereMultipleLights() {
      scene1.geometries.add(sphere);

      // Adding multiple light sources
      scene1.light.add(new DirectionalLight(sphereLightColor, sphereLightDirection));
      scene1.light.add(new PointLight(new Color(500, 300, 0), new Point(50, -50, 25))
              .setKl(0.001).setKq(0.0002));
      scene1.light.add(new SpotLight(new Color(200, 500, 300), new Point(-50, -50, 25), new Vector(1, 1, -0.5))
              .setKl(0.001).setKq(0.0001).setNarrowBeam(10.0));

      // Rendering the image
      camera1.setResolution(500, 500) //
              .build() //
              .renderImage() //
              .writeToImage("lightSphereMultiple");
   }

   /**
    * Produce a picture of two triangles lighted by multiple light sources.
    */
   @Test
   void trianglesMultipleLights() {
      scene2.geometries.add(triangle1, triangle2);

      // Adding multiple light sources
      scene2.light.add(new DirectionalLight(trianglesLightColor, trianglesLightDirection));
      scene2.light.add(new PointLight(new Color(500, 300, 0), new Point(20, 30, -100))
              .setKl(0.001).setKq(0.0002));
      scene2.light.add(new SpotLight(new Color(200, 500, 300), new Point(-30, 10, -100), new Vector(-2, -2, -2))
              .setKl(0.001).setKq(0.0001).setNarrowBeam(10.0));

      // Rendering the image
      camera2.setResolution(500, 500) //
              .build() //
              .renderImage() //
              .writeToImage("lightTrianglesMultiple");
   }

   /**
    * Vertices of the triangular base for the {@link #sphereOnTrianglesWithMultipleLights()} test.
    */
   Point[] baseVertices = {
           new Point(-70, -40, -50),
           new Point(70, -40, -50),
           new Point(0, -40, 50)
   };

   /**
    * Produce a picture of a sphere on a triangular base with multiple colored lights.
    */
   @Test
   void sphereOnTrianglesWithMultipleLights() {
      // Create a triangular base for the sphere
      Geometry base = new Triangle(baseVertices[0], baseVertices[1], baseVertices[2])
              .setEmission(new Color(20, 20, 80))
              .setMaterial(new Material().setKD(0.4).setKS(0.3).setShininess(100));

      // Create a sphere above the triangular base
      Geometry sphere = new Sphere(30, new Point(0, 0, 0))
              .setEmission(new Color(80, 20, 20))
              .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(SHININESS));

      // Add the geometries to the scene
      scene2.geometries.add(sphere, base);

      // Add multiple colored lights from different directions
      scene2.light.add(new DirectionalLight(new Color(300, 150, 150), new Vector(0, 1, -1))); // Red-ish from above
      scene2.light.add(new PointLight(new Color(150, 300, 150), new Point(-50, 50, 50)) // Green-ish from left
              .setKl(0.001).setKq(0.0002));
      scene2.light.add(new SpotLight(new Color(150, 150, 300), new Point(50, 50, 50), new Vector(-1, -1, -1)) // Blue-ish from right
              .setKl(0.001).setKq(0.0001).setNarrowBeam(15.0));

      // Render the image
      camera2.setResolution(500, 500)
              .build()
              .renderImage()
              .writeToImage("coolSphereOnTrianglesMultiColorLights");
   }

}