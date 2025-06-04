package renderer;

import static java.awt.Color.*;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;

/**
 * Tests for reflection and transparency functionality, test for partial
 * shadows
 * (with transparency)
 * @author Dan Zilberstein
 */
class ReflectionRefractionTests {
   /** Default constructor to satisfy JavaDoc generator */
   ReflectionRefractionTests() { /* to satisfy JavaDoc generator */ }

   /** Scene for the tests */
   private final Scene          scene         = new Scene("Test scene");
   /** Camera builder for the tests with triangles */
   private final Camera.Builder cameraBuilder = Camera.getBuilder()     //
           .setRayTracer(scene, RayTracerType.SIMPLE);

   /** Produce a picture of a sphere lighted by a spot light */
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

   /** Produce a picture of a sphere lighted by a spot light */
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
    * Produce a picture of a two triangles lighted by a spot light with a
    * partially
    * transparent Sphere producing partial shadow
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


   @Test
   void etherealGallery() {
      // === כדורים צבעוניים עם שקיפות ===

      // כדור כחול מרכזי - שקוף עם בהירות גבוהה
      scene.geometries.add(
              new Sphere(35d, new Point(0, 25, -120))
                      .setEmission(new Color(3, 5, 8))
                      .setMaterial(new Material()
                              .setKD(0.2).setKS(0.8).setShininess(150)
                              .setKT(0.7).setKR(0.3))
      );

      // כדור אדום - רובי עם זוהר
      scene.geometries.add(
              new Sphere(22d, new Point(-65, 45, -85))
                      .setEmission(new Color(8, 3, 3))
                      .setMaterial(new Material()
                              .setKD(0.3).setKS(0.7).setShininess(120)
                              .setKT(0.5).setKR(0.5))
      );

      // כדור ירוק - אמרלד
      scene.geometries.add(
              new Sphere(18d, new Point(75, 35, -95))
                      .setEmission(new Color(3, 8, 5))
                      .setMaterial(new Material()
                              .setKD(0.25).setKS(0.75).setShininess(110)
                              .setKT(0.6).setKR(0.4))
      );

      // === משולשים משקפים ===

      // משולש מראה גדול - זהב
      scene.geometries.add(
              new Triangle(
                      new Point(-30, 70, -140),
                      new Point(30, 70, -140),
                      new Point(0, 110, -120))
                      .setEmission(new Color(6, 5, 2))
                      .setMaterial(new Material()
                              .setKD(0.1).setKS(0.9).setShininess(200)
                              .setKR(0.8))
      );

      // משולש כסף
      scene.geometries.add(
              new Triangle(
                      new Point(45, 15, -60),
                      new Point(90, 15, -80),
                      new Point(67, 55, -70))
                      .setEmission(new Color(5, 5, 6))
                      .setMaterial(new Material()
                              .setKD(0.1).setKS(0.9).setShininess(180)
                              .setKR(0.7))
      );

      // === פירמידה שקופה ===

      Point pyramidBase1 = new Point(-40, -10, -180);
      Point pyramidBase2 = new Point(40, -10, -180);
      Point pyramidBase3 = new Point(40, -10, -120);
      Point pyramidBase4 = new Point(-40, -10, -120);
      Point pyramidApex = new Point(0, 50, -150);

      Material glassMaterial = new Material()
              .setKD(0.1).setKS(0.9).setShininess(160)
              .setKT(0.8).setKR(0.2);

      // פאות הפירמידה
      scene.geometries.add(
              new Triangle(pyramidBase1, pyramidBase2, pyramidApex)
                      .setEmission(new Color(4, 5, 7))
                      .setMaterial(glassMaterial)
      );

      scene.geometries.add(
              new Triangle(pyramidBase2, pyramidBase3, pyramidApex)
                      .setEmission(new Color(5, 4, 7))
                      .setMaterial(glassMaterial)
      );

      scene.geometries.add(
              new Triangle(pyramidBase3, pyramidBase4, pyramidApex)
                      .setEmission(new Color(7, 5, 4))
                      .setMaterial(glassMaterial)
      );

      scene.geometries.add(
              new Triangle(pyramidBase4, pyramidBase1, pyramidApex)
                      .setEmission(new Color(5, 7, 4))
                      .setMaterial(glassMaterial)
      );

      // === מישורים ===

      // רצפה עם השתקפות קלה
      scene.geometries.add(
              new Plane(new Point(0, -25, 0), new Vector(0, 1, 0))
                      .setEmission(new Color(6, 5, 4))
                      .setMaterial(new Material()
                              .setKD(0.7).setKS(0.3).setShininess(40)
                              .setKR(0.3))
      );

      // קיר אחורי מאט
      scene.geometries.add(
              new Plane(new Point(0, 0, -250), new Vector(0, 0, 1))
                      .setEmission(new Color(3, 2, 4))
                      .setMaterial(new Material()
                              .setKD(0.9).setKS(0.1).setShininess(10))
      );

      // קיר צדדי עם השתקפות
      scene.geometries.add(
              new Plane(new Point(-120, 0, 0), new Vector(1, 0, 0))
                      .setEmission(new Color(4, 5, 6))
                      .setMaterial(new Material()
                              .setKD(0.5).setKS(0.5).setShininess(60)
                              .setKR(0.4))
      );

      // === תאורה ===

      // תאורת סביבה עדינה
      scene.setAmbientLight(new AmbientLight(new Color(1, 1, 2)));

      // אור ספוט ראשי
      scene.light.add(
              new SpotLight(
                      new Color(400, 350, 300),
                      new Point(-100, 150, 50),
                      new Vector(2, -3, -4))
                      .setKl(0.0003).setKq(0.00003)
      );

      // אור נקודתי משני
      scene.light.add(
              new PointLight(
                      new Color(200, 250, 350),
                      new Point(120, 80, -40))
                      .setKl(0.0003).setKq(0.00002)
      );

      // אור כיווני רך
      scene.light.add(
              new DirectionalLight(
                      new Color(30, 40, 50),
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
}