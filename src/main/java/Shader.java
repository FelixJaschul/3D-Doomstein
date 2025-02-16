public class Shader {

    private int color;
    private final int[] pixels;
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final int factor = 32;

    public Shader(int color, int[] pixels, int x, int y, int width, int height) {

        this.color = color;
        this.pixels = pixels;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

    }

    public void loadShaders() {
		
		downScaling();

    }

	private void downScalingShader() {
		
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
