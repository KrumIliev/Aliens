package com.kdi.aliens;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.kdi.aliens.input.KeyInput;
import com.kdi.aliens.state.GameStateManager;
import com.kdi.aliens.util.ContentManager;
import com.kdi.aliens.util.Reference;

public class GamePanel extends JPanel implements Runnable {

	private static final long serialVersionUID = -1796066489627262395L;

	public static final String GAME_NAME = "ALIENS ARE REAL";
	public static final int WIDTH = 1280; // Window width
	public static final int HEIGHT = 720; // Window height
	public static final int SCALE = 1; // Used for lowering graphics

	private Thread thread;
	private boolean running;
	private int FPS = 60;
	private long targetTime = 1000 / FPS;

	private BufferedImage image;
	private Graphics2D graphics;

	private GameStateManager gameStateManager;

	public GamePanel() {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
	}

	private void init() {
		BufferedImage temp = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gd.getDefaultConfiguration();
		image = gc.createCompatibleImage(temp.getWidth(), temp.getHeight(), temp.getTransparency());
		graphics = (Graphics2D) image.getGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);

		/**
		 * Render load screen
		 */
		try {
			BufferedImage loadingImage = ImageIO.read(GamePanel.class.getResourceAsStream(Reference.RESOURCE_BACKGROUNDS + "menu_background.png"));
			BufferedImage loadingText = ImageIO.read(GamePanel.class.getResourceAsStream(Reference.RESOURCE_BACKGROUNDS + "loading.png"));

			graphics.drawImage(loadingImage, 0, 0, null);
			graphics.drawImage(loadingText, WIDTH / 2 - loadingText.getWidth() / 2, HEIGHT / 2 - loadingText.getHeight() / 2, null);
			drawToScreen();

		} catch (IOException e) {
			e.printStackTrace();
		}

		/**
		 * Loading resources
		 */
		ContentManager.loadImages();
		ContentManager.loadFonts();
		ContentManager.loadAudio();

		/**
		 * Running game
		 */
		running = true;
		gameStateManager = new GameStateManager();
	}

	private void update() {
		gameStateManager.update();
	}

	private void drawToScreen() {
		Graphics graphics = getGraphics();
		graphics.drawImage(image, 0, 0, WIDTH, HEIGHT, null);
		graphics.dispose();
	}

	private void draw() {
		gameStateManager.render(graphics);
	}

	@Override
	public void run() {
		init();
		long start, elapsed, wait;

		while (running) {

			start = System.nanoTime();

			update();
			draw();
			drawToScreen();

			elapsed = System.nanoTime() - start;
			wait = targetTime - elapsed / 1000000;
			if (wait < 0) wait = 5;

			try {
				Thread.sleep(wait);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void addNotify() {
		super.addNotify();
		if (thread == null) {
			thread = new Thread(this);
			addKeyListener(new KeyInput());
			thread.start();
		}
	}
}
