//
//package renderer;
//
//import static java.awt.Color.*;
//
//import org.junit.jupiter.api.Test;
//
//import geometries.Sphere;
//import geometries.Triangle;
//import lighting.AmbientLight;
//import primitives.*;
//import scene.Scene;
//import scene.SceneBuilder;
//
//
//public class SceneBuilder {
//    public static Scene createScene() {
//        Scene scene = new Scene("Custom Scene");
//
//        // רקע תכלת: מישור רחוק מאחורי הסצנה
//        scene.geometries().add(
//                new Plane(
//                        new Point(0, 0, -1000), // מיקום רחוק
//                        new Vector(0, 0, 1)      // פונה לכיוון הצופה
//                ).setEmission(new Color(173, 216, 230)) // תכלת
//        );
//
//        // שמש: כדור צהוב בקוטר 100
//        scene.geometries().add(
//                new Sphere(50, new Point(300, 300, -900))
//                        .setEmission(new Color(255, 255, 0)) // צהוב
//        );
//
//        // הר: משולש ירוק כהה
//        scene.geometries().add(
//                new Triangle(
//                        new Point(-500, -200, -900),
//                        new Point(0, 200, -900),
//                        new Point(500, -200, -900)
//                ).setEmission(new Color(0, 100, 0)) // ירוק כהה
//        );
//
//        // גזע העץ: מלבן חום משני משולשים
//        scene.geometries().add(
//                new Triangle(
//                        new Point(-250, -100, -800),
//                        new Point(-220, -100, -800),
//                        new Point(-250, 100, -800)
//                ).setEmission(new Color(139, 69, 19)) // חום
//        );
//        scene.geometries().add(
//                new Triangle(
//                        new Point(-250, 100, -800),
//                        new Point(-220, -100, -800),
//                        new Point(-220, 100, -800)
//                ).setEmission(new Color(139, 69, 19))
//        );
//
//        // עלי העץ: שלושה כדורים ירוקים
//        scene.geometries().add(
//                new Sphere(40, new Point(-235, 150, -790))
//                        .setEmission(new Color(0, 128, 0)) // ירוק
//        );
//        scene.geometries().add(
//                new Sphere(40, new Point(-260, 180, -790))
//                        .setEmission(new Color(0, 128, 0))
//        );
//        scene.geometries().add(
//                new Sphere(40, new Point(-210, 180, -790))
//                        .setEmission(new Color(0, 128, 0))
//        );
//
//        return scene;
//    }
//}
