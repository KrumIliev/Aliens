package com.kdi.aliens.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.kdi.aliens.GamePanel;
import com.kdi.aliens.entities.Player;
import com.kdi.aliens.util.Reference;

public class HUD {

	private Player player;

	private BufferedImage[] spritesHealth;
	private BufferedImage[] spritesEnergy;
	private BufferedImage icon, coins;

	private int hWidth = 53;
	private int hHeight = 45;

	private int eWidth = 16;
	private int eHeight = 26;

	public HUD(Player player) {
		this.player = player;

		try {
			BufferedImage imageHealth = ImageIO.read(getClass().getResourceAsStream(Reference.RESOURCE_HUD + "health.png"));
			BufferedImage imageEnergy = ImageIO.read(getClass().getResourceAsStream(Reference.RESOURCE_HUD + "pink_energy.png"));
			icon = ImageIO.read(getClass().getResourceAsStream(Reference.RESOURCE_HUD + "lives.png"));
			coins = ImageIO.read(getClass().getResourceAsStream(Reference.RESOURCE_HUD + "coins.png"));

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
		renderOther(graphics);
	}

	private void renderHealth(Graphics2D graphics) {
		double halfHealthCount = player.getHealth() % 1;
		double fullHealthCount = player.getHealth() - halfHealthCount;
		double emptyHealthCount;

		if (halfHealthCount > 0) {
			emptyHealthCount = player.getMaxHealth() - fullHealthCount - 1;
		} else {
			emptyHealthCount = player.getMaxHealth() - fullHealthCount;
		}

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
			graphics.drawImage(spritesHealth[2], xOffsetTotal, offset, null);
			xOffsetTotal = xOffsetTotal + xSpace + spritesHealth[2].getWidth();
		}
	}

	private void renderEnergy(Graphics2D graphics) {

		int yOffset = 90;
		int xOffsetTotal = 10;

		for (int i = 0; i < player.getEnergy() / 100; i++) {
			if (i == 0) {
				graphics.drawImage(spritesEnergy[0], xOffsetTotal, yOffset, null);
				xOffsetTotal += spritesEnergy[0].getWidth();
			} else if (i == player.getEnergy() / 100 - 1) {
				graphics.drawImage(spritesEnergy[2], xOffsetTotal, yOffset, null);
			} else {
				graphics.drawImage(spritesEnergy[1], xOffsetTotal, yOffset, null);
				xOffsetTotal += spritesEnergy[1].getWidth();
			}
		}
	}

	private void renderOther(Graphics2D graphics) {

		Font font = new Font("Comic Note", Font.PLAIN, 80);

		/**
		 * Rendering lives
		 */
		graphics.drawImage(icon, 20, 140, null);
		graphics.setColor(new Color(241, 156, 183));
		graphics.setFont(font);
		graphics.drawString(String.valueOf(player.getLives()), 120, 195);

		/**
		 * Rendering coins
		 */
		graphics.drawImage(coins, GamePanel.WIDTH - coins.getWidth() - 200, 20, null);
		graphics.setColor(new Color(241, 156, 183));
		graphics.setFont(font);
		graphics.drawString(String.valueOf(player.getCoins()), GamePanel.WIDTH - coins.getWidth() - 130 , 65);
	}
}
