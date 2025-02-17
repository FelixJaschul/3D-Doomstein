public class Shader {

    private int color;
    private final int[] pixels;
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final int factor = 32;
    private final int blockSize = 8;

    private static final char[] asciiChars = {'@', '#', '%', '*', '+', '=', '-', ':', '.', ' '};

    public Shader(int color, int[] pixels, int x, int y, int width, int height) {

        this.color = color;
        this.pixels = pixels;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

    }

    public void loadShaders() {
		
		downScaleShader(true);
        grayScaleShader(true, 0.2126, 0.7152, 0.0722);
        asciiShader(false);

    }

    private double getAverageBrightness() {
        double totalBrightness = 0;
        int pixelCount = 0;

        for (int blockY = y; blockY < y + blockSize && blockY < height; blockY++) {
            for (int blockX = x; blockX < x + blockSize && blockX < width; blockX++) {

                int color = pixels[blockX + blockY * width];

                int r = (color >> 16) & 0xFF;
                int g = (color >> 8) & 0xFF;
                int b = color & 0xFF;

                double brightness = (0.2126 * r + 0.7152 * g + 0.0722 * b) / 255.0;

                totalBrightness += brightness;
                pixelCount++;

            }

        }

        return totalBrightness / pixelCount;
    }

    private void downScaleShader(boolean running) {

        if (running) {

            int r = (color >> 16) & 0xFF;
            int g = (color >> 8) & 0xFF;
            int b = color & 0xFF;

            // Higher factor = fewer colors, more pixel-art look
            r = (r / factor) * factor;
            g = (g / factor) * factor;
            b = (b / factor) * factor;

            color = (r << 16) | (g << 8) | b;

            pixels[x + y * (width)] = color;

        }

    }

    private void grayScaleShader(boolean running, double r1, double g1, double b1) {

        if (running) {

            int r = (color >> 16) & 0xFF;
            int g = (color >> 8) & 0xFF;
            int b = color & 0xFF;

            double brightness = (r1 * r + g1 * g + b1 * b) / 255.0;

            int color = (int) (brightness * 255);
            color = (color << 16) | (color << 8) | color;

            pixels[x + y * width] = color;

        }

    }

    private void asciiShader(boolean running) {

        if (running) {

            if (x % blockSize == 0 && y % blockSize == 0) {

                double averageBrightness = getAverageBrightness();

                int index = (int) (averageBrightness * (asciiChars.length - 1));
                char asciiChar = asciiChars[index];

                int asciiColor = (int) (averageBrightness * 255);
                asciiColor = (asciiColor << 16) | (asciiColor << 8) | asciiColor;

                // System.out.println("Brightness: " + averageBrightness + ", ASCII: " + asciiChar + ", Color: " + asciiColor);

                for (int blockY = y; blockY < y + blockSize && blockY < height; blockY++) {

                    for (int blockX = x; blockX < x + blockSize && blockX < width; blockX++) {

                        pixels[blockX + blockY * width] = asciiColor;

                    }

                }

            }

        }

    }

}
