package src;

import src.Texture;
import src.Camera;
import src.Screen;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Cursor;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

// This class has the main game loop and map data
public class Game extends JFrame implements Runnable{

	private static final long serialVersionUID = 1L;
	public int mapWidth = 16;
	public int mapHeight = 16;
	public int n = 2; // scaling Factor

	private boolean running;

	public int[] pixels;
	public ArrayList<Texture> textures;

	public Thread thread;
	public BufferedImage image;
	public Camera camera;
	public Screen screen;

	private GraphicsDevice device;

	public static int[][] map =
		// TODO Create Level-Editor for this shit
		{
			{2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},
			{2,0,0,0,0,0,0,0,2,0,0,0,0,0,0,2},
			{2,0,1,1,1,1,1,0,0,0,0,0,0,0,0,2},
			{2,0,1,0,0,0,1,0,2,0,0,0,0,0,0,2},
			{2,0,1,0,0,0,1,0,2,2,2,0,0,2,2,2},
			{2,0,1,0,0,0,1,0,2,0,0,0,0,0,0,2},
			{2,0,1,0,0,0,1,0,2,0,0,0,0,0,0,2},
			{2,0,0,0,0,0,0,0,2,0,0,0,0,0,0,2},
			{2,2,2,0,0,0,2,2,2,2,2,0,0,2,2,2},
			{2,0,0,0,0,0,2,0,0,0,0,0,0,0,0,2},
			{2,0,0,0,0,0,2,0,0,0,0,0,0,0,0,2},
			{2,0,0,0,0,0,2,0,0,2,2,2,2,0,0,2},
			{2,0,0,0,0,0,2,0,0,2,2,2,2,0,0,2},
			{2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
			{2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2}
		};

	public Game() {

		int width = 480 * n;
		int height = 640 * n;

		thread = new Thread(this);
		image = new BufferedImage(height, width, BufferedImage.TYPE_INT_RGB);
		textures = new ArrayList<Texture>();
		camera = new Camera(4.5, 4.5, 1, 0, 0, -.66, width, height, this);
		screen = new Screen(map, mapWidth, mapHeight, textures, height, width);

		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();

		textures.add(Texture.wood);
		textures.add(Texture.brick);
		textures.add(Texture.bluestone);
		textures.add(Texture.stone);
		textures.add(Texture.floor);
		textures.add(Texture.ceiling);

		addKeyListener(camera);
		addMouseMotionListener(camera);

		setCursor(getToolkit().createCustomCursor(
			new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB),
			new Point(0, 0), "blank cursor"
		));

		setSize(height, width);
		setUndecorated(true);
		setResizable(false);
		setTitle("Untitled");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.black);
		setLocationRelativeTo(null);

		setVisible(true);
		requestFocus();
		start();
	}	

	private synchronized void start() {

		running = true;
		thread.start();
	}

	public synchronized void stop() {

		running = false;

		try { thread.join(); } 
		catch(InterruptedException e) { e.printStackTrace(); }
	}

	public void render() {

		BufferStrategy bs = getBufferStrategy();

		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();

		g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null); // Render die Szene

		/* BufferedImage handImage;

		try {
			handImage = ImageIO.read(new File("res/hand/hand5.png"));
			g.drawImage(handImage, 120 * n, 220 * n, 380 * n, 380 * n, null); // Waffe zentriert vor die Kamera zeichnen
		} catch (IOException e) { e.printStackTrace(); } */

		g.dispose();
		bs.show();
	}

	public void run() {

		long lastTime = System.nanoTime();
		final double ns = 1000000000.0 / 60.0; // 60 times per second
		double delta = 0;

		requestFocus();

		while(running) {
			long now = System.nanoTime();
			delta = delta + ((now-lastTime) / ns);
			lastTime = now;

			while (delta >= 1) { // Make sure update is only happening 60 times a second
				// handles all of the logic restricted time
				screen.update(camera, pixels);
				camera.update(map);

				delta--;
			}

			render();
		}
	}

	public static void main(String [] args) {

		Game game = new Game();
	}
}
