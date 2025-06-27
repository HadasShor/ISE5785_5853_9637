package renderer;
import java.util.stream.*;
import primitives.*;
import primitives.Vector;
import scene.Scene;

import java.util.*;

import static primitives.Util.isZero;

/**
 * Camera class represents a virtual camera for rendering 3D scenes.
 * @author Hadas_Shor, Nurit_Ezra
 */
public class Camera implements Cloneable {

    /**
     * Number of threads for rendering the image.
     */
    private int threadsCount = 0;

    /**
     * Default number of threads for rendering.
     */
    private static final int SPARE_THREADS = 2;

    /**
     * Interval for printing debug information in percentage.
     * If set to 0, no debug information will be printed.
     */
    private double printInterval = 0;

    /**
     * Pixel manager for supporting:
     * <ul>
     * <li>multi-threading</li>
     * <li>debug print of progress percentage in Console window/tab</li>
     * </ul>
     */
    private PixelManager pixelManager;

    /**
     * Flag to enable or disable anti-aliasing.
     */
    private boolean AA_FLAG = true;

    /**
     * Default grid size for anti-aliasing
     */
    private int AA_GRID_SIZE = 9;

    /**
     * Flag to enable or disable adaptive anti-aliasing.
     */
    private boolean ADAPTIVE_AA_FLAG = false;

    /**
     * Maximum recursion depth for adaptive antialiasing.
     */
    private int AA_MAX_RECURSION_DEPTH = 3;

