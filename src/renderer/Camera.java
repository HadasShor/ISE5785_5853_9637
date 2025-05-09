package renderer;

import primitives.*;
import scene.Scene;

import javax.imageio.ImageIO;
import java.util.MissingResourceException;

public class Camera implements Cloneable {
    // Camera properties
    private Point p0 = new Point(0, 0, 0); // Camera position
    private Vector vTo = new Vector(0, 0, -1); // Camera direction
    private Vector vUp = new Vector(0, 1, 0); // Camera up vector
    private Vector vRight = new Vector(1, 0, 0); // Camera right vector
    private double width = 0.0; // Width of the view plane
    private double height = 0.0; // Height of the view plane
    private double distance = 0.0; // Distance from the camera to the view plane
    //X עמודות
    //Y שורות
    private RayTracerBase rayTracer;
    private ImageWriter imageWriter;
    private int Nx=1;
    private int Ny=1;
    private Camera() {
        // Private constructor to prevent instantiation
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * construct a ray through a pixel
     *
     * @param nX the number of pixels in the x direction
     * @param nY the number of pixels in the y direction
     * @param j  the x index of the pixel
     * @param i  the y index of the pixel
     * @return the ray that passes through the pixel
     */

    public Ray constructRay(int nX, int nY, int j, int i) {
        // 1. Compute the center of the view plane
        Point pc = p0.add(vTo.scale(distance));

        // 2. Compute the offsets from the center to the pixel (i, j)
        double yI = -(i - (nY - 1) / 2d) * height / nY;
        double xJ = (j - (nX - 1) / 2d) * width / nX;

        // 3. Compute the actual pixel's center position
        Point pIJ = pc;
        if (!Util.isZero(xJ)) pIJ = pIJ.add(vRight.scale(xJ));
        if (!Util.isZero(yI)) pIJ = pIJ.add(vUp.scale(yI));

        // 4. Create ray
        return new Ray(p0, pIJ.subtract(p0).normalize());
    }

    public Ray constructRayThroughPixel(int nX, int nY, double j, double i) {
        //todo
        return null;
    }

    public Point getP0() {
        return p0;
    }

    public Vector getvTo() {
        return vTo;
    }

    public Vector getvUp() {
        return vUp;
    }

    public Vector getvRight() {
        return vRight;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getDistance() {
        return distance;
    }

    public Camera renderImage() {

        for(int i = 0; i < Nx; i++) {
            for(int j = 0; j < Ny; j++) {
                castRay(i, j);/////////check maybe we need to change i to j
            }
        }
        return this;
    }

    /**
     * Print a grid on the image
     * @param interval the interval between the lines of the grid
     * @param color the color of the grid
     */
    public Camera printGrid(int interval, Color color) {

        for(int i = 0; i < Ny; i++) {
            for(int j = 0; j < Nx; j++) {
                if(i % interval == 0 || j % interval == 0) {
                    imageWriter.writePixel(j, i, color);
                }
            }
        }
        return this;
    }

    public Camera writeToImage(String filePath) {
        imageWriter.writeToImage(filePath);//////we need to check this
        return this;
    }

    private void castRay(int x, int y) {
        Ray ray = constructRay(Nx, Ny, x, y);
        Color color = rayTracer.traceRay(ray);
        imageWriter.writePixel(x, y, color);
    }

    public static class Builder {
        private final Camera camera = new Camera();


        public Builder setLocation(Point point) {
            camera.p0 = point;
            return this;
        }

        public Builder setDirection(Vector vTo, Vector vUp) {
            if (vTo.dotProduct(vUp) == 0) {
                camera.vTo = vTo.normalize();
                camera.vUp = vUp.normalize();
            } else {
                throw new IllegalArgumentException("vTo and vUp must be orthogonal");
            }
            return this;
        }

        public Builder setDirection(Point point, Vector vUp) {
            camera.vTo = point.subtract(camera.p0).normalize();
            camera.vRight = camera.vTo.crossProduct(vUp).normalize();
            camera.vUp = camera.vRight.crossProduct(camera.vTo).normalize();  // ← זה מה שצריך
            return this;
        }

        public Builder setDirection(Point point) {
            return  setDirection(point, Vector.AXIS_Y);
        }

        public Builder setVpSize(double width, double height) {
            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("Width and height must be > 0");
            }
            camera.width = width;
            camera.height = height;
            return this;
        }

        public Builder setVpDistance(double distance) {
            if (distance <= 0.0) {
                throw new IllegalArgumentException("distance must be >= 0.0");
            }
            camera.distance = distance;
            return this;
        }



        public Builder setResolution(int nx, int ny) {
            camera.imageWriter = new ImageWriter(nx, ny);
            camera.Nx = nx;
            camera.Ny = ny;

            return this;
        }

        public Builder setRayTracer(Scene scene, RayTracerType rayTracerType) {
            switch (rayTracerType) {
                case SIMPLE:
                    camera.rayTracer = new SimpleRayTracer(scene, RayTracerType.SIMPLE);
                    break;
                default:
                    camera.rayTracer=null;
                    //throw new IllegalArgumentException("Invalid ray tracer type");
            }
            return this;
        }

        public Camera build() {
            camera.imageWriter = new ImageWriter(camera.Nx, camera.Ny);
            if (camera.p0 == null)
                throw new MissingResourceException("p0 must has value", "Camera", "p0");
            if (camera.vUp == null)
                throw new MissingResourceException("vUp must has value", "Camera", "vUp");
            if (camera.vTo == null)
                throw new MissingResourceException("vTo must has value", "Camera", "vTo");
            if (camera.width == 0)
                throw new MissingResourceException("Width must be greater than 0", "Camera", "width");
            if (camera.height == 0)
                throw new MissingResourceException("Height must be greater than 0", "Camera", "height");
            if (camera.distance == 0)
                throw new MissingResourceException("Distance must be greater than 0", "Camera", "distance");
            if (camera.p0 == null)
                throw new MissingResourceException("Camera position must be set", "Camera", "p0");
            Point p0 = new Point(0, 0, 0); // Camera position

            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();
            double a = camera.vTo.dotProduct(camera.vRight);
            double b = camera.vTo.dotProduct(camera.vUp);
            double c = camera.vRight.dotProduct(camera.vUp);
            double d = camera.vTo.length();
            double m = camera.vUp.length();

            if (!Util.isZero(camera.vTo.dotProduct(camera.vRight)) ||
                    !Util.isZero(camera.vTo.dotProduct(camera.vUp)) ||
                    !Util.isZero(camera.vRight.dotProduct(camera.vUp)))
                throw new IllegalArgumentException("vTo, vUp and vRight must be orthogonal");

//            if (camera.vTo.length() != 1 || camera.vUp.length() != 1 || camera.vRight.length() != 1)
//                throw new IllegalArgumentException("vTo, vUp and vRight must be normalized");
            if (!Util.isCloseToOne(camera.vTo.length()) || !Util.isCloseToOne(camera.vUp.length()) || !Util.isCloseToOne(camera.vRight.length()))
                throw new IllegalArgumentException("vTo, vUp and vRight must be normalized");

            if (camera.width <= 0 || camera.height <= 0)
                throw new IllegalArgumentException("width and height must be positive");

            if (camera.distance <= 0)
                throw new IllegalArgumentException("distance from camera to view must be positive");
            if (camera.Nx <= 0 || camera.Ny <= 0)
                throw new IllegalArgumentException("Nx and Ny must be positive");
            if (camera.imageWriter == null)
                throw new MissingResourceException("ImageWriter must be set", "Camera", "imageWriter");
            if (camera.rayTracer == null) {
                camera.rayTracer = new SimpleRayTracer(null, RayTracerType.SIMPLE);
            }

            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }



    }
}
