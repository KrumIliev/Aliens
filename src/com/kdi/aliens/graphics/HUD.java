package com.kdi.aliens.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.kdi.aliens.GamePanel;
import com.kdi.aliens.entities.Player;

public class HUD {

	private Player player;

	private BufferedImage[] spritesHealth;
	private BufferedImage[] spritesEnergy;
	private BufferedImage icon;

	private int hWidth = 53;
	private int hHeight = 45;

	private int eWidth = 16;
	private int eHeight = 26;

	public HUD(Player player) {
		this.player = player;

		try {
			BufferedImage imageHealth = ImageIO.read(getClass().getResourceAsStream("/sprites/items/health.png"));
			BufferedImage imageEnergy = ImageIO.read(getClass().getResourceAsStream("/sprites/items/pink_energy.png"));
			icon = ImageIO.read(getClass().getResourceAsStream("/sprites/items/lives.png"));

			spritesHealth = new BufferedImage[imageHealth.getWidth() / hWidth];
			for (int i = 0; i < spritesHealth.length; i++) {
				spritesHealth[i] = imageHealth.getSubimage(i * hWidth, 0, hWidth, hHeight);
			}

			spritesEnergy = new BufferedImage[imageEnergy.getWidth() / eWidth];
			for (int i = 0; i < spritesEnergy.length; i++) {
				spritesEnergy[i] = imageEnergy.getSubimage(i * eWidth, 0, eWidth, eHeight);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void render(Graphics2D graphics) {
		renderHealth(graphics);
		renderEnergy(graphics);
		renderLives(graphics);
	}

	private void renderHealth(Graphics2D graphics) {
		double fullHealthCount = player.getHealth();
		double halfHealthCount = player.getHealth() % 1;
		double emptyHealthCount = player.getMaxHealth() - fullHealthCount - halfHealthCount;

		int offset = 20;

		int xOffsetTotal = offset; // Next heart offset

		int xSpace = 5; // Space between hearts

		for (int i = 0; i < fullHealthCount; i++) {
			graphics.drawImage(spritesHealth[0], xOffsetTotal, offset, null);
			xOffsetTotal = xOffsetTotal + xSpace + spritesHealth[0].getWidth();
		}

		if (halfHealthCount > 0) {
			graphics.drawImage(spritesHealth[1], xOffsetTotal, offset, null);
			xOffsetTotal = xOffsetTotal + xSpace + spritesHealth[1].getWidth();
		}

		for (int i = 0; i < emptyHealthCount; i++) {
			graphics.drawImage(spritesHealth[3], xOffsetTotal, offset, null);
			xOffsetTotal = xOffsetTotal + xSpace + spritesHealth[3].getWidth();
		}
	}

	private void renderEnergy(Graphics2D graphics) {

		int yOffset = 90;
		int xOffsetTotal = 10;

		for (int i = 0; i < player.getBullets() / 100; i++) {
			if (i == 0) {
				graphics.drawImage(spritesEnergy[0], xOffsetTotal, yOffset, null);
				xOffsetTotal += spritesEnergy[0].getWidth();
			} else if (i == player.getBullets() / 100 - 1) {
				graphics.drawImage(spritesEnergy[2], xOffsetTotal, yOffset, null);
			} else {
				graphics.drawImage(spritesEnergy[1], xOffsetTotal, yOffset, null);
				xOffsetTotal += spritesEnergy[1].getWidth();
			}
		}
	}

	private void renderLives(Graphics2D graphics) {

		Font font = new Font("Comic Note", Font.PLAIN, 80);

		graphics.drawImage(icon, GamePanel.WIDTH - icon.getWidth() - 100, 20, null);
		graphics.setColor(new Color(241, 156, 183));
		graphics.setFont(font);
		FontMetrics fm = graphics.getFontMetrics(font);
		int stringWidth = fm.stringWidth(String.valueOf(player.getLives()));
		int stringHeight = fm.getHeight();
		graphics.drawString(String.valueOf(player.getLives()), GamePanel.WIDTH - stringWidth - 30, stringHeight - 7);

	}
}
