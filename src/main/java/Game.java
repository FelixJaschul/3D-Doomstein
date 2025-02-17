import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.Serial;
import java.util.ArrayList;

public class Game extends JFrame implements Runnable {

    @Serial
    private static final long serialVersionUID = 1L;
    public static int[][] map =
            {
                    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
                    {2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2},
                    {2, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 2},
                    {2, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 2},
                    {2, 0, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0, 0, 2, 2, 2},
                    {2, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 2},
                    {2, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 2},
                    {2, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 2},
                    {2, 2, 2, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 2, 2, 2},
                    {2, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 2},
                    {2, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 2},
                    {2, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 2},
                    {2, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 2},
                    {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
                    {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}
            };

    private final int scalingFactor = 2;

    public int[] pixels;
    public final Camera camera;

    private final Thread thread;
    private final BufferedImage image;
    private final Screen screen;
    private boolean running;

    public Game() {

        int height = 480 * scalingFactor;
        int width = 640 * scalingFactor;

        thread = new Thread(this);
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        ArrayList<Texture> textures = new ArrayList<Texture>();
        camera = new Camera(4.5, 4.5, 1, 0, 0, -.66, width, height, this);
        int mapWidth = 16;
        int mapHeight = 16;
        screen = new Screen(map, mapWidth, mapHeight, textures, width, height);

        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

        textures.add(Texture.wood); // 0
        textures.add(Texture.brick); // 1
        textures.add(Texture.bluestone); // 2
        textures.add(Texture.stone); // 3

        textures.add(Texture.floor); // 4
        textures.add(Texture.ceiling); // 5

        addKeyListener(camera);
        addMouseMotionListener(camera);

        setCursor(getToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "blank cursor"));
        setSize(width, height);
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

    public static void main(String[] args) {

        Game game = new Game();
    }

    private synchronized void start() {

        running = true;
        thread.start();
    }

    public synchronized void stop() {

        running = false;

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void render() {

        boolean fire0 = false, fire1 = false, fire2 = false;
        boolean idle = true;

        BufferedImage handNormal = Texture.handNormal.getImage();
        BufferedImage handFire = Texture.handFire.getImage();
        BufferedImage hand1BeforeFire = Texture.hand1BeforeFire.getImage();
        BufferedImage hand2BeforeFire = Texture.hand2BeforeFire.getImage();

        BufferStrategy bs = getBufferStrategy();

        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null); // Render die Szene

        // if (idle) g.drawImage(handNormal, 350 * scalingFactor, 360 * scalingFactor, 120 * scalingFactor, 120 * scalingFactor, null);
        if (fire0) g.drawImage(handFire, 350 * scalingFactor, 360 * scalingFactor, 120 * scalingFactor, 120 * scalingFactor, null);
        if (fire1) g.drawImage(hand1BeforeFire, 350 * scalingFactor, 360 * scalingFactor, 120 * scalingFactor, 120 * scalingFactor, null);
        if (fire2) g.drawImage(hand2BeforeFire, 350 * scalingFactor, 360 * scalingFactor, 120 * scalingFactor, 120 * scalingFactor, null);

        g.dispose();
        bs.show();
    }

    public void run() {

        long lastTime = System.nanoTime();
        final double ns = 1000000000.0 / 60.0; // 60 times per second
        double delta = 0;

        requestFocus();

        while (running) {
            long now = System.nanoTime();
            delta = delta + ((now - lastTime) / ns);
            lastTime = now;

            while (delta >= 1) { // Make sure update is only happening 60 times a second
                // handles all the logic restricted time
                screen.update(camera, pixels);
                camera.update(map);

                delta--;
            }

            render();
        }
    }
}
