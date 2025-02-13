package src;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

// This class contains image utilities
public class Texture {

	public int[] pixels;
	private String loc;
	public final int SIZE;

	public BufferedImage image;

	// Konstruktor
	public Texture(String location, int size) {
		loc = location;
		SIZE = size;
		pixels = new int[SIZE * SIZE]; // Initialisiere das Pixel-Array
		load();  // Lade das Bild
	}

	// Lade das Bild und speichere es in `image`
	private void load() {
		try {
			image = ImageIO.read(new File(loc)); // Bild laden
			int w = image.getWidth();
			int h = image.getHeight();
			image.getRGB(0, 0, w, h, pixels, 0, w); // RGB-Werte in Pixel-Array kopieren
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Bild zurückgeben
	public BufferedImage getImage() {
		return image;
	}

	// Statische Instanzen von Texturen
	public static Texture wood = new Texture("res/wood.jpg", 64);
	public static Texture brick = new Texture("res/redbrick.jpg", 64);
	public static Texture bluestone = new Texture("res/bluestone.jpg", 64);
	public static Texture stone = new Texture("res/greystone.jpg", 64);

	public static Texture floor = new Texture("res/floor.jpg", 64);
	public static Texture ceiling = new Texture("res/ceiling.jpg", 64);

	public static Texture handNormal = new Texture("res/hand/handNormal.png", 64);
	public static Texture handFire = new Texture("res/hand/handFire.png", 64);
	public static Texture hand2BeforeFire = new Texture("res/hand/hand2BeforeFire.png", 64);
	public static Texture hand1BeforeFire = new Texture("res/hand/hand1BeforeFire.png", 64);
}
