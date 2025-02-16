import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Texture {

    public static Texture wood = new Texture("res/room/wood.jpg", 64);
    public static Texture brick = new Texture("res/room/redbrick.jpg", 64);
    public static Texture bluestone = new Texture("res/room/bluestone.jpg", 64);
    public static Texture stone = new Texture("res/room/greystone.jpg", 64);
    public static Texture floor = new Texture("res/room/floor.jpg", 64);
    public static Texture ceiling = new Texture("res/room/ceiling.jpg", 64);
    public static Texture handNormal = new Texture("res/hand/handNormal.png", 64);
    public static Texture handFire = new Texture("res/hand/handFire.png", 64);
    public static Texture hand2BeforeFire = new Texture("res/hand/hand2BeforeFire.png", 64);
    public static Texture hand1BeforeFire = new Texture("res/hand/hand1BeforeFire.png", 64);

    public final int SIZE;
	public int[] pixels;

    private final String location;
    private BufferedImage image;

    // Konstruktor
    public Texture(String location, int size) {
        this.location = location;
        SIZE = size;
        pixels = new int[SIZE * SIZE];
        load();
    }

    private void load() {
        try {
            image = ImageIO.read(new File(location));
            int w = image.getWidth();
            int h = image.getHeight();
            image.getRGB(0, 0, w, h, pixels, 0, w); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Bild zurückgeben
    public BufferedImage getImage() {
        return image;
    }
}
