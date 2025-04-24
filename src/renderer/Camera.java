package renderer;

import primitives.*;

import java.util.MissingResourceException;

public class Camera implements Cloneable {
    // Camera properties
    private Point p0 = new Point(0,0,0); // Camera position
    private Vector vTo = new Vector(0,0,-1); // Camera direction
    private Vector vUp = new Vector(0,1,0); // Camera up vector
    private Vector vRight = new Vector(1,0,0); // Camera right vector
    private double width = 0.0; // Width of the view plane
    private double height = 0.0; // Height of the view plane
    private double distance = 0.0; // Distance from the camera to the view plane
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
    public Ray constructRayThroughPixel(int nX, int nY, double j, double i){
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

    private Camera(){
        // Private constructor to prevent instantiation
    }
    public static Builder getBuilder() {
        return new Builder();
    }

    public static class Builder {
        private  Camera camera = new Camera();

        public Builder setLocation(Point point) {
            camera.p0 = point;
            return this;
        }

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

    public Builder setDirection(Point point, Vector vUp) {
        camera.vTo = point.subtract(camera.p0).normalize();
        camera.vRight = camera.vTo.crossProduct(vUp).normalize();
        camera.vUp = camera.vTo.crossProduct(camera.vRight).normalize();

        return this;
    }

    public Builder setDirection(Point point) {
        camera.vTo = point.subtract(camera.p0).normalize();
        camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();
        return this;
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
        if (distance < 0.0) {
           throw new IllegalArgumentException("distance must be >= 0.0");}
        camera.distance = distance;
        return this;
    }

    public Builder setResolution(int nX, int nY) {
            return this;
    }

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

        if (camera.vTo.length() != 1 || camera.vUp.length() != 1 || camera.vRight.length() != 1)
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
