package renderer;

import primitives.*;
import scene.Scene;

import javax.imageio.ImageIO;
import java.util.MissingResourceException;

import static primitives.Util.isZero;

/**
 * Camera class represents a virtual camera for rendering 3D scenes.
 * @author Hadas_Shor, Nurit_Ezra
 */
public class Camera implements Cloneable {
    /**
     * Camera position in 3D space.
     */
    private Point p0 = new Point(0, 0, 0);

    /**
     * Forward direction vector (towards view plane).
     */
    private Vector vTo = new Vector(0, 0, -1);

    /**
     * Upward direction vector (points upward from the view plane).
     */
    private Vector vUp = new Vector(0, 1, 0);

    /**
     * Rightward direction vector (perpendicular to vTo and vUp).
     */
    private Vector vRight = new Vector(1, 0, 0);

    /**
     * Width of the view plane.
     */
    private double width = 0.0;

    /**
     * Height of the view plane.
     */
    private double height = 0.0;

    /**
     * Distance from the camera to the view plane.
     */
    private double distance = 0.0;

    /**
     * Ray tracer used to trace rays through the scene.
     */
    private RayTracerBase rayTracer;

    /**
     * Image writer used to output the rendered image.
     */
    private ImageWriter imageWriter;

    /**
     * Number of pixels in the horizontal direction (columns).
     */
    private int Nx = 1;

    /**
     * Number of pixels in the vertical direction (rows).
     */
    private int Ny = 1;

    /**
     * Private constructor to prevent direct instantiation.
     */
    private Camera() {
        // Private constructor to prevent instantiation
    }

    /**
     * Returns a builder instance for the camera.
     *
     * @return new Builder instance
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * Constructs a ray through a specific pixel on the view plane.
     *
     * @param nX number of pixels in the x-direction
     * @param nY number of pixels in the y-direction
     * @param j  x-index of the pixel
     * @param i  y-index of the pixel
     * @return ray from the camera through the pixel
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        Point pc = p0.add(vTo.scale(distance));

        double yI = -(i - (nY - 1) / 2d) * height / nY;
        double xJ = (j - (nX - 1) / 2d) * width / nX;

        Point pIJ = pc;

        if (!isZero(xJ))
            pIJ = pIJ.add(vRight.scale(xJ));
        if (!isZero(yI))
            pIJ = pIJ.add(vUp.scale(yI));

        return new Ray(p0, pIJ.subtract(p0));
    }


    /**
     * Gets the camera's position.
     * @return The camera's position.
     */
    public Point getP0() {
        return p0;
    }

    /**
     * Gets the forward direction vector.
     * @return The forward direction vector.
     */
    public Vector getvTo() {
        return vTo;
    }

    /**
     * Gets the upward direction vector.
     * @return The upward direction vector.
     */
    public Vector getvUp() {
        return vUp;
    }

    /**
     * Gets the rightward direction vector.
     * @return The rightward direction vector.
     */
    public Vector getvRight() {
        return vRight;
    }

    /**
     * Gets the width of the view plane.
     * @return The width of the view plane.
     */
    public double getWidth() {
        return width;
    }

    /**
     * Gets the height of the view plane.
     * @return The height of the view plane.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Gets the distance from the camera to the view plane.
     * @return The distance to the view plane.
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Renders the image by casting rays through all pixels and calculating their colors.
     *
     * @return this camera instance
     */
    public Camera renderImage() {
        for (int i = 0; i < Ny; i++) { // Corrected loop order to match writePixel(j, i)
            for (int j = 0; j < Nx; j++) {
                castRay(j, i);
            }
        }
        return this;
    }

    /**
     * Draws a grid over the image with the specified interval and color.
     *
     * @param interval the spacing between grid lines
     * @param color    the color of the grid lines
     * @return this camera instance
     */
    public Camera printGrid(int interval, Color color) {
        for (int i = 0; i < Ny; i++) {
            for (int j = 0; j < Nx; j++) {
                if (i % interval == 0 || j % interval == 0) {
                    imageWriter.writePixel(j, i, color);
                }
            }
        }
        return this;
    }

    /**
     * Saves the rendered image to a file.
     *
     * @param filePath the name or path of the output file
     * @return this camera instance
     */
    public Camera writeToImage(String filePath) {
        imageWriter.writeToImage(filePath);
        return this;
    }

    /**
     * Helper method to cast a ray through a pixel and write its color.
     *
     * @param x x-coordinate of the pixel
     * @param y y-coordinate of the pixel
     */
    private void castRay(int x, int y) {
        Ray ray = constructRay(Nx, Ny, x, y);
        Color color = rayTracer.traceRay(ray);
        imageWriter.writePixel(x, y, color);
    }

    /**
     * Builder class for constructing a Camera instance.
     * This class provides methods to set the camera's properties
     */
    public static class Builder {
        /**
         * The camera instance being built.
         */
        private final Camera camera = new Camera();

        /**
         * Sets the location of the camera.
         *
         * @param point the position of the camera
         * @return this builder
         */
        public Builder setLocation(Point point) {
            camera.p0 = point;
            return this;
        }

        /**
         * Sets the direction vectors (vTo and vUp) of the camera.
         *
         * @param vTo forward direction vector
         * @param vUp up direction vector
         * @return this builder
         * @throws IllegalArgumentException if vTo and vUp are not orthogonal
         */
        public Builder setDirection(Vector vTo, Vector vUp) {
            if (vTo.dotProduct(vUp) == 0) {
                camera.vTo = vTo.normalize();
                camera.vUp = vUp.normalize();
                camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize(); // Calculate vRight here
            } else {
                throw new IllegalArgumentException("vTo and vUp must be orthogonal");
            }
            return this;
        }

        /**
         * Sets the direction based on a point to look at and an up vector.
         *
         * @param point point the camera should look at
         * @param vUp   up direction vector
         * @return this builder
         */
        public Builder setDirection(Point point, Vector vUp) {
            camera.vTo = point.subtract(camera.p0).normalize();
            camera.vRight = camera.vTo.crossProduct(vUp).normalize();
            camera.vUp = camera.vRight.crossProduct(camera.vTo).normalize();
            return this;
        }

        /**
         * Sets the direction based on a point to look at. Assumes up is Y-axis.
         *
         * @param point point the camera should look at
         * @return this builder
         */
        public Builder setDirection(Point point) {
            return setDirection(point, Vector.AXIS_Y);
        }

        /**
         * Sets the view plane size.
         *
         * @param width  width of the view plane
         * @param height height of the view plane
         * @return this builder
         * @throws IllegalArgumentException if width or height are non-positive
         */
        public Builder setVpSize(double width, double height) {
            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("Width and height must be > 0");
            }
            camera.width = width;
            camera.height = height;
            return this;
        }

        /**
         * Sets the distance from the camera to the view plane.
         *
         * @param distance the distance
         * @return this builder
         * @throws IllegalArgumentException if distance is non-positive
         */
        public Builder setVpDistance(double distance) {
            if (distance <= 0.0) {
                throw new IllegalArgumentException("distance must be >= 0.0");
            }
            camera.distance = distance;
            return this;
        }

        /**
         * Sets the resolution of the image (number of pixels in each direction).
         *
         * @param nx number of pixels horizontally
         * @param ny number of pixels vertically
         * @return this builder
         */
        public Builder setResolution(int nx, int ny) {
            camera.Nx = nx;
            camera.Ny = ny;
            return this;
        }

        /**
         * Sets the ray tracer algorithm to be used for rendering.
         *
         * @param scene         the scene to render
         * @param rayTracerType the type of ray tracer
         * @return this builder
         */
        public Builder setRayTracer(Scene scene, RayTracerType rayTracerType) {
            switch (rayTracerType) {
                case SIMPLE:
                    camera.rayTracer = new SimpleRayTracer(scene, RayTracerType.SIMPLE);
                    break;
                default:
                    camera.rayTracer = null;
            }
            return this;
        }

        /**
         * Builds the final {@link Camera} instance, validating all required fields.
         *
         * @return cloned camera instance
         * @throws MissingResourceException   if a required field is not set
         * @throws IllegalArgumentException if vectors are not orthogonal or normalized
         */
        public Camera build() {
               if (camera.p0 == null)
               // throw new MissingResourceException("p0 must has value", "Camera", "p0");
                   camera.p0 = Point.ZERO;
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

            // vRight is calculated in setDirection, ensure it's calculated even without explicit setDirection call
            if (camera.vRight == null) {
                camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();
            }

            if (!isZero(camera.vTo.dotProduct(camera.vRight)) ||
                    !isZero(camera.vTo.dotProduct(camera.vUp)) ||
                    !isZero(camera.vRight.dotProduct(camera.vUp)))
                throw new IllegalArgumentException("vTo, vUp and vRight must be orthogonal");

            if (!Util.isCloseToOne(camera.vTo.length()) ||
                    !Util.isCloseToOne(camera.vUp.length()) ||
                    !Util.isCloseToOne(camera.vRight.length()))
                throw new IllegalArgumentException("vTo, vUp and vRight must be normalized");

            if (camera.width <= 0 || camera.height <= 0)
                throw new IllegalArgumentException("width and height must be positive");

            if (camera.distance <= 0)
                throw new IllegalArgumentException("distance from camera to view must be positive");

            if (camera.Nx <= 0 || camera.Ny <= 0)
                throw new IllegalArgumentException("Nx and Ny must be positive");

            if (camera.imageWriter == null)
                camera.imageWriter = new ImageWriter(camera.Nx, camera.Ny);

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