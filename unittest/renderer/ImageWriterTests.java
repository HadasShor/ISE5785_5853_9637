package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;

import static org.junit.jupiter.api.Assertions.*;

class ImageWriterTests {


    @Test
    void creatYellowSubmarineTest()
    {
        Color Red = new Color(java.awt.Color.RED);
        Color Yellow = new Color(java.awt.Color.YELLOW);
        int nx=800;
        int ny=500;
        int gap =50;
        ImageWriter imageWriter= new ImageWriter( nx, ny);
        for (int i=0; i< nx; i++)
        {
            for (int j=0; j< ny; j++)
            {
                if (i%gap==0 || j%gap==0)
                    imageWriter.writePixel(i,j,Red);
                else
                    imageWriter.writePixel(i,j,Yellow);
            }
        }
        imageWriter.writeToImage("YellowSubmarine");
    }

    @Test
    void createRedDotsOnYellowTest()
    {
        Color Red = new Color(java.awt.Color.CYAN);
        Color Yellow = new Color(java.awt.Color.YELLOW);
        int nx = 800;
        int ny = 500;
        int dotRadius = 5;
        int spacing = 50;
        ImageWriter imageWriter = new ImageWriter(nx, ny);

        for (int i = 0; i < nx; i++)
        {
            for (int j = 0; j < ny; j++)
            {
                boolean isDot = false;
                for (int x = spacing / 2; x < nx; x += spacing)
                {
                    for (int y = spacing / 2; y < ny; y += spacing)
                    {
                        int dx = i - x;
                        int dy = j - y;
                        if (dx * dx + dy * dy <= dotRadius * dotRadius)
                        {
                            isDot = true;
                            break;
                        }
                    }
                    if (isDot) break;
                }
                imageWriter.writePixel(i, j, isDot ? Red : Yellow);
            }
        }

        imageWriter.writeToImage("RedDotsOnYellow");
    }

}