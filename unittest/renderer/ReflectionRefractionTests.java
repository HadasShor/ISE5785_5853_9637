package renderer;

import static java.awt.Color.*;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;

/**
 * Tests for reflection and transparency functionality, test for partial
 * shadows (with transparency).
 * @author Dan Zilberstein
 */
class ReflectionRefractionTests {
   /** Default constructor to satisfy JavaDoc generator */
   ReflectionRefractionTests() { /* to satisfy JavaDoc generator */ }

   /** Scene for the tests */
   private final Scene          scene         = new Scene("Test scene");
   /** Camera builder for the tests */
   private final Camera.Builder cameraBuilder = Camera.getBuilder()     //
           .setRayTracer(scene, RayTracerType.SIMPLE);

   /** Produce a picture of two spheres with reflection and refraction effects. */
   @Test
   void twoSpheres() {
      scene.geometries.add( //
              new Sphere(50d, new Point(0, 0, -50)).setEmission(new Color(BLUE)) //
                      .setMaterial(new Material().setKD(0.4).setKS(0.3).setShininess(100).setKT(0.3)), //
              new Sphere(25d, new Point(0, 0, -50)).setEmission(new Color(RED)) //
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100))); //
      scene.light.add( //
              new SpotLight(new Color(1000, 600, 0), new Point(-100, -100, 500), new Vector(-1, -1, -2)) //
                      .setKl(0.0004).setKq(0.0000006));

      cameraBuilder
              .setLocation(new Point(0, 0, 1000)) //
              .setDirection(Point.ZERO, Vector.AXIS_Y) //
              .setVpDistance(1000).setVpSize(150, 150) //
              .setResolution(500, 500) //
              .build() //
              .renderImage() //
              .writeToImage("refractionTwoSpheres");
   }

   /** Produce a picture of two spheres reflected on mirrored triangles. */
   @Test
   void twoSpheresOnMirrors() {
      scene.geometries.add( //
              new Sphere(400d, new Point(-950, -900, -1000)).setEmission(new Color(0, 50, 100)) //
                      .setMaterial(new Material().setKD(0.25).setKS(0.25).setShininess(20) //
                              .setKT(new Double3(0.5, 0, 0))), //
              new Sphere(200d, new Point(-950, -900, -1000)).setEmission(new Color(100, 50, 20)) //
                      .setMaterial(new Material().setKD(0.25).setKS(0.25).setShininess(20)), //
              new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500), //
                      new Point(670, 670, 3000)) //
                      .setEmission(new Color(20, 20, 20)) //
                      .setMaterial(new Material().setKR(1)), //
              new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500), //
                      new Point(-1500, -1500, -2000)) //
                      .setEmission(new Color(20, 20, 20)) //
                      .setMaterial(new Material().setKR(new Double3(0.5, 0, 0.4))));
      scene.setAmbientLight(new AmbientLight(new Color(26, 26, 26)));
      scene.light.add(new SpotLight(new Color(1020, 400, 400), new Point(-750, -750, -150), new Vector(-1, -1, -4)) //
              .setKl(0.00001).setKq(0.000005));

      cameraBuilder
              .setLocation(new Point(0, 0, 10000)) //
              .setDirection(Point.ZERO, Vector.AXIS_Y) //
              .setVpDistance(10000).setVpSize(2500, 2500) //
              .setResolution(500, 500) //
              .build() //
              .renderImage() //
              .writeToImage("reflectionTwoSpheresMirrored");
   }

   /**
    * Produce a picture of two triangles lighted by a spot light with a partially
    * transparent Sphere producing partial shadow.
    */
   @Test
   void trianglesTransparentSphere() {
      scene.geometries.add(
              new Triangle(new Point(-150, -150, -115), new Point(150, -150, -135),
                      new Point(75, 75, -150))
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(60)),
              new Triangle(new Point(-150, -150, -115), new Point(-70, 70, -140), new Point(75, 75, -150))
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(60)),
              new Sphere(30d, new Point(60, 50, -50)).setEmission(new Color(BLUE))
                      .setMaterial(new Material().setKD(0.2).setKS(0.2).setShininess(30).setKT(0.6)));
      scene.setAmbientLight(new AmbientLight(new Color(38, 38, 38)));
      scene.light.add(
              new SpotLight(new Color(700, 400, 400), new Point(60, 50, 0), new Vector(0, 0, -1))
                      .setKl(4E-5).setKq(2E-7));

      cameraBuilder
              .setLocation(new Point(0, 0, 1000)) //
              .setDirection(Point.ZERO, Vector.AXIS_Y) //
              .setVpDistance(1000).setVpSize(200, 200) //
              .setResolution(600, 600) //
              .build() //
              .renderImage() //
              .writeToImage("refractionShadow");
   }

   /**
    * Produces a picture of a crystal constellation featuring various geometries with complex
    * reflection and refraction properties, illuminated by multiple light sources.
    */
   @Test
   void crystalConstellation() {
      // Adding multiple geometries with different materials
      scene.geometries.add(
              // Central transparent crystal sphere with reflections
              new Sphere(45d, new Point(0, 30, -80))
                      .setEmission(new Color(20, 30, 50))
                      .setMaterial(new Material()
                              .setKD(0.1).setKS(0.8).setShininess(120)
                              .setKT(0.85).setKR(0.15)),

              // Ruby sphere with partial transparency and red color
              new Sphere(25d, new Point(-60, 15, -45))
                      .setEmission(new Color(40, 10, 10))
                      .setMaterial(new Material()
                              .setKD(0.3).setKS(0.7).setShininess(100)
                              .setKT(0.7).setKR(0.3)),

              // Emerald sphere with a green hue
              new Sphere(20d, new Point(70, 40, -30))
                      .setEmission(new Color(10, 35, 15))
                      .setMaterial(new Material()
                              .setKD(0.25).setKS(0.75).setShininess(110)
                              .setKT(0.75).setKR(0.25)),

              // Large mirror triangle for creating reflections
              new Triangle(
                      new Point(-80, -20, -120),
                      new Point(30, -20, -140),
                      new Point(-25, 90, -130))
                      .setMaterial(new Material()
                              .setKD(0.1).setKS(0.9).setShininess(200)
                              .setKR(0.9)),

              // Bronze triangle with metallic properties
              new Triangle(
                      new Point(40, -30, -100),
                      new Point(120, -30, -110),
                      new Point(80, 50, -105))
                      .setEmission(new Color(30, 20, 10))
                      .setMaterial(new Material()
                              .setKD(0.4).setKS(0.6).setShininess(80)
                              .setKR(0.6)),

              // Transparent glass triangle
              new Triangle(
                      new Point(-40, 60, -60),
                      new Point(-10, 60, -70),
                      new Point(-25, 85, -65))
                      .setMaterial(new Material()
                              .setKD(0.05).setKS(0.95).setShininess(150)
                              .setKT(0.8).setKR(0.2)),

              // Stone floor plane with subtle reflections
              new Plane(new Point(0, -50, 0), new Vector(0, 1, 0))
                      .setEmission(new Color(15, 15, 12))
                      .setMaterial(new Material()
                              .setKD(0.6).setKS(0.4).setShininess(25)
                              .setKR(0.15)),

              // Vertical mirror wall plane
              new Plane(new Point(-150, 0, 0), new Vector(1, 0, 0))
                      .setMaterial(new Material()
                              .setKD(0.05).setKS(0.95).setShininess(180)
                              .setKR(0.85))
      );

      // Gentle ambient lighting
      scene.setAmbientLight(new AmbientLight(new Color(8, 8, 12)));

      // Multiple light sources with realistic attenuation
      scene.light.add(
              new SpotLight(
                      new Color(800, 600, 400),
                      new Point(-80, 120, 100),
                      new Vector(1, -2, -3))
                      .setKl(0.0001).setKq(0.00005)
      );

      scene.light.add(
              new PointLight(
                      new Color(400, 500, 700),
                      new Point(100, 60, 50))
                      .setKl(0.00008).setKq(0.00003)
      );

      scene.light.add(
              new DirectionalLight(
                      new Color(50, 60, 80),
                      new Vector(0.3, -0.5, -1))
      );
      // Camera setup for optimal view
      cameraBuilder
              .setLocation(new Point(50, 80, 200))
              .setDirection(new Point(0, 20, -60), Vector.AXIS_Y)
              .setVpDistance(180)
              .setVpSize(220, 220)
              .setResolution(800, 800)
              .build()
              .renderImage()
              .writeToImage("crystalConstellation");
   }
   /**
    * Produces a picture of crystal prisms and spheres with intricate reflection and refraction effects,
    * illuminated by multiple light sources.
    */
   @Test
   void crystalPrismReflections() {
      // Set up a dark background with subtle ambient lighting
      scene.setBackground(new Color(5, 5, 15));
      scene.setAmbientLight(new AmbientLight(new Color(20, 15, 25)));

      // Create a visually stunning scene with multiple geometries
      scene.geometries.add(
              // Large triangular prism - pink/purple with light emission and transparency
              new Triangle(new Point(-150, -120, -150), new Point(150, -120, -150), new Point(0, 120, -50))
                      .setEmission(new Color(180, 80, 190))
                      .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(100).setKT(0.4).setKR(0.1)),

              // Second triangle creating the prism effect
              new Triangle(new Point(-150, -120, -250), new Point(150, -120, -250), new Point(0, 120, -150))
                      .setEmission(new Color(120, 40, 160))
                      .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(100).setKT(0.5).setKR(0.1)),

              // Floor triangle with reflective properties
              new Triangle(new Point(-200, -120, -300), new Point(200, -120, -50), new Point(-200, -120, -50))
                      .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(80).setKR(0.3)),

              // Clear transparent sphere with high refraction
              new Sphere(40d, new Point(-60, -40, -100))
                      .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(100).setKT(0.85).setKR(0.15)),

              // Green sphere with partial transparency
              new Sphere(35d, new Point(70, -40, -80))
                      .setEmission(new Color(GREEN))
                      .setMaterial(new Material().setKD(0.2).setKS(0.8).setShininess(80).setKT(0.5).setKR(0.2)),

              // Small glowing sphere as a light source representation
              new Sphere(15d, new Point(0, 80, -90))
                      .setEmission(new Color(WHITE).scale(2))
                      .setMaterial(new Material().setKD(0.1).setKS(0.9).setShininess(300).setKR(0.1)),

              // Background plane with slight reflection
              new Plane(new Point(0, 0, -300), new Vector(0, 0, 1))
                      .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(40).setKR(0.1)));

      // Add multiple light sources with realistic attenuation for dramatic lighting
      // Main light source - warm light
      scene.light.add(
              new PointLight(new Color(800, 500, 500), new Point(-50, 100, 50))
                      .setKl(0.0001).setKq(0.00005)
      );

      // Secondary light source - cool light for contrast
      scene.light.add(
              new SpotLight(new Color(300, 300, 500), new Point(100, 50, 100), new Vector(-1, -0.5, -1))
                      .setKl(0.0001).setKq(0.00007)
      );

      // Directional light to highlight prism effect
      scene.light.add(
              new DirectionalLight(new Color(100, 60, 120), new Vector(0.5, -0.5, -0.5))
      );

      // Position camera to capture all optical effects
      cameraBuilder
              .setLocation(new Point(0, 0, 1000))
              .setDirection(new Point(0, 0, -100), new Vector(0, 1, 0))
              .setVpDistance(1000).setVpSize(200, 200)
              .setResolution(800, 800)
              .build()
              .renderImage()
              .writeToImage("crystalPrismReflections");
   }

}