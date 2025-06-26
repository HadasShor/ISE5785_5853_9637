package renderer;

import static java.awt.Color.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;

import java.util.Random;

/**
 * Tests for reflection and transparency functionality, test for partial
 * shadows
 * (with transparency)
 * @author Dan Zilberstein
 */
class ReflectionRefractionTests {
   /**
    * Default constructor to satisfy JavaDoc generator
    */
   ReflectionRefractionTests() { /* to satisfy JavaDoc generator */ }

   /**
    * Scene for the tests
    */
   private final Scene scene = new Scene("Test scene");
   /**
    * Camera builder for the tests with triangles
    */
   private final Camera.Builder cameraBuilder = Camera.getBuilder()     //
           .setRayTracer(scene, RayTracerType.SIMPLE);

   /**
    * Tests the rendering of two spheres illuminated by a spotlight.
    * The outer sphere is blue and partially transparent, while the inner sphere is red and opaque.
    * This test demonstrates refraction of light through a transparent object.
    */
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

   /**
    * Tests the rendering of two spheres positioned on reflective surfaces.
    * Demonstrates multiple reflections and refraction effects.
    * The outer sphere has selective transparency (only in the red channel).
    */
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
    * Tests the rendering of two triangles illuminated by a spotlight,
    * with a partially transparent sphere creating partial shadow.
    * Demonstrates partial shadow effects due to transparency (partial refraction).
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
    * Creates a complex scene with a constellation of crystals.
    * Includes mirror pyramids, transparent and semi-transparent crystal spheres,
    * mirror surfaces, and multiple reflection and refraction effects.
    * Demonstrates the use of multiple light sources and various materials in a single scene.
    */
   @Test
   void crystalConstellation() {
      // הוספת גאומטריות מרובות עם חומרים שונים

      // פירמידה משופרת ראשונה - נקודות ברורות
      Point basePoint1 = new Point(-50, -20, -150); // קדמי שמאלי
      Point basePoint2 = new Point(50, -20, -150);  // קדמי ימני
      Point basePoint3 = new Point(50, -20, -50);   // אחורי ימני
      Point basePoint4 = new Point(-50, -20, -50);  // אחורי שמאלי
      Point apexPoint = new Point(0, 70, -100);     // קודקוד הפירמידה

      // חומר מראה לפירמידה הראשונה
      Material mirrorMaterial = new Material()
              .setKD(0.1).setKS(0.9).setShininess(200)
              .setKR(0.9);

      // פירמידה שנייה (ברונזה) - נקודות ברורות
      Point bronzeBase1 = new Point(40, -30, -120);  // קדמי שמאלי
      Point bronzeBase2 = new Point(120, -30, -130); // קדמי ימני
      Point bronzeBase3 = new Point(110, -30, -70);  // אחורי ימני
      Point bronzeBase4 = new Point(30, -30, -60);   // אחורי שמאלי
      Point bronzeApex = new Point(75, 50, -95);     // קודקוד הפירמידה

      // חומר ברונזה לפירמידה השנייה
      Material bronzeMaterial = new Material()
              .setKD(0.4).setKS(0.6).setShininess(80)
              .setKR(0.6);

      scene.geometries.add(
              // כדור קריסטל מרכזי שקוף עם השתקפויות
              new Sphere(45d, new Point(0, 30, -80))
                      .setEmission(new Color(20, 30, 50))
                      .setMaterial(new Material()
                              .setKD(0.1).setKS(0.8).setShininess(120)
                              .setKT(0.85).setKR(0.15)),

              // כדור רובי עם שקיפות חלקית וצבע אדום
              new Sphere(25d, new Point(-60, 15, -45))
                      .setEmission(new Color(40, 10, 10))
                      .setMaterial(new Material()
                              .setKD(0.3).setKS(0.7).setShininess(100)
                              .setKT(0.7).setKR(0.3)),

              // כדור אמרלד עם גוון ירוק
              new Sphere(20d, new Point(70, 40, -30))
                      .setEmission(new Color(10, 35, 15))
                      .setMaterial(new Material()
                              .setKD(0.25).setKS(0.75).setShininess(110)
                              .setKT(0.75).setKR(0.25)),

              // פירמידת מראה ראשונה
              // פאה קדמית
              new Triangle(basePoint1, basePoint2, apexPoint)
                      .setEmission(new Color(20, 20, 80)) // צבע כחול-אפור
                      .setMaterial(mirrorMaterial),

              // פאה ימנית
              new Triangle(basePoint2, basePoint3, apexPoint)
                      .setEmission(new Color(20, 80, 20)) // צבע ירוק-אפור
                      .setMaterial(mirrorMaterial),

              // פאה אחורית
              new Triangle(basePoint3, basePoint4, apexPoint)
                      .setEmission(new Color(80, 20, 20)) // צבע אדום-אפור
                      .setMaterial(mirrorMaterial),

              // פאה שמאלית
              new Triangle(basePoint4, basePoint1, apexPoint)
                      .setEmission(new Color(80, 80, 20)) // צבע צהוב-אפור
                      .setMaterial(mirrorMaterial),

              // בסיס (מורכב משני משולשים)
              new Triangle(basePoint1, basePoint2, basePoint3)
                      .setEmission(new Color(40, 40, 40)) // צבע אפור כהה
                      .setMaterial(mirrorMaterial),

              new Triangle(basePoint1, basePoint3, basePoint4)
                      .setEmission(new Color(40, 40, 40)) // צבע אפור כהה
                      .setMaterial(mirrorMaterial),

              // פירמידת ברונזה (במקום המשולש הברונזה)
              // פאה קדמית
              new Triangle(bronzeBase1, bronzeBase2, bronzeApex)
                      .setEmission(new Color(35, 22, 10)) // ברונזה כהה
                      .setMaterial(bronzeMaterial),

              // פאה ימנית
              new Triangle(bronzeBase2, bronzeBase3, bronzeApex)
                      .setEmission(new Color(30, 20, 10)) // ברונזה רגילה
                      .setMaterial(bronzeMaterial),

              // פאה אחורית
              new Triangle(bronzeBase3, bronzeBase4, bronzeApex)
                      .setEmission(new Color(38, 25, 13)) // ברונזה בהירה
                      .setMaterial(bronzeMaterial),

              // פאה שמאלית
              new Triangle(bronzeBase4, bronzeBase1, bronzeApex)
                      .setEmission(new Color(28, 18, 8)) // ברונזה כהה
                      .setMaterial(bronzeMaterial),

              // בסיס (מורכב משני משולשים)
              new Triangle(bronzeBase1, bronzeBase2, bronzeBase3)
                      .setEmission(new Color(25, 15, 5)) // ברונזה מאוד כהה
                      .setMaterial(bronzeMaterial),

              new Triangle(bronzeBase1, bronzeBase3, bronzeBase4)
                      .setEmission(new Color(25, 15, 5)) // ברונזה מאוד כהה
                      .setMaterial(bronzeMaterial),

              // משולש זכוכית שקוף
              new Triangle(
                      new Point(-40, 60, -60),
                      new Point(-10, 60, -70),
                      new Point(-25, 85, -65))
                      .setMaterial(new Material()
                              .setKD(0.05).setKS(0.95).setShininess(150)
                              .setKT(0.8).setKR(0.2)),

              // מישור רצפה אבן עם השתקפויות קלות
              new Plane(new Point(0, -50, 0), new Vector(0, 1, 0))
                      .setEmission(new Color(15, 15, 12))
                      .setMaterial(new Material()
                              .setKD(0.6).setKS(0.4).setShininess(25)
                              .setKR(0.15)),

              // מישור קיר מראה אנכי
              new Plane(new Point(-150, 0, 0), new Vector(1, 0, 0))
                      .setMaterial(new Material()
                              .setKD(0.05).setKS(0.95).setShininess(180)
                              .setKR(0.85))
      );

      // תאורת סביבה עדינה
      scene.setAmbientLight(new AmbientLight(new Color(8, 8, 12)));

      // מקורות אור מרובים עם הנחתה ריאליסטית
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
      // הגדרת מצלמה לתצוגה אופטימלית
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
    * Creates a scene with crystalline prism surfaces featuring colorful reflections.
    * Includes transparent triangles, transparent and semi-transparent spheres, and a reflective floor.
    * Demonstrates light dispersion and reflection effects in transparent and colored surfaces.
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
      // מקור אור ראשי - אור חם
      scene.light.add(
              new PointLight(new Color(800, 500, 500), new Point(-50, 100, 50))
                      .setKl(0.0001).setKq(0.00005)
      );

// מקור אור משני - אור קר לניגודיות
      scene.light.add(
              new SpotLight(new Color(300, 300, 500), new Point(100, 50, 100), new Vector(-1, -0.5, -1))
                      .setKl(0.0001).setKq(0.00007)
      );

// אור כיווני להדגשת אפקט פריזמה
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

   /**
    * Creates a scene with a metallic green pyramid and crystal spheres.
    * A simpler version of the etherealGallery with fewer objects.
    * Demonstrates reflection and refraction effects from metallic and crystalline surfaces.
    */
   @Test
   void etherealGallery_piramudandsphere() {
      // === הגדרת הבסיס של הפירמידה ===
      Point pyramidBase1 = new Point(-40, -10, -180);
      Point pyramidBase2 = new Point(40, -10, -180);
      Point pyramidBase3 = new Point(40, -10, -120);
      Point pyramidBase4 = new Point(-40, -10, -120);
      Point pyramidApex = new Point(0, 50, -150);

      // חומר מטאלי ירוק עם יותר גוון ירוק
      Material metallicGreenMaterial = new Material()
              .setKD(0.4).setKS(0.9).setShininess(180)
              .setKT(0.15).setKR(0.6);

      // צבע ירוק יותר עשיר לפירמידה
      // Color brightGreen = new Color(0.5, 5, 1.0);
      Color brightGreen = new Color(0, 153, 51);

      // פאות הפירמידה - כולן באותו צבע ירוק מטאלי בהיר
      scene.geometries.add(
              new Triangle(pyramidBase1, pyramidBase2, pyramidApex)
                      .setEmission(brightGreen)
                      .setMaterial(metallicGreenMaterial)
      );

      scene.geometries.add(
              new Triangle(pyramidBase2, pyramidBase3, pyramidApex)
                      .setEmission(brightGreen)
                      .setMaterial(metallicGreenMaterial)
      );

      scene.geometries.add(
              new Triangle(pyramidBase3, pyramidBase4, pyramidApex)
                      .setEmission(brightGreen)
                      .setMaterial(metallicGreenMaterial)
      );

      scene.geometries.add(
              new Triangle(pyramidBase4, pyramidBase1, pyramidApex)
                      .setEmission(brightGreen)
                      .setMaterial(metallicGreenMaterial)
      );

      // === הוספת יהלומים (כדורים מבריקים) ===

      // חומר יהלום - מבריק מאוד עם שקיפות והשתקפות גבוהות
      Material diamondMaterial = new Material()
              .setKD(0.05).setKS(0.95).setShininess(300)
              .setKT(0.6).setKR(0.8);

      // צבע יהלום מבריק
      Color diamondColor = new Color(8, 8, 8);

      // שני כדורים קטנים מימין לפירמידה
      scene.geometries.add(
              new Sphere(10d, new Point(60, 10, -140))
                      .setEmission(diamondColor)
                      .setMaterial(diamondMaterial)
      );

      scene.geometries.add(
              new Sphere(8d, new Point(85, 5, -160))
                      .setEmission(diamondColor)
                      .setMaterial(diamondMaterial)
      );

      // כדור אחד משמאל לפירמידה - ממוקם יותר מקדימה
      scene.geometries.add(
              new Sphere(30d, new Point(-65, 15, -75)) // שיניתי את ערך ה-Z מ -150 ל -50 להזזה קדימה
                      .setEmission(diamondColor)
                      .setMaterial(diamondMaterial)
      );

      // === מישורים - עוד פחות מראתיים ורקע בהיר יותר ===

      // רצפה עם מינימום השתקפות ובהירה יותר
      scene.geometries.add(
              new Plane(new Point(0, -25, 0), new Vector(0, 1, 0))
                      .setEmission(new Color(7, 6, 5)) // בהיר יותר
                      .setMaterial(new Material()
                              .setKD(0.95).setKS(0.15).setShininess(20)
                              .setKR(0.08)) // עוד פחות השתקפות
      );

      // קיר אחורי מאט בהיר יותר
      scene.geometries.add(
              new Plane(new Point(0, 0, -250), new Vector(0, 0, 1))
                      .setEmission(new Color(4, 3, 5)) // בהיר יותר
                      .setMaterial(new Material()
                              .setKD(0.98).setKS(0.02).setShininess(5)
                              .setKR(0.02)) // כמעט ללא השתקפות
      );

      // קיר צדדי בהיר יותר וללא השתקפות כמעט
      scene.geometries.add(
              new Plane(new Point(-120, 0, 0), new Vector(1, 0, 0))
                      .setEmission(new Color(5, 6, 7)) // בהיר יותר
                      .setMaterial(new Material()
                              .setKD(0.9).setKS(0.2).setShininess(30)
                              .setKR(0.1)) // מינימום השתקפות
      );

      // === תאורה ===

      // תאורת סביבה עדינה
      scene.setAmbientLight(new AmbientLight(new Color(0.6, 0.6, 1.1))); // מעט בהיר יותר

      // אור ספוט ראשי
      scene.light.add(
              new SpotLight(
                      new Color(250, 200, 150),
                      new Point(-100, 150, 50),
                      new Vector(2, -3, -4))
                      .setKl(0.0003).setKq(0.00003)
      );

      // אור נקודתי משני
      scene.light.add(
              new PointLight(
                      new Color(100, 150, 200),
                      new Point(120, 80, -40))
                      .setKl(0.0003).setKq(0.00002)
      );

      // אור כיווני רך
      scene.light.add(
              new DirectionalLight(
                      new Color(15, 20, 25),
                      new Vector(-0.2, -0.6, -1))
      );

      // === הגדרת המצלמה ===
      cameraBuilder
              .setLocation(new Point(80, 60, 150))
              .setDirection(new Point(-10, 25, -100), Vector.AXIS_Y)
              .setVpDistance(200)
              .setVpSize(240, 240)
              .setResolution(800, 800)
              .build()
              .renderImage()
              .writeToImage("etherealGallery_piramudandsphere");
   }


   /**
    * Creates an ethereal gallery with a pyramid and complex arrays of connected spheres.
    * Features crystal spheres connected with shiny black connectors in a complex spatial arrangement.
    * Demonstrates advanced scene building techniques with connected objects and material combinations.
    */
   @Test
   void etherealGallery() {
      // === הגדרת הבסיס של הפירמידה ===
      Point pyramidBase1 = new Point(-40, -10, -180);
      Point pyramidBase2 = new Point(40, -10, -180);
      Point pyramidBase3 = new Point(40, -10, -120);
      Point pyramidBase4 = new Point(-40, -10, -120);
      Point pyramidApex = new Point(0, 50, -150);

      // חומר מטאלי ירוק עם יותר גוון ירוק
      Material metallicGreenMaterial = new Material()
              .setKD(0.4).setKS(0.9).setShininess(180)
              .setKT(0.15).setKR(0.6);

      // צבע ירוק יותר עשיר לפירמידה
      Color brightGreen = new Color(0.5, 5, 1.0);

      // === חומרים לשימוש כללי ===
      // חומר יהלום - מבריק מאוד עם שקיפות והשתקפות גבוהות
      Material diamondMaterial = new Material()
              .setKD(0.05).setKS(0.95).setShininess(300)
              .setKT(0.6).setKR(0.8);

      // צבע יהלום מבריק
      Color diamondColor = new Color(8, 8, 8);

      // חומר שחור מבריק למחברים
      Material shinyBlackMaterial = new Material()
              .setKD(0.1).setKS(0.9).setShininess(200)
              .setKT(0.0).setKR(0.7);

      Color blackColor = new Color(0.1, 0.1, 0.1);

      // === פיזור אחיד של זוגות עיגולים עם מחברים ===

      // זוג 1 - ימין למעלה
      addConnectedSpheres(
              scene,
              new Point(60, 30, -140), new Point(80, 45, -160),
              10, 8,
              diamondMaterial, diamondColor,
              shinyBlackMaterial, blackColor
      );

      // זוג 2 - מרכז למעלה
      addConnectedSpheres(
              scene,
              new Point(-5, 60, -150), new Point(20, 75, -135),
              9, 7,
              diamondMaterial, diamondColor,
              shinyBlackMaterial, blackColor
      );

      // זוג 3 - שמאל למעלה
      addConnectedSpheres(
              scene,
              new Point(-80, 40, -140), new Point(-100, 60, -160),
              8, 9,
              diamondMaterial, diamondColor,
              shinyBlackMaterial, blackColor
      );

      // זוג 4 - ימין אמצע
      addConnectedSpheres(
              scene,
              new Point(90, 0, -170), new Point(110, 15, -190),
              12, 10,
              diamondMaterial, diamondColor,
              shinyBlackMaterial, blackColor
      );

      // זוג 5 - מרכז אמצע (קדמי יותר)
      addConnectedSpheres(
              scene,
              new Point(20, 20, -80), new Point(40, 5, -60),
              7, 5,
              diamondMaterial, diamondColor,
              shinyBlackMaterial, blackColor
      );

      // זוג 6 - שמאל אמצע
      addConnectedSpheres(
              scene,
              new Point(-70, 10, -130), new Point(-90, 25, -110),
              8, 6,
              diamondMaterial, diamondColor,
              shinyBlackMaterial, blackColor
      );

      // זוג 7 - ימין למטה
      addConnectedSpheres(
              scene,
              new Point(70, -15, -160), new Point(90, -10, -180),
              7, 8,
              diamondMaterial, diamondColor,
              shinyBlackMaterial, blackColor
      );

      // זוג 8 - מרכז למטה
      addConnectedSpheres(
              scene,
              new Point(0, -15, -100), new Point(-20, -5, -120),
              9, 7,
              diamondMaterial, diamondColor,
              shinyBlackMaterial, blackColor
      );

      // זוג 9 - שמאל למטה (קדמי)
      addConnectedSpheres(
              scene,
              new Point(-60, -10, -80), new Point(-80, 5, -60),
              11, 9,
              diamondMaterial, diamondColor,
              shinyBlackMaterial, blackColor
      );

      // === מישורים - עוד פחות מראתיים ורקע בהיר יותר ===

      // רצפה עם מינימום השתקפות ובהירה יותר
      scene.geometries.add(
              new Plane(new Point(0, -25, 0), new Vector(0, 1, 0))
                      .setEmission(new Color(7, 6, 5)) // בהיר יותר
                      .setMaterial(new Material()
                              .setKD(0.95).setKS(0.15).setShininess(20)
                              .setKR(0.08)) // עוד פחות השתקפות
      );

      // קיר אחורי מאט בהיר יותר
      scene.geometries.add(
              new Plane(new Point(0, 0, -250), new Vector(0, 0, 1))
                      .setEmission(new Color(4, 3, 5)) // בהיר יותר
                      .setMaterial(new Material()
                              .setKD(0.98).setKS(0.02).setShininess(5)
                              .setKR(0.02)) // כמעט ללא השתקפות
      );

      // קיר צדדי בהיר יותר וללא השתקפות כמעט
      scene.geometries.add(
              new Plane(new Point(-120, 0, 0), new Vector(1, 0, 0))
                      .setEmission(new Color(5, 6, 7)) // בהיר יותר
                      .setMaterial(new Material()
                              .setKD(0.9).setKS(0.2).setShininess(30)
                              .setKR(0.1)) // מינימום השתקפות
      );

      // === תאורה ===

      // תאורת סביבה עדינה
      scene.setAmbientLight(new AmbientLight(new Color(0.6, 0.6, 1.1))); // מעט בהיר יותר

      // אור ספוט ראשי
      scene.light.add(
              new SpotLight(
                      new Color(250, 200, 150),
                      new Point(-100, 150, 50),
                      new Vector(2, -3, -4))
                      .setKl(0.0003).setKq(0.00003)
      );

      // אור נקודתי משני
      scene.light.add(
              new PointLight(
                      new Color(100, 150, 200),
                      new Point(120, 80, -40))
                      .setKl(0.0003).setKq(0.00002)
      );

      // אור כיווני רך
      scene.light.add(
              new DirectionalLight(
                      new Color(15, 20, 25),
                      new Vector(-0.2, -0.6, -1))
      );

      // === הגדרת המצלמה ===
      cameraBuilder
              .setLocation(new Point(80, 60, 150))
              .setDirection(new Point(-10, 25, -100), Vector.AXIS_Y)
              .setVpDistance(200)
              .setVpSize(240, 240)
              .setResolution(800, 800)
              .build()
              .renderImage()
              .writeToImage("etherealGallery");
   }

   /**
    * Creates a version of the ethereal gallery with emphasis on the spheres.
    * Includes a metallic green pyramid, crystal spheres, and a cylinder.
    * Demonstrates the combination of different geometries in a single scene.
    */
   private void addConnectedSpheres(
           Scene scene,
           Point sphere1Center, Point sphere2Center,
           double sphere1Radius, double sphere2Radius,
           Material sphereMaterial, Color sphereColor,
           Material connectorMaterial, Color connectorColor
   ) {
      // הוספת שני הכדורים
      scene.geometries.add(
              new Sphere(sphere1Radius, sphere1Center)
                      .setEmission(sphereColor)
                      .setMaterial(sphereMaterial)
      );

      scene.geometries.add(
              new Sphere(sphere2Radius, sphere2Center)
                      .setEmission(sphereColor)
                      .setMaterial(sphereMaterial)
      );

      // חישוב נקודות לקופסה/מחבר שבין הכדורים
      // וקטור כיוון בין מרכזי הכדורים
      Vector direction = sphere2Center.subtract(sphere1Center);

      // וקטורים מאונכים לכיוון
      Vector up = new Vector(0, 1, 0);
      Vector side;

      // אם הכיוון כמעט מקביל לווקטור למעלה, נבחר וקטור צד אחר
      if (Math.abs(direction.dotProduct(up)) > 0.9 * direction.length() * up.length()) {
         side = new Vector(1, 0, 0);
      } else {
         // אחרת, ניצור וקטור צד מאונך לכיוון ולמעלה
         side = direction.crossProduct(up).normalize();
      }

      // וקטור למעלה חדש, מאונך לכיוון ולצד
      up = side.crossProduct(direction).normalize();

      // גודל המחבר - מותאם לגודל הכדורים
      double avgRadius = (sphere1Radius + sphere2Radius) / 2;
      double width = avgRadius * 0.35; // רוחב המחבר ביחס לרדיוס הממוצע
      double height = avgRadius * 0.35; // גובה המחבר

      // יצירת נקודות המחבר
      Vector widthVector = side.scale(width / 2);
      Vector heightVector = up.scale(height / 2);

      // עומק החדירה לכל כדור - מותאם לגודלו
      double penetration1 = sphere1Radius * 0.7;
      double penetration2 = sphere2Radius * 0.7;

      // נקודות בכדור הראשון
      Point p1Near = sphere1Center.add(direction.normalize().scale(penetration1));
      Point p1Bottom = p1Near.add(widthVector.scale(-1)).add(heightVector.scale(-1));
      Point p1Top = p1Near.add(widthVector.scale(-1)).add(heightVector);
      Point p1Right = p1Near.add(widthVector).add(heightVector.scale(-1));
      Point p1TopRight = p1Near.add(widthVector).add(heightVector);

      // נקודות בכדור השני
      Point p2Near = sphere2Center.add(direction.normalize().scale(-penetration2));
      Point p2Bottom = p2Near.add(widthVector.scale(-1)).add(heightVector.scale(-1));
      Point p2Top = p2Near.add(widthVector.scale(-1)).add(heightVector);
      Point p2Right = p2Near.add(widthVector).add(heightVector.scale(-1));
      Point p2TopRight = p2Near.add(widthVector).add(heightVector);

      // פאות המחבר (6 פאות מורכבות מ-12 משולשים)

      // פאה תחתונה
      scene.geometries.add(new Triangle(p1Bottom, p2Bottom, p1Right)
              .setEmission(connectorColor).setMaterial(connectorMaterial));
      scene.geometries.add(new Triangle(p1Right, p2Bottom, p2Right)
              .setEmission(connectorColor).setMaterial(connectorMaterial));

      // פאה עליונה
      scene.geometries.add(new Triangle(p1Top, p1TopRight, p2Top)
              .setEmission(connectorColor).setMaterial(connectorMaterial));
      scene.geometries.add(new Triangle(p1TopRight, p2TopRight, p2Top)
              .setEmission(connectorColor).setMaterial(connectorMaterial));

      // פאה שמאלית
      scene.geometries.add(new Triangle(p1Bottom, p1Top, p2Bottom)
              .setEmission(connectorColor).setMaterial(connectorMaterial));
      scene.geometries.add(new Triangle(p1Top, p2Top, p2Bottom)
              .setEmission(connectorColor).setMaterial(connectorMaterial));

      // פאה ימנית
      scene.geometries.add(new Triangle(p1Right, p2Right, p1TopRight)
              .setEmission(connectorColor).setMaterial(connectorMaterial));
      scene.geometries.add(new Triangle(p1TopRight, p2Right, p2TopRight)
              .setEmission(connectorColor).setMaterial(connectorMaterial));

      // פאה קדמית (בכדור הראשון)
      scene.geometries.add(new Triangle(p1Bottom, p1Right, p1Top)
              .setEmission(connectorColor).setMaterial(connectorMaterial));
      scene.geometries.add(new Triangle(p1Right, p1TopRight, p1Top)
              .setEmission(connectorColor).setMaterial(connectorMaterial));

      // פאה אחורית (בכדור השני)
      scene.geometries.add(new Triangle(p2Bottom, p2Top, p2Right)
              .setEmission(connectorColor).setMaterial(connectorMaterial));
      scene.geometries.add(new Triangle(p2Top, p2TopRight, p2Right)
              .setEmission(connectorColor).setMaterial(connectorMaterial));
   }


   /**
    * Creates a version of the ethereal gallery with emphasis on the spheres.
    * Includes a metallic green pyramid, crystal spheres, and a cylinder.
    * Demonstrates the combination of different geometries in a single scene.
    */
   @Test
   void etherealGallery_sphere() {
      // === הגדרת הבסיס של הפירמידה ===
      Point pyramidBase1 = new Point(-40, -10, -180);
      Point pyramidBase2 = new Point(40, -10, -180);
      Point pyramidBase3 = new Point(40, -10, -120);
      Point pyramidBase4 = new Point(-40, -10, -120);
      Point pyramidApex = new Point(0, 50, -150);

      // חומר מטאלי ירוק עם יותר גוון ירוק
      Material metallicGreenMaterial = new Material()
              .setKD(0.4).setKS(0.9).setShininess(180)
              .setKT(0.15).setKR(0.6);

      // צבע ירוק יותר עשיר לפירמידה
      Color brightGreen = new Color(0.5, 5, 1.0);

      // פאות הפירמידה - כולן באותו צבע ירוק מטאלי בהיר
      scene.geometries.add(
              new Triangle(pyramidBase1, pyramidBase2, pyramidApex)
                      .setEmission(brightGreen)
                      .setMaterial(metallicGreenMaterial)
      );

      scene.geometries.add(
              new Triangle(pyramidBase2, pyramidBase3, pyramidApex)
                      .setEmission(brightGreen)
                      .setMaterial(metallicGreenMaterial)
      );

      scene.geometries.add(
              new Triangle(pyramidBase3, pyramidBase4, pyramidApex)
                      .setEmission(brightGreen)
                      .setMaterial(metallicGreenMaterial)
      );

      scene.geometries.add(
              new Triangle(pyramidBase4, pyramidBase1, pyramidApex)
                      .setEmission(brightGreen)
                      .setMaterial(metallicGreenMaterial)
      );

      Point p = new Point(0, 0, 0);
      Vector v = new Vector(0, 0, 1);
      scene.geometries.add(new Cylinder(new Ray(p, v), 2, 5));
      // === הוספת יהלומים (כדורים מבריקים) ===

      // חומר יהלום - מבריק מאוד עם שקיפות והשתקפות גבוהות
      Material diamondMaterial = new Material()
              .setKD(0.05).setKS(0.95).setShininess(300)
              .setKT(0.6).setKR(0.8);

      // צבע יהלום מבריק
      Color diamondColor = new Color(8, 8, 8);

      // נקודות הכדורים הקטנים מימין לפירמידה
      Point sphere1Center = new Point(60, 10, -140);
      Point sphere2Center = new Point(85, 5, -160);

      // שני כדורים קטנים מימין לפירמידה
      scene.geometries.add(
              new Sphere(10d, sphere1Center)
                      .setEmission(diamondColor)
                      .setMaterial(diamondMaterial)
      );

      scene.geometries.add(
              new Sphere(8d, sphere2Center)
                      .setEmission(diamondColor)
                      .setMaterial(diamondMaterial)
      );

      // === קופסה שחורה מבריקה במקום גליל ===

      // חומר שחור מבריק לקופסה
      Material shinyBlackMaterial = new Material()
              .setKD(0.1).setKS(0.9).setShininess(200)
              .setKT(0.0).setKR(0.7);

      Color blackColor = new Color(0.1, 0.1, 0.1);


      // כדור אחד משמאל לפירמידה - ממוקם יותר מקדימה
      scene.geometries.add(
              new Sphere(30d, new Point(-65, 15, -75))
                      .setEmission(diamondColor)
                      .setMaterial(diamondMaterial)
      );

      // === מישורים - עוד פחות מראתיים ורקע בהיר יותר ===

      // רצפה עם מינימום השתקפות ובהירה יותר
      scene.geometries.add(
              new Plane(new Point(0, -25, 0), new Vector(0, 1, 0))
                      .setEmission(new Color(7, 6, 5)) // בהיר יותר
                      .setMaterial(new Material()
                              .setKD(0.95).setKS(0.15).setShininess(20)
                              .setKR(0.08)) // עוד פחות השתקפות
      );

      // קיר אחורי מאט בהיר יותר
      scene.geometries.add(
              new Plane(new Point(0, 0, -250), new Vector(0, 0, 1))
                      .setEmission(new Color(4, 3, 5)) // בהיר יותר
                      .setMaterial(new Material()
                              .setKD(0.98).setKS(0.02).setShininess(5)
                              .setKR(0.02)) // כמעט ללא השתקפות
      );

      // קיר צדדי בהיר יותר וללא השתקפות כמעט
      scene.geometries.add(
              new Plane(new Point(-120, 0, 0), new Vector(1, 0, 0))
                      .setEmission(new Color(5, 6, 7)) // בהיר יותר
                      .setMaterial(new Material()
                              .setKD(0.9).setKS(0.2).setShininess(30)
                              .setKR(0.1)) // מינימום השתקפות
      );

      // === תאורה ===

      // תאורת סביבה עדינה
      scene.setAmbientLight(new AmbientLight(new Color(0.6, 0.6, 1.1))); // מעט בהיר יותר

      // אור ספוט ראשי
      scene.light.add(
              new SpotLight(
                      new Color(250, 200, 150),
                      new Point(-100, 150, 50),
                      new Vector(2, -3, -4))
                      .setKl(0.0003).setKq(0.00003)
      );

      // אור נקודתי משני
      scene.light.add(
              new PointLight(
                      new Color(100, 150, 200),
                      new Point(120, 80, -40))
                      .setKl(0.0003).setKq(0.00002)
      );

      // אור כיווני רך
      scene.light.add(
              new DirectionalLight(
                      new Color(15, 20, 25),
                      new Vector(-0.2, -0.6, -1))
      );

      // === הגדרת המצלמה ===
      cameraBuilder
              .setLocation(new Point(80, 60, 150))
              .setDirection(new Point(-10, 25, -100), Vector.AXIS_Y)
              .setVpDistance(200)
              .setVpSize(240, 240)
              .setResolution(800, 800)
              .build()
              .renderImage()
              .writeToImage("etherealGallery");
   }


   /**
    * Creates a complex scene using multi-threaded processing.
    * Includes a green pyramid, crystal spheres, and a connecting cylinder between them.
    * Demonstrates the use of multi-threading capabilities to improve rendering times.
    */
   @Test
   void new_test_multithreaded() {
      // === הגדרת הבסיס של הפירמידה ===
      Point pyramidBase1 = new Point(-40, -10, -180);
      Point pyramidBase2 = new Point(40, -10, -180);
      Point pyramidBase3 = new Point(40, -10, -120);
      Point pyramidBase4 = new Point(-40, -10, -120);
      Point pyramidApex = new Point(0, 50, -150);

      // חומר מטאלי ירוק עם יותר גוון ירוק
      Material metallicGreenMaterial = new Material()
              .setKD(0.4).setKS(0.9).setShininess(180)
              .setKT(0.15).setKR(0.6);

      // צבע ירוק יותר עשיר לפירמידה
      // Color brightGreen = new Color(0.5, 5, 1.0);
      Color brightGreen = new Color(0, 153, 51);

      // פאות הפירמידה - כולן באותו צבע ירוק מטאלי בהיר
      scene.geometries.add(
              new Triangle(pyramidBase1, pyramidBase2, pyramidApex)
                      .setEmission(brightGreen)
                      .setMaterial(metallicGreenMaterial)
      );

      scene.geometries.add(
              new Triangle(pyramidBase2, pyramidBase3, pyramidApex)
                      .setEmission(brightGreen)
                      .setMaterial(metallicGreenMaterial)
      );

      scene.geometries.add(
              new Triangle(pyramidBase3, pyramidBase4, pyramidApex)
                      .setEmission(brightGreen)
                      .setMaterial(metallicGreenMaterial)
      );

      scene.geometries.add(
              new Triangle(pyramidBase4, pyramidBase1, pyramidApex)
                      .setEmission(brightGreen)
                      .setMaterial(metallicGreenMaterial)
      );

      // === הוספת יהלומים (כדורים מבריקים) ===

      // חומר יהלום - מבריק מאוד עם שקיפות והשתקפות גבוהות
      Material diamondMaterial = new Material()
              .setKD(0.05).setKS(0.95).setShininess(300)
              .setKT(0.6).setKR(0.8);

      // צבע יהלום מבריק
      Color diamondColor = new Color(8, 8, 8);

      // נקודות המרכז של שני הכדורים
      Point sphere1Center = new Point(60, 10, -140);
      Point sphere2Center = new Point(85, 5, -160);

      // שני כדורים קטנים מימין לפירמידה
      scene.geometries.add(
              new Sphere(10d, sphere1Center)
                      .setEmission(diamondColor)
                      .setMaterial(diamondMaterial)
      );

      scene.geometries.add(
              new Sphere(8d, sphere2Center)
                      .setEmission(diamondColor)
                      .setMaterial(diamondMaterial)
      );

      // === הוספת גליל בין שני הכדורים ===
      // יצירת וקטור כיוון מהכדור הראשון לשני
      Vector cylinderDirection = sphere2Center.subtract(sphere1Center);

      // חומר מטאלי לגליל
      Material cylinderMaterial = new Material()
              .setKD(0.2).setKS(0.8).setShininess(100)
              .setKT(0.3).setKR(0.5);

      // הוספת הגליל בין שני הכדורים
      scene.geometries.add(
              new Cylinder(new Ray(sphere1Center, cylinderDirection), 5, cylinderDirection.length())
                      .setEmission(new Color(120, 120, 200))
                      .setMaterial(cylinderMaterial)
      );

      // כדור אחד משמאל לפירמידה - ממוקם יותר מקדימה
      scene.geometries.add(
              new Sphere(30d, new Point(-65, 15, -75)) // שיניתי את ערך ה-Z מ -150 ל -50 להזזה קדימה
                      .setEmission(diamondColor)
                      .setMaterial(diamondMaterial)
      );

      // === מישורים - עוד פחות מראתיים ורקע בהיר יותר ===

      // רצפה עם מינימום השתקפות ובהירה יותר
      scene.geometries.add(
              new Plane(new Point(0, -25, 0), new Vector(0, 1, 0))
                      .setEmission(new Color(7, 6, 5)) // בהיר יותר
                      .setMaterial(new Material()
                              .setKD(0.95).setKS(0.15).setShininess(20)
                              .setKR(0.08)) // עוד פחות השתקפות
      );

      // קיר אחורי מאט בהיר יותר
      scene.geometries.add(
              new Plane(new Point(0, 0, -250), new Vector(0, 0, 1))
                      .setEmission(new Color(4, 3, 5)) // בהיר יותר
                      .setMaterial(new Material()
                              .setKD(0.98).setKS(0.02).setShininess(5)
                              .setKR(0.02)) // כמעט ללא השתקפות
      );

      // קיר צדדי בהיר יותר וללא השתקפות כמעט
      scene.geometries.add(
              new Plane(new Point(-120, 0, 0), new Vector(1, 0, 0))
                      .setEmission(new Color(5, 6, 7)) // בהיר יותר
                      .setMaterial(new Material()
                              .setKD(0.9).setKS(0.2).setShininess(30)
                              .setKR(0.1)) // מינימום השתקפות
      );

      // === תאורה ===

      // תאורת סביבה עדינה
      scene.setAmbientLight(new AmbientLight(new Color(0.6, 0.6, 1.1))); // מעט בהיר יותר

      // אור ספוט ראשי
      scene.light.add(
              new SpotLight(
                      new Color(250, 200, 150),
                      new Point(-100, 150, 50),
                      new Vector(2, -3, -4))
                      .setKl(0.0003).setKq(0.00003)
      );

      // אור נקודתי משני
      scene.light.add(
              new PointLight(
                      new Color(100, 150, 200),
                      new Point(120, 80, -40))
                      .setKl(0.0003).setKq(0.00002)
      );

      // אור כיווני רך
      scene.light.add(
              new DirectionalLight(
                      new Color(15, 20, 25),
                      new Vector(-0.2, -0.6, -1))
      );

      // === הגדרת המצלמה ===
      cameraBuilder
              .setLocation(new Point(80, 60, 150))
              .setDirection(new Point(-10, 25, -100), Vector.AXIS_Y)
              .setVpDistance(200)
              .setVpSize(240, 240)
              .setResolution(800, 800)
              // הגדרות מולטי-תרדינג
              .setMultithreading(8)      // שימוש ב-8 תהליכונים
              .setDebugPrint(5)          // הדפסת התקדמות כל 5%
              .setRayTracer(scene, RayTracerType.SIMPLE)  // חשוב להגדיר את הריי טרייסר
              .build()
              .renderImage()
              .writeToImage("new_test_multithreaded");
   }


   /**
    * Creates a wonderland of cylinders and tubes.
    * Includes transparent cylinders, intersecting metallic tubes, and a spiral structure.
    * Demonstrates transparency, reflection, and color effects in cylindrical shapes.
    * Uses multi-threaded processing to improve rendering performance.
    */

   @Test
   void cylindricalWonderland() {
      // === מגדל צילינדרים שקופים ===


      scene.geometries.add(
              new Cylinder(new Ray(new Point(0, -25, -120), new Vector(0, 1, 0)), 28d, 85d)
                      .setEmission(new Color(5, 5, 7))
                      .setMaterial(new Material()
                              .setKD(0.1).setKS(0.9).setShininess(170)
                              .setKT(0.7).setKR(0.3))
      );

      // צילינדר ארגמן - גבוה ודק
      scene.geometries.add(
              new Cylinder(new Ray(new Point(-55, -25, -100), new Vector(0, 1, 0)), 15d, 110d)
                      .setEmission(new Color(7, 2, 6))
                      .setMaterial(new Material()
                              .setKD(0.2).setKS(0.8).setShininess(130)
                              .setKT(0.5).setKR(0.4))
      );

      // צילינדר טורקיז - בינוני
      scene.geometries.add(
              new Cylinder(new Ray(new Point(60, -25, -90), new Vector(0, 1, 0)), 18d, 70d)
                      .setEmission(new Color(2, 7, 8))
                      .setMaterial(new Material()
                              .setKD(0.25).setKS(0.75).setShininess(120)
                              .setKT(0.6).setKR(0.35))
      );

      // === צינורות מתכתיים מצטלבים ===

      // צינור זהב אופקי
      scene.geometries.add(
              new Tube(new Ray(new Point(-90, 20, -120), new Vector(1, 0.1, 0).normalize()), 8d)
                      .setEmission(new Color(8, 7, 2))
                      .setMaterial(new Material()
                              .setKD(0.3).setKS(0.7).setShininess(140)
                              .setKR(0.6))
      );

      // צינור כסף אנכי
      scene.geometries.add(
              new Tube(new Ray(new Point(-30, -25, -160), new Vector(0, 1, 0)), 6d)
                      .setEmission(new Color(7, 7, 8))
                      .setMaterial(new Material()
                              .setKD(0.2).setKS(0.8).setShininess(150)
                              .setKR(0.7))
      );

      // צינור ארד באלכסון
      scene.geometries.add(
              new Tube(new Ray(new Point(40, -25, -150), new Vector(1, 3, 1).normalize()), 7d)
                      .setEmission(new Color(6, 4, 2))
                      .setMaterial(new Material()
                              .setKD(0.3).setKS(0.7).setShininess(130)
                              .setKR(0.5))
      );

      // === צילינדרים המכילים צינורות ===

      // צילינדר חיצוני שקוף
      scene.geometries.add(
              new Cylinder(new Ray(new Point(-15, 15, -80), new Vector(0.2, 1, 0.1).normalize()), 12d, 60d)
                      .setEmission(new Color(3, 3, 6))
                      .setMaterial(new Material()
                              .setKD(0.15).setKS(0.85).setShininess(160)
                              .setKT(0.85).setKR(0.15))
      );

      // צינור פנימי מוזהב (בתוך השקוף)
      scene.geometries.add(
              new Tube(new Ray(new Point(-15, 15, -80), new Vector(0.2, 1, 0.1).normalize()), 5d)
                      .setEmission(new Color(9, 8, 3))
                      .setMaterial(new Material()
                              .setKD(0.3).setKS(0.7).setShininess(200)
                              .setKR(0.6))
      );

      // === מבנה ספירלי מצינורות ===

      // הנקודה המרכזית של הספירלה
      Point spiralCenter = new Point(35, 10, -110);
      double spiralRadius = 25;
      double tubeRadius = 3.5;

      // צינור ספירלי ראשון - טורקיז
      scene.geometries.add(
              new Tube(
                      new Ray(
                              spiralCenter.add(new Vector(spiralRadius, 0, 0)),
                              new Vector(0, 1, 0).normalize()),
                      tubeRadius)
                      .setEmission(new Color(1, 8, 9))
                      .setMaterial(new Material()
                              .setKD(0.2).setKS(0.8).setShininess(100)
                              .setKT(0.3).setKR(0.3))
      );

      // צינור ספירלי שני - סגול
      scene.geometries.add(
              new Tube(
                      new Ray(
                              spiralCenter.add(new Vector(0, 0, spiralRadius)),
                              new Vector(0, 1, 0).normalize()),
                      tubeRadius)
                      .setEmission(new Color(7, 1, 9))
                      .setMaterial(new Material()
                              .setKD(0.2).setKS(0.8).setShininess(100)
                              .setKT(0.4).setKR(0.2))
      );

      // צינור ספירלי שלישי - ירוק
      scene.geometries.add(
              new Tube(
                      new Ray(
                              spiralCenter.add(new Vector(-spiralRadius, 0, 0)),
                              new Vector(0, 1, 0).normalize()),
                      tubeRadius)
                      .setEmission(new Color(1, 9, 3))
                      .setMaterial(new Material()
                              .setKD(0.2).setKS(0.8).setShininess(100)
                              .setKT(0.5).setKR(0.2))
      );

      // === רצפה ורקע ===

      // רצפה משתקפת
      scene.geometries.add(
              new Plane(new Point(0, -25, 0), new Vector(0, 1, 0))
                      .setEmission(new Color(3, 3, 4))
                      .setMaterial(new Material()
                              .setKD(0.5).setKS(0.5).setShininess(80)
                              .setKR(0.4))
      );

      // קיר אחורי עם גוון עדין
      scene.geometries.add(
              new Plane(new Point(0, 0, -200), new Vector(0, 0, 1))
                      .setEmission(new Color(2, 1, 3))
                      .setMaterial(new Material()
                              .setKD(0.8).setKS(0.2).setShininess(10)
                              .setKR(0.1))
      );

      // === תאורה ===

      // תאורת סביבה עדינה
      scene.setAmbientLight(new AmbientLight(new Color(1, 1, 2)));

      // אור ספוט דרמטי - אור ראשי
      scene.light.add(
              new SpotLight(
                      new Color(500, 400, 300),
                      new Point(-80, 120, 40),
                      new Vector(2, -3, -5))
                      .setKl(0.0002).setKq(0.00002)
      );

      // אור נקודתי לגיוון - כחלחל
      scene.light.add(
              new PointLight(
                      new Color(150, 200, 380),
                      new Point(100, 70, -30))
                      .setKl(0.0003).setKq(0.00003)
      );

      // אור ספוט נוסף - מדגיש את המבנה הספירלי
      scene.light.add(
              new SpotLight(
                      new Color(350, 180, 220),
                      new Point(50, 100, -20),
                      new Vector(-0.5, -1, -1))
                      .setKl(0.0004).setKq(0.00004)
      );

      // אור כיווני עדין - מילוי צללים
      scene.light.add(
              new DirectionalLight(
                      new Color(20, 35, 45),
                      new Vector(-0.3, -0.5, -0.8))
      );

      // === הגדרת המצלמה ===
      cameraBuilder
              .setLocation(new Point(90, 70, 130))
              .setDirection(new Point(0, 15, -110), Vector.AXIS_Y)
              .setVpDistance(180)
              .setVpSize(220, 220)
              .setResolution(900, 900)
              .setRayTracer(scene, RayTracerType.SIMPLE)
              // הוספת תמיכה בתהליכונים
              .setMultithreading(8)        // 8 תהליכונים במקביל
              .setDebugPrint(5)            // הדפסת התקדמות כל 5%
              .build()
              .renderImage()
              .writeToImage("cylindricalWonderland_multithreaded");

   }

   /**
    * Creates a 3D scene with dual chains of Y-shaped molecules in a visually appealing arrangement.
    * <p>
    * This test demonstrates advanced 3D rendering techniques including:
    * - Creation of complex molecular structures with precise spatial positioning
    * - Implementation of multiple material properties (reflection, shininess)
    * - Strategic light placement with multiple colored light sources for dramatic effect
    * - Multi-threaded rendering for improved performance
    * <p>
    * The scene contains two parallel chains of Y-shaped molecules positioned at different depths,
    * placed on a reflective dark surface, and illuminated by various light sources to create
    * a visually striking scientific visualization.
    */
//   @Test
//   void dualChainedYMoleculesTest() {
//      // רקע שחור
//      scene.setBackground(new Color(0, 0, 0));
//
//      /
//      / חומר לאטומים - שחור יותר אך מבריק מאוד
//      Material atomMaterial = new Material()
//              .setKD(0.15).setKS(0.98).setShininess(1500)  // יותר מבריק, פחות דיפוזי
//              .setKT(0.0).setKR(0.75);  // יותר השתקפות לברק מוגבר
//
//      Material bondMaterial = new Material()
//              .setKD(0.1).setKS(0.9).setShininess(200)
//              .setKT(0.0).setKR(0.7);
//
//      // צבעים - שחור יותר מבריק לאטומים
//      Color atomColor = new Color(8, 8, 10);  // שחור יותר עמוק
//      Color bondColor = new Color(150, 150, 150);  // אפור לקשרים
//
//      // רצפה כהה מבריקה
//      scene.geometries.add(
//              new Plane(new Point(0, -10, 0), new Vector(0, 1, 0))
//                      .setEmission(new Color(20, 20, 20))
//                      .setMaterial(new Material()
//                              .setKD(0.2).setKS(0.8).setShininess(100)
//                              .setKR(0.6))  // השתקפות גבוהה
//      );
//
//      // === הגדרות הסצנה המקורית ===
//      int numberOfMolecules = 5;   // מספר מולקולות בשרשרת
//      double atomSize = 6.0;       // גודל האטומים
//      double bondThickness = 1.5;  // עובי הקשרים
//      double moleculeSpacing = 30.0;  // מרחק בין מולקולות
//
//      // === יצירת שרשרת המולקולות המקורית (רחוקה) ===
//      createMoleculeChain(scene, numberOfMolecules, atomSize, bondThickness, moleculeSpacing,
//              -100, // ערך Z המקורי
//              atomMaterial, atomColor, bondMaterial, bondColor);
//
//      // === יצירת שרשרת מולקולות זהה קרובה יותר למצלמה ===
//      createMoleculeChain(scene, numberOfMolecules, atomSize, bondThickness, moleculeSpacing,
//              -50, // ערך Z קרוב יותר
//              atomMaterial, atomColor, bondMaterial, bondColor);
//
//      // === תאורה ===
//      scene.setAmbientLight(new AmbientLight(new Color(0.05, 0.05, 0.05)));
//
//      // אור ספוט חזק מלפנים ומלמעלה
//      scene.light.add(
//              new SpotLight(
//                      new Color(900, 900, 900),
//                      new Point(0, 50, 50),
//                      new Vector(0, -1, -1))
//                      .setKl(0.0001).setKq(0.000005)
//      );
//
//      // אור נקודתי מימין להארת הצדדים
//      scene.light.add(
//              new PointLight(
//                      new Color(400, 400, 400),
//                      new Point(50, 30, 0))
//                      .setKl(0.0002).setKq(0.00002)
//      );
//
//      // אור נקודתי משמאל להארת הצדדים השמאליים
//      scene.light.add(
//              new PointLight(
//                      new Color(400, 400, 400),
//                      new Point(-50, 30, 0))
//                      .setKl(0.0002).setKq(0.00002)
//      );
//
//      // תאורה צבעונית עדינה - תוספת אור בגוון כחול עדין
//      scene.light.add(
//              new PointLight(
//                      new Color(150, 180, 350), // כחול עדין
//                      new Point(-30, 40, -40))
//                      .setKl(0.0002).setKq(0.00003)
//      );
//
//      // תאורה צבעונית עדינה - תוספת אור בגוון ירקרק עדין
//      scene.light.add(
//              new PointLight(
//                      new Color(180, 350, 200), // ירקרק עדין
//                      new Point(40, 25, -60))
//                      .setKl(0.0002).setKq(0.00003)
//      );
//
//      // תאורה צבעונית עדינה - תוספת אור בגוון סגלגל עדין
//      scene.light.add(
//              new PointLight(
//                      new Color(240, 120, 300), // סגלגל עדין
//                      new Point(10, 15, -120))
//                      .setKl(0.0003).setKq(0.00004)
//      );
//
//      // === המצלמה ===
//      cameraBuilder
//              .setLocation(new Point(-300, 10, 850))  // מצלמה רחוקה
//              .setDirection(new Point(0, 0, -100), Vector.AXIS_Y)
//              .setVpDistance(1000)
//              .setVpSize(200, 100)
//              .setResolution(1600, 1600)
//              .setRayTracer(scene, RayTracerType.SIMPLE)
//              // הוספת תמיכה בתהליכונים
//              .setMultithreading(7)
//              .setDebugPrint(5)
//              .build()
//              .renderImage()
//              .writeToImage("NEW_dual_molecule_chains_red_light");
//   }
//
//   /**
//    * Creates a complete chain of molecules at the specified position in 3D space.
//    *
//    * This method handles the placement of multiple Y-shaped molecules in a chain formation,
//    * with each molecule slightly rotated for visual variety. It also creates bonds between
//    * consecutive molecules in the chain.
//    *
//    * @param scene              The scene to which the molecule chain will be added
//    * @param numberOfMolecules  Number of molecules in the chain
//    * @param atomSize           Size (radius) of each atom sphere
//    * @param bondThickness      Thickness (radius) of the bonds between atoms
//    * @param moleculeSpacing    Distance between consecutive molecules in the chain
//    * @param zPosition          Z-axis position of the entire chain
//    * @param atomMaterial       Material properties for the atoms
//    * @param atomColor          Color of the atoms
//    * @param bondMaterial       Material properties for the bonds
//    * @param bondColor          Color of the bonds
//    */
//   private void createMoleculeChain(
//           Scene scene,
//           int numberOfMolecules,  // מספר מולקולות בשרשרת
//           double atomSize,        // גודל האטומים
//           double bondThickness,   // עובי הקשרים
//           double moleculeSpacing, // מרחק בין מולקולות
//           double zPosition,       // מיקום ציר Z של השרשרת
//           Material atomMaterial, Color atomColor,
//           Material bondMaterial, Color bondColor
//   ) {
//      // מיקום התחלתי - המולקולה השמאלית ביותר בשרשרת
//      double startX = -(numberOfMolecules-1) * moleculeSpacing/2;
//
//      // מערך לשמירת נקודות חיבור בין המולקולות
//      Point[] connectionPoints = new Point[numberOfMolecules];
//
//      // יצירת כל המולקולות Y עם נטייה שונה
//      for (int i = 0; i < numberOfMolecules; i++) {
//         double xPos = startX + i * moleculeSpacing;
//
//         // יצירת זוויות סיבוב שונות לכל מולקולה
//         double rotateY = 5 * Math.sin(i * 2.1);      // סיבוב סביב ציר Y
//         double rotateX = 8 * Math.cos(i * 1.7 + 1);  // סיבוב סביב ציר X
//         double rotateZ = 4 * Math.sin(i * 1.3 + 2);  // סיבוב סביב ציר Z
//
//         // נקודת הבסיס למולקולה הזו
//         Point basePoint = new Point(xPos,
//                 -5 + Math.sin(i * 0.8) * 3,
//                 zPosition + Math.cos(i * 0.9) * 10);
//
//         // יצירת מולקולה Y עם הנטייה המוגדרת
//         Point connectionPoint = createRotatedYMolecule(scene, basePoint, atomSize, bondThickness,
//                 rotateX, rotateY, rotateZ,
//                 atomMaterial, atomColor, bondMaterial, bondColor);
//
//         // שמירת נקודת החיבור למולקולה הבאה
//         connectionPoints[i] = connectionPoint;
//      }
//
//      // הוספת הקשרים (גלילים) בין המולקולות
//      for (int i = 0; i < numberOfMolecules - 1; i++) {
//         addBond(scene, connectionPoints[i], connectionPoints[i+1], bondThickness, bondColor, bondMaterial);
//      }
//   }
//
//   /**
//    * Creates a Y-shaped molecule with specified rotation angles in 3D space.
//    *
//    * This method builds a Y-shaped molecular structure consisting of four atoms connected by bonds:
//    * - A base atom at the bottom
//    * - A middle atom connected vertically to the base atom
//    * - Two upper atoms connected diagonally to the middle atom
//    *
//    * The entire structure can be rotated around all three axes to create variety in the molecule chain.
//    *
//    * @param scene          The scene to which the molecule will be added
//    * @param basePoint      The 3D point where the base atom will be positioned
//    * @param atomSize       Size (radius) of each atom sphere
//    * @param bondThickness  Thickness (radius) of the bonds between atoms
//    * @param rotateX        Rotation angle around X axis in degrees
//    * @param rotateY        Rotation angle around Y axis in degrees
//    * @param rotateZ        Rotation angle around Z axis in degrees
//    * @param atomMaterial   Material properties for the atoms
//    * @param atomColor      Color of the atoms
//    * @param bondMaterial   Material properties for the bonds
//    * @param bondColor      Color of the bonds
//    * @return               The 3D point of the top-right atom, used as connection point to the next molecule
//    */
//   private Point createRotatedYMolecule(
//           Scene scene, Point basePoint, double atomSize, double bondThickness,
//           double rotateX, double rotateY, double rotateZ,
//           Material atomMaterial, Color atomColor, Material bondMaterial, Color bondColor
//   ) {
//      // יצירת האטום התחתון
//      scene.geometries.add(
//              new Sphere(atomSize, basePoint)
//                      .setEmission(atomColor)
//                      .setMaterial(atomMaterial)
//      );
//
//      // חישוב וקטור מעלה עם הסיבוב המבוקש
//      Vector upVector = new Vector(0, 1, 0);
//      upVector = rotateVector(upVector, rotateX, rotateY, rotateZ);
//
//      // מרחק אנכי סטנדרטי
//      double verticalDistance = 15.0;
//
//      // חישוב מיקום האטום האמצעי
//      Point middlePoint = basePoint.add(upVector.scale(verticalDistance));
//      scene.geometries.add(
//              new Sphere(atomSize, middlePoint)
//                      .setEmission(atomColor)
//                      .setMaterial(atomMaterial)
//      );
//
//      // קשר בין האטום התחתון לאטום האמצעי
//      addBond(scene, basePoint, middlePoint, bondThickness, bondColor, bondMaterial);
//
//      // חישוב הוקטורים האלכסוניים עם הסיבוב
//      Vector baseRightDiag = new Vector(0.7, 0.7, 0).normalize();
//      Vector baseLeftDiag = new Vector(-0.7, 0.7, 0).normalize();
//
//      // הפעלת אותו סיבוב על הוקטורים האלכסוניים
//      Vector rightDiagonal = rotateVector(baseRightDiag, rotateX, rotateY, rotateZ);
//      Vector leftDiagonal = rotateVector(baseLeftDiag, rotateX, rotateY, rotateZ);
//
//      // מרחק אלכסוני
//      double diagonalDistance = 18.0;
//
//      // יצירת האטום הימני העליון
//      Point topRightPoint = middlePoint.add(rightDiagonal.scale(diagonalDistance));
//      scene.geometries.add(
//              new Sphere(atomSize, topRightPoint)
//                      .setEmission(atomColor)
//                      .setMaterial(atomMaterial)
//      );
//
//      // קשר לאטום הימני העליון
//      addBond(scene, middlePoint, topRightPoint, bondThickness, bondColor, bondMaterial);
//
//      // יצירת האטום השמאלי העליון
//      Point topLeftPoint = middlePoint.add(leftDiagonal.scale(diagonalDistance));
//      scene.geometries.add(
//              new Sphere(atomSize, topLeftPoint)
//                      .setEmission(atomColor)
//                      .setMaterial(atomMaterial)
//      );
//
//      // קשר לאטום השמאלי העליון
//      addBond(scene, middlePoint, topLeftPoint, bondThickness, bondColor, bondMaterial);
//
//      // מחזיר את האטום הימני העליון כנקודת חיבור למולקולה הבאה
//      return topRightPoint;
//   }
//
//   /**
//    * Performs rotation of a vector around all three axes.
//    * Applies sequential rotations: first around X axis, then Y axis, and finally Z axis.
//    *
//    * @param v        The original vector to be rotated
//    * @param angleX   Rotation angle around the X axis in degrees
//    * @param angleY   Rotation angle around the Y axis in degrees
//    * @param angleZ   Rotation angle around the Z axis in degrees
//    * @return         A new vector resulting from the applied rotations
//    */
//   private Vector rotateVector(Vector v, double angleX, double angleY, double angleZ) {
//      // המרת מעלות לרדיאנים
//      double radX = Math.toRadians(angleX);
//      double radY = Math.toRadians(angleY);
//      double radZ = Math.toRadians(angleZ);
//
//      // קריאת ערכי הוקטור המקורי
//      double x = v.xyz.d1();
//      double y = v.xyz.d2();
//      double z = v.xyz.d3();
//
//      // סיבוב סביב ציר X
//      double yNew = y * Math.cos(radX) - z * Math.sin(radX);
//      double zNew = y * Math.sin(radX) + z * Math.cos(radX);
//      y = yNew;
//      z = zNew;
//
//      // סיבוב סביב ציר Y
//      double xNew = x * Math.cos(radY) + z * Math.sin(radY);
//      zNew = -x * Math.sin(radY) + z * Math.cos(radY);
//      x = xNew;
//      z = zNew;
//
//      // סיבוב סביב ציר Z
//      xNew = x * Math.cos(radZ) - y * Math.sin(radZ);
//      yNew = x * Math.sin(radZ) + y * Math.cos(radZ);
//      x = xNew;
//      y = yNew;
//
//      // החזרת וקטור מסובב חדש
//      return new Vector(x, y, z);
//   }
//
//   /**
//    * Adds a bond (cylinder) between two atoms in 3D space.
//    * The bond is represented as a cylinder stretching between two points with defined radius and material.
//    *
//    * @param scene    The scene instance to which the cylinder will be added
//    * @param atom1    The point representing the first atom's position
//    * @param atom2    The point representing the second atom's position
//    * @param radius   The radius of the cylinder representing the bond
//    * @param color    The color of the cylinder
//    * @param material The material of the cylinder with reflection, transparency, and shininess properties
//    */
//   private void addBond(
//           Scene scene,
//           Point atom1, Point atom2,
//           double radius,
//           Color color, Material material
//   ) {
//      Vector direction = atom2.subtract(atom1);
//      double length = direction.length();
//
//      scene.geometries.add(
//              new Cylinder(new Ray(atom1, direction), radius, length)
//                      .setEmission(color)
//                      .setMaterial(material)
//      );
//   }
   @Test
   void cylindricalWonderland_new() {
      // === מגדל צילינדרים שקופים ===

      scene.geometries.add(
              new Cylinder(new Ray(new Point(0, -25, -120), new Vector(0, 1, 0)), 28d, 85d)
                      .setEmission(new Color(5, 5, 7))
                      .setMaterial(new Material()
                              .setKD(0.1).setKS(0.9).setShininess(170)
                              .setKT(0.7).setKR(0.3))
      );

      // צילינדר ארגמן - גבוה ודק
      scene.geometries.add(
              new Cylinder(new Ray(new Point(-55, -25, -100), new Vector(0, 1, 0)), 15d, 110d)
                      .setEmission(new Color(7, 2, 6))
                      .setMaterial(new Material()
                              .setKD(0.2).setKS(0.8).setShininess(130)
                              .setKT(0.5).setKR(0.4))
      );

      // צילינדר טורקיז - בינוני
      scene.geometries.add(
              new Cylinder(new Ray(new Point(60, -25, -90), new Vector(0, 1, 0)), 18d, 70d)
                      .setEmission(new Color(2, 7, 8))
                      .setMaterial(new Material()
                              .setKD(0.25).setKS(0.75).setShininess(120)
                              .setKT(0.6).setKR(0.35))
      );

      // === צינורות מתכתיים מצטלבים ===

      // צינור זהב אופקי
      scene.geometries.add(
              new Tube(new Ray(new Point(-90, 20, -120), new Vector(1, 0.1, 0).normalize()), 8d)
                      .setEmission(new Color(8, 7, 2))
                      .setMaterial(new Material()
                              .setKD(0.3).setKS(0.7).setShininess(140)
                              .setKR(0.6))
      );

      // צינור כסף אנכי
      scene.geometries.add(
              new Tube(new Ray(new Point(-30, -25, -160), new Vector(0, 1, 0)), 6d)
                      .setEmission(new Color(7, 7, 8))
                      .setMaterial(new Material()
                              .setKD(0.2).setKS(0.8).setShininess(150)
                              .setKR(0.7))
      );

      // צינור ארד באלכסון
      scene.geometries.add(
              new Tube(new Ray(new Point(40, -25, -150), new Vector(1, 3, 1).normalize()), 7d)
                      .setEmission(new Color(6, 4, 2))
                      .setMaterial(new Material()
                              .setKD(0.3).setKS(0.7).setShininess(130)
                              .setKR(0.5))
      );

      // === צילינדרים המכילים צינורות ===

      // צילינדר חיצוני שקוף
      scene.geometries.add(
              new Cylinder(new Ray(new Point(-15, 15, -80), new Vector(0.2, 1, 0.1).normalize()), 12d, 60d)
                      .setEmission(new Color(3, 3, 6))
                      .setMaterial(new Material()
                              .setKD(0.15).setKS(0.85).setShininess(160)
                              .setKT(0.85).setKR(0.15))
      );

      // צינור פנימי מוזהב (בתוך השקוף)
      scene.geometries.add(
              new Tube(new Ray(new Point(-15, 15, -80), new Vector(0.2, 1, 0.1).normalize()), 5d)
                      .setEmission(new Color(9, 8, 3))
                      .setMaterial(new Material()
                              .setKD(0.3).setKS(0.7).setShininess(200)
                              .setKR(0.6))
      );

      // === מבנה ספירלי מצינורות ===

      // הנקודה המרכזית של הספירלה
      Point spiralCenter = new Point(35, 10, -110);
      double spiralRadius = 25;
      double tubeRadius = 3.5;

      // צינור ספירלי ראשון - טורקיז
      scene.geometries.add(
              new Tube(
                      new Ray(
                              spiralCenter.add(new Vector(spiralRadius, 0, 0)),
                              new Vector(0, 1, 0).normalize()),
                      tubeRadius)
                      .setEmission(new Color(1, 8, 9))
                      .setMaterial(new Material()
                              .setKD(0.2).setKS(0.8).setShininess(100)
                              .setKT(0.3).setKR(0.3))
      );

      // צינור ספירלי שני - סגול
      scene.geometries.add(
              new Tube(
                      new Ray(
                              spiralCenter.add(new Vector(0, 0, spiralRadius)),
                              new Vector(0, 1, 0).normalize()),
                      tubeRadius)
                      .setEmission(new Color(7, 1, 9))
                      .setMaterial(new Material()
                              .setKD(0.2).setKS(0.8).setShininess(100)
                              .setKT(0.4).setKR(0.2))
      );

      // צינור ספירלי שלישי - ירוק
      scene.geometries.add(
              new Tube(
                      new Ray(
                              spiralCenter.add(new Vector(-spiralRadius, 0, 0)),
                              new Vector(0, 1, 0).normalize()),
                      tubeRadius)
                      .setEmission(new Color(1, 9, 3))
                      .setMaterial(new Material()
                              .setKD(0.2).setKS(0.8).setShininess(100)
                              .setKT(0.5).setKR(0.2))
      );

      // === רצפה ורקע ===

      // רצפה משתקפת
      scene.geometries.add(
              new Plane(new Point(0, -25, 0), new Vector(0, 1, 0))
                      .setEmission(new Color(3, 3, 4))
                      .setMaterial(new Material()
                              .setKD(0.5).setKS(0.5).setShininess(80)
                              .setKR(0.4))
      );

      // קיר אחורי עם גוון עדין
      scene.geometries.add(
              new Plane(new Point(0, 0, -200), new Vector(0, 0, 1))
                      .setEmission(new Color(2, 1, 3))
                      .setMaterial(new Material()
                              .setKD(0.8).setKS(0.2).setShininess(10)
                              .setKR(0.1))
      );

      // === תאורה ===

      // תאורת סביבה עדינה
      scene.setAmbientLight(new AmbientLight(new Color(1, 1, 2)));

      // אור ספוט דרמטי - אור ראשי
      scene.light.add(
              new SpotLight(
                      new Color(500, 400, 300),
                      new Point(-80, 120, 40),
                      new Vector(2, -3, -5))
                      .setKl(0.0002).setKq(0.00002)
      );

      // אור נקודתי לגיוון - כחלחל
      scene.light.add(
              new PointLight(
                      new Color(150, 200, 380),
                      new Point(100, 70, -30))
                      .setKl(0.0003).setKq(0.00003)
      );

      // אור ספוט נוסף - מדגיש את המבנה הספירלי
      scene.light.add(
              new SpotLight(
                      new Color(350, 180, 220),
                      new Point(50, 100, -20),
                      new Vector(-0.5, -1, -1))
                      .setKl(0.0004).setKq(0.00004)
      );

      // אור כיווני עדין - מילוי צללים
      scene.light.add(
              new DirectionalLight(
                      new Color(20, 35, 45),
                      new Vector(-0.3, -0.5, -0.8))
      );


      // === הגדרת המצלמה ===
      cameraBuilder
              .setLocation(new Point(90, 70, 130))
              .setDirection(new Point(0, 15, -110), Vector.AXIS_Y)
              .setVpDistance(180)
              .setVpSize(220, 220)
              .setResolution(900, 900)
              .setRayTracer(scene, RayTracerType.SIMPLE)
              // הוספת תמיכה בתהליכונים
              .setMultithreading(7)        // 8 תהליכונים במקביל
              .setDebugPrint(5)            // הדפסת התקדמות כל 5%
              // הפעלת Adaptive Antialiasing
              .setAdaptiveAntiAliasing(true)
              .setAdaptiveAntiAliasingDepth(3)
              .setAdaptiveAntiAliasingThreshold(0.01)
              .build()
              .renderImage()
              .writeToImage("cylindricalWonderland_adaptive_aa");


   }

   //#####################################################################################################################
   @Test
   void dualChainedYMoleculesTest() {
      // רקע שחור עמוק
      scene.setBackground(new Color(5, 5, 15));

      // חומר לאטומים - ירוק/כחול מבריק עם שקיפות מתונה
      Material atomMaterial = new Material()
              .setKD(0.25).setKS(0.6).setShininess(100)
              .setKT(0.45).setKR(0.08);

// חומר לקשרים - גם קשרים שקופים
      Material bondMaterial = new Material()
              .setKD(0.22).setKS(0.5).setShininess(80)
              .setKT(0.4).setKR(0.05);

// צבעים ירוקים וכחולים חיים ועדינים
      // Color atomColor = new Color(60, 170, 190);   // טורקיז-כחול-ירקרק
      // Color bondColor = new Color(80, 220, 140);   // ירוק בהיר-שקוף

// או אפשרות לגוונים יותר כחולים
      Color atomColor = new Color(70, 150, 240);   // כחול עז
      Color bondColor = new Color(40, 200, 180);   // טורקיז-ירוק

      scene.geometries.add(
              new Plane(new Point(0, -15, 0), new Vector(0, 1, 0))
                      .setEmission(new Color(3, 3, 4))
                      .setMaterial(new Material()
                              .setKD(0.5).setKS(0.5).setShininess(80)
                              .setKR(0.4))
      );

      // === יצירת פירמידות ברקע ===
      createBackgroundPyramids(scene);

      // === הגדרות הסצנה המקורית ===
      int numberOfMolecules = 3;   // יותר מולקולות
      double atomSize = 8.0;       // אטומים קצת יותר גדולים
      double bondThickness = 3.5;  // קשרים עבים יותר
      double moleculeSpacing = 35.0;  // מרחק מעט גדול יותר

      // === יצירת שרשרת המולקולות המקורית (רחוקה) ===
      createMoleculeChain(scene, numberOfMolecules, atomSize, bondThickness, moleculeSpacing,
              -150, // ערך Z רחוק יותר
              atomMaterial, atomColor, bondMaterial, bondColor);

      // === יצירת שרשרת מולקולות זהה קרובה יותר למצלמה ===
      createMoleculeChain(scene, numberOfMolecules, atomSize, bondThickness, moleculeSpacing,
              -80, // ערך Z קרוב יותר
              atomMaterial, atomColor, bondMaterial, bondColor);

      // === יצירת שרשרת שלישית באמצע עם צבעים שונים ===
      createMoleculeChain(scene, numberOfMolecules, atomSize, bondThickness, moleculeSpacing,
              -110, // ערך Z באמצע
              atomMaterial, new Color(180, 80, 220), bondMaterial, new Color(130, 100, 180));


      scene.setAmbientLight(new AmbientLight(new Color(10, 10, 20)));

      // אור ספוט דרמטי - אור ראשי (פחות עוצמה)
      scene.light.add(
              new SpotLight(
                      new Color(180, 140, 110),
                      new Point(-80, 120, 40),
                      new Vector(2, -3, -5))
                      .setKl(0.0002).setKq(0.00002)
      );

      // אור נקודתי לגיוון - פחות עוצמה
      scene.light.add(
              new PointLight(
                      new Color(60, 70, 120),
                      new Point(100, 70, -30))
                      .setKl(0.0003).setKq(0.00003)
      );

      // אור ספוט נוסף - פחות עוצמה
      scene.light.add(
              new SpotLight(
                      new Color(90, 60, 80),
                      new Point(50, 100, -20),
                      new Vector(-0.5, -1, -1))
                      .setKl(0.0004).setKq(0.00004)
      );

      // אור כיווני עדין - מילוי צללים
      scene.light.add(
              new DirectionalLight(
                      new Color(10, 18, 22),
                      new Vector(-0.3, -0.5, -0.8))
      );

      // === המצלמה רחוקה משמעותית ===
      cameraBuilder
              .setLocation(new Point(-500, 50, 1200))  // מצלמה רחוקה הרבה יותר
              .setDirection(new Point(0, 0, -100), Vector.AXIS_Y)
              .setVpDistance(1200)
              .setVpSize(300, 200)  // שדה ראייה רחב יותר
              .setResolution(800, 800)  // רזולוציה גבוהה יותר
              .setRayTracer(scene, RayTracerType.SIMPLE)
              .setMultithreading(7)
              .setDebugPrint(3)
              .build()
              .renderImage()
              .writeToImage("ENHANCED_dual_molecule_chains_purple_pyramids_dimmed");
   }

   /**
    * Creates impressive background pyramids to enhance the scene with transparency
    */
   private void createBackgroundPyramids(Scene scene) {
      Material pyramidMaterial = new Material()
              .setKD(0.4).setKS(0.6).setShininess(100)
              .setKT(0.3).setKR(0.6);

      // פירמידות גדולות ברקע
      double pyramidSize = 40.0;

      // פירמידה ימנית ברקע
      createPyramid(scene, new Point(200, -15, -300), pyramidSize,
              new Color(50, 30, 80), pyramidMaterial);

      // פירמידה שמאלית ברקע
      createPyramid(scene, new Point(-200, -15, -300), pyramidSize,
              new Color(70, 40, 100), pyramidMaterial);

      // פירמידה מרכזית רחוקה
      createPyramid(scene, new Point(0, -15, -400), pyramidSize * 1.5,
              new Color(40, 25, 70), pyramidMaterial);

      // פירמידות קטנות יותר בצדדים
      createPyramid(scene, new Point(300, -15, -250), pyramidSize * 0.7,
              new Color(60, 35, 90), pyramidMaterial);

      createPyramid(scene, new Point(-300, -15, -250), pyramidSize * 0.7,
              new Color(60, 35, 90), pyramidMaterial);

      // פירמידות קטנות נוספות לעומק
      createPyramid(scene, new Point(100, -15, -350), pyramidSize * 0.5,
              new Color(35, 20, 60), pyramidMaterial);

      createPyramid(scene, new Point(-100, -15, -350), pyramidSize * 0.5,
              new Color(35, 20, 60), pyramidMaterial);
   }

   /**
    * Creates a single pyramid at the specified location
    */
   private void createPyramid(Scene scene, Point baseCenter, double size, Color color, Material material) {
      double halfSize = size / 2;

      // נקודות הבסיס של הפירמידה (ריבוע)
      Point p1 = new Point(baseCenter.xyz.d1() - halfSize, baseCenter.xyz.d2(), baseCenter.xyz.d3() - halfSize);
      Point p2 = new Point(baseCenter.xyz.d1() + halfSize, baseCenter.xyz.d2(), baseCenter.xyz.d3() - halfSize);
      Point p3 = new Point(baseCenter.xyz.d1() + halfSize, baseCenter.xyz.d2(), baseCenter.xyz.d3() + halfSize);
      Point p4 = new Point(baseCenter.xyz.d1() - halfSize, baseCenter.xyz.d2(), baseCenter.xyz.d3() + halfSize);

      // נקודת הפסגה
      Point apex = new Point(baseCenter.xyz.d1(), baseCenter.xyz.d2() + size, baseCenter.xyz.d3());

      // בסיס הפירמידה
      scene.geometries.add(
              new Polygon(p1, p2, p3, p4)
                      .setEmission(color.scale(0.8))
                      .setMaterial(material)
      );

      // הצלעות של הפירמידה
      scene.geometries.add(
              new Triangle(p1, p2, apex)
                      .setEmission(color)
                      .setMaterial(material)
      );

      scene.geometries.add(
              new Triangle(p2, p3, apex)
                      .setEmission(color.scale(1.1))
                      .setMaterial(material)
      );

      scene.geometries.add(
              new Triangle(p3, p4, apex)
                      .setEmission(color)
                      .setMaterial(material)
      );

      scene.geometries.add(
              new Triangle(p4, p1, apex)
                      .setEmission(color.scale(0.9))
                      .setMaterial(material)
      );
   }

   /**
    * Creates a complete chain of molecules at the specified position in 3D space.
    */
   private void createMoleculeChain(
           Scene scene,
           int numberOfMolecules,
           double atomSize,
           double bondThickness,
           double moleculeSpacing,
           double zPosition,
           Material atomMaterial, Color atomColor,
           Material bondMaterial, Color bondColor
   ) {
      double startX = -(numberOfMolecules - 1) * moleculeSpacing / 2;
      Point[] connectionPoints = new Point[numberOfMolecules];

      for (int i = 0; i < numberOfMolecules; i++) {
         double xPos = startX + i * moleculeSpacing;

         // יצירת זוויות סיבוב שונות לכל מולקולה
         double rotateY = 8 * Math.sin(i * 2.3);
         double rotateX = 12 * Math.cos(i * 1.9 + 1);
         double rotateZ = 6 * Math.sin(i * 1.5 + 2);

         Point basePoint = new Point(xPos,
                 -10 + Math.sin(i * 0.9) * 4,
                 zPosition + Math.cos(i * 1.1) * 15);

         Point connectionPoint = createRotatedYMolecule(scene, basePoint, atomSize, bondThickness,
                 rotateX, rotateY, rotateZ,
                 atomMaterial, atomColor, bondMaterial, bondColor);

         connectionPoints[i] = connectionPoint;
      }

      // הוספת הקשרים בין המולקולות
      for (int i = 0; i < numberOfMolecules - 1; i++) {
         addBond(scene, connectionPoints[i], connectionPoints[i + 1], bondThickness, bondColor, bondMaterial);
      }
   }

   /**
    * Creates a Y-shaped molecule with specified rotation angles in 3D space.
    */
   private Point createRotatedYMolecule(
           Scene scene, Point basePoint, double atomSize, double bondThickness,
           double rotateX, double rotateY, double rotateZ,
           Material atomMaterial, Color atomColor, Material bondMaterial, Color bondColor
   ) {
      // יצירת האטום התחתון
      scene.geometries.add(
              new Sphere(atomSize, basePoint)
                      .setEmission(atomColor)
                      .setMaterial(atomMaterial)
      );

      Vector upVector = new Vector(0, 1, 0);
      upVector = rotateVector(upVector, rotateX, rotateY, rotateZ);

      double verticalDistance = 15.0;  // מרחק קצת יותר גדול

      Point middlePoint = basePoint.add(upVector.scale(verticalDistance));
      scene.geometries.add(
              new Sphere(atomSize, middlePoint)
                      .setEmission(atomColor)
                      .setMaterial(atomMaterial)
      );

      addBond(scene, basePoint, middlePoint, bondThickness, bondColor, bondMaterial);

      Vector baseRightDiag = new Vector(0.7, 0.7, 0).normalize();
      Vector baseLeftDiag = new Vector(-0.7, 0.7, 0).normalize();

      Vector rightDiagonal = rotateVector(baseRightDiag, rotateX, rotateY, rotateZ);
      Vector leftDiagonal = rotateVector(baseLeftDiag, rotateX, rotateY, rotateZ);

      double diagonalDistance = 13.0;  // מרחק קצת יותר גדול

      Point topRightPoint = middlePoint.add(rightDiagonal.scale(diagonalDistance));
      scene.geometries.add(
              new Sphere(atomSize, topRightPoint)
                      .setEmission(atomColor)
                      .setMaterial(atomMaterial)
      );

      addBond(scene, middlePoint, topRightPoint, bondThickness, bondColor, bondMaterial);

      Point topLeftPoint = middlePoint.add(leftDiagonal.scale(diagonalDistance));
      scene.geometries.add(
              new Sphere(atomSize, topLeftPoint)
                      .setEmission(atomColor)
                      .setMaterial(atomMaterial)
      );

      addBond(scene, middlePoint, topLeftPoint, bondThickness, bondColor, bondMaterial);

      return topRightPoint;
   }

   /**
    * Performs rotation of a vector around all three axes.
    */
   private Vector rotateVector(Vector v, double angleX, double angleY, double angleZ) {
      double radX = Math.toRadians(angleX);
      double radY = Math.toRadians(angleY);
      double radZ = Math.toRadians(angleZ);

      double x = v.xyz.d1();
      double y = v.xyz.d2();
      double z = v.xyz.d3();

      // סיבוב סביב ציר X
      double yNew = y * Math.cos(radX) - z * Math.sin(radX);
      double zNew = y * Math.sin(radX) + z * Math.cos(radX);
      y = yNew;
      z = zNew;

      // סיבוב סביב ציר Y
      double xNew = x * Math.cos(radY) + z * Math.sin(radY);
      zNew = -x * Math.sin(radY) + z * Math.cos(radY);
      x = xNew;
      z = zNew;

      // סיבוב סביב ציר Z
      xNew = x * Math.cos(radZ) - y * Math.sin(radZ);
      yNew = x * Math.sin(radZ) + y * Math.cos(radZ);
      x = xNew;
      y = yNew;

      return new Vector(x, y, z);
   }

   /**
    * Adds a bond (cylinder) between two atoms in 3D space.
    */
   private void addBond(
           Scene scene,
           Point atom1, Point atom2,
           double radius,
           Color color, Material material
   ) {
      Vector direction = atom2.subtract(atom1);
      double length = direction.length();

      scene.geometries.add(
              new Cylinder(new Ray(atom1, direction), radius, length)
                      .setEmission(color)
                      .setMaterial(material)
      );
   }

//////////////////////////////////////////////////////////////////////////
///
///
///
///
///
///
///
///
///
///
///
///
///
///
///
///
///
///
///qqqqqqqqqqqqqqqqqqqqqqqqq


//
//@Test
//void minimalisticStaircaseWithSpotlightTest() {
//   // === Set scene background and ambient light ===
//   // Dark blue-gray background for a moody, dramatic effect
//   scene.setBackground(new Color(5, 5, 5)); // Dark blue-gray like in the image
//
//   // Very soft ambient light for subtle detail visibility
//   scene.setAmbientLight(new AmbientLight(new Color(0.15, 0.15, 0.18)));
//
//   // === Create materials ===
//   Material stairMaterial = new Material()
//           .setKD(0.4).setKS(0.6).setShininess(80)   // מטאלי עדין
//           .setKT(0).setKR(0.4);
//
//   Material wallMaterial = new Material()
//           .setKD(0.8).setKS(0.2).setShininess(30)
//           .setKT(0.4).setKR(0.05);
//
//   Material floorMaterial = new Material()
//           .setKD(0.1).setKS(0.2).setShininess(300)
//           .setKT(0).setKR(0.8);
//
//   // Sphere materials - balanced for soft lighting
//   Material sphereMaterial = new Material()
//           .setKD(0.4).setKS(0.6).setShininess(80)
//           .setKT(0).setKR(0.4);
//
//   double stepWidth = 30;
//   double stepHeight = 15;
//   double stepDepth = 45;
//
//   Color stairColor = new Color(140, 130, 110); // Lighter, more neutral stairs
//   Color wallColor = new Color(40, 45, 55); // Dark blue-gray for walls
//   Color floorColor = new Color(20, 25, 30);
//
//   // --- Base structure under the stairs ---
//   addBaseStructure(scene, -150, 0, -stepDepth/2, 150, -20, stepDepth/2, stairColor, stairMaterial);
//
//   // --- Side and back walls of staircase structure ---
//   scene.geometries.add(
//           new Polygon(
//                   new Point(-150, -20, -stepDepth/2),
//                   new Point(-150, -20, stepDepth/2),
//                   new Point(-150, 10*stepHeight, stepDepth/2),
//                   new Point(-150, 10*stepHeight, -stepDepth/2))
//                   .setEmission(stairColor)
//                   .setMaterial(stairMaterial)
//   );
//   scene.geometries.add(
//           new Polygon(
//                   new Point(150, -20, -stepDepth/2),
//                   new Point(150, -20, stepDepth/2),
//                   new Point(150, 10*stepHeight, stepDepth/2),
//                   new Point(150, 10*stepHeight, -stepDepth/2))
//                   .setEmission(stairColor)
//                   .setMaterial(stairMaterial)
//   );
//   scene.geometries.add(
//           new Polygon(
//                   new Point(-150, -20, stepDepth/2),
//                   new Point(150, -20, stepDepth/2),
//                   new Point(150, 10*stepHeight, stepDepth/2),
//                   new Point(-150, 10*stepHeight, stepDepth/2))
//                   .setEmission(stairColor)
//                   .setMaterial(stairMaterial)
//   );
//   scene.geometries.add(
//           new Polygon(
//                   new Point(-150, -20, -stepDepth/2),
//                   new Point(150, -20, -stepDepth/2),
//                   new Point(150, 0, -stepDepth/2),
//                   new Point(-150, 0, -stepDepth/2))
//                   .setEmission(stairColor)
//                   .setMaterial(stairMaterial)
//   );
//
//   // --- Stairs ---
//   for (int i = 0; i < 10; i++) {
//      double x = -150 + i * stepWidth;
//      double y = i * stepHeight;
//
//      // Horizontal step
//      scene.geometries.add(
//              new Polygon(
//                      new Point(x, y, -stepDepth/2),
//                      new Point(x + stepWidth, y, -stepDepth/2),
//                      new Point(x + stepWidth, y, stepDepth/2),
//                      new Point(x, y, stepDepth/2))
//                      .setEmission(stairColor)
//                      .setMaterial(stairMaterial)
//      );
//      // Vertical riser
//      if (i < 9) {
//         scene.geometries.add(
//                 new Polygon(
//                         new Point(x + stepWidth, y, -stepDepth/2),
//                         new Point(x + stepWidth, y + stepHeight, -stepDepth/2),
//                         new Point(x + stepWidth, y + stepHeight, stepDepth/2),
//                         new Point(x + stepWidth, y, stepDepth/2))
//                         .setEmission(stairColor)
//                         .setMaterial(stairMaterial)
//         );
//      }
//      // Side/back/under fills for full enclosure
//      if (i > 0) {
//         scene.geometries.add(
//                 new Polygon(
//                         new Point(x, y - stepHeight, stepDepth/2),
//                         new Point(x, y, stepDepth/2),
//                         new Point(x, y, -stepDepth/2),
//                         new Point(x, y - stepHeight, -stepDepth/2))
//                         .setEmission(stairColor)
//                         .setMaterial(stairMaterial)
//         );
//         scene.geometries.add(
//                 new Polygon(
//                         new Point(x, y - stepHeight, -stepDepth/2),
//                         new Point(x + stepWidth, y - stepHeight, -stepDepth/2),
//                         new Point(x + stepWidth, y - stepHeight, stepDepth/2),
//                         new Point(x, y - stepHeight, stepDepth/2))
//                         .setEmission(stairColor)
//                         .setMaterial(stairMaterial)
//         );
//      }
//   }
//
//   // === Add colorful spheres - with softer, more natural colors ===
//   Color[] sphereColors = {
//           new Color(150, 45, 45),   // Soft Red
//           new Color(45, 45, 150),   // Soft Blue
//           new Color(150, 100, 45),  // Soft Orange
//           new Color(100, 45, 150),  // Soft Purple
//           new Color(45, 150, 100),  // Soft Green
//           new Color(150, 150, 45),  // Soft Yellow
//           new Color(150, 80, 100),  // Soft Pink
//           new Color(45, 120, 150),  // Soft Cyan
//           new Color(120, 150, 45),  // Soft Lime
//           new Color(150, 60, 90)    // Soft Magenta
//   };
//
//   double sphereRadius = 8;
//
//   // Add spheres on each step - with random positioning
//   Random random = new Random(42); // fixed seed for consistent results
//   for (int i = 0; i < 10; i++) {
//      double baseX = -150 + i * stepWidth;
//      double y = i * stepHeight + sphereRadius; // On top of step
//
//      // Random position within the step boundaries
//      double x = baseX + stepWidth * (0.2 + random.nextDouble() * 0.6); // 20%-80% of step width
//      double z = (random.nextDouble() - 0.5) * stepDepth * 0.6; // random depth within step
//
//      scene.geometries.add(
//              new Sphere(sphereRadius, new Point(x, y, z))
//                      .setEmission(sphereColors[i])
//                      .setMaterial(sphereMaterial)
//      );
//   }
//
//   // Add a couple of spheres on the floor (like in the image)
//   scene.geometries.add(
//           new Sphere(12, new Point(-180, -8, -15))
//                   .setEmission(new Color(150, 45, 45))
//                   .setMaterial(sphereMaterial)
//   );
//
//   scene.geometries.add(
//           new Sphere(10, new Point(-200, -10, 10))
//                   .setEmission(new Color(45, 45, 130))
//                   .setMaterial(sphereMaterial)
//   );
//
//   // === הוספת פירמידה מלאה לפני המדרגות במקום משולש ===
//   // נקודות הבסיס של הפירמידה (ריבוע)
//// נקודות הבסיס של הפירמידה (ריבוע) - מעבירים קרוב למצלמה
//   Point pyramidBase1 = new Point(-180, -20, -15);
//   Point pyramidBase2 = new Point(-130, -20, -15);
//   Point pyramidBase3 = new Point(-130, -20, 15);
//   Point pyramidBase4 = new Point(-180, -20, 15);
//   Point pyramidApex = new Point(-155, 25, 0);
//
//   Material pyramidMaterial = new Material()
//           .setKD(0.2).setKS(0.8).setShininess(130)
//           .setKT(0.5).setKR(0.4);
//
//   Color pyramidColor = new Color(7, 2, 6);
//
//   // בסיס הפירמידה
//   scene.geometries.add(
//           new Polygon(pyramidBase1, pyramidBase2, pyramidBase3, pyramidBase4)
//                   .setEmission(pyramidColor.scale(0.8))
//                   .setMaterial(pyramidMaterial)
//   );
//
//   // פאות הפירמידה
//   scene.geometries.add(
//           new Triangle(pyramidBase1, pyramidBase2, pyramidApex)
//                   .setEmission(pyramidColor)
//                   .setMaterial(pyramidMaterial)
//   );
//
//   scene.geometries.add(
//           new Triangle(pyramidBase2, pyramidBase3, pyramidApex)
//                   .setEmission(pyramidColor.scale(1.1))
//                   .setMaterial(pyramidMaterial)
//   );
//
//   scene.geometries.add(
//           new Triangle(pyramidBase3, pyramidBase4, pyramidApex)
//                   .setEmission(pyramidColor)
//                   .setMaterial(pyramidMaterial)
//   );
//
//   scene.geometries.add(
//           new Triangle(pyramidBase4, pyramidBase1, pyramidApex)
//                   .setEmission(pyramidColor.scale(0.9))
//                   .setMaterial(pyramidMaterial)
//   );
//
//   // === הוספת צילינדר בצבע תכלת מטאלי לפני המדרגות ===
//   scene.geometries.add(
//           new Cylinder(new Ray(new Point(-110, -20, 0), new Vector(0, 1, 0)), 18d, 45d)
//                   .setEmission(new Color(2, 150, 160))
//                   .setMaterial(new Material()
//                           .setKD(0.2).setKS(0.8).setShininess(150)
//                           .setKR(0.7))
//   );
//
//   // --- Room walls (dark to not distract from stairs) ---
//   scene.geometries.add(
//           new Polygon(
//                   new Point(-250, -20, -100),
//                   new Point(-250, -20, 100),
//                   new Point(-250, 250, 100),
//                   new Point(-250, 250, -100))
//                   .setEmission(wallColor)
//                   .setMaterial(wallMaterial)
//   );
//   scene.geometries.add(
//           new Polygon(
//                   new Point(-250, -20, 100),
//                   new Point(200, -20, 100),
//                   new Point(200, 250, 100),
//                   new Point(-250, 250, 100))
//                   .setEmission(wallColor)
//                   .setMaterial(wallMaterial)
//   );
//   scene.geometries.add(
//           new Plane(new Point(0, -20, 0), new Vector(0, 1, 0))
//                   .setEmission(floorColor)
//                   .setMaterial(floorMaterial)
//   );
//
//   // === Soft, balanced lighting setup like in the image ===
//
//   // Main soft spotlight - רך ומאוזן
//   scene.light.add(
//           new SpotLight(
//                   new Color(300, 280, 250), // Soft warm light
//                   new Point(-150, 150, -60),
//                   new Vector(1, -1, 0.5).normalize())
//                   .setKl(0.0005).setKq(0.00001)
//                   .setNarrowBeam(35)
//   );
//
//   // Secondary fill light - אור מילוי עדין
//   scene.light.add(
//           new SpotLight(
//                   new Color(180, 160, 140), // Softer fill
//                   new Point(50, 100, -40),
//                   new Vector(-0.8, -0.6, 0.3).normalize())
//                   .setKl(0.001).setKq(0.00002)
//                   .setNarrowBeam(45)
//   );
//
//   // Directional light for even softer shadows - אור כיווני עדין
//   scene.light.add(
//           new DirectionalLight(
//                   new Color(40, 45, 50), // Very soft blue-gray
//                   new Vector(0.3, -0.7, -0.6))
//   );
//
//   // Point light for sphere highlighting - נקודת אור לכדורים
//   scene.light.add(
//           new PointLight(
//                   new Color(120, 110, 100), // Gentle point light
//                   new Point(-100, 80, 20))
//                   .setKl(0.002).setKq(0.0001)
//   );
//
//   // === Camera setup ===
//   Camera.getBuilder()
//           .setLocation(new Point(-400, 80, -700)) // Adjusted for better angle
//           .setDirection(new Point(-50, 60, 0), Vector.AXIS_Y) // Looking toward the stairs
//           .setVpDistance(1000)
//           .setVpSize(400, 400)
//           .setResolution(1000, 1000) // Higher resolution for better quality
//           .setMultithreading(8)
//           .setDebugPrint(5)
//           .setRayTracer(scene, RayTracerType.SIMPLE)
//           .build()
//           .renderImage()
//           .writeToImage("pyramid_and_turquoise_cylinder_staircase");
//}
//
//   /**
//    * Helper method to add a base structure under the stairs
//    */
//   private void addBaseStructure(Scene scene, double minX, double minY, double minZ,
//                                 double maxX, double maxY, double maxZ,
//                                 Color color, Material material) {
//      scene.geometries.add(
//              new Polygon(
//                      new Point(minX, minY, minZ),
//                      new Point(maxX, minY, minZ),
//                      new Point(maxX, maxY, minZ),
//                      new Point(minX, maxY, minZ))
//                      .setEmission(color)
//                      .setMaterial(material)
//      );
//      scene.geometries.add(
//              new Polygon(
//                      new Point(minX, minY, maxZ),
//                      new Point(maxX, minY, maxZ),
//                      new Point(maxX, maxY, maxZ),
//                      new Point(minX, maxY, maxZ))
//                      .setEmission(color)
//                      .setMaterial(material)
//      );
//      scene.geometries.add(
//              new Polygon(
//                      new Point(minX, minY, minZ),
//                      new Point(minX, minY, maxZ),
//                      new Point(minX, maxY, maxZ),
//                      new Point(minX, maxY, minZ))
//                      .setEmission(color)
//                      .setMaterial(material)
//      );
//      scene.geometries.add(
//              new Polygon(
//                      new Point(maxX, minY, minZ),
//                      new Point(maxX, minY, maxZ),
//                      new Point(maxX, maxY, maxZ),
//                      new Point(maxX, maxY, minZ))
//                      .setEmission(color)
//                      .setMaterial(material)
//      );
//      scene.geometries.add(
//              new Polygon(
//                      new Point(minX, minY, minZ),
//                      new Point(maxX, minY, minZ),
//                      new Point(maxX, minY, maxZ),
//                      new Point(minX, minY, maxZ))
//                      .setEmission(color)
//                      .setMaterial(material)
//      );
//      scene.geometries.add(
//              new Polygon(
//                      new Point(minX, maxY, minZ),
//                      new Point(maxX, maxY, minZ),
//                      new Point(maxX, maxY, maxZ),
//                      new Point(minX, maxY, maxZ))
//                      .setEmission(color)
//                      .setMaterial(material)
//      );
//   }
@Test
void minimalisticStaircaseWithSpotlightTest1() {
   // === Set scene background and ambient light - MUCH DARKER ===
   scene.setBackground(new Color(1, 1, 1)); // Very dark background

   // === Create materials ===
   // MATTE stairs material - not shiny
   Material stairMaterial = new Material()
           .setKD(0.9)     // High diffuse = matte
           .setKS(0.05)    // Low specular = no shine
           .setShininess(1) // Very low shininess = rough surface
           .setKR(0.0);    // No reflection = not shiny

   Material wallMaterial = new Material()
           .setKD(0.8).setKS(0.2).setShininess(30)
           .setKT(0.4).setKR(0.05);

   Material floorMaterial = new Material()
           .setKD(0.6).setKS(0.4).setShininess(80)
           .setKT(0.5).setKR(0.2);

   // Sphere materials - HIGHLY REFLECTIVE like mirrors
   Material sphereMaterial = new Material()
           .setKD(0.1).setKS(0.2).setShininess(500)
           .setKR(0.95);  // Very high reflection like the example

   double stepWidth = 30;
   double stepHeight = 15;
   double stepDepth = 45;

   Color stairColor = new Color(140, 130, 110); // Lighter, more neutral stairs
   Color wallColor = new Color(40, 45, 55); // Dark blue-gray for walls
   Color floorColor = new Color(35, 40, 50); // Dark blue-gray floor

   // --- Base structure under the stairs ---
   addBaseStructure1(scene, -150, 0, -stepDepth/2, 150, -20, stepDepth/2, stairColor, stairMaterial);

   // --- פירמידה גדולה ליד הצילינדר בצד שמאל-למטה של התמונה, ליד המדרגות ---
   Point pyramidBase1 = new Point(120, -100, -80);
   Point pyramidBase2 = new Point(165, -100, -80);
   Point pyramidBase3 = new Point(165, -100, -30);
   Point pyramidBase4 = new Point(120, -100, -30);
   Point pyramidApex = new Point(42.5, 35, -55); // גבוה ומשמעותי

   Material pyramidMaterial = new Material()
           .setKD(0.2).setKS(0.8).setShininess(130)
           .setKT(0.5).setKR(0.4);

   Color pyramidColor = new Color(7, 2, 6);

   // בסיס הפירמידה (ריבוע)
   scene.geometries.add(
           new Polygon(pyramidBase1, pyramidBase2, pyramidBase3, pyramidBase4)
                   .setEmission(pyramidColor.scale(0.8))
                   .setMaterial(pyramidMaterial)
   );

   // פאות הפירמידה (ארבעה משולשים)
   scene.geometries.add(
           new Triangle(pyramidBase1, pyramidBase2, pyramidApex)
                   .setEmission(pyramidColor)
                   .setMaterial(pyramidMaterial)
   );
   scene.geometries.add(
           new Triangle(pyramidBase2, pyramidBase3, pyramidApex)
                   .setEmission(pyramidColor.scale(1.1))
                   .setMaterial(pyramidMaterial)
   );
   scene.geometries.add(
           new Triangle(pyramidBase3, pyramidBase4, pyramidApex)
                   .setEmission(pyramidColor)
                   .setMaterial(pyramidMaterial)
   );
   scene.geometries.add(
           new Triangle(pyramidBase4, pyramidBase1, pyramidApex)
                   .setEmission(pyramidColor.scale(0.9))
                   .setMaterial(pyramidMaterial)
   );

   // --- Side and back walls of staircase structure ---
   scene.geometries.add(
           new Polygon(
                   new Point(-150, -20, -stepDepth/2),
                   new Point(-150, -20, stepDepth/2),
                   new Point(-150, 10*stepHeight, stepDepth/2),
                   new Point(-150, 10*stepHeight, -stepDepth/2))
                   .setEmission(stairColor)
                   .setMaterial(stairMaterial)
   );
   scene.geometries.add(
           new Polygon(
                   new Point(150, -20, -stepDepth/2),
                   new Point(150, -20, stepDepth/2),
                   new Point(150, 10*stepHeight, stepDepth/2),
                   new Point(150, 10*stepHeight, -stepDepth/2))
                   .setEmission(stairColor)
                   .setMaterial(stairMaterial)
   );
   scene.geometries.add(
           new Polygon(
                   new Point(-150, -20, stepDepth/2),
                   new Point(150, -20, stepDepth/2),
                   new Point(150, 10*stepHeight, stepDepth/2),
                   new Point(-150, 10*stepHeight, stepDepth/2))
                   .setEmission(stairColor)
                   .setMaterial(stairMaterial)
   );
   scene.geometries.add(
           new Polygon(
                   new Point(-150, -20, -stepDepth/2),
                   new Point(150, -20, -stepDepth/2),
                   new Point(150, 0, -stepDepth/2),
                   new Point(-150, 0, -stepDepth/2))
                   .setEmission(stairColor)
                   .setMaterial(stairMaterial)
   );
   scene.geometries.add(
           new Polygon(
                   new Point(-250, 250, -100),   // פינה שמאלית אחורית למעלה
                   new Point(200, 250, -100),    // פינה ימנית אחורית למעלה
                   new Point(200, 250, 100),     // פינה ימנית קדמית למעלה
                   new Point(-250, 250, 100))    // פינה שמאלית קדמית למעלה
                   .setEmission(wallColor)
                   .setMaterial(wallMaterial)
   );

   // --- Stairs ---
   for (int i = 0; i < 10; i++) {
      double x = -150 + i * stepWidth;
      double y = i * stepHeight;

      // Horizontal step
      scene.geometries.add(
              new Polygon(
                      new Point(x, y, -stepDepth/2),
                      new Point(x + stepWidth, y, -stepDepth/2),
                      new Point(x + stepWidth, y, stepDepth/2),
                      new Point(x, y, stepDepth/2))
                      .setEmission(stairColor)
                      .setMaterial(stairMaterial)
      );
      // Vertical riser
      if (i < 9) {
         scene.geometries.add(
                 new Polygon(
                         new Point(x + stepWidth, y, -stepDepth/2),
                         new Point(x + stepWidth, y + stepHeight, -stepDepth/2),
                         new Point(x + stepWidth, y + stepHeight, stepDepth/2),
                         new Point(x + stepWidth, y, stepDepth/2))
                         .setEmission(stairColor)
                         .setMaterial(stairMaterial)
         );
      }
      // Side/back/under fills for full enclosure
      if (i > 0) {
         scene.geometries.add(
                 new Polygon(
                         new Point(x, y - stepHeight, stepDepth/2),
                         new Point(x, y, stepDepth/2),
                         new Point(x, y, -stepDepth/2),
                         new Point(x, y - stepHeight, -stepDepth/2))
                         .setEmission(stairColor)
                         .setMaterial(stairMaterial)
         );
         scene.geometries.add(
                 new Polygon(
                         new Point(x, y - stepHeight, -stepDepth/2),
                         new Point(x + stepWidth, y - stepHeight, -stepDepth/2),
                         new Point(x + stepWidth, y - stepHeight, stepDepth/2),
                         new Point(x, y - stepHeight, stepDepth/2))
                         .setEmission(stairColor)
                         .setMaterial(stairMaterial)
         );
         //############################################
         scene.geometries.add(
                 new Polygon(
                         new Point(200, -20, -100),   // פינה ימנית אחורית למטה
                         new Point(200, -20, 100),    // פינה ימנית קדמית למטה
                         new Point(200, 250, 100),    // פינה ימנית קדמית למעלה
                         new Point(200, 250, -100)    // פינה ימנית אחורית למעלה
                 )
                         .setEmission(wallColor)
                         .setMaterial(wallMaterial)
         );
         //#############################################333
      }
   }

   // === For VIBRANT colorful spheres ===
   Color[] sphereColors = {
           new Color(50, 50, 120),     // Very vibrant blue
           new Color(100, 100, 70),    // Very bright gold
           new Color(90, 30, 150),     // Very rich purple
           new Color(50, 120, 50),     // Very bright green
           new Color(150, 90, 30),     // Very vibrant orange
           new Color(120, 120, 30),    // Very bright yellow
           new Color(150, 30, 90),     // Very vibrant magenta
           new Color(30, 90, 150),     // Very bright cyan
           new Color(90, 150, 30),     // Very vibrant lime
           new Color(120, 60, 90)      // Very bright pink
   };

   // Colorful light colors - REDUCED for darker scene
   Color[] lightColors = {
           new Color(20, 20, 35),    // Blue light - reduced
           new Color(90, 90, 60),    // Metallic light - reduced
           new Color(75, 25, 100),   // Purple light - reduced
           new Color(40, 75, 40),    // Green light - reduced
           new Color(100, 60, 25),   // Bronze/orange light - reduced
           new Color(100, 100, 40),  // Yellow light - reduced
           new Color(100, 25, 60),   // Magenta light - reduced
           new Color(25, 60, 100),   // Cyan light - reduced
           new Color(60, 100, 25),   // Lime light - reduced
           new Color(90, 40, 60)     // Pink light - reduced
   };

   double sphereRadius = 8;

   // Add spheres on each step WITH colorful lights nearby
   Random random = new Random(42); // fixed seed for consistent results
   for (int i = 0; i < 10; i++) {
      double baseX = -150 + i * stepWidth;
      double y = i * stepHeight + sphereRadius; // On top of step

      // Random position within the step boundaries
      double x = baseX + stepWidth * (0.2 + random.nextDouble() * 0.6); // 20%-80% of step width
      double z = (random.nextDouble() - 0.5) * stepDepth * 0.6; // random depth within step

      // Add the sphere
      scene.geometries.add(
              new Sphere(sphereRadius, new Point(x, y, z))
                      .setEmission(sphereColors[i])
                      .setMaterial(sphereMaterial)  // Using the highly reflective material
      );

      // Add colorful point light near each sphere - WITH MORE ATTENUATION
      scene.light.add(
              new PointLight(
                      lightColors[i], // Matching colorful light
                      new Point(x - 15, y + 25, z + 10)) // Positioned above and to the side of sphere
                      .setKl(0.008).setKq(0.0008) // Increased attenuation
      );

      // Add secondary smaller light for extra color effect - WITH MORE ATTENUATION
      scene.light.add(
              new PointLight(
                      lightColors[i].scale(0.6), // Dimmer version of the same color
                      new Point(x + 10, y + 15, z - 8)) // Different angle
                      .setKl(0.012).setKq(0.0012) // Increased attenuation
      );
   }

   // Add a couple of spheres on the floor WITH their colorful lights
   scene.geometries.add(
           new Sphere(12, new Point(-180, -8, -15))
                   .setEmission(new Color(25, 5, 5))  // Dark red
                   .setMaterial(sphereMaterial)       // Highly reflective
   );
   // Red light for floor sphere
   scene.light.add(
           new PointLight(
                   new Color(100, 25, 25), // Reduced red light
                   new Point(-165, 15, -5)) // Above the red sphere
                   .setKl(0.004).setKq(0.0002)
   );

   scene.geometries.add(
           new Sphere(10, new Point(-200, -10, 10))
                   .setEmission(new Color(5, 5, 25))  // Dark blue
                   .setMaterial(sphereMaterial)       // Highly reflective
   );
   // Blue light for floor sphere
   scene.light.add(
           new PointLight(
                   new Color(25, 25, 100), // Reduced blue light
                   new Point(-185, 12, 20)) // Above the blue sphere
                   .setKl(0.004).setKq(0.0002)
   );

   // --- Room walls (dark to not distract from stairs) ---
   scene.geometries.add(
           new Polygon(
                   new Point(-250, -20, -100),
                   new Point(-250, -20, 100),
                   new Point(-250, 250, 100),
                   new Point(-250, 250, -100))
                   .setEmission(wallColor)
                   .setMaterial(wallMaterial)
   );
   scene.geometries.add(
           new Polygon(
                   new Point(-250, -20, 100),
                   new Point(200, -20, 100),
                   new Point(200, 250, 100),
                   new Point(-250, 250, 100))
                   .setEmission(wallColor)
                   .setMaterial(wallMaterial)
   );
   scene.geometries.add(
           new Plane(new Point(0, -20, 0), new Vector(0, 1, 0))
                   .setEmission(floorColor)
                   .setMaterial(floorMaterial)
   );

   // === Enhanced lighting for better reflections - MINIMAL LIGHTING ===

   // Main soft spotlight - minimal
   scene.light.add(
           new SpotLight(
                   new Color(3, 3, 2), // Very minimal
                   new Point(-150, 150, -60),
                   new Vector(1, -1, 0.5).normalize())
                   .setKl(0.015).setKq(0.001) // Strong attenuation
                   .setNarrowBeam(35)
   );

   // Secondary fill light - almost nothing
   scene.light.add(
           new SpotLight(
                   new Color(1, 1, 1), // Almost nothing
                   new Point(50, 100, -40),
                   new Vector(-0.8, -0.6, 0.3).normalize())
                   .setKl(0.02).setKq(0.002) // Extreme attenuation
                   .setNarrowBeam(45)
   );

   // Directional light - minimal
   scene.light.add(
           new DirectionalLight(
                   new Color(1, 1, 1), // Almost nothing
                   new Vector(0.3, -0.7, -0.6))
   );

   // === Camera setup ===
   Camera.getBuilder()
           .setLocation(new Point(-600, 80, -720)) // Adjusted for better angle
           .setDirection(new Point(-50, 60, 0), Vector.AXIS_Y) // Looking toward the stairs
           .setVpDistance(1000)
           .setVpSize(400, 400)
           .setResolution(1000, 1000) // Higher resolution for better quality
           .setMultithreading(8)
           .setDebugPrint(5)
           .setRayTracer(scene, RayTracerType.SIMPLE)
           .build()
           .renderImage()
           .writeToImage("colorful_staircase_with_colorful_lights_moreDark");
}

   /**
    * Helper method to add a base structure under the stairs,
    * וגם צילינדר גדול בצד שמאל-למטה של התמונה, ליד המדרגות.
    */
   private void addBaseStructure1(Scene scene, double minX, double minY, double minZ,
                                  double maxX, double maxY, double maxZ,
                                  Color color, Material material) {
      scene.geometries.add(
              new Polygon(
                      new Point(minX, minY, minZ),
                      new Point(maxX, minY, minZ),
                      new Point(maxX, maxY, minZ),
                      new Point(minX, maxY, minZ))
                      .setEmission(color)
                      .setMaterial(material)
      );
      scene.geometries.add(
              new Polygon(
                      new Point(minX, minY, maxZ),
                      new Point(maxX, minY, maxZ),
                      new Point(maxX, maxY, maxZ),
                      new Point(minX, maxY, maxZ))
                      .setEmission(color)
                      .setMaterial(material)
      );
      scene.geometries.add(
              new Polygon(
                      new Point(minX, minY, minZ),
                      new Point(minX, minY, maxZ),
                      new Point(minX, maxY, maxZ),
                      new Point(minX, maxY, minZ))
                      .setEmission(color)
                      .setMaterial(material)
      );
      scene.geometries.add(
              new Polygon(
                      new Point(maxX, minY, minZ),
                      new Point(maxX, minY, maxZ),
                      new Point(maxX, maxY, maxZ),
                      new Point(maxX, maxY, minZ))
                      .setEmission(color)
                      .setMaterial(material)
      );
      scene.geometries.add(
              new Polygon(
                      new Point(minX, minY, minZ),
                      new Point(maxX, minY, minZ),
                      new Point(maxX, minY, maxZ),
                      new Point(minX, minY, maxZ))
                      .setEmission(color)
                      .setMaterial(material)
      );
      scene.geometries.add(
              new Polygon(
                      new Point(minX, maxY, minZ),
                      new Point(maxX, maxY, minZ),
                      new Point(maxX, maxY, maxZ),
                      new Point(minX, maxY, maxZ))
                      .setEmission(color)
                      .setMaterial(material)
      );
      // צילינדר גדול בצד שמאל-למטה של התמונה, ליד המדרגות
      scene.geometries.add(
              new Cylinder(new Ray(new Point(60, -30, -55), new Vector(0, 1, 0)), 15d, 80d)
                      .setEmission(new Color(2, 150, 160))
                      .setMaterial(new Material()
                              .setKD(0.2).setKS(0.8).setShininess(150)
                              .setKR(0.7))
      );
   }
}