    /**
     * Variance threshold for adaptive antialiasing.
     */
    private double AA_VARIANCE_THRESHOLD = 0.01;

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
    public Ray constructRay(int nX, int nY, double j, double i) {
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

    /** This function renders image's pixel color map from the scene
     * included in the ray tracer object
     * @return the camera object itself
     */
    public Camera renderImage() {
        pixelManager = new PixelManager(Nx, Ny, printInterval);
        return switch (threadsCount) {
            case 0 -> renderImageNoThreads();
            case -1 -> renderImageStream();
            default -> renderImageRawThreads();
        };
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
     * Construct rays through a pixel in the view plane
     *
     * @param nX the number of pixels in the x direction
     * @param nY the number of pixels in the y direction
     * @param j  the x index of the pixel
     * @param i  the y index of the pixel
     * @return the list of rays through the pixel
     */
    public List<Ray> constructRaysJ(int nX, int nY, int j, int i) {

        List<Ray> rays = new ArrayList<>();
        Random random = new Random();
        double Ry = height / nY;
        double Rx = width / nX;
        double stepY = Ry / AA_GRID_SIZE;
        double stepX = Rx / AA_GRID_SIZE;

        for (int subI = 0; subI < AA_GRID_SIZE; subI++) {
            for (int subJ = 0; subJ < AA_GRID_SIZE; subJ++) {
                double jitterY = random.nextDouble() % AA_GRID_SIZE; // Random offset in Y direction
                double jitterX = random.nextDouble() % AA_GRID_SIZE; // Random offset in X direction

                double offsetI = (subI + jitterY) * stepY;
                double offsetJ = (subJ + jitterX) * stepX;
                double Yi = -(i - (nY - 1) / 2d) * Ry + offsetI;
                double Xj = (j - (nX - 1) / 2d) * Rx + offsetJ;
                Point pIJ = p0;
                if (!isZero(Xj)) pIJ = pIJ.add(vRight.scale(Xj));
                if (!isZero(Yi)) pIJ = pIJ.add(vUp.scale(Yi));
                pIJ = pIJ.add(vTo.scale(distance)); // pIJ is the center of the pixel in the view plane

                Ray ray = new Ray(p0, pIJ.subtract(p0).normalize());
                rays.add(ray);
            }
        }
        return rays;
    }

    /**
     * Casts rays for a specific pixel in the image and writes the resulting color.
     *
     * This method handles the ray casting process for an individual pixel (j,i) in the image:
     * 1. Constructs multiple rays for the pixel (for anti-aliasing or supersampling)
     * 2. Traces each ray to determine its color contribution
     * 3. Averages all color contributions from the rays
     * 4. Writes the final color to the output image
     * 5. Updates the pixel processing manager
     *
     * @param nX  The horizontal resolution of the image
     * @param nY  The vertical resolution of the image
     * @param j   The horizontal coordinate of the pixel (column)
     * @param i   The vertical coordinate of the pixel (row)
     */
    private void castRayJ(int nX, int nY, int j, int i) {
        // Check if the coordinates are within the image bounds
        if (j < 0 || j >= Nx || i < 0 || i >= Ny) {
            return;  // Don't write if outside bounds
        }

        List<Ray> rays = constructRaysJ(nX, nY, j, i);
        Color color = Color.BLACK;
        for (Ray ray : rays) {
            color = color.add(rayTracer.traceRay(ray));
        }
        color = color.scale(1d / rays.size());
        imageWriter.writePixel(j, i, color);
        pixelManager.pixelDone();
    }

    /**
     * Helper method to cast a ray through a pixel with adaptive antialiasing and write its color.
     *
     * @param nX horizontal resolution
     * @param nY vertical resolution
     * @param j  horizontal pixel index
     * @param i  vertical pixel index
     */
    private void castRayAdaptive(int nX, int nY, int j, int i) {
        // Check if the coordinates are within the image bounds
        if (j < 0 || j >= Nx || i < 0 || i >= Ny) {
            return;  // Don't write if outside bounds
        }

        // Cast adaptive rays
        Color color = adaptiveCast(nX, nY, j, i, j + 1, i + 1, 0, AA_MAX_RECURSION_DEPTH, AA_VARIANCE_THRESHOLD);

        // Write the result to the image
        imageWriter.writePixel(j, i, color);
        pixelManager.pixelDone();
    }

    /**
     * Recursively casts rays for adaptive antialiasing.
     *
     * @param nX        horizontal resolution
     * @param nY        vertical resolution
     * @param minX      minimum X coordinate in pixel space
     * @param minY      minimum Y coordinate in pixel space
     * @param maxX      maximum X coordinate in pixel space
     * @param maxY      maximum Y coordinate in pixel space
     * @param depth     current recursion depth
     * @param maxDepth  maximum recursion depth
     * @param threshold variance threshold to stop subdivision
     * @return averaged color for the region
     */
    private Color adaptiveCast(int nX, int nY, double minX, double minY, double maxX, double maxY,
                               int depth, int maxDepth, double threshold) {
        // Base case: maximum depth reached
        if (depth >= maxDepth) {
            Ray ray = constructRay(nX, nY, (minX + maxX) / 2, (minY + maxY) / 2);
            return rayTracer.traceRay(ray);
        }

        // Sample at corners and center of the region
        Ray rayTopLeft = constructRay(nX, nY, minX, minY);
        Ray rayTopRight = constructRay(nX, nY, maxX, minY);
        Ray rayBottomLeft = constructRay(nX, nY, minX, maxY);
        Ray rayBottomRight = constructRay(nX, nY, maxX, maxY);
        Ray rayCenter = constructRay(nX, nY, (minX + maxX) / 2, (minY + maxY) / 2);

        // Get colors for the five sample points
        Color colorTopLeft = rayTracer.traceRay(rayTopLeft);
        Color colorTopRight = rayTracer.traceRay(rayTopRight);
        Color colorBottomLeft = rayTracer.traceRay(rayBottomLeft);
        Color colorBottomRight = rayTracer.traceRay(rayBottomRight);
        Color colorCenter = rayTracer.traceRay(rayCenter);

        // Calculate the average color of the five samples
        Color averageColor = colorTopLeft.add(colorTopRight)
                .add(colorBottomLeft)
                .add(colorBottomRight)
                .add(colorCenter)
                .scale(0.2);

        // Calculate the variance (measure of color differences)
        double variance = calculateColorVariance(
                new Color[]{colorTopLeft, colorTopRight, colorBottomLeft, colorBottomRight, colorCenter},
                averageColor);

        // If variance is low, we can stop subdividing
        if (variance < threshold) {
            return averageColor;
        }

        // Otherwise, recursively subdivide the region into four quadrants
        double midX = (minX + maxX) / 2;
        double midY = (minY + maxY) / 2;

        Color topLeftQuad = adaptiveCast(nX, nY, minX, minY, midX, midY,
                depth + 1, maxDepth, threshold);
        Color topRightQuad = adaptiveCast(nX, nY, midX, minY, maxX, midY,
                depth + 1, maxDepth, threshold);
        Color bottomLeftQuad = adaptiveCast(nX, nY, minX, midY, midX, maxY,
                depth + 1, maxDepth, threshold);
        Color bottomRightQuad = adaptiveCast(nX, nY, midX, midY, maxX, maxY,
                depth + 1, maxDepth, threshold);

        // Return the average color of the four quadrants
        return topLeftQuad.add(topRightQuad)
                .add(bottomLeftQuad)
                .add(bottomRightQuad)
                .scale(0.25);
    }

    /**
     * Calculate the variance of colors compared to their average.
     *
     * @param colors array of colors
     * @param average the average color
     * @return the variance value
     */
    private double calculateColorVariance(Color[] colors, Color average) {
        double variance = 0;

        for (Color color : colors) {
            // Calculate the color difference
            Color diff = color.subtract(average);

            // Since subtract clamps at 0, we need to handle negative values too
            Color diffNeg = average.subtract(color);

            // Use the lengthSquared method which computes the squared distance in RGB space
            double distanceSquared = diff.lengthSquared() + diffNeg.lengthSquared();

            variance += distanceSquared;
        }

        // Return average variance
        return variance / colors.length;
    }

    /**
     * Render image using multi-threading by parallel streaming
     * @return the camera object itself
     */
    private Camera renderImageStream() {
        IntStream.range(0, Ny).parallel()
                .forEach(i -> IntStream.range(0, Nx).parallel()
                        .forEach(j -> {
                            if (AA_FLAG) {
                                if (ADAPTIVE_AA_FLAG)
                                    castRayAdaptive(Nx, Ny, j, i);
                                else
                                    castRayJ(Nx, Ny, j, i);
                            }
                            else
                                castRay(j, i);
                        }));
        return this;
    }

    /**
     * Render image without multi-threading
     * @return the camera object itself
     */
    private Camera renderImageNoThreads() {
        for (int i = 0; i < Ny; ++i)
            for (int j = 0; j < Nx; ++j)
                if (AA_FLAG) {
                    if (ADAPTIVE_AA_FLAG)
                        castRayAdaptive(Nx, Ny, j, i);
                    else
                        castRayJ(Nx, Ny, j, i);
                }
                else
                    castRay(j, i);
        return this;
    }

    /**
     * Render image using multi-threading by creating and running raw threads
     * @return the camera object itself
     */
    private Camera renderImageRawThreads() {
        var threads = new LinkedList<Thread>();
        while (threadsCount-- > 0)
            threads.add(new Thread(() -> {
                Pixel pixel;
                while ((pixel = pixelManager.nextPixel()) != null)
                    if (AA_FLAG) {
                        if (ADAPTIVE_AA_FLAG)
                            castRayAdaptive(Nx, Ny, pixel.col(), pixel.row());
                        else
                            castRayJ(Nx, Ny, pixel.col(), pixel.row());
                    }
                    else
                        castRay(pixel.col(), pixel.row());
            }));
        for (var thread : threads) thread.start();
        try {
            for (var thread : threads) thread.join();
        } catch (InterruptedException ignored) {}
        return this;
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
         * Enables or disables adaptive antialiasing.
         *
         * @param enable true to enable adaptive antialiasing
         * @return this builder
         */
        public Builder setAdaptiveAntiAliasing(boolean enable) {
            camera.ADAPTIVE_AA_FLAG = enable;
            return this;
        }

        /**
         * Sets the maximum recursion depth for adaptive antialiasing.
         * Higher values produce better quality but take longer to render.
         *
         * @param depth the maximum recursion depth
         * @return this builder
         */
        public Builder setAdaptiveAntiAliasingDepth(int depth) {
            if (depth < 1) {
                throw new IllegalArgumentException("Recursion depth must be at least 1");
            }
            camera.AA_MAX_RECURSION_DEPTH = depth;
            return this;
        }

        /**
         * Sets the variance threshold for adaptive antialiasing.
         * Lower values produce higher quality but take longer to render.
         *
         * @param threshold the variance threshold
         * @return this builder
         */
        public Builder setAdaptiveAntiAliasingThreshold(double threshold) {
            if (threshold <= 0) {
                throw new IllegalArgumentException("Threshold must be positive");
            }
            camera.AA_VARIANCE_THRESHOLD = threshold;
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

        /**
         * Set multi-threading <br>
         * Parameter value meaning:
         * <ul>
         * <li>-2 - number of threads is number of logical processors less 2</li>
         * <li>-1 - stream processing parallelization (implicit multi-threading) is used</li>
         * <li>0 - multi-threading is not activated</li>
         * <li>1 and more - literally number of threads</li>
         * </ul>
         * @param threads number of threads
         * @return builder object itself
         */
        public Builder setMultithreading(int threads) {
            if (threads < -3)
                throw new IllegalArgumentException("Multithreading parameter must be -2 or higher");
            if (threads == -2) {
                int cores = Runtime.getRuntime().availableProcessors() - SPARE_THREADS;
                camera.threadsCount = cores <= 2 ? 1 : cores;
            } else
                camera.threadsCount = threads;
            return this;
        }

        /**
         * Set debug printing interval. If it's zero - there won't be printing at all
         * @param interval printing interval in %
         * @return builder object itself
         */
        public Builder setDebugPrint(double interval) {
            if (interval < 0) throw new IllegalArgumentException("interval parameter must be non-negative");
            camera.printInterval = interval;
            return this;
        }
    }
}