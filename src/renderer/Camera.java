package renderer;

import primitives.*;

import java.util.MissingResourceException;
/**
 * The {@code Camera} class represents a pinhole camera model in a 3D space.
 * It is defined by its position, view direction, up and right vectors,
 * and the view plane dimensions and distance.
 * <p>
 * Use the {@link Builder} class to construct a camera instance.
 */
public class Camera implements Cloneable {
    /** Camera position */
    private Point p0 = new Point(0, 0, 0);
    /** Camera direction */
    private Vector vTo = new Vector(0, 0, -1);
    /** Camera up vector */
    private Vector vUp = new Vector(0, 1, 0);
    /** Camera right vector */
    private Vector vRight = new Vector(1, 0, 0);
    /** Width of the view plane */
    private double width = 0.0;
    /** Height of the view plane */
    private double height = 0.0;
    /** Distance from the camera to the view plane */
    private double distance = 0.0;
    //X עמודות
    //Y שורות

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
        Point pIJ = p0;
        double yI = -(i - (nY - 1) / 2d) * height / nY;
        double xJ = (j - (nX - 1) / 2d) * width / nX;

        //check if xJ or yI are not zero, so we will not add zero vector
        if (!Util.isZero(xJ)) pIJ = pIJ.add(vRight.scale(xJ));
        if (!Util.isZero(yI)) pIJ = pIJ.add(vUp.scale(yI));

        // we need to move the point in the direction of vTo by distance
        pIJ = pIJ.add(vTo.scale(distance));

