public class Shader {

    private int color;
    private int[] pixels;
    private int x;
    private int y;
    private int width;
    private int height;

    public Shader(int color, int[] pixels, int x, int y, int width, int height) {

        this.color = color;
        this.pixels = pixels;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

    }

    public void loadShader() {

        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        int factor = 32; // Higher factor = fewer colors, more pixel-art look
        r = (r / factor) * factor;
        g = (g / factor) * factor;
        b = (b / factor) * factor;

        color = (r << 16) | (g << 8) | b;

        pixels[x + y*(width)] = color;

    }

}
