package com.kdi.aliens.state;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Random;

import com.kdi.aliens.AlienGame;
import com.kdi.aliens.graphics.Background;
import com.kdi.aliens.input.KeyInput;
import com.kdi.aliens.util.ContentManager;
import com.kdi.aliens.util.Reference;

public class MenuState extends GameState {

	private Background background;
	private String[] options = { "Start", "Options", "Quit" };
	private int currentChoise = 0;

	private Color titleColor;
	private Font titleFont;

	private Font defaultFont;

	private byte navigationTimer = 0;

	public MenuState(GameStateManager gameStateManager) {
		super(gameStateManager);

		try {

			Random random = new Random();

			if (random.nextBoolean())
				background = new Background(ContentManager.getImage(Reference.CM_BACKGROUND_MENU_1), 1);
			else
				background = new Background(ContentManager.getImage(Reference.CM_BACKGROUND_MENU_2), 1);

			background.setVector(-0.5, 0);

			titleColor = Color.MAGENTA;
			titleFont = new Font("Comic Note", Font.PLAIN, 100);

			defaultFont = new Font("Comic Note", Font.PLAIN, 60);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init() {}

	@Override
	public void update() {
		if (navigationTimer > 10) hanleInput();
		if (navigationTimer > 10) navigationTimer = 10;
		navigationTimer++;
		background.update();
	}

	@Override
	public void render(Graphics2D graphics) {
		background.render(graphics);

		graphics.setColor(titleColor);
		graphics.setFont(titleFont);
		graphics.drawString(AlienGame.GAME_NAME, centerHorisontal(graphics, titleFont, AlienGame.GAME_NAME), 200);

		graphics.setFont(defaultFont);
		for (int i = 0; i < options.length; i++) {
			if (currentChoise == i)
				graphics.setColor(Color.RED);
			else
				graphics.setColor(Color.MAGENTA);

			graphics.drawString(options[i], centerHorisontal(graphics, defaultFont, options[i]), 450 + i * 60);
		}
	}

	private int centerHorisontal(Graphics2D graphics, Font font, String text) {
		FontMetrics fm = graphics.getFontMetrics(font);
		int stringWidth = fm.stringWidth(text);
		return (AlienGame.WIDTH + stringWidth) / 2 - stringWidth;
	}

	private void select() {
		if (currentChoise == 0) gameStateManager.setState(GameStateManager.LEVEL1);
		if (currentChoise == 1) gameStateManager.setState(GameStateManager.OPTIONS);
		if (currentChoise == 2) System.exit(0);
	}

	@Override
	public void hanleInput() {
		if (KeyInput.keys[KeyEvent.VK_ENTER]) select();

		if (KeyInput.keys[KeyEvent.VK_UP]) {
			navigationTimer = 0;
			currentChoise--;
			if (currentChoise == -1) currentChoise = options.length - 1;
		}

		if (KeyInput.keys[KeyEvent.VK_DOWN]) {
			navigationTimer = 0;
			currentChoise++;
			if (currentChoise == options.length) currentChoise = 0;
		}
	}

}