        return new Ray(p0, pIJ.subtract(p0).normalize());
    }
    /**
     * Constructs a ray through a pixel (planned overload).
     *
     * @param nX Number of pixels in the X direction
     * @param nY Number of pixels in the Y direction
     * @param j  Pixel index in X as double
     * @param i  Pixel index in Y as double
     * @return A {@link Ray} through the specified pixel
     */
    public Ray constructRayThroughPixel(int nX, int nY, double j, double i){
        //todo
        return null;
    }

    /**
     * Returns the position of the camera.
     * @return Camera position (P0)
     */
    public Point getP0() {
        return p0;
    }

    /**
     * Returns the view direction vector of the camera.
     * @return View direction vector (vTo)
     */
    public Vector getvTo() {
        return vTo;
    }

    /**
     * Returns the upward direction vector of the camera.
     * @return Up direction vector (vUp)
     */
    public Vector getvUp() {
        return vUp;
    }

    /**
     * Returns the right direction vector of the camera.
     * @return Right direction vector (vRight)
     */
    public Vector getvRight() {
        return vRight;
    }

    /**
     * Returns the width of the view plane.
     * @return Width of the view plane
     */
    public double getWidth() {
        return width;
    }

    /**
     * Returns the height of the view plane.
     * @return Height of the view plane
     */
    public double getHeight() {
        return height;
    }

    /**
     * Returns the distance from the camera to the view plane.
     * @return Distance from camera to view plane
     */
    public double getDistance() {
        return distance;
    }


    /**
     * Private constructor to prevent direct instantiation of Camera.
     * Use the {@link Builder} to create instances.
     */
    private Camera(){
        // Private constructor to prevent instantiation
    }
    /**
     * Creates and returns a new {@link Builder} for constructing a {@link Camera}.
     * @return a builder instance
     */
    public static Builder getBuilder() {
        return new Builder();
    }


    /**
     * The {@code Builder} class is used to construct a {@link Camera} instance
     * with a fluent API style.
     */
    public static class Builder {
        /** The camera being built */
        private Camera camera = new Camera();
        /**
         * Constructs a new {@code Builder} for creating a {@link Camera} object.
         */
        public Builder() {
            // default constructor
        }
        /**
         * Sets the location (P₀) of the camera.
         *
         * @param point The 3D point representing the camera position
         * @return This builder instance
         */
        public Builder setLocation(Point point) {
            camera.p0 = point;
            return this;
        }
        /**
         * Sets the view direction (vTo) and up vector (vUp).
         *
         * @param vTo Direction the camera is looking at
         * @param vUp The upward direction vector
         * @return This builder instance
         * @throws IllegalArgumentException if the vectors are not orthogonal
         */
        public Builder setDirection(Vector vTo, Vector vUp) {
            if( vTo.dotProduct(vUp)==0) {
                camera.vTo = vTo.normalize();
                camera.vUp = vUp.normalize();
                //camera.vRight = vTo.crossProduct(vUp).normalize();
            } else {
                throw new IllegalArgumentException("vTo and vUp must be orthogonal");
            }
            return this;
        }
        /**
         * Sets the direction using a target point and up vector.
         *
         * @param point A target point the camera is looking at
         * @param vUp   The up vector
         * @return This builder instance
         * @throws IllegalArgumentException if cross product fails
         */
    public Builder setDirection(Point point, Vector vUp) {
        camera.vTo = point.subtract(camera.p0).normalize();
        camera.vRight = camera.vTo.crossProduct(vUp).normalize();
        if (Util.isZero(camera.vRight.length()))
        {
            throw new IllegalArgumentException("vUp and vRight must be zero");
        }
        camera.vUp = camera.vRight.crossProduct(camera.vTo).normalize();

        return this;
    }
        /**
         * Sets the direction using a target point and default up vector (Y-axis).
         *
         * @param point A target point the camera is looking at
         * @return This builder instance
         */
    public Builder setDirection(Point point) {

        return  setDirection(point, Vector.AXIS_Y);

    }
        /**
         * Sets the view plane size.
         *
         * @param width  Width of the view plane
         * @param height Height of the view plane
         * @return This builder instance
         * @throws IllegalArgumentException if width or height is non-positive
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
         * Sets the view plane distance.
         *
         * @param distance Distance from camera to view plane
         * @return This builder instance
         * @throws IllegalArgumentException if distance is non-positive
         */
    public Builder setVpDistance(double distance) {
        if (distance <= 0.0) {
           throw new IllegalArgumentException("distance must be >= 0.0");}
        camera.distance = distance;
        return this;
    }
        /**
         * Dummy method for future resolution setting.
         *
         * @param nX Number of columns
         * @param nY Number of rows
         * @return This builder instance
         */
    public Builder setResolution(int nX, int nY) {
            return this;
    }

        /**
         * Builds and returns a {@link Camera} instance with the provided parameters.
         *
         * @return A valid {@code Camera} instance
         * @throws MissingResourceException if any required parameter is missing
         * @throws IllegalArgumentException if vectors are not orthogonal or normalized
         */
    public Camera build()  {
        if(camera.p0 == null)
            throw new MissingResourceException("p0 must has value","Camera", "p0");
        if(camera.vUp == null)
            throw new MissingResourceException("vUp must has value","Camera", "vUp");
        if(camera.vTo == null)
            throw new MissingResourceException("vTo must has value","Camera", "vTo");
        if(camera.width==0)
            throw new MissingResourceException("Width must be greater than 0", "Camera", "width");
        if(camera.height==0)
            throw new MissingResourceException("Height must be greater than 0", "Camera", "height");
        if(camera.distance==0)
            throw new MissingResourceException("Distance must be greater than 0", "Camera", "distance");
        if(camera.p0==null)
            throw new MissingResourceException("Camera position must be set", "Camera", "p0");
        Point p0 = new Point(0,0,0); // Camera position

        camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();

        if (!Util.isZero(camera.vTo.dotProduct(camera.vRight)) ||
        !Util.isZero(camera.vTo.dotProduct(camera.vUp)) ||
                !Util.isZero(camera.vRight.dotProduct(camera.vUp)))
            throw new IllegalArgumentException("vTo, vUp and vRight must be orthogonal");

//        if (Util.isCloseToOne(camera.vTo.length()) != 1 || camera.vUp.length() != 1 || camera.vRight.length() != 1)
//            throw new IllegalArgumentException("vTo, vUp and vRight must be normalized");
        if (!Util.isCloseToOne(camera.vTo.length()) || !Util.isCloseToOne(camera.vUp.length()) || !Util.isCloseToOne(camera.vRight.length()))
            throw new IllegalArgumentException("vTo, vUp and vRight must be normalized");

        if (camera.width <= 0 || camera.height <= 0)
            throw new IllegalArgumentException("width and height must be positive");

        if (camera.distance <= 0)
            throw new IllegalArgumentException("distance from camera to view must be positive");

        try {
                return (Camera) camera.clone();
            }
            catch (CloneNotSupportedException e) {
                return null;
            }
        }
    }

}
