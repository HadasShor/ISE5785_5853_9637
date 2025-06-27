////package renderer;
////
////import geometries.Sphere;
////import geometries.Triangle;
////import lighting.AmbientLight;
////import lighting.PointLight;
////import lighting.SpotLight;
////import primitives.Color;
////import primitives.Material;
////import primitives.Point;
////import primitives.Vector;
////import scene.Scene;
////
////public class geometricFlower {
////
////    public static void main(String[] args) {
////        // --- 1. יצירת סצנה והגדרות בסיסיות ---
////        Scene scene = new Scene("Expanded Geometric Flower");
////        scene.setBackground(Color.BLACK); // רקע שחור דרמטי
////        scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.DARK_GRAY), 0.05)); // תאורת סביבה חלשה מאוד
////
////        // --- 2. מרכז הפרח ---
////        // ספירה מרכזית
////        Sphere centralSphere = new Sphere(2.5, new Point(0, 0, 0));
////        centralSphere.setEmission(new Color(255, 220, 0)); // צהוב זהוב
////        centralSphere.setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(30).setKR(0.3)); // מעט השתקפות
////        scene.geometries.add(centralSphere);
////
////        // ספירות קטנות יותר סביב המרכז
////        for (int i = 0; i < 8; i++) {
////            double angle = i * (360.0 / 8);
////            double x = 1.8 * Math.cos(Math.toRadians(angle));
////            double y = 1.8 * Math.sin(Math.toRadians(angle));
////            Sphere smallSphere = new Sphere(0.8, new Point(x, y, 0.5));
////            smallSphere.setEmission(new Color(200, 150, 0)); // צהוב-כתום עמוק
////            smallSphere.setMaterial(new Material().setKD(0.4).setKS(0.6).setShininess(40).setKR(0.2));
////            scene.geometries.add(smallSphere);
////        }
////
////        // --- 3. עלי כותרת מורכבים (באמצעות משולשים) ---
////        // נבנה שכבות של עלי כותרת בגדלים וגוונים שונים
////        Color[] petalColors = {
////                new Color(255, 150, 150), // ורוד בהיר
////                new Color(255, 100, 100), // ורוד חזק
////                new Color(255, 50, 50)    // אדום-ורוד
////        };
////        double[] petalRadii = {4.0, 3.0, 2.0}; // רדיוס ליציאת עלי הכותרת
////        double[] zDepths = {-0.5, -0.8, -1.1}; // עומק Z עבור שכבות שונות
////
////        for (int layer = 0; layer < 3; layer++) { // 3 שכבות של עלי כותרת
////            int numPetals = 12 + layer * 4; // יותר עלי כותרת בשכבות פנימיות
////            double currentRadius = petalRadii[layer];
////            Color currentColor = petalColors[layer];
////            double currentZ = zDepths[layer];
////
////            for (int i = 0; i < numPetals; i++) {
////                double angle1 = i * (360.0 / numPetals);
////                double angle2 = (i + 1) * (360.0 / numPetals);
////
////                // קודקודי משולש העלה
////                Point p0 = new Point(0, 0, currentZ); // מרכז הפרח בעומק מסוים
////                Point p1 = new Point(
////                        currentRadius * Math.cos(Math.toRadians(angle1)),
////                        currentRadius * Math.sin(Math.toRadians(angle1)),
////                        currentZ
////                );
////                Point p2 = new Point(
////                        currentRadius * Math.cos(Math.toRadians(angle2)),
////                        currentRadius * Math.sin(Math.toRadians(angle2)),
////                        currentZ
////                );
////
////                Triangle petal = new Triangle(p0, p1, p2);
////                petal.setEmission(currentColor);
////                petal.setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(10)); // חומר פחות מבריק
////                scene.geometries.add(petal);
////
////                // נסה להוסיף גם משולש הפוך קטן יותר לפרטים
////                if (layer == 0) { // רק לשכבה החיצונית
////                    Point p0_inner = new Point(0, 0, currentZ + 0.1);
////                    Point p1_inner = new Point(
////                            (currentRadius * 0.8) * Math.cos(Math.toRadians(angle1 + 10)),
////                            (currentRadius * 0.8) * Math.sin(Math.toRadians(angle1 + 10)),
////                            currentZ + 0.1
////                    );
////                    Point p2_inner = new Point(
////                            (currentRadius * 0.8) * Math.cos(Math.toRadians(angle2 - 10)),
////                            (currentRadius * 0.8) * Math.sin(Math.toRadians(angle2 - 10)),
////                            currentZ + 0.1
////                    );
////                    Triangle innerPetal = new Triangle(p0_inner, p1_inner, p2_inner);
////                    innerPetal.setEmission(currentColor.reduce(2)); // גוון מעט כהה יותר
////                    innerPetal.setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(15));
////                    scene.geometries.add(innerPetal);
////                }
////            }
////        }
////
////
////        // --- 4. גבעול ועלה ---
////        // גבעול (כמה משולשים ליצירת צינור)
////        Color stemColor = new Color(50, 150, 50); // ירוק כהה
////        Material stemMaterial = new Material().setKD(0.7).setKS(0.1).setShininess(5);
////        double stemRadius = 0.5;
////        double stemLength = 8.0;
////        int stemSegments = 8;
////
////        for (int i = 0; i < stemSegments; i++) {
////            double angle1 = i * (360.0 / stemSegments);
////            double angle2 = (i + 1) * (360.0 / stemSegments);
////
////            Point p1_bottom = new Point(stemRadius * Math.cos(Math.toRadians(angle1)), stemRadius * Math.sin(Math.toRadians(angle1)), -stemLength);
////            Point p2_bottom = new Point(stemRadius * Math.cos(Math.toRadians(angle2)), stemRadius * Math.sin(Math.toRadians(angle2)), -stemLength);
////            Point p1_top = new Point(stemRadius * Math.cos(Math.toRadians(angle1)), stemRadius * Math.sin(Math.toRadians(angle1)), 0);
////            Point p2_top = new Point(stemRadius * Math.cos(Math.toRadians(angle2)), stemRadius * Math.sin(Math.toRadians(angle2)), 0);
////
////            // צד אחד של הצינור
////            scene.geometries.add(new Triangle(p1_top, p2_top, p1_bottom).setEmission(stemColor).setMaterial(stemMaterial));
////            scene.geometries.add(new Triangle(p2_top, p2_bottom, p1_bottom).setEmission(stemColor).setMaterial(stemMaterial));
////        }
////
////
////        // עלה אחד (שני משולשים ליצירת צורה שטוחה)
////        Color leafColor = new Color(100, 200, 100); // ירוק בהיר
////        Material leafMaterial = new Material().setKD(0.8).setKS(0.1).setShininess(5);
////
////        Point leafP1 = new Point(stemRadius + 0.5, -1, -5);
////        Point leafP2 = new Point(stemRadius + 3, -1, -5.5);
////        Point leafP3 = new Point(stemRadius + 1, -1, -4.5);
////        Point leafP4 = new Point(stemRadius + 2.5, -1, -4.0);
////
////        scene.geometries.add(new Triangle(leafP1, leafP2, leafP3).setEmission(leafColor).setMaterial(leafMaterial));
////        scene.geometries.add(new Triangle(leafP2, leafP4, leafP3).setEmission(leafColor).setMaterial(leafMaterial));
////
////
////        // --- 5. תאורה דרמטית ---
////        // אור ראשי (Key Light) - מגיע מלמעלה ובצד, חזק
////        scene.light.add(new SpotLight(new Color(700, 700, 700), // אור לבן חזק
////                new Point(10, 10, 10), // מיקום גבוה וקדימה-ימינה
////                new Vector(-0.5, -0.5, -1)) // מכוון לעבר המרכז
////                .setKl(0.00001).setKq(0.000001));
////
////        // אור משני (Fill Light) - מגיע מלמטה ובצד הנגדי, חלש יותר, עם גוון קריר
////        scene.light.add(new SpotLight(new Color(200, 200, 400), // כחול בהיר
////                new Point(-15, -15, 5), // מיקום נמוך ושמאלה-אחורה
////                new Vector(0.5, 0.5, -0.5)) // מכוון לעבר המרכז
////                .setKl(0.00005).setKq(0.000005));
////
////        // אור אחורי (Rim Light) - ליצירת זוהר בקצוות
////        scene.light.add(new PointLight(new Color(150, 150, 0), // צהוב חם
////                new Point(0, -10, 5)).setKl(0.001).setKq(0.0001));
////
////
////        // --- 6. הגדרות מצלמה ורנדור ---
////        Camera.Builder cameraBuilder = Camera.getBuilder()
////                .setLocation(new Point(0, 0, 15)) // מצלמה קרובה יותר כדי שהפרח ימלא את המסך
////                .setDirection(Point.ZERO, new Vector(0, 1, 0)) // מסתכלת לעבר המרכז
////                .setVpDistance(10) // מרחק מישור התצוגה
////                .setVpSize(10, 10) // גודל מישור התצוגה
////                .setResolution(600, 600) // רזולוציית תמונה גבוהה יותר
////                .setRayTracer(scene, RayTracerType.SIMPLE); // או RayTracerType.SIMPLE אם BASIC לא זמין
////
////        // 7. בנייה, רנדור ושמירה של התמונה
////        cameraBuilder.build().renderImage().writeToImage("geometricFlowerExpanded");
////    }
////}
//
//
//package renderer;
//
//import geometries.Sphere;
//import geometries.Triangle;
//import lighting.AmbientLight;
//import lighting.PointLight;
//import lighting.SpotLight;
//import primitives.Color;
//import primitives.Material;
//import primitives.Point;
//import primitives.Vector;
//import scene.Scene;
//import geometries.Geometries;
//
//import static primitives.Util.alignZero;
//
//public class geometricFlower {
//
//    public static void main(String[] args) {
//        // 3. עלי כותרת (משולשים דקים וארוכים) - קוד מתוקן
//        int numPetals = 20; // מספר עלי כותרת
//        double petalLength = 3.0; // אורך עלי הכותרת
//        double petalBaseWidth = 0.4; // רוחב הבסיס של עלה הכותרת (הרוחב בפועל יהיה כפול מזה)
//        double petalZ = 0.1; // קצת מעל מרכז הפרח
//
//        for (int i = 0; i < numPetals; i++) {
//            double angleRad = Math.toRadians(i * (360.0 / numPetals));
//            double angleDeg = i * (360.0 / numPetals);
//
//            // קודקוד השפיץ של העלה (הכי רחוק מהמרכז)
//            Point tipPoint = new Point(
//                    petalLength * Math.cos(angleRad),
//                    petalLength * Math.sin(angleRad),
//                    petalZ
//            );
//
//            // נקודות הבסיס של העלה (קרובות למרכז)
//            // נשתמש בזווית קצת שונה כדי ליצור את הרוחב בבסיס
//            double baseAngle1 = Math.toRadians(angleDeg - (360.0 / numPetals) / 2 + 5); // פחות 5 מעלות למרכז
//            double baseAngle2 = Math.toRadians(angleDeg + (360.0 / numPetals) / 2 - 5); // פלוס 5 מעלות למרכז
//
//            Point basePoint1 = new Point(
//                    petalBaseWidth * Math.cos(baseAngle1),
//                    petalBaseWidth * Math.sin(baseAngle1),
//                    petalZ
//            );
//
//            Point basePoint2 = new Point(
//                    petalBaseWidth * Math.cos(baseAngle2),
//                    petalBaseWidth * Math.sin(baseAngle2),
//                    petalZ
//            );
//
//            // משולש ראשי לעלה
//            Triangle petal = new Triangle(
//                    tipPoint,      // השפיץ של העלה
//                    basePoint1,    // קודקוד בסיס 1
//                    basePoint2     // קודקוד בסיס 2
//            );
//            petal.setEmission(new Color(200, 100, 200)); // צבע סגול
//            petal.setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(10));
//            scene.geometries.add(petal);
//
//            // משולש נוסף ליצירת עובי קל לעלה (אפשר לשחק עם ה-Z)
//            // זה יוצר שני משולשים שיוצרים מעין "מעוין" שטוח לעלה
//            Triangle petalBack = new Triangle(
//                    new Point(0, 0, petalZ - 0.1), // נקודה קצת יותר נמוכה במרכז
//                    basePoint2,
//                    basePoint1
//            );
//            petalBack.setEmission(new Color(150, 50, 150)); // גוון סגול כהה יותר
//            petalBack.setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(10));
//            scene.geometries.add(petalBack);
//        }
//    }
//}