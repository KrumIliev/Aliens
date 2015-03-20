package com.kdi.aliens.state;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Random;

import com.kdi.aliens.GamePanel;
import com.kdi.aliens.graphics.Background;
import com.kdi.aliens.input.KeyInput;
import com.kdi.aliens.util.AudioPlayer;
import com.kdi.aliens.util.Reference;
import com.kdi.aliens.util.Storage;

public class OptionsState extends GameState {

	private Background background;
	private String[] optionsData;

	public static String[] OPTIONS = { "Texture Quality", "Sound", "Music", "Cancel", "Save" };
	private int currentChoise = 0;

	private byte navigationTimer = 0;

	private AudioPlayer audioPlayer;

	private Font font;

	private int xOffset = 250;

	public OptionsState(GameStateManager gameStateManager) {
		super(gameStateManager);
		optionsData = Storage.getOptions();

		try {

			Random random = new Random();

			if (random.nextBoolean()) {
				background = new Background("menu_background.png", 1);
			} else {
				background = new Background("menu_background_1.png", 1);
			}

			background.setVector(-0.5, 0);

			font = new Font("Comic Note", Font.PLAIN, 60);

			audioPlayer = new AudioPlayer(Reference.RESOURCE_MUSIC + "menu.mp3");
			audioPlayer.play();

		} catch (Exception e) {
			e.printStackTrace();
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
				graphics.drawString(OPTIONS[i], xOffset, 200 + counter * 120);
			} else {
				graphics.drawString(OPTIONS[i], GamePanel.WIDTH - fm.stringWidth(OPTIONS[i]) - xOffset, 200 + counter * 120);
			}
		}
	}

	@Override
	public void release() {
		audioPlayer.stop();
	}

	@Override
	public void hanleInput() {
		if (KeyInput.keys[KeyEvent.VK_ENTER]) {
			navigationTimer = 0;
			select();
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
				select();
			}
		}

		if (KeyInput.keys[KeyEvent.VK_RIGHT]) {
			navigationTimer = 0;
			if (currentChoise == OPTIONS.length - 2) {
				currentChoise++;
			} else if (currentChoise < OPTIONS.length - 2) {
				select();
			}
		}
	}

	private void select() {
		if (currentChoise == 0) {
			if (optionsData[0].equalsIgnoreCase("high")) {
				optionsData[0] = "Low";
			} else if (optionsData[0].equalsIgnoreCase("medium")) {
				optionsData[0] = "High";
			} else {
				optionsData[0] = "Medium";
			}
		}

		if (currentChoise == 1) {
			if (optionsData[1].equalsIgnoreCase("off")) {
				optionsData[1] = "On";
			} else {
				optionsData[1] = "Off";
			}
		}

		if (currentChoise == 2) {
			if (optionsData[2].equalsIgnoreCase("off")) {
				optionsData[2] = "On";
			} else {
				optionsData[2] = "Off";
			}
		}

		if (currentChoise == 3) gameStateManager.setState(GameStateManager.MENU);

		if (currentChoise == 4) save();
	}

	private void save() {
		Storage.saveOptions(optionsData);
		gameStateManager.setState(GameStateManager.MENU);
	}
}
