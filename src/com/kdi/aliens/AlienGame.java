package com.kdi.aliens;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.kdi.aliens.input.KeyInput;
import com.kdi.aliens.state.GameStateManager;
import com.kdi.aliens.util.ContentManager;
import com.kdi.aliens.util.Reference;

public class AlienGame extends Canvas implements Runnable {

	static {
		System.setProperty("sun.java2d.opengl", "True");
		System.setProperty("sun.java2d.d3d", "True");
		System.setProperty("sun.java2d.ddforcevram", "True");
		System.setProperty("sun.java2d.transaccel", "True");
	}

	private static final long serialVersionUID = 1L;

	private boolean isRunning = true;
	private BufferStrategy strategy;
	private BufferedImage background;
	private Graphics2D backgroundGraphics;
	private Graphics2D graphics;
	private JFrame frame;
	private Thread gameThread;

	private static GraphicsConfiguration config = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
			.getDefaultConfiguration();

	public static final String GAME_NAME = "ALIENS ARE REAL";
	public static final int WIDTH = 1280; // Window width
	public static final int HEIGHT = 720; // Window height

	private GameStateManager gameStateManager;

	// create a hardware accelerated image
	public final BufferedImage create(final int width, final int height, final boolean alpha) {
		return config.createCompatibleImage(width, height, alpha ? Transparency.TRANSLUCENT : Transparency.OPAQUE);
	}

	public synchronized void start() {
		isRunning = true;
		gameThread = new Thread(this);
		gameThread.start();
	}

	public synchronized void stop() {
		isRunning = false;
		try {
			gameThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public AlienGame() {
		super(config);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));

		frame = new JFrame(GAME_NAME);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);
		frame.pack();
		frame.setVisible(true);

		addKeyListener(new KeyInput());

		background = create(WIDTH, HEIGHT, false);

		createBufferStrategy(2);
		do {
			strategy = getBufferStrategy();
		} while (strategy == null);

		start();
	}

	private Graphics2D getBuffer() {
		if (graphics == null) {
			try {
				graphics = (Graphics2D) strategy.getDrawGraphics();
			} catch (IllegalStateException e) {
				return null;
			}
		}
		return graphics;
	}

	private boolean updateScreen() {
		graphics.dispose();
		graphics = null;

		try {
			strategy.show();
			Toolkit.getDefaultToolkit().sync();
			return (!strategy.contentsLost());

		} catch (NullPointerException e) {
			return true;

		} catch (IllegalStateException e) {
			return true;
		}
	}

	private void loading(Graphics2D g) {

		try {

			Graphics2D gb = getBuffer();

			BufferedImage loadingImage = ImageIO.read(AlienGame.class.getResourceAsStream(Reference.RESOURCE_BACKGROUNDS + "menu_background.png"));
			BufferedImage loadingText = ImageIO.read(AlienGame.class.getResourceAsStream(Reference.RESOURCE_BACKGROUNDS + "loading.png"));

			g.drawImage(loadingImage, 0, 0, null);
			g.drawImage(loadingText, WIDTH / 2 - loadingText.getWidth() / 2, HEIGHT / 2 - loadingText.getHeight() / 2, null);

			gb.drawImage(background, 0, 0, null);
			gb.dispose();
			updateScreen();

		} catch (IOException e) {
			e.printStackTrace();
		}

		ContentManager.loadImages();
		ContentManager.loadFonts();
		ContentManager.loadAudio();
	}

	@Override
	public void run() {
		backgroundGraphics = (Graphics2D) background.getGraphics();
		backgroundGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);

		loading(backgroundGraphics);

		long fpsWait = (long) (1.0 / 90 * 1000); // TODO check value
		gameStateManager = new GameStateManager();

		requestFocus();

		while (isRunning) {
			long renderStart = System.nanoTime();
			updateGame();

			do {
				Graphics2D bg = getBuffer();

				renderGame(backgroundGraphics);
				bg.drawImage(background, 0, 0, null);
				bg.dispose();

			} while (!updateScreen());

			// Better do some FPS limiting here
			long renderTime = (System.nanoTime() - renderStart) / 1000000;
			try {
				Thread.sleep(Math.max(0, fpsWait - renderTime));
			} catch (InterruptedException e) {
				Thread.interrupted();
				break;
			}
			renderTime = (System.nanoTime() - renderStart) / 1000000;
		}

		stop();
	}

	public void updateGame() {
		gameStateManager.update();
	}

	public void renderGame(Graphics2D g) {
		gameStateManager.render(g);
	}

	public static void main(String[] args) {
		new AlienGame();
	}

}
