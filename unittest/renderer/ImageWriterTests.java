package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link ImageWriter} class.
 * <p>
 * This class contains a series of tests for the methods implemented in the {@link ImageWriter} class,
 * ensuring that the image writing operations behave as expected. The tests include creating images with
 * different patterns and colors.
 * </p>
 */
class ImageWriterTests {

    /**
     * Test method for creating an image named "YellowSubmarine".
     * This image consists of a yellow background with a red grid drawn every 50 pixels.
     * The grid helps in verifying that pixel placement is accurate and uniform.
     */
    @Test
    void creatYellowSubmarineTest() {
        /** Red color used for the grid lines */
        Color Red = new Color(java.awt.Color.RED);
        /** Yellow color used for the background */
        Color Yellow = new Color(java.awt.Color.YELLOW);
        /** Image width in pixels */
        int nx = 800;
        /** Image height in pixels */
        int ny = 500;
        /** Distance between grid lines */
        int gap = 50;
        /** Instance of ImageWriter for creating the image */
        ImageWriter imageWriter = new ImageWriter(nx, ny);

        for (int i = 0; i < nx; i++) {
            for (int j = 0; j < ny; j++) {
                // Draw red grid lines at every 'gap' interval, otherwise fill with yellow
                if (i % gap == 0 || j % gap == 0)
                    imageWriter.writePixel(i, j, Red);
                else
                    imageWriter.writePixel(i, j, Yellow);
            }
        }
        // Write the generated image to a file named "YellowSubmarine"
        imageWriter.writeToImage("YellowSubmarine");
    }

    /**
     * Test method for creating an image named "RedDotsOnYellow".
     * This image consists of a yellow background with cyan dots arranged in a grid pattern.
     * Each dot is a filled circle with a given radius and spacing between them.
     */
    @Test
    void createRedDotsOnYellowTest() {
        /** Cyan color used for the dots (despite the method name suggesting red) */
        Color Red = new Color(java.awt.Color.CYAN);
        /** Yellow color used for the background */
        Color Yellow = new Color(java.awt.Color.YELLOW);
        /** Image width in pixels */
        int nx = 800;
        /** Image height in pixels */
        int ny = 500;
        /** Radius of each dot in pixels */
        int dotRadius = 5;
        /** Distance between centers of adjacent dots */
        int spacing = 50;
        /** Instance of ImageWriter for creating the image */
        ImageWriter imageWriter = new ImageWriter(nx, ny);

        for (int i = 0; i < nx; i++) {
            for (int j = 0; j < ny; j++) {
                /** Flag to determine whether the current pixel is part of a dot */
                boolean isDot = false;

                for (int x = spacing / 2; x < nx; x += spacing) {
                    for (int y = spacing / 2; y < ny; y += spacing) {
                        /** Horizontal distance from current pixel to dot center */
                        int dx = i - x;
                        /** Vertical distance from current pixel to dot center */
                        int dy = j - y;
                        // If the pixel is within the radius of a dot center, mark it as a dot
                        if (dx * dx + dy * dy <= dotRadius * dotRadius) {
                            isDot = true;
                            break;
                        }
                    }
                    if (isDot) break;
                }

                // Write either a dot color or background color to the pixel
                imageWriter.writePixel(i, j, isDot ? Red : Yellow);
            }
        }

        // Write the generated image to a file named "RedDotsOnYellow"
        imageWriter.writeToImage("RedDotsOnYellow");
    }




}