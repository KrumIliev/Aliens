package com.kdi.aliens.state;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import com.kdi.aliens.GamePanel;
import com.kdi.aliens.graphics.Background;
import com.kdi.aliens.input.KeyInput;
import com.kdi.aliens.util.ContentManager;
import com.kdi.aliens.util.Reference;
import com.kdi.aliens.util.Storage;

public class OptionsState extends GameState {

	private Background background;
	private String[] optionsData;

	public static String[] OPTIONS = { "Resolution", "Fullscreen", "Texture Quality", "Sound", "Sound Volume", "Music", "Music Volume", "Cancel",
			"Save" };
	private int currentChoise = 0;

	private byte navigationTimer = 0;

	private Font font;

	private int xOffset = 100;

	private boolean fullscreenSupported;
	private ArrayList<String> resolutions;
	private int currentResolution;

	public OptionsState(GameStateManager gameStateManager) {
		super(gameStateManager);
		optionsData = Storage.getOptions();

		try {

			Random random = new Random();

			if (random.nextBoolean()) {
				background = new Background(ContentManager.getImage(Reference.CM_BACKGROUND_MENU_1), 1);
			} else {
				background = new Background(ContentManager.getImage(Reference.CM_BACKGROUND_MENU_2), 1);
			}

			background.setVector(-0.5, 0);

			font = new Font("Comic Note", Font.PLAIN, 60);

			getScreenResolutions();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getScreenResolutions() {
		resolutions = new ArrayList<String>();

		GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = g.getDefaultScreenDevice();
		DisplayMode[] m = device.getDisplayModes();

		for (int j = 0; j < m.length; j++) {
			if (m[j].getWidth() > 1000) {
				String res = m[j].getWidth() + "x" + m[j].getHeight();
				if (!resolutions.contains(res)) {
					resolutions.add(res);
				}
			}
		}

		fullscreenSupported = device.isFullScreenSupported();

		String currRes = GamePanel.WIDTH + "x" + GamePanel.HEIGHT;
		for (int i = 0; i < resolutions.size(); i++) {
			System.out.println(resolutions.get(i));
			if (resolutions.get(i).equals(currRes)) {
				currentResolution = i;
			}
		}
	}

	@Override
	public void init() {}

	@Override
	public void update() {
		if (navigationTimer > 5) hanleInput();
		if (navigationTimer > 5) navigationTimer = 5;
		navigationTimer++;
		background.update();
	}

	@Override
	public void render(Graphics2D graphics) {
		background.render(graphics);

		graphics.setFont(font);

		int counter = 0;
		FontMetrics fm = graphics.getFontMetrics(font);

		for (int i = 0; i < OPTIONS.length; i++) {
			if (currentChoise == i) {
				graphics.setColor(Color.RED);
			} else {
				graphics.setColor(Color.MAGENTA);
			}

			if (i < OPTIONS.length - 2) {
				graphics.drawString(OPTIONS[i], xOffset, 200 + counter * 60);
				graphics.drawString(optionsData[i], GamePanel.WIDTH - fm.stringWidth(optionsData[i]) - xOffset, 200 + counter * 60);
				counter++;

			} else if (i < OPTIONS.length - 1) {
				graphics.drawString(OPTIONS[i], xOffset, GamePanel.HEIGHT - 100);

			} else {
				graphics.drawString(OPTIONS[i], GamePanel.WIDTH - fm.stringWidth(OPTIONS[i]) - xOffset, GamePanel.HEIGHT - 100);
			}
		}
	}

	@Override
	public void hanleInput() {
		if (KeyInput.keys[KeyEvent.VK_ENTER]) {
			navigationTimer = 0;
			if (currentChoise == 0) {
				changeResolution(1);
			} else if (currentChoise == 4 || currentChoise == 6) {
				volume(1);
			} else {
				select();
			}
		}

		if (KeyInput.keys[KeyEvent.VK_UP]) {
			navigationTimer = 0;
			currentChoise--;
			if (currentChoise == -1) currentChoise = OPTIONS.length - 1;
		}

		if (KeyInput.keys[KeyEvent.VK_DOWN]) {
			navigationTimer = 0;
			currentChoise++;
			if (currentChoise == OPTIONS.length) currentChoise = 0;
		}

		if (KeyInput.keys[KeyEvent.VK_LEFT]) {
			navigationTimer = 0;
			if (currentChoise == OPTIONS.length - 1) {
				currentChoise--;
			} else if (currentChoise < OPTIONS.length - 2) {

				if (currentChoise == 0) {
					changeResolution(-1);
				} else if (currentChoise == 4 || currentChoise == 6) {
					volume(-1);
				} else {
					select();
				}
			}
		}

		if (KeyInput.keys[KeyEvent.VK_RIGHT]) {
			navigationTimer = 0;
			if (currentChoise == OPTIONS.length - 2) {
				currentChoise++;
			} else if (currentChoise < OPTIONS.length - 2) {
				if (currentChoise == 0) {
					changeResolution(1);
				} else if (currentChoise == 4 || currentChoise == 6) {
					volume(1);
				} else {
					select();
				}
			}
		}
	}

	private void volume(int value) {
		int volume = Integer.valueOf(optionsData[currentChoise]);
		volume += value;
		if (volume > 10) volume = 10;
		if (volume < 1) volume = 1;
		optionsData[currentChoise] = String.valueOf(volume);
	}

	private void changeResolution(int value) {
		currentResolution += value;
		if (currentResolution == resolutions.size()) currentResolution = 0;
		if (currentResolution < 0) currentResolution = resolutions.size() - 1;
		optionsData[currentChoise] = resolutions.get(currentResolution);
	}

	private void select() {
		if (currentChoise == 1) {
			System.out.println(fullscreenSupported);
			if (fullscreenSupported) {
				if (optionsData[1].equalsIgnoreCase("off")) {
					optionsData[1] = "On";
				} else {
					optionsData[1] = "Off";
				}
			} else {
				optionsData[1] = "Off";
			}
		}

		if (currentChoise == 2) {
			if (optionsData[2].equalsIgnoreCase("high")) {
				optionsData[2] = "Low";
			} else if (optionsData[2].equalsIgnoreCase("medium")) {
				optionsData[2] = "High";
			} else {
				optionsData[2] = "Medium";
			}
		}

		if (currentChoise == 3) {
			if (optionsData[3].equalsIgnoreCase("off")) {
				optionsData[3] = "On";
			} else {
				optionsData[3] = "Off";
			}
		}

		if (currentChoise == 5) {
			if (optionsData[5].equalsIgnoreCase("off")) {
				optionsData[5] = "On";
			} else {
				optionsData[5] = "Off";
			}
		}

		if (currentChoise == 7) gameStateManager.setState(GameStateManager.MENU);

		if (currentChoise == 8) save();
	}

	private void save() {
		Storage.saveOptions(optionsData);
		gameStateManager.setState(GameStateManager.MENU);
	}
}
