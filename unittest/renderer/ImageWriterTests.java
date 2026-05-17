package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;

/**
 * Test class for {@link renderer.ImageWriter}
 */
class ImageWriterTests {
    /**
     * Test data
     */
    private static final int xRes = 800;
    private static final int yRes = 500;
    private static final int rectangleSize = 50;
    private static final Color rectangleColor = new Color(245, 245, 32);
    private static final Color borderColor = new Color(16, 6, 204);

    /**
     * Test method for creating image using {@link renderer.ImageWriter}
     */
    @Test
    void testImageWriter() {
        final ImageWriter testImageWriter = new ImageWriter(xRes, yRes);
        for (int x = 0; x < xRes; x++) {
            for (int y = 0; y < yRes; y++) {
                testImageWriter.writePixel(x, y, (x % rectangleSize == 0 || y % rectangleSize == 0 ? borderColor : rectangleColor));
            }
        }

        testImageWriter.writeToImage("ImageWriter test image");
    }
}
