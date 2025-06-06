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
////      // פאות הקופסה (6 פאות מורכבות מ-12 משולשים)
////
////      // פאה תחתונה
////      scene.geometries.add(new Triangle(p1Bottom, p2Bottom, p1Right)
////              .setEmission(blackColor).setMaterial(shinyBlackMaterial));
////      scene.geometries.add(new Triangle(p1Right, p2Bottom, p2Right)
////              .setEmission(blackColor).setMaterial(shinyBlackMaterial));
////
////      // פאה עליונה
////      scene.geometries.add(new Triangle(p1Top, p1TopRight, p2Top)
////              .setEmission(blackColor).setMaterial(shinyBlackMaterial));
////      scene.geometries.add(new Triangle(p1TopRight, p2TopRight, p2Top)
////              .setEmission(blackColor).setMaterial(shinyBlackMaterial));
////
////      // פאה שמאלית
////      scene.geometries.add(new Triangle(p1Bottom, p1Top, p2Bottom)
////              .setEmission(blackColor).setMaterial(shinyBlackMaterial));
////      scene.geometries.add(new Triangle(p1Top, p2Top, p2Bottom)
////              .setEmission(blackColor).setMaterial(shinyBlackMaterial));
////
////      // פאה ימנית
////      scene.geometries.add(new Triangle(p1Right, p2Right, p1TopRight)
////              .setEmission(blackColor).setMaterial(shinyBlackMaterial));
////      scene.geometries.add(new Triangle(p1TopRight, p2Right, p2TopRight)
////              .setEmission(blackColor).setMaterial(shinyBlackMaterial));
////
////      // פאה קדמית (בכדור הראשון)
////      scene.geometries.add(new Triangle(p1Bottom, p1Right, p1Top)
////              .setEmission(blackColor).setMaterial(shinyBlackMaterial));
////      scene.geometries.add(new Triangle(p1Right, p1TopRight, p1Top)
////              .setEmission(blackColor).setMaterial(shinyBlackMaterial));
////
////      // פאה אחורית (בכדור השני)
////      scene.geometries.add(new Triangle(p2Bottom, p2Top, p2Right)
////              .setEmission(blackColor).setMaterial(shinyBlackMaterial));
////      scene.geometries.add(new Triangle(p2Top, p2TopRight, p2Right)
////              .setEmission(blackColor).setMaterial(shinyBlackMaterial));
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





   /**try the picture with sphere*/


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

      Point p=new Point(0, 0, 0);
      Vector v=  new Vector(0, 0, 1);
      scene.geometries.add(new Cylinder(new Ray(p,v), 2, 5));
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





/**piramida definition**/


//      // פאות הקופסה (6 פאות מורכבות מ-12 משולשים)
//
//      // פאה תחתונה
//      scene.geometries.add(new Triangle(p1Bottom, p2Bottom, p1Right)
//              .setEmission(blackColor).setMaterial(shinyBlackMaterial));
//      scene.geometries.add(new Triangle(p1Right, p2Bottom, p2Right)
//              .setEmission(blackColor).setMaterial(shinyBlackMaterial));
//
//      // פאה עליונה
//      scene.geometries.add(new Triangle(p1Top, p1TopRight, p2Top)
//              .setEmission(blackColor).setMaterial(shinyBlackMaterial));
//      scene.geometries.add(new Triangle(p1TopRight, p2TopRight, p2Top)
//              .setEmission(blackColor).setMaterial(shinyBlackMaterial));
//
//      // פאה שמאלית
//      scene.geometries.add(new Triangle(p1Bottom, p1Top, p2Bottom)
//              .setEmission(blackColor).setMaterial(shinyBlackMaterial));
//      scene.geometries.add(new Triangle(p1Top, p2Top, p2Bottom)
//              .setEmission(blackColor).setMaterial(shinyBlackMaterial));
//
//      // פאה ימנית
//      scene.geometries.add(new Triangle(p1Right, p2Right, p1TopRight)
//              .setEmission(blackColor).setMaterial(shinyBlackMaterial));
//      scene.geometries.add(new Triangle(p1TopRight, p2Right, p2TopRight)
//              .setEmission(blackColor).setMaterial(shinyBlackMaterial));
//
//      // פאה קדמית (בכדור הראשון)
//      scene.geometries.add(new Triangle(p1Bottom, p1Right, p1Top)
//              .setEmission(blackColor).setMaterial(shinyBlackMaterial));
//      scene.geometries.add(new Triangle(p1Right, p1TopRight, p1Top)
//              .setEmission(blackColor).setMaterial(shinyBlackMaterial));
//
//      // פאה אחורית (בכדור השני)
//      scene.geometries.add(new Triangle(p2Bottom, p2Top, p2Right)
//              .setEmission(blackColor).setMaterial(shinyBlackMaterial));
//      scene.geometries.add(new Triangle(p2Top, p2TopRight, p2Right)
//              .setEmission(blackColor).setMaterial(shinyBlackMaterial));







@Test
void new_test () {
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
           .build()
           .renderImage()
           .writeToImage("new_test");
}



   @Test
   void cylindricalWonderland() {
      // === מגדל צילינדרים שקופים ===

      // צילינדר קריסטל מרכזי - בהיר ושקוף
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
              .setResolution(900, 900).setRayTracer(scene, RayTracerType.SIMPLE)
              .build()
              .renderImage()
              .writeToImage("cylindricalWonderland");
   }
}