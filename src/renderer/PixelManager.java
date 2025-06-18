package renderer;

/**
 * The PixelManager class handles concurrent pixel processing for ray tracing.
 *
 * It provides thread-safe methods for:
 * - Allocating pixels to different threads for processing
 * - Tracking rendering progress
 * - Displaying completion percentage at specified intervals
 *
 * This class is essential for implementing efficient multi-threaded rendering.
 */
public class PixelManager {

    /**
     * The maximum number of rows in the image
     */
    private final int maxRows;

    /**
     * The maximum number of columns in the image
     */
    private final int maxCols;

    /**
     * The interval at which to print progress (as a percentage)
     */
    private final double printInterval;

    /**
     * The total number of pixels to process in the image
     */
    private final int totalPixels;

    /**
     * Counter tracking how many pixels have been processed
     */
    private int pixelsDone = 0;

    /**
     * The current row being processed
     */
    private int currentRow = 0;

    /**
     * The current column being processed
     */
    private int currentCol = -1;

    /**
     * Constructs a new PixelManager for thread-safe pixel allocation.
     *
     * @param rows The number of rows in the image
     * @param cols The number of columns in the image
     * @param printInterval The interval at which to print progress (0-100)
     */
    public PixelManager(int rows, int cols, double printInterval) {
        this.maxRows = rows;
        this.maxCols = cols;
        this.printInterval = printInterval;
        this.totalPixels = rows * cols;
    }

    /**
     * Returns the next pixel to process in a thread-safe manner.
     * This method ensures that each pixel is processed exactly once,
     * even when called from multiple threads.
     *
     * @return The next pixel to process, or null if all pixels are allocated
     */
    public synchronized Pixel nextPixel() {
        if (++currentCol >= maxCols) {
            currentCol = 0;
            ++currentRow;
        }
        if (currentRow >= maxRows)
            return null;
        return new Pixel(currentRow, currentCol);
    }

    /**
     * Marks a pixel as processed and optionally prints progress.
     * This method is called after a pixel has been fully rendered.
     * When the specified progress interval is reached, it prints
     * the current completion percentage to the console.
     */
    public synchronized void pixelDone() {
        pixelsDone++;
        if (printInterval > 0 && pixelsDone % (int)(totalPixels * printInterval / 100) == 0) {
            double percent = (100.0 * pixelsDone) / totalPixels;
            System.out.printf("%.1f%% pixels done...\n", percent);
        }
    }
}