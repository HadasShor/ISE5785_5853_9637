package renderer;

/**
 * PixelManager for managing concurrent pixel access and progress printing.
 */
public class PixelManager {
    private final int maxRows;
    private final int maxCols;
    private final double printInterval;
    private final int totalPixels;
    private int pixelsDone = 0;
    private int currentRow = 0;
    private int currentCol = -1;

    public PixelManager(int rows, int cols, double printInterval) {
        this.maxRows = rows;
        this.maxCols = cols;
        this.printInterval = printInterval;
        this.totalPixels = rows * cols;
    }

    /**
     * Returns the next pixel to process, or null if done.
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
     * Marks a pixel as done and optionally prints progress.
     */
    public synchronized void pixelDone() {
        pixelsDone++;
        if (printInterval > 0 && pixelsDone % (int)(totalPixels * printInterval / 100) == 0) {
            double percent = (100.0 * pixelsDone) / totalPixels;
            System.out.printf("%.1f%% pixels done...\n", percent);
        }
    }
}