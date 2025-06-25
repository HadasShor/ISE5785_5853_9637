package renderer;

import static java.awt.Color.*;

import org.junit.jupiter.api.Tag;
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
    * Produce a picture of a sphere lighted by a spot light
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
    * Produce a picture of a sphere lighted by a spot light
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

   //סט בסיס עיגול גדול משולש ו 2 קטנים
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


//   @Test
//   void etherealGallery() {
//      // === הגדרת הבסיס של הפירמידה ===
//      Point pyramidBase1 = new Point(-40, -10, -180);
//      Point pyramidBase2 = new Point(40, -10, -180);
//      Point pyramidBase3 = new Point(40, -10, -120);
//      Point pyramidBase4 = new Point(-40, -10, -120);
//      Point pyramidApex = new Point(0, 50, -150);
//
//      // חומר מטאלי ירוק עם יותר גוון ירוק
//      Material metallicGreenMaterial = new Material()
//              .setKD(0.4).setKS(0.9).setShininess(180)
//              .setKT(0.15).setKR(0.6);
//
//      // צבע ירוק יותר עשיר לפירמידה
//      Color brightGreen = new Color(0.5, 5, 1.0);
//
//      // פאות הפירמידה - כולן באותו צבע ירוק מטאלי בהיר
//      scene.geometries.add(
//              new Triangle(pyramidBase1, pyramidBase2, pyramidApex)
//                      .setEmission(brightGreen)
//                      .setMaterial(metallicGreenMaterial)
//      );
//
//      scene.geometries.add(
//              new Triangle(pyramidBase2, pyramidBase3, pyramidApex)
//                      .setEmission(brightGreen)
//                      .setMaterial(metallicGreenMaterial)
//      );
//
//      scene.geometries.add(
//              new Triangle(pyramidBase3, pyramidBase4, pyramidApex)
//                      .setEmission(brightGreen)
//                      .setMaterial(metallicGreenMaterial)
//      );
//
//      scene.geometries.add(
//              new Triangle(pyramidBase4, pyramidBase1, pyramidApex)
//                      .setEmission(brightGreen)
//                      .setMaterial(metallicGreenMaterial)
//      );
//
//      // === הוספת יהלומים (כדורים מבריקים) ===
//
//      // חומר יהלום - מבריק מאוד עם שקיפות והשתקפות גבוהות
//      Material diamondMaterial = new Material()
//              .setKD(0.05).setKS(0.95).setShininess(300)
//              .setKT(0.6).setKR(0.8);
//
//      // צבע יהלום מבריק
//      Color diamondColor = new Color(8, 8, 8);
//
//      // נקודות הכדורים הקטנים מימין לפירמידה
//      Point sphere1Center = new Point(60, 10, -140);
//      Point sphere2Center = new Point(85, 5, -160);
//
//      // שני כדורים קטנים מימין לפירמידה
//      scene.geometries.add(
//              new Sphere(10d, sphere1Center)
//                      .setEmission(diamondColor)
//                      .setMaterial(diamondMaterial)
//      );
//
//      scene.geometries.add(
//              new Sphere(8d, sphere2Center)
//                      .setEmission(diamondColor)
//                      .setMaterial(diamondMaterial)
//      );
//
//      // === קופסה שחורה מבריקה במקום גליל ===
//
//      // חומר שחור מבריק לקופסה
//      Material shinyBlackMaterial = new Material()
//              .setKD(0.1).setKS(0.9).setShininess(200)
//              .setKT(0.0).setKR(0.7);
//
//      Color blackColor = new Color(0.1, 0.1, 0.1);
//
//      // חישוב נקודות לקופסה דקה שמחברת בין הכדורים
//      // וקטור כיוון בין מרכזי הכדורים
//      Vector direction = sphere2Center.subtract(sphere1Center);
//
//      // וקטורים מאונכים לכיוון
//      Vector up = new Vector(0, 1, 0);
//      Vector side;
//
//      // אם הכיוון כמעט מקביל לווקטור למעלה, נבחר וקטור צד אחר
//      if (Math.abs(direction.dotProduct(up)) > 0.9 * direction.length() * up.length()) {
//         side = new Vector(1, 0, 0);
//      } else {
//         // אחרת, ניצור וקטור צד מאונך לכיוון ולמעלה
//         side = direction.crossProduct(up).normalize();
//      }
//
//      // וקטור למעלה חדש, מאונך לכיוון ולצד
//      up = side.crossProduct(direction).normalize();
//
//      // גודל הקופסה
//      double width = 3;
//      double height = 3;
//
//      // יצירת נקודות הקופסה
//      Vector widthVector = side.scale(width / 2);
//      Vector heightVector = up.scale(height / 2);
//
//      // נקודות בכדור הראשון
//      Point p1Near = sphere1Center.add(direction.normalize().scale(8));  // קצת פנימה מהכדור הראשון
//      Point p1Bottom = p1Near.add(widthVector.scale(-1)).add(heightVector.scale(-1));
//      Point p1Top = p1Near.add(widthVector.scale(-1)).add(heightVector);
//      Point p1Right = p1Near.add(widthVector).add(heightVector.scale(-1));
//      Point p1TopRight = p1Near.add(widthVector).add(heightVector);
//
//      // נקודות בכדור השני
//      Point p2Near = sphere2Center.add(direction.normalize().scale(-6)); // קצת פנימה מהכדור השני
//      Point p2Bottom = p2Near.add(widthVector.scale(-1)).add(heightVector.scale(-1));
//      Point p2Top = p2Near.add(widthVector.scale(-1)).add(heightVector);
//      Point p2Right = p2Near.add(widthVector).add(heightVector.scale(-1));
//      Point p2TopRight = p2Near.add(widthVector).add(heightVector);
//

   /// /      // פאות הקופסה (6 פאות מורכבות מ-12 משולשים)
   /// /
   /// /      // פאה תחתונה
   /// /      scene.geometries.add(new Triangle(p1Bottom, p2Bottom, p1Right)
   /// /              .setEmission(blackColor).setMaterial(shinyBlackMaterial));
   /// /      scene.geometries.add(new Triangle(p1Right, p2Bottom, p2Right)
   /// /              .setEmission(blackColor).setMaterial(shinyBlackMaterial));
   /// /
   /// /      // פאה עליונה
   /// /      scene.geometries.add(new Triangle(p1Top, p1TopRight, p2Top)
   /// /              .setEmission(blackColor).setMaterial(shinyBlackMaterial));
   /// /      scene.geometries.add(new Triangle(p1TopRight, p2TopRight, p2Top)
   /// /              .setEmission(blackColor).setMaterial(shinyBlackMaterial));
   /// /
   /// /      // פאה שמאלית
   /// /      scene.geometries.add(new Triangle(p1Bottom, p1Top, p2Bottom)
   /// /              .setEmission(blackColor).setMaterial(shinyBlackMaterial));
   /// /      scene.geometries.add(new Triangle(p1Top, p2Top, p2Bottom)
   /// /              .setEmission(blackColor).setMaterial(shinyBlackMaterial));
   /// /
   /// /      // פאה ימנית
   /// /      scene.geometries.add(new Triangle(p1Right, p2Right, p1TopRight)
   /// /              .setEmission(blackColor).setMaterial(shinyBlackMaterial));
   /// /      scene.geometries.add(new Triangle(p1TopRight, p2Right, p2TopRight)
   /// /              .setEmission(blackColor).setMaterial(shinyBlackMaterial));
   /// /
   /// /      // פאה קדמית (בכדור הראשון)
   /// /      scene.geometries.add(new Triangle(p1Bottom, p1Right, p1Top)
   /// /              .setEmission(blackColor).setMaterial(shinyBlackMaterial));
   /// /      scene.geometries.add(new Triangle(p1Right, p1TopRight, p1Top)
   /// /              .setEmission(blackColor).setMaterial(shinyBlackMaterial));
   /// /
   /// /      // פאה אחורית (בכדור השני)
   /// /      scene.geometries.add(new Triangle(p2Bottom, p2Top, p2Right)
   /// /              .setEmission(blackColor).setMaterial(shinyBlackMaterial));
   /// /      scene.geometries.add(new Triangle(p2Top, p2TopRight, p2Right)
   /// /              .setEmission(blackColor).setMaterial(shinyBlackMaterial));
//
//      // כדור אחד משמאל לפירמידה - ממוקם יותר מקדימה
//      scene.geometries.add(
//              new Sphere(30d, new Point(-65, 15, -75))
//                      .setEmission(diamondColor)
//                      .setMaterial(diamondMaterial)
//      );
//
//      // === מישורים - עוד פחות מראתיים ורקע בהיר יותר ===
//
//      // רצפה עם מינימום השתקפות ובהירה יותר
//      scene.geometries.add(
//              new Plane(new Point(0, -25, 0), new Vector(0, 1, 0))
//                      .setEmission(new Color(7, 6, 5)) // בהיר יותר
//                      .setMaterial(new Material()
//                              .setKD(0.95).setKS(0.15).setShininess(20)
//                              .setKR(0.08)) // עוד פחות השתקפות
//      );
//
//      // קיר אחורי מאט בהיר יותר
//      scene.geometries.add(
//              new Plane(new Point(0, 0, -250), new Vector(0, 0, 1))
//                      .setEmission(new Color(4, 3, 5)) // בהיר יותר
//                      .setMaterial(new Material()
//                              .setKD(0.98).setKS(0.02).setShininess(5)
//                              .setKR(0.02)) // כמעט ללא השתקפות
//      );
//
//      // קיר צדדי בהיר יותר וללא השתקפות כמעט
//      scene.geometries.add(
//              new Plane(new Point(-120, 0, 0), new Vector(1, 0, 0))
//                      .setEmission(new Color(5, 6, 7)) // בהיר יותר
//                      .setMaterial(new Material()
//                              .setKD(0.9).setKS(0.2).setShininess(30)
//                              .setKR(0.1)) // מינימום השתקפות
//      );
//
//      // === תאורה ===
//
//      // תאורת סביבה עדינה
//      scene.setAmbientLight(new AmbientLight(new Color(0.6, 0.6, 1.1))); // מעט בהיר יותר
//
//      // אור ספוט ראשי
//      scene.light.add(
//              new SpotLight(
//                      new Color(250, 200, 150),
//                      new Point(-100, 150, 50),
//                      new Vector(2, -3, -4))
//                      .setKl(0.0003).setKq(0.00003)
//      );
//
//      // אור נקודתי משני
//      scene.light.add(
//              new PointLight(
//                      new Color(100, 150, 200),
//                      new Point(120, 80, -40))
//                      .setKl(0.0003).setKq(0.00002)
//      );
//
//      // אור כיווני רך
//      scene.light.add(
//              new DirectionalLight(
//                      new Color(15, 20, 25),
//                      new Vector(-0.2, -0.6, -1))
//      );
//
//      // === הגדרת המצלמה ===
//      cameraBuilder
//              .setLocation(new Point(80, 60, 150))
//              .setDirection(new Point(-10, 25, -100), Vector.AXIS_Y)
//              .setVpDistance(200)
//              .setVpSize(240, 240)
//              .setResolution(800, 800)
//              .build()
//              .renderImage()
//              .writeToImage("etherealGallery");
//   }


//   // פאות הפירמידה - כולן באותו צבע ירוק מטאלי בהיר
//   scene.geometries.add(
//           new Triangle(pyramidBase1, pyramidBase2, pyramidApex)
//                   .setEmission(brightGreen)
//                   .setMaterial(metallicGreenMaterial)
//   );
//
//   scene.geometries.add(
//           new Triangle(pyramidBase2, pyramidBase3, pyramidApex)
//                   .setEmission(brightGreen)
//                   .setMaterial(metallicGreenMaterial)
//   );
//
//   scene.geometries.add(
//           new Triangle(pyramidBase3, pyramidBase4, pyramidApex)
//                   .setEmission(brightGreen)
//                   .setMaterial(metallicGreenMaterial)
//   );
//
//   scene.geometries.add(
//           new Triangle(pyramidBase4, pyramidBase1, pyramidApex)
//                   .setEmission(brightGreen)
//                   .setMaterial(metallicGreenMaterial)
//   );
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
//
//   // === פירמידה ירוקה ===
//   scene.geometries.add(
//           new Triangle(pyramidBase1, pyramidBase2, pyramidApex)
//                   .setEmission(brightGreen)
//                   .setMaterial(metallicGreenMaterial)
//   );
//
//   scene.geometries.add(
//           new Triangle(pyramidBase2, pyramidBase3, pyramidApex)
//                   .setEmission(brightGreen)
//                   .setMaterial(metallicGreenMaterial)
//   );
//
//   scene.geometries.add(
//           new Triangle(pyramidBase3, pyramidBase4, pyramidApex)
//                   .setEmission(brightGreen)
//                   .setMaterial(metallicGreenMaterial)
//   );
//
//   scene.geometries.add(
//           new Triangle(pyramidBase4, pyramidBase1, pyramidApex)
//                   .setEmission(brightGreen)
//                   .setMaterial(metallicGreenMaterial)
//   );

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
    * פונקציה שמוסיפה זוג עיגולים עם מחבר ביניהם
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
    * try the picture with sphere
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


//   @Test
//   void new_test() {
//      // === הגדרת הבסיס של הפירמידה ===
//      Point pyramidBase1 = new Point(-40, -10, -180);
//      Point pyramidBase2 = new Point(40, -10, -180);
//      Point pyramidBase3 = new Point(40, -10, -120);
//      Point pyramidBase4 = new Point(-40, -10, -120);
//      Point pyramidApex = new Point(0, 50, -150);
//
//      // חומר מטאלי ירוק עם יותר גוון ירוק
//      Material metallicGreenMaterial = new Material()
//              .setKD(0.4).setKS(0.9).setShininess(180)
//              .setKT(0.15).setKR(0.6);
//
//      // צבע ירוק יותר עשיר לפירמידה
//      // Color brightGreen = new Color(0.5, 5, 1.0);
//      Color brightGreen = new Color(0, 153, 51);
//
//      // פאות הפירמידה - כולן באותו צבע ירוק מטאלי בהיר
//      scene.geometries.add(
//              new Triangle(pyramidBase1, pyramidBase2, pyramidApex)
//                      .setEmission(brightGreen)
//                      .setMaterial(metallicGreenMaterial)
//      );
//
//      scene.geometries.add(
//              new Triangle(pyramidBase2, pyramidBase3, pyramidApex)
//                      .setEmission(brightGreen)
//                      .setMaterial(metallicGreenMaterial)
//      );
//
//      scene.geometries.add(
//              new Triangle(pyramidBase3, pyramidBase4, pyramidApex)
//                      .setEmission(brightGreen)
//                      .setMaterial(metallicGreenMaterial)
//      );
//
//      scene.geometries.add(
//              new Triangle(pyramidBase4, pyramidBase1, pyramidApex)
//                      .setEmission(brightGreen)
//                      .setMaterial(metallicGreenMaterial)
//      );
//
//      // === הוספת יהלומים (כדורים מבריקים) ===
//
//      // חומר יהלום - מבריק מאוד עם שקיפות והשתקפות גבוהות
//      Material diamondMaterial = new Material()
//              .setKD(0.05).setKS(0.95).setShininess(300)
//              .setKT(0.6).setKR(0.8);
//
//      // צבע יהלום מבריק
//      Color diamondColor = new Color(8, 8, 8);
//
//      // נקודות המרכז של שני הכדורים
//      Point sphere1Center = new Point(60, 10, -140);
//      Point sphere2Center = new Point(85, 5, -160);
//
//      // שני כדורים קטנים מימין לפירמידה
//      scene.geometries.add(
//              new Sphere(10d, sphere1Center)
//                      .setEmission(diamondColor)
//                      .setMaterial(diamondMaterial)
//      );
//
//      scene.geometries.add(
//              new Sphere(8d, sphere2Center)
//                      .setEmission(diamondColor)
//                      .setMaterial(diamondMaterial)
//      );
//
//      // === הוספת גליל בין שני הכדורים ===
//      // יצירת וקטור כיוון מהכדור הראשון לשני
//      Vector cylinderDirection = sphere2Center.subtract(sphere1Center);
//
//      // חומר מטאלי לגליל
//      Material cylinderMaterial = new Material()
//              .setKD(0.2).setKS(0.8).setShininess(100)
//              .setKT(0.3).setKR(0.5);
//
//      // הוספת הגליל בין שני הכדורים
//      scene.geometries.add(
//              new Cylinder(new Ray(sphere1Center, cylinderDirection), 5, cylinderDirection.length())
//                      .setEmission(new Color(120, 120, 200))
//                      .setMaterial(cylinderMaterial)
//      );
//
//      // כדור אחד משמאל לפירמידה - ממוקם יותר מקדימה
//      scene.geometries.add(
//              new Sphere(30d, new Point(-65, 15, -75)) // שיניתי את ערך ה-Z מ -150 ל -50 להזזה קדימה
//                      .setEmission(diamondColor)
//                      .setMaterial(diamondMaterial)
//      );
//
//      // === מישורים - עוד פחות מראתיים ורקע בהיר יותר ===
//
//      // רצפה עם מינימום השתקפות ובהירה יותר
//      scene.geometries.add(
//              new Plane(new Point(0, -25, 0), new Vector(0, 1, 0))
//                      .setEmission(new Color(7, 6, 5)) // בהיר יותר
//                      .setMaterial(new Material()
//                              .setKD(0.95).setKS(0.15).setShininess(20)
//                              .setKR(0.08)) // עוד פחות השתקפות
//      );
//
//      // קיר אחורי מאט בהיר יותר
//      scene.geometries.add(
//              new Plane(new Point(0, 0, -250), new Vector(0, 0, 1))
//                      .setEmission(new Color(4, 3, 5)) // בהיר יותר
//                      .setMaterial(new Material()
//                              .setKD(0.98).setKS(0.02).setShininess(5)
//                              .setKR(0.02)) // כמעט ללא השתקפות
//      );
//
//      // קיר צדדי בהיר יותר וללא השתקפות כמעט
//      scene.geometries.add(
//              new Plane(new Point(-120, 0, 0), new Vector(1, 0, 0))
//                      .setEmission(new Color(5, 6, 7)) // בהיר יותר
//                      .setMaterial(new Material()
//                              .setKD(0.9).setKS(0.2).setShininess(30)
//                              .setKR(0.1)) // מינימום השתקפות
//      );
//
//      // === תאורה ===
//
//      // תאורת סביבה עדינה
//      scene.setAmbientLight(new AmbientLight(new Color(0.6, 0.6, 1.1))); // מעט בהיר יותר
//
//      // אור ספוט ראשי
//      scene.light.add(
//              new SpotLight(
//                      new Color(250, 200, 150),
//                      new Point(-100, 150, 50),
//                      new Vector(2, -3, -4))
//                      .setKl(0.0003).setKq(0.00003)
//      );
//
//      // אור נקודתי משני
//      scene.light.add(
//              new PointLight(
//                      new Color(100, 150, 200),
//                      new Point(120, 80, -40))
//                      .setKl(0.0003).setKq(0.00002)
//      );
//
//      // אור כיווני רך
//      scene.light.add(
//              new DirectionalLight(
//                      new Color(15, 20, 25),
//                      new Vector(-0.2, -0.6, -1))
//      );
//
//      // === הגדרת המצלמה ===
//      cameraBuilder
//              .setLocation(new Point(80, 60, 150))
//              .setDirection(new Point(-10, 25, -100), Vector.AXIS_Y)
//              .setVpDistance(200)
//              .setVpSize(240, 240)
//              .setResolution(800, 800)
//              .build()
//              .renderImage()
//              .writeToImage("new_test");
//   }
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


@Test
void dualChainedYMoleculesTest() {
   // רקע שחור
   scene.setBackground(new Color(0, 0, 0));

   // === חומרים ===
   Material atomMaterial = new Material()
           .setKD(0.05).setKS(0.95).setShininess(1000)  // מבריק מאוד
           .setKT(0.0).setKR(0.9);  // השתקפות גבוהה

   Material bondMaterial = new Material()
           .setKD(0.1).setKS(0.9).setShininess(200)
           .setKT(0.0).setKR(0.7);

   // צבעים - אפור כהה מבריק
   Color atomColor = new Color(40, 40, 40);  // כמעט שחור
   Color bondColor = new Color(150, 150, 150);  // אפור לקשרים

   // רצפה כהה מבריקה
   scene.geometries.add(
           new Plane(new Point(0, -10, 0), new Vector(0, 1, 0))
                   .setEmission(new Color(20, 20, 20))
                   .setMaterial(new Material()
                           .setKD(0.2).setKS(0.8).setShininess(100)
                           .setKR(0.6))  // השתקפות גבוהה
   );

   // === הגדרות הסצנה המקורית ===
   int numberOfMolecules = 5;   // מספר מולקולות בשרשרת
   double atomSize = 6.0;       // גודל האטומים
   double bondThickness = 1.5;  // עובי הקשרים
   double moleculeSpacing = 30.0;  // מרחק בין מולקולות

   // === יצירת שרשרת המולקולות המקורית (רחוקה) ===
   createMoleculeChain(scene, numberOfMolecules, atomSize, bondThickness, moleculeSpacing,
           -100, // ערך Z המקורי
           atomMaterial, atomColor, bondMaterial, bondColor);

   // === יצירת שרשרת מולקולות זהה קרובה יותר למצלמה ===
   createMoleculeChain(scene, numberOfMolecules, atomSize, bondThickness, moleculeSpacing,
           -50, // ערך Z קרוב יותר
           atomMaterial, atomColor, bondMaterial, bondColor);

   // === תאורה ===
   scene.setAmbientLight(new AmbientLight(new Color(0.05, 0.05, 0.05)));

   // אור ספוט חזק מלפנים ומלמעלה
   scene.light.add( // תיקון: lights במקום light
           new SpotLight(
                   new Color(900, 900, 900),
                   new Point(0, 50, 50),
                   new Vector(0, -1, -1))
                   .setKl(0.0001).setKq(0.000005)
   );

   // אור נקודתי מימין להארת הצדדים
   scene.light.add( // תיקון: lights במקום light
           new PointLight(
                   new Color(400, 400, 400),
                   new Point(50, 30, 0))
                   .setKl(0.0002).setKq(0.00002)
   );

   // אור נקודתי משמאל להארת הצדדים השמאליים
   scene.light.add( // תיקון: lights במקום light
           new PointLight(
                   new Color(400, 400, 400),
                   new Point(-50, 30, 0))
                   .setKl(0.0002).setKq(0.00002)
   );

   // אור נקודתי קדמי נוסף להארת השרשרת הקדמית
   scene.light.add(
           new PointLight(
                   new Color(300, 300, 300),
                   new Point(0, 20, -20))
                   .setKl(0.0002).setKq(0.00002)
   );

   // === המצלמה ===
   cameraBuilder
           .setLocation(new Point(-300, 10, 850))  // מצלמה רחוקה
           .setDirection(new Point(0, 0, -100), Vector.AXIS_Y)
           .setVpDistance(1000)
           .setVpSize(200, 100)
           .setResolution(1600, 800)
           .build()
           .renderImage()
           .writeToImage("dual_molecule_chains_right_+");
}

   /**
    * יוצר שרשרת מולקולות שלמה במיקום הרצוי
    */
   private void createMoleculeChain(
           Scene scene,
           int numberOfMolecules,  // מספר מולקולות בשרשרת
           double atomSize,        // גודל האטומים
           double bondThickness,   // עובי הקשרים
           double moleculeSpacing, // מרחק בין מולקולות
           double zPosition,       // מיקום ציר Z של השרשרת
           Material atomMaterial, Color atomColor,
           Material bondMaterial, Color bondColor
   ) {
      // מיקום התחלתי - המולקולה השמאלית ביותר בשרשרת
      double startX = -(numberOfMolecules-1) * moleculeSpacing/2;

      // מערך לשמירת נקודות חיבור בין המולקולות
      Point[] connectionPoints = new Point[numberOfMolecules];

      // יצירת כל המולקולות Y עם נטייה שונה
      for (int i = 0; i < numberOfMolecules; i++) {
         double xPos = startX + i * moleculeSpacing;

         // יצירת זוויות סיבוב שונות לכל מולקולה
         double rotateY = 5 * Math.sin(i * 2.1);      // סיבוב סביב ציר Y
         double rotateX = 8 * Math.cos(i * 1.7 + 1);  // סיבוב סביב ציר X
         double rotateZ = 4 * Math.sin(i * 1.3 + 2);  // סיבוב סביב ציר Z

         // נקודת הבסיס למולקולה הזו
         Point basePoint = new Point(xPos,
                 -5 + Math.sin(i * 0.8) * 3,
                 zPosition + Math.cos(i * 0.9) * 10);

         // יצירת מולקולה Y עם הנטייה המוגדרת
         Point connectionPoint = createRotatedYMolecule(scene, basePoint, atomSize, bondThickness,
                 rotateX, rotateY, rotateZ,
                 atomMaterial, atomColor, bondMaterial, bondColor);

         // שמירת נקודת החיבור למולקולה הבאה
         connectionPoints[i] = connectionPoint;
      }

      // הוספת הקשרים (גלילים) בין המולקולות
      for (int i = 0; i < numberOfMolecules - 1; i++) {
         addBond(scene, connectionPoints[i], connectionPoints[i+1], bondThickness, bondColor, bondMaterial);
      }
   }

   /**
    * יוצר מולקולה בצורת Y עם נטייה מוגדרת
    * מחזיר את נקודת החיבור למולקולה הבאה בשרשרת
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

      // חישוב וקטור מעלה עם הסיבוב המבוקש
      Vector upVector = new Vector(0, 1, 0);
      upVector = rotateVector(upVector, rotateX, rotateY, rotateZ);

      // מרחק אנכי סטנדרטי
      double verticalDistance = 15.0;

      // חישוב מיקום האטום האמצעי
      Point middlePoint = basePoint.add(upVector.scale(verticalDistance));
      scene.geometries.add(
              new Sphere(atomSize, middlePoint)
                      .setEmission(atomColor)
                      .setMaterial(atomMaterial)
      );

      // קשר בין האטום התחתון לאטום האמצעי
      addBond(scene, basePoint, middlePoint, bondThickness, bondColor, bondMaterial);

      // חישוב הוקטורים האלכסוניים עם הסיבוב
      Vector baseRightDiag = new Vector(0.7, 0.7, 0).normalize();
      Vector baseLeftDiag = new Vector(-0.7, 0.7, 0).normalize();

      // הפעלת אותו סיבוב על הוקטורים האלכסוניים
      Vector rightDiagonal = rotateVector(baseRightDiag, rotateX, rotateY, rotateZ);
      Vector leftDiagonal = rotateVector(baseLeftDiag, rotateX, rotateY, rotateZ);

      // מרחק אלכסוני
      double diagonalDistance = 18.0;

      // יצירת האטום הימני העליון
      Point topRightPoint = middlePoint.add(rightDiagonal.scale(diagonalDistance));
      scene.geometries.add(
              new Sphere(atomSize, topRightPoint)
                      .setEmission(atomColor)
                      .setMaterial(atomMaterial)
      );

      // קשר לאטום הימני העליון
      addBond(scene, middlePoint, topRightPoint, bondThickness, bondColor, bondMaterial);

      // יצירת האטום השמאלי העליון
      Point topLeftPoint = middlePoint.add(leftDiagonal.scale(diagonalDistance));
      scene.geometries.add(
              new Sphere(atomSize, topLeftPoint)
                      .setEmission(atomColor)
                      .setMaterial(atomMaterial)
      );

      // קשר לאטום השמאלי העליון
      addBond(scene, middlePoint, topLeftPoint, bondThickness, bondColor, bondMaterial);

      // מחזיר את האטום הימני העליון כנקודת חיבור למולקולה הבאה
      return topRightPoint;
   }

   /**
    * מבצע סיבוב של וקטור בשלושת הצירים
    */
   private Vector rotateVector(Vector v, double angleX, double angleY, double angleZ) {
      // המרת מעלות לרדיאנים
      double radX = Math.toRadians(angleX);
      double radY = Math.toRadians(angleY);
      double radZ = Math.toRadians(angleZ);

      // קריאת ערכי הוקטור המקורי
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

      // החזרת וקטור מסובב חדש
      return new Vector(x, y, z);
   }

   /**
    * מוסיף קשר (גליל) בין שני אטומים
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



   /////


//
//   @Test
//   void minimalisticStaircaseTest() {
//      // === Set scene background and ambient light ===
//      // Soft cream (off-white) background color
//      scene.setBackground(new Color(245, 240, 230));
//
//      // Soft ambient light for subtle illumination
//      scene.setAmbientLight(new AmbientLight(new Color(0.4, 0.4, 0.4)));
//
//      // === Create materials ===
//      // Solid opaque material for the staircase (warm light brown)
//      Material stairMaterial = new Material()
//              .setKD(0.9).setKS(0.1).setShininess(30)
//              .setKT(0).setKR(0.05); // Minimal reflection for wooden appearance
//
//      // Material for neutral walls
//      Material wallMaterial = new Material()
//              .setKD(0.95).setKS(0.05).setShininess(10)
//              .setKT(0).setKR(0.02);
//
//      // Material for floor
//      Material floorMaterial = new Material()
//              .setKD(0.9).setKS(0.1).setShininess(20)
//              .setKT(0).setKR(0.1);
//
//      // Material for dark pastel objects - matte finish with minimal reflection
//      Material darkPastelMaterial = new Material()
//              .setKD(0.85).setKS(0.15).setShininess(50)
//              .setKT(0).setKR(0.05);
//
//      // Enhanced material for better shadow interaction
//      Material enhancedMaterial = new Material()
//              .setKD(0.88).setKS(0.12).setShininess(60)
//              .setKT(0).setKR(0.03);
//
//      // === Create the staircase (10 steps, consistent warm light brown color) ===
//      double stepWidth = 30;
//      double stepHeight = 15;
//      double stepDepth = 45;
//
//      // Warm light brown color for stairs
//      //Color stairColor = new Color(180, 140, 100); // Light brown with warm undertones
//      Color stairColor = new Color(230, 150, 180); // Warm pink / Rose pink
//      // Neutral colors for walls
//      Color wallColor = new Color(220, 215, 205); // Light gray with warm undertones
//      Color floorColor = new Color(210, 200, 190); // Slightly darker cream for floor
//
//      // First, create the base structure that supports the staircase
//      // Using the exact same warm brown color as the stairs for consistency
//      addBaseStructure(scene, -150, 0, -stepDepth/2, 150, -20, stepDepth/2, stairColor, stairMaterial);
//
//      // Fill in the sides of the staircase to ensure there are no gaps
//      // Left side wall of the staircase structure (same color as stairs)
//      scene.geometries.add(
//              new Polygon(
//                      new Point(-150, -20, -stepDepth/2),
//                      new Point(-150, -20, stepDepth/2),
//                      new Point(-150, 10*stepHeight, stepDepth/2),
//                      new Point(-150, 10*stepHeight, -stepDepth/2))
//                      .setEmission(stairColor)
//                      .setMaterial(stairMaterial)
//      );
//
//      // Right side wall of the staircase structure (same color as stairs)
//      scene.geometries.add(
//              new Polygon(
//                      new Point(150, -20, -stepDepth/2),
//                      new Point(150, -20, stepDepth/2),
//                      new Point(150, 10*stepHeight, stepDepth/2),
//                      new Point(150, 10*stepHeight, -stepDepth/2))
//                      .setEmission(stairColor)
//                      .setMaterial(stairMaterial)
//      );
//
//      // Back wall connecting base to the back of steps (same color as stairs)
//      scene.geometries.add(
//              new Polygon(
//                      new Point(-150, -20, stepDepth/2),
//                      new Point(150, -20, stepDepth/2),
//                      new Point(150, 10*stepHeight, stepDepth/2),
//                      new Point(-150, 10*stepHeight, stepDepth/2))
//                      .setEmission(stairColor)
//                      .setMaterial(stairMaterial)
//      );
//
//      // Front wall connecting base to the front of steps (same color as stairs)
//      scene.geometries.add(
//              new Polygon(
//                      new Point(-150, -20, -stepDepth/2),
//                      new Point(150, -20, -stepDepth/2),
//                      new Point(150, 0, -stepDepth/2),
//                      new Point(-150, 0, -stepDepth/2))
//                      .setEmission(stairColor)
//                      .setMaterial(stairMaterial)
//      );
//
//      for (int i = 0; i < 10; i++) {
//         double x = -150 + i * stepWidth;
//         double y = i * stepHeight;
//
//         // Add each stair step - horizontal part (top surface)
//         scene.geometries.add(
//                 new Polygon(
//                         new Point(x, y, -stepDepth/2),
//                         new Point(x + stepWidth, y, -stepDepth/2),
//                         new Point(x + stepWidth, y, stepDepth/2),
//                         new Point(x, y, stepDepth/2))
//                         .setEmission(stairColor)
//                         .setMaterial(stairMaterial)
//         );
//
//         // Add the vertical part of the step (front face)
//         if (i < 9) {
//            scene.geometries.add(
//                    new Polygon(
//                            new Point(x + stepWidth, y, -stepDepth/2),
//                            new Point(x + stepWidth, y + stepHeight, -stepDepth/2),
//                            new Point(x + stepWidth, y + stepHeight, stepDepth/2),
//                            new Point(x + stepWidth, y, stepDepth/2))
//                            .setEmission(stairColor) // Same color for consistency
//                            .setMaterial(stairMaterial)
//            );
//         }
//
//         // Fill in the space behind each step to ensure consistency
//         if (i > 0) {
//            scene.geometries.add(
//                    new Polygon(
//                            new Point(x, y - stepHeight, stepDepth/2),
//                            new Point(x, y, stepDepth/2),
//                            new Point(x, y, -stepDepth/2),
//                            new Point(x, y - stepHeight, -stepDepth/2))
//                            .setEmission(stairColor) // Same color for consistency
//                            .setMaterial(stairMaterial)
//            );
//         }
//
//         // Fill in the underneath of each step to ensure it's fully enclosed
//         if (i > 0) {
//            scene.geometries.add(
//                    new Polygon(
//                            new Point(x, y - stepHeight, -stepDepth/2),
//                            new Point(x + stepWidth, y - stepHeight, -stepDepth/2),
//                            new Point(x + stepWidth, y - stepHeight, stepDepth/2),
//                            new Point(x, y - stepHeight, stepDepth/2))
//                            .setEmission(stairColor) // Same color for consistency
//                            .setMaterial(stairMaterial)
//            );
//         }
//      }
//
//      // === Add room walls with neutral color ===
//      // Left wall of the room
//      scene.geometries.add(
//              new Polygon(
//                      new Point(-200, -20, -100),
//                      new Point(-200, -20, 100),
//                      new Point(-200, 200, 100),
//                      new Point(-200, 200, -100))
//                      .setEmission(wallColor)
//                      .setMaterial(wallMaterial)
//      );
//
//      // Back wall of the room
//      scene.geometries.add(
//              new Polygon(
//                      new Point(-200, -20, 100),
//                      new Point(200, -20, 100),
//                      new Point(200, 200, 100),
//                      new Point(-200, 200, 100))
//                      .setEmission(wallColor)
//                      .setMaterial(wallMaterial)
//      );
//
//      // Floor with slightly different tone
//      scene.geometries.add(
//              new Plane(new Point(0, -20, 0), new Vector(0, 1, 0))  // Floor at level -20
//                      .setEmission(floorColor)
//                      .setMaterial(floorMaterial)
//      );
//
//      // === Add dark pastel-colored objects on the staircase ===
//
//      // --- Add 3 Spheres in dark pastel colors ---
//
//      // Dark dusty blue sphere on step 2 - placed much lower
//      double sphere1Radius = 7;
//      double step2Y = 2 * stepHeight; // Y-coordinate of step 2
//      scene.geometries.add(
//              new Sphere(sphere1Radius, new Point(-110, step2Y -8, 0)) // מונמך כמעט בגובה הרדיוס כך שיגע במדרגה
//                      .setEmission(new Color(80, 110, 130))  // Dark dusty blue
//                      .setMaterial(darkPastelMaterial)
//      );
//
//      // Dark mauve sphere on step 5 - placed much lower
//      double sphere2Radius = 8;
//      double step5Y = 5 * stepHeight; // Y-coordinate of step 5
//      scene.geometries.add(
//              new Sphere(sphere2Radius, new Point(-45, step5Y - 22, -10)) // מונמך כמעט בגובה הרדיוס כך שיגע במדרגה
//                      .setEmission(new Color(110, 90, 120))  // Dark mauve
//                      .setMaterial(darkPastelMaterial)
//      );
//
//      // Dark sage green sphere on step 9 - already correctly placed on step
//      double sphere3Radius = 6.5;
//      double step9Y = 9 * stepHeight; // Y-coordinate of step 9
//      scene.geometries.add(
//              new Sphere(sphere3Radius, new Point(110, step9Y - sphere3Radius -2, 0))
//                      .setEmission(new Color(100, 120, 90))  // Dark sage green
//                      .setMaterial(darkPastelMaterial)
//      );
//
//      // --- Add 2 Pyramids in dark pastel colors ---
//
//      // Dark terracotta pyramid on step 3 - placed much lower
//      double step3Y = 3 * stepHeight;
//      createPyramid(scene,
//              new Point(-75, step3Y - 15, 10),  // בסיס הפירמידה מונמך משמעותית
//              12, 18,                     // Base size and height
//              new Color(130, 90, 80),     // Dark terracotta
//              enhancedMaterial
//      );
//
//      // Dark slate pyramid on step 7 - placed much lower
//      double step7Y = 7 * stepHeight;
//      createPyramid(scene,
//              new Point(50, step7Y -15, -7),  // בסיס הפירמידה מונמך משמעותית
//              14, 20,                     // Base size and height
//              new Color(90, 100, 110),    // Dark slate
//              enhancedMaterial
//      );
//
//      // --- Add 2 Cylinders in dark pastel colors ---
//
//      // Dark olive cylinder on step 1 - placed much lower
//      double step1Y = 1 * stepHeight;
//      scene.geometries.add(
//              new Cylinder(
//                      new Ray(new Point(-140, step1Y +8, 20), new Vector(0, 1, 0)), // בסיס הגליל מונמך משמעותית
//                      5, 20)                                                      // Radius and height
//                      .setEmission(new Color(110, 115, 70))                      // Dark olive
//                      .setMaterial(darkPastelMaterial)
//      );
//
//      // Dark rust cylinder on step 8 - already correctly placed on step
//      double step8Y = 8 * stepHeight;
//      scene.geometries.add(
//              new Cylinder(
//                      new Ray(new Point(80, step8Y , -10), new Vector(0, 1, 0)),
//                      4.5, 22)                                                  // Radius and height
//                      .setEmission(new Color(120, 80, 70))                     // Dark rust
//                      .setMaterial(darkPastelMaterial)
//      );
//
//      // === Add soft, architectural lighting ===
//      // Main directional light for soft shadows
//      scene.light.add(
//              new DirectionalLight(
//                      new Color(180, 170, 160), // Warm white light
//                      new Vector(-0.5, -0.7, -0.5))
//      );
//
//      // Soft fill light from opposite direction
//      scene.light.add(
//              new DirectionalLight(
//                      new Color(120, 120, 140), // Cool fill light for contrast
//                      new Vector(0.5, -0.2, -0.8))
//      );
//
//      // Ambient accent light from above
//      scene.light.add(
//              new SpotLight(
//                      new Color(250, 240, 220), // Warm accent light
//                      new Point(0, 200, -50), // From above
//                      new Vector(0, -1, 0.2)) // Pointing slightly forward
//                      .setKl(0.0003).setKq(0.00002)
//      );
//
//      // === Set up and render with camera ===
//      // Camera positioned to showcase the staircase architecture
//      Camera.getBuilder()
//              .setLocation(new Point(-400, 120, -600))  // Set back to see full staircase
//              .setDirection(new Point(0, 50, 0), Vector.AXIS_Y)  // Looking at middle of staircase
//              .setVpDistance(1000)
//              .setVpSize(300, 300)
//              .setResolution(800, 800)
//              // Multithreading setup
//              .setMultithreading(8)
//              .setDebugPrint(5)
//              .setRayTracer(scene, RayTracerType.SIMPLE)
//              .build()
//              .renderImage()
//              .writeToImage("minimalistic_staircase_dark_objects");
//   }
//
//   /**
//    * Helper method to create a pyramid at a specific position
//    */
//   private void createPyramid(Scene scene, Point base, double baseSize, double height,
//                              Color color, Material material) {
//      Point p1 = base.add(new Vector(-baseSize/2, 0, -baseSize/2));
//      Point p2 = base.add(new Vector(baseSize/2, 0, -baseSize/2));
//      Point p3 = base.add(new Vector(baseSize/2, 0, baseSize/2));
//      Point p4 = base.add(new Vector(-baseSize/2, 0, baseSize/2));
//      Point apex = base.add(new Vector(0, height, 0));
//
//      // Add base/bottom of pyramid
//      scene.geometries.add(
//              new Polygon(p1, p2, p3, p4)
//                      .setEmission(color.scale(0.9))
//                      .setMaterial(material)
//      );
//
//      // Add pyramid sides
//      scene.geometries.add(
//              new Triangle(p1, p2, apex)
//                      .setEmission(color)
//                      .setMaterial(material),
//              new Triangle(p2, p3, apex)
//                      .setEmission(color)
//                      .setMaterial(material),
//              new Triangle(p3, p4, apex)
//                      .setEmission(color)
//                      .setMaterial(material),
//              new Triangle(p4, p1, apex)
//                      .setEmission(color)
//                      .setMaterial(material)
//      );
//   }
//
//   /**
//    * Helper method to add a base structure under the stairs
//    */
//   private void addBaseStructure(Scene scene, double minX, double minY, double minZ,
//                                 double maxX, double maxY, double maxZ,
//                                 Color color, Material material) {
//      // Create a solid block under the stairs with consistent color
//      // Front face
//      scene.geometries.add(
//              new Polygon(
//                      new Point(minX, minY, minZ),
//                      new Point(maxX, minY, minZ),
//                      new Point(maxX, maxY, minZ),
//                      new Point(minX, maxY, minZ))
//                      .setEmission(color)
//                      .setMaterial(material)
//      );
//
//      // Back face
//      scene.geometries.add(
//              new Polygon(
//                      new Point(minX, minY, maxZ),
//                      new Point(maxX, minY, maxZ),
//                      new Point(maxX, maxY, maxZ),
//                      new Point(minX, maxY, maxZ))
//                      .setEmission(color)
//                      .setMaterial(material)
//      );
//
//      // Left face
//      scene.geometries.add(
//              new Polygon(
//                      new Point(minX, minY, minZ),
//                      new Point(minX, minY, maxZ),
//                      new Point(minX, maxY, maxZ),
//                      new Point(minX, maxY, minZ))
//                      .setEmission(color)
//                      .setMaterial(material)
//      );
//
//      // Right face
//      scene.geometries.add(
//              new Polygon(
//                      new Point(maxX, minY, minZ),
//                      new Point(maxX, minY, maxZ),
//                      new Point(maxX, maxY, maxZ),
//                      new Point(maxX, maxY, minZ))
//                      .setEmission(color)
//                      .setMaterial(material)
//      );
//
//      // Top face - should match bottom of first stair
//      scene.geometries.add(
//              new Polygon(
//                      new Point(minX, minY, minZ),
//                      new Point(maxX, minY, minZ),
//                      new Point(maxX, minY, maxZ),
//                      new Point(minX, minY, maxZ))
//                      .setEmission(color)
//                      .setMaterial(material)
//      );
//
//      // Bottom face
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


   /// //2code
//   @Test
//   void minimalisticStaircaseTest() {
//      // === Set scene background and ambient light ===
//      // Soft cream (off-white) background color
//      scene.setBackground(new Color(245, 240, 230));
//
//      // Soft ambient light for subtle illumination
//      //scene.setAmbientLight(new AmbientLight(new Color(0.1, 0.1, 0.1)));
//
//      // === Create materials ===
//      // Solid opaque material for the staircase (warm light brown)
//      Material stairMaterial = new Material()
//              .setKD(0.9).setKS(0.1).setShininess(30)
//              .setKT(0).setKR(0.05); // Minimal reflection for wooden appearance
//
//      // Material for neutral walls
//      Material wallMaterial = new Material()
//              .setKD(0.95).setKS(0.05).setShininess(10)
//              .setKT(0).setKR(0.02);
//
//      // Material for floor
//      Material floorMaterial = new Material()
//              .setKD(0.9).setKS(0.1).setShininess(20)
//              .setKT(0).setKR(0.1);
//
//      // Material for dark pastel objects - matte finish with minimal reflection
//      Material darkPastelMaterial = new Material()
//              .setKD(0.85).setKS(0.15).setShininess(50)
//              .setKT(0).setKR(0.05);
//
//      // Enhanced material for better shadow interaction
//      Material enhancedMaterial = new Material()
//              .setKD(0.88).setKS(0.12).setShininess(60)
//              .setKT(0).setKR(0.03);
//
//      // === Create the staircase (10 steps, consistent warm light brown color) ===
//      double stepWidth = 30;
//      double stepHeight = 15;
//      double stepDepth = 45;
//
//      // Warm light brown color for stairs
//      //Color stairColor = new Color(180, 140, 100); // Light brown with warm undertones
//      Color stairColor = new Color(230, 150, 180); // Warm pink / Rose pink
//      // Neutral colors for walls
//      Color wallColor = new Color(220, 215, 205); // Light gray with warm undertones
//      Color floorColor = new Color(210, 200, 190); // Slightly darker cream for floor
//
//      // First, create the base structure that supports the staircase
//      // Using the exact same warm brown color as the stairs for consistency
//      addBaseStructure(scene, -150, 0, -stepDepth/2, 150, -20, stepDepth/2, stairColor, stairMaterial);
//
//      // Fill in the sides of the staircase to ensure there are no gaps
//      // Left side wall of the staircase structure (same color as stairs)
//      scene.geometries.add(
//              new Polygon(
//                      new Point(-150, -20, -stepDepth/2),
//                      new Point(-150, -20, stepDepth/2),
//                      new Point(-150, 10*stepHeight, stepDepth/2),
//                      new Point(-150, 10*stepHeight, -stepDepth/2))
//                      .setEmission(stairColor)
//                      .setMaterial(stairMaterial)
//      );
//
//      // Right side wall of the staircase structure (same color as stairs)
//      scene.geometries.add(
//              new Polygon(
//                      new Point(150, -20, -stepDepth/2),
//                      new Point(150, -20, stepDepth/2),
//                      new Point(150, 10*stepHeight, stepDepth/2),
//                      new Point(150, 10*stepHeight, -stepDepth/2))
//                      .setEmission(stairColor)
//                      .setMaterial(stairMaterial)
//      );
//
//      // Back wall connecting base to the back of steps (same color as stairs)
//      scene.geometries.add(
//              new Polygon(
//                      new Point(-150, -20, stepDepth/2),
//                      new Point(150, -20, stepDepth/2),
//                      new Point(150, 10*stepHeight, stepDepth/2),
//                      new Point(-150, 10*stepHeight, stepDepth/2))
//                      .setEmission(stairColor)
//                      .setMaterial(stairMaterial)
//      );
//
//      // Front wall connecting base to the front of steps (same color as stairs)
//      scene.geometries.add(
//              new Polygon(
//                      new Point(-150, -20, -stepDepth/2),
//                      new Point(150, -20, -stepDepth/2),
//                      new Point(150, 0, -stepDepth/2),
//                      new Point(-150, 0, -stepDepth/2))
//                      .setEmission(stairColor)
//                      .setMaterial(stairMaterial)
//      );
//
//      for (int i = 0; i < 10; i++) {
//         double x = -150 + i * stepWidth;
//         double y = i * stepHeight;
//
//         // Add each stair step - horizontal part (top surface)
//         scene.geometries.add(
//                 new Polygon(
//                         new Point(x, y, -stepDepth/2),
//                         new Point(x + stepWidth, y, -stepDepth/2),
//                         new Point(x + stepWidth, y, stepDepth/2),
//                         new Point(x, y, stepDepth/2))
//                         .setEmission(stairColor)
//                         .setMaterial(stairMaterial)
//         );
//
//         // Add the vertical part of the step (front face)
//         if (i < 9) {
//            scene.geometries.add(
//                    new Polygon(
//                            new Point(x + stepWidth, y, -stepDepth/2),
//                            new Point(x + stepWidth, y + stepHeight, -stepDepth/2),
//                            new Point(x + stepWidth, y + stepHeight, stepDepth/2),
//                            new Point(x + stepWidth, y, stepDepth/2))
//                            .setEmission(stairColor) // Same color for consistency
//                            .setMaterial(stairMaterial)
//            );
//         }
//
//         // Fill in the space behind each step to ensure consistency
//         if (i > 0) {
//            scene.geometries.add(
//                    new Polygon(
//                            new Point(x, y - stepHeight, stepDepth/2),
//                            new Point(x, y, stepDepth/2),
//                            new Point(x, y, -stepDepth/2),
//                            new Point(x, y - stepHeight, -stepDepth/2))
//                            .setEmission(stairColor) // Same color for consistency
//                            .setMaterial(stairMaterial)
//            );
//         }
//
//         // Fill in the underneath of each step to ensure it's fully enclosed
//         if (i > 0) {
//            scene.geometries.add(
//                    new Polygon(
//                            new Point(x, y - stepHeight, -stepDepth/2),
//                            new Point(x + stepWidth, y - stepHeight, -stepDepth/2),
//                            new Point(x + stepWidth, y - stepHeight, stepDepth/2),
//                            new Point(x, y - stepHeight, stepDepth/2))
//                            .setEmission(stairColor) // Same color for consistency
//                            .setMaterial(stairMaterial)
//            );
//         }
//      }
//
//      // === Add room walls with neutral color ===
//      // Left wall of the room
//      scene.geometries.add(
//              new Polygon(
//                      new Point(-200, -20, -100),
//                      new Point(-200, -20, 100),
//                      new Point(-200, 200, 100),
//                      new Point(-200, 200, -100))
//                      .setEmission(wallColor)
//                      .setMaterial(wallMaterial)
//      );
//
//      // Back wall of the room
//      scene.geometries.add(
//              new Polygon(
//                      new Point(-200, -20, 100),
//                      new Point(200, -20, 100),
//                      new Point(200, 200, 100),
//                      new Point(-200, 200, 100))
//                      .setEmission(wallColor)
//                      .setMaterial(wallMaterial)
//      );
//
//      // Floor with slightly different tone
//      scene.geometries.add(
//              new Plane(new Point(0, -20, 0), new Vector(0, 1, 0))  // Floor at level -20
//                      .setEmission(floorColor)
//                      .setMaterial(floorMaterial)
//      );
//
//      // === Add dark pastel-colored objects on the staircase ===
//
//      // --- Add 3 Spheres in dark pastel colors ---
//
//      // Dark dusty blue sphere on step 2 - placed much lower
//      double sphere1Radius = 7;
//      double step2Y = 2 * stepHeight; // Y-coordinate of step 2
//      scene.geometries.add(
//              new Sphere(sphere1Radius, new Point(-110, step2Y -8, 0)) // מונמך כמעט בגובה הרדיוס כך שיגע במדרגה
//                      .setEmission(new Color(80, 110, 130))  // Dark dusty blue
//                      .setMaterial(darkPastelMaterial)
//      );
//
//      // Dark mauve sphere on step 5 - placed much lower
//      double sphere2Radius = 8;
//      double step5Y = 5 * stepHeight; // Y-coordinate of step 5
//      scene.geometries.add(
//              new Sphere(sphere2Radius, new Point(-45, step5Y - 22, -10)) // מונמך כמעט בגובה הרדיוס כך שיגע במדרגה
//                      .setEmission(new Color(110, 90, 120))  // Dark mauve
//                      .setMaterial(darkPastelMaterial)
//      );
//
//      // Dark sage green sphere on step 9 - already correctly placed on step
//      double sphere3Radius = 6.5;
//      double step9Y = 9 * stepHeight; // Y-coordinate of step 9
//      scene.geometries.add(
//              new Sphere(sphere3Radius, new Point(110, step9Y - sphere3Radius -2, 0))
//                      .setEmission(new Color(100, 120, 90))  // Dark sage green
//                      .setMaterial(darkPastelMaterial)
//      );
//
//      // --- Add 2 Pyramids in dark pastel colors ---
//
//      // Dark terracotta pyramid on step 3 - placed much lower
//      double step3Y = 3 * stepHeight;
//      createPyramid(scene,
//              new Point(-75, step3Y - 15, 10),  // בסיס הפירמידה מונמך משמעותית
//              12, 18,                     // Base size and height
//              new Color(130, 90, 80),     // Dark terracotta
//              enhancedMaterial
//      );
//
//      // Dark slate pyramid on step 7 - placed much lower
//      double step7Y = 7 * stepHeight;
//      createPyramid(scene,
//              new Point(50, step7Y -15, -7),  // בסיס הפירמידה מונמך משמעותית
//              14, 20,                     // Base size and height
//              new Color(90, 100, 110),    // Dark slate
//              enhancedMaterial
//      );
//
//      // --- Add 2 Cylinders in dark pastel colors ---
//
//      // Dark olive cylinder on step 1 - placed much lower
//      double step1Y = 1 * stepHeight;
//      scene.geometries.add(
//              new Cylinder(
//                      new Ray(new Point(-140, step1Y +8, 20), new Vector(0, 1, 0)), // בסיס הגליל מונמך משמעותית
//                      5, 20)                                                      // Radius and height
//                      .setEmission(new Color(110, 115, 70))                      // Dark olive
//                      .setMaterial(darkPastelMaterial)
//      );
//
//      // Dark rust cylinder on step 8 - already correctly placed on step
//      double step8Y = 8 * stepHeight;
//      scene.geometries.add(
//              new Cylinder(
//                      new Ray(new Point(80, step8Y , -10), new Vector(0, 1, 0)),
//                      4.5, 22)                                                  // Radius and height
//                      .setEmission(new Color(120, 80, 70))                     // Dark rust
//                      .setMaterial(darkPastelMaterial)
//      );
//
//      // === Set up and render with camera ===
//      // Camera positioned to showcase the staircase architecture
//      Camera.getBuilder()
//              .setLocation(new Point(-400, 120, -600))  // Set back to see full staircase
//              .setDirection(new Point(0, 50, 0), Vector.AXIS_Y)  // Looking at middle of staircase
//              .setVpDistance(1000)
//              .setVpSize(300, 300)
//              .setResolution(800, 800)
//              // Multithreading setup
//              .setMultithreading(8)
//              .setDebugPrint(5)
//              .setRayTracer(scene, RayTracerType.SIMPLE)
//              .build()
//              .renderImage()
//              .writeToImage("minimalistic_staircase_no_lights");
//   }
//
//   /**
//    * Helper method to create a pyramid at a specific position
//    */
//   private void createPyramid(Scene scene, Point base, double baseSize, double height,
//                              Color color, Material material) {
//      Point p1 = base.add(new Vector(-baseSize/2, 0, -baseSize/2));
//      Point p2 = base.add(new Vector(baseSize/2, 0, -baseSize/2));
//      Point p3 = base.add(new Vector(baseSize/2, 0, baseSize/2));
//      Point p4 = base.add(new Vector(-baseSize/2, 0, baseSize/2));
//      Point apex = base.add(new Vector(0, height, 0));
//
//      // Add base/bottom of pyramid
//      scene.geometries.add(
//              new Polygon(p1, p2, p3, p4)
//                      .setEmission(color.scale(0.9))
//                      .setMaterial(material)
//      );
//
//      // Add pyramid sides
//      scene.geometries.add(
//              new Triangle(p1, p2, apex)
//                      .setEmission(color)
//                      .setMaterial(material),
//              new Triangle(p2, p3, apex)
//                      .setEmission(color)
//                      .setMaterial(material),
//              new Triangle(p3, p4, apex)
//                      .setEmission(color)
//                      .setMaterial(material),
//              new Triangle(p4, p1, apex)
//                      .setEmission(color)
//                      .setMaterial(material)
//      );
//   }
//
//   /**
//    * Helper method to add a base structure under the stairs
//    */
//   private void addBaseStructure(Scene scene, double minX, double minY, double minZ,
//                                 double maxX, double maxY, double maxZ,
//                                 Color color, Material material) {
//      // Create a solid block under the stairs with consistent color
//      // Front face
//      scene.geometries.add(
//              new Polygon(
//                      new Point(minX, minY, minZ),
//                      new Point(maxX, minY, minZ),
//                      new Point(maxX, maxY, minZ),
//                      new Point(minX, maxY, minZ))
//                      .setEmission(color)
//                      .setMaterial(material)
//      );
//
//      // Back face
//      scene.geometries.add(
//              new Polygon(
//                      new Point(minX, minY, maxZ),
//                      new Point(maxX, minY, maxZ),
//                      new Point(maxX, maxY, maxZ),
//                      new Point(minX, maxY, maxZ))
//                      .setEmission(color)
//                      .setMaterial(material)
//      );
//
//      // Left face
//      scene.geometries.add(
//              new Polygon(
//                      new Point(minX, minY, minZ),
//                      new Point(minX, minY, maxZ),
//                      new Point(minX, maxY, maxZ),
//                      new Point(minX, maxY, minZ))
//                      .setEmission(color)
//                      .setMaterial(material)
//      );
//
//      // Right face
//      scene.geometries.add(
//              new Polygon(
//                      new Point(maxX, minY, minZ),
//                      new Point(maxX, minY, maxZ),
//                      new Point(maxX, maxY, maxZ),
//                      new Point(maxX, maxY, minZ))
//                      .setEmission(color)
//                      .setMaterial(material)
//      );
//
//      // Top face - should match bottom of first stair
//      scene.geometries.add(
//              new Polygon(
//                      new Point(minX, minY, minZ),
//                      new Point(maxX, minY, minZ),
//                      new Point(maxX, minY, maxZ),
//                      new Point(minX, minY, maxZ))
//                      .setEmission(color)
//                      .setMaterial(material)
//      );
//
//      // Bottom face
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


   /// 3
   @Test
   void minimalisticStaircaseTest() {
      // === Set scene background and ambient light ===
      // Very dark background color - almost black with slight blue tint
      scene.setBackground(new Color(2, 2, 4));

      // Minimal ambient light but enough to see colors
      scene.setAmbientLight(new AmbientLight(new Color(0.04, 0.04, 0.06)));

      // === Create materials ===
      // Material for the staircase
      Material stairMaterial = new Material()
              .setKD(0.85).setKS(0.15).setShininess(30)
              .setKT(0).setKR(0.05);

      // Material for neutral walls
      Material wallMaterial = new Material()
              .setKD(0.85).setKS(0.15).setShininess(10)
              .setKT(0).setKR(0.02);

      // Material for floor
      Material floorMaterial = new Material()
              .setKD(0.85).setKS(0.15).setShininess(20)
              .setKT(0).setKR(0.1);

      // Material for objects - medium shine
      Material objectMaterial = new Material()
              .setKD(0.7).setKS(0.3).setShininess(100)
              .setKT(0.1).setKR(0.1);  // Slight transparency for better light effect

      // === Create the staircase (10 steps) ===
      double stepWidth = 30;
      double stepHeight = 15;
      double stepDepth = 45;

      // Colors from the preferred image but with better visibility
      Color stairColor = new Color(70, 40, 60);      // Dark pink-mauve, visible in shadows
      Color wallColor = new Color(50, 50, 60);       // Dark blue-gray
      Color floorColor = new Color(40, 45, 50);      // Dark blue-gray floor

      // First, create the base structure that supports the staircase
      addBaseStructure(scene, -150, 0, -stepDepth/2, 150, -20, stepDepth/2, stairColor, stairMaterial);

      // Fill in the sides of the staircase to ensure there are no gaps
      // Left side wall of the staircase structure (same color as stairs)
      scene.geometries.add(
              new Polygon(
                      new Point(-150, -20, -stepDepth/2),
                      new Point(-150, -20, stepDepth/2),
                      new Point(-150, 10*stepHeight, stepDepth/2),
                      new Point(-150, 10*stepHeight, -stepDepth/2))
                      .setEmission(stairColor)
                      .setMaterial(stairMaterial)
      );

      // Right side wall of the staircase structure (same color as stairs)
      scene.geometries.add(
              new Polygon(
                      new Point(150, -20, -stepDepth/2),
                      new Point(150, -20, stepDepth/2),
                      new Point(150, 10*stepHeight, stepDepth/2),
                      new Point(150, 10*stepHeight, -stepDepth/2))
                      .setEmission(stairColor)
                      .setMaterial(stairMaterial)
      );

      // Back wall connecting base to the back of steps (same color as stairs)
      scene.geometries.add(
              new Polygon(
                      new Point(-150, -20, stepDepth/2),
                      new Point(150, -20, stepDepth/2),
                      new Point(150, 10*stepHeight, stepDepth/2),
                      new Point(-150, 10*stepHeight, stepDepth/2))
                      .setEmission(stairColor)
                      .setMaterial(stairMaterial)
      );

      // Front wall connecting base to the front of steps (same color as stairs)
      scene.geometries.add(
              new Polygon(
                      new Point(-150, -20, -stepDepth/2),
                      new Point(150, -20, -stepDepth/2),
                      new Point(150, 0, -stepDepth/2),
                      new Point(-150, 0, -stepDepth/2))
                      .setEmission(stairColor)
                      .setMaterial(stairMaterial)
      );

      for (int i = 0; i < 10; i++) {
         double x = -150 + i * stepWidth;
         double y = i * stepHeight;

         // Add each stair step - horizontal part (top surface)
         scene.geometries.add(
                 new Polygon(
                         new Point(x, y, -stepDepth/2),
                         new Point(x + stepWidth, y, -stepDepth/2),
                         new Point(x + stepWidth, y, stepDepth/2),
                         new Point(x, y, stepDepth/2))
                         .setEmission(stairColor)
                         .setMaterial(stairMaterial)
         );

         // Add the vertical part of the step (front face)
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

         // Fill in the space behind each step to ensure consistency
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
         }

         // Fill in the underneath of each step to ensure it's fully enclosed
         if (i > 0) {
            scene.geometries.add(
                    new Polygon(
                            new Point(x, y - stepHeight, -stepDepth/2),
                            new Point(x + stepWidth, y - stepHeight, -stepDepth/2),
                            new Point(x + stepWidth, y - stepHeight, stepDepth/2),
                            new Point(x, y - stepHeight, stepDepth/2))
                            .setEmission(stairColor)
                            .setMaterial(stairMaterial)
            );
         }
      }

      // === Add room walls with neutral color ===
      // Left wall of the room
      scene.geometries.add(
              new Polygon(
                      new Point(-200, -20, -100),
                      new Point(-200, -20, 100),
                      new Point(-200, 200, 100),
                      new Point(-200, 200, -100))
                      .setEmission(wallColor)
                      .setMaterial(wallMaterial)
      );

      // Back wall of the room
      scene.geometries.add(
              new Polygon(
                      new Point(-200, -20, 100),
                      new Point(200, -20, 100),
                      new Point(200, 200, 100),
                      new Point(-200, 200, 100))
                      .setEmission(wallColor)
                      .setMaterial(wallMaterial)
      );

      // Floor with slightly different tone
      scene.geometries.add(
              new Plane(new Point(0, -20, 0), new Vector(0, 1, 0))  // Floor at level -20
                      .setEmission(floorColor)
                      .setMaterial(floorMaterial)
      );

      // === Add geometric objects with nice pastel colors like in the preferred image ===
      // Colors similar to the preferred image
      Color sphereColor1 = new Color(160, 200, 180);  // Light mint green
      Color sphereColor2 = new Color(210, 170, 190);  // Light pink/mauve
      Color sphereColor3 = new Color(170, 190, 220);  // Light blue

      Color pyramidColor1 = new Color(180, 200, 230); // Light periwinkle blue
      Color pyramidColor2 = new Color(220, 210, 170); // Light cream/beige

      Color cylinderColor1 = new Color(190, 220, 190); // Light sage
      Color cylinderColor2 = new Color(220, 180, 160); // Light terracotta

      // Sphere on step 2
      double sphere1Radius = 7;
      double step2Y = 2 * stepHeight;
      scene.geometries.add(
              new Sphere(sphere1Radius, new Point(-110, step2Y -8, 0))
                      .setEmission(sphereColor1)
                      .setMaterial(objectMaterial)
      );

      // Sphere on step 5
      double sphere2Radius = 8;
      double step5Y = 5 * stepHeight;
      scene.geometries.add(
              new Sphere(sphere2Radius, new Point(-45, step5Y - 22, -10))
                      .setEmission(sphereColor2)
                      .setMaterial(objectMaterial)
      );

      // Sphere on step 9
      double sphere3Radius = 6.5;
      double step9Y = 9 * stepHeight;
      scene.geometries.add(
              new Sphere(sphere3Radius, new Point(110, step9Y - sphere3Radius -2, 0))
                      .setEmission(sphereColor3)
                      .setMaterial(objectMaterial)
      );

      // Pyramid on step 3
      double step3Y = 3 * stepHeight;
      createPyramid(scene,
              new Point(-75, step3Y - 15, 10),
              12, 18,
              pyramidColor1,
              objectMaterial
      );

      // Pyramid on step 7
      double step7Y = 7 * stepHeight;
      createPyramid(scene,
              new Point(50, step7Y -15, -7),
              14, 20,
              pyramidColor2,
              objectMaterial
      );

      // Cylinder on step 1
      double step1Y = 1 * stepHeight;
      scene.geometries.add(
              new Cylinder(
                      new Ray(new Point(-140, step1Y +8, 20), new Vector(0, 1, 0)),
                      5, 20)
                      .setEmission(cylinderColor1)
                      .setMaterial(objectMaterial)
      );

      // Cylinder on step 8
      double step8Y = 8 * stepHeight;
      scene.geometries.add(
              new Cylinder(
                      new Ray(new Point(80, step8Y , -10), new Vector(0, 1, 0)),
                      4.5, 22)
                      .setEmission(cylinderColor2)
                      .setMaterial(objectMaterial)
      );

      // === Add lighting from top of staircase ===
      // Clear any existing lights
      scene.light.clear();

      // Precise coordinates for top of staircase
      double topStairY = 9 * stepHeight;
      double topStairX = -150 + 9 * stepWidth; // X-coordinate of the last step

      // Spotlight from top of stairs with better lighting
      scene.light.add(
              new SpotLight(
                      new Color(220, 215, 210), // Warm white light
                      new Point(topStairX - 5, topStairY + 35, 0), // Positioned at top of stairs
                      new Vector(-0.8, -0.6, 0)) // Vector pointing down along the stairs
                      .setKl(0.0004).setKq(0.00004) // Moderate attenuation for better reach
      );

      // Add subtle fill light for better color visibility
      scene.light.add(
              new DirectionalLight(
                      new Color(20, 18, 30), // Very dim purple-blue fill light
                      new Vector(0.3, -0.5, -0.3))
      );

      // === Set up and render with camera ===
      // Camera positioned to showcase the staircase architecture
      Camera.getBuilder()
              .setLocation(new Point(-400, 120, -600))  // Set back to see full staircase
              .setDirection(new Point(0, 50, 0), Vector.AXIS_Y)  // Looking at middle of staircase
              .setVpDistance(1000)
              .setVpSize(300, 300)
              .setResolution(800, 800)
              // Multithreading setup
              .setMultithreading(8)
              .setDebugPrint(5)
              .setRayTracer(scene, RayTracerType.SIMPLE)
              .build()
              .renderImage()
              .writeToImage("minimalistic_staircase_enhanced_colors");
   }

   /**
    * Helper method to create a pyramid at a specific position
    */
   private void createPyramid(Scene scene, Point base, double baseSize, double height,
                              Color color, Material material) {
      Point p1 = base.add(new Vector(-baseSize/2, 0, -baseSize/2));
      Point p2 = base.add(new Vector(baseSize/2, 0, -baseSize/2));
      Point p3 = base.add(new Vector(baseSize/2, 0, baseSize/2));
      Point p4 = base.add(new Vector(-baseSize/2, 0, baseSize/2));
      Point apex = base.add(new Vector(0, height, 0));

      // Add base/bottom of pyramid
      scene.geometries.add(
              new Polygon(p1, p2, p3, p4)
                      .setEmission(color.scale(0.9))
                      .setMaterial(material)
      );

      // Add pyramid sides
      scene.geometries.add(
              new Triangle(p1, p2, apex)
                      .setEmission(color)
                      .setMaterial(material),
              new Triangle(p2, p3, apex)
                      .setEmission(color)
                      .setMaterial(material),
              new Triangle(p3, p4, apex)
                      .setEmission(color)
                      .setMaterial(material),
              new Triangle(p4, p1, apex)
                      .setEmission(color)
                      .setMaterial(material)
      );
   }

   /**
    * Helper method to add a base structure under the stairs
    */
   private void addBaseStructure(Scene scene, double minX, double minY, double minZ,
                                 double maxX, double maxY, double maxZ,
                                 Color color, Material material) {
      // Create a solid block under the stairs with consistent color
      // Front face
      scene.geometries.add(
              new Polygon(
                      new Point(minX, minY, minZ),
                      new Point(maxX, minY, minZ),
                      new Point(maxX, maxY, minZ),
                      new Point(minX, maxY, minZ))
                      .setEmission(color)
                      .setMaterial(material)
      );

      // Back face
      scene.geometries.add(
              new Polygon(
                      new Point(minX, minY, maxZ),
                      new Point(maxX, minY, maxZ),
                      new Point(maxX, maxY, maxZ),
                      new Point(minX, maxY, maxZ))
                      .setEmission(color)
                      .setMaterial(material)
      );

      // Left face
      scene.geometries.add(
              new Polygon(
                      new Point(minX, minY, minZ),
                      new Point(minX, minY, maxZ),
                      new Point(minX, maxY, maxZ),
                      new Point(minX, maxY, minZ))
                      .setEmission(color)
                      .setMaterial(material)
      );

      // Right face
      scene.geometries.add(
              new Polygon(
                      new Point(maxX, minY, minZ),
                      new Point(maxX, minY, maxZ),
                      new Point(maxX, maxY, maxZ),
                      new Point(maxX, maxY, minZ))
                      .setEmission(color)
                      .setMaterial(material)
      );

      // Top face - should match bottom of first stair
      scene.geometries.add(
              new Polygon(
                      new Point(minX, minY, minZ),
                      new Point(maxX, minY, minZ),
                      new Point(maxX, minY, maxZ),
                      new Point(minX, minY, maxZ))
                      .setEmission(color)
                      .setMaterial(material)
      );

      // Bottom face
      scene.geometries.add(
              new Polygon(
                      new Point(minX, maxY, minZ),
                      new Point(maxX, maxY, minZ),
                      new Point(maxX, maxY, maxZ),
                      new Point(minX, maxY, maxZ))
                      .setEmission(color)
                      .setMaterial(material)
      );
   }

}
