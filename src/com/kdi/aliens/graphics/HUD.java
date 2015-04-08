package com.kdi.aliens.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.kdi.aliens.AlienGame;
import com.kdi.aliens.entities.Player;
import com.kdi.aliens.util.Reference;

public class HUD {

	private Player player;

	private BufferedImage[] spritesHealth;
	private BufferedImage[] spritesEnergy;
	private BufferedImage[] spritesKeys;
	private BufferedImage icon, coins;

	private int hWidth = 29;
	private int hHeight = 25;

	private int eWidth = 18;
	private int eHeight = 18;

	private int kWidth = 28;
	private int kHeight = 25;

	private int topYOffset;
	private int energyYOffset;
	private int livesYOffset;

	private int keys = 4; //TODO input from constructor later

	public HUD(Player player) {
		this.player = player;

		try {
			BufferedImage hud = ImageIO.read(getClass().getResourceAsStream(Reference.RESOURCE_HUD + "hud.png"));
			coins = hud.getSubimage(25 * 5, 0, 25, 25);
			icon = hud.getSubimage(0, 0, 25, 25);

			spritesHealth = new BufferedImage[3];
			for (int i = 0; i < spritesHealth.length; i++)
				spritesHealth[i] = hud.getSubimage(i * hWidth, 25, hWidth, hHeight);

			spritesEnergy = new BufferedImage[3];
			for (int i = 0; i < spritesEnergy.length; i++)
				spritesEnergy[i] = hud.getSubimage(i * eWidth, eHeight * 5, eWidth, eHeight);

			spritesKeys = new BufferedImage[hud.getWidth() / kWidth];
			for (int i = 0; i < spritesKeys.length; i++)
				spritesKeys[i] = hud.getSubimage(i * kWidth, kHeight * 2, kWidth, kHeight);

		} catch (Exception e) {
			e.printStackTrace();
		}

		topYOffset = 20;
		energyYOffset = topYOffset + hHeight + 15;
		livesYOffset = energyYOffset + eHeight + 15;
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

		if (halfHealthCount > 0)
			emptyHealthCount = player.getMaxHealth() - fullHealthCount - 1;
		else
			emptyHealthCount = player.getMaxHealth() - fullHealthCount;

		int xOffsetTotal = topYOffset; // Next heart offset

		int xSpace = 5; // Space between hearts

		for (int i = 0; i < fullHealthCount; i++) {
			graphics.drawImage(spritesHealth[0], xOffsetTotal, topYOffset, null);
			xOffsetTotal = xOffsetTotal + xSpace + spritesHealth[0].getWidth();
		}

		if (halfHealthCount > 0) {
			graphics.drawImage(spritesHealth[1], xOffsetTotal, topYOffset, null);
			xOffsetTotal = xOffsetTotal + xSpace + spritesHealth[1].getWidth();
		}

		for (int i = 0; i < emptyHealthCount; i++) {
			graphics.drawImage(spritesHealth[2], xOffsetTotal, topYOffset, null);
			xOffsetTotal = xOffsetTotal + xSpace + spritesHealth[2].getWidth();
		}
	}

	private void renderEnergy(Graphics2D graphics) {
		int xOffsetTotal = 17;

		for (int i = 0; i < player.getEnergy() / 100; i++) {
			if (i == 0) {
				graphics.drawImage(spritesEnergy[0], xOffsetTotal, energyYOffset, null);
				xOffsetTotal += spritesEnergy[0].getWidth();
			} else if (i == player.getEnergy() / 100 - 1) {
				graphics.drawImage(spritesEnergy[2], xOffsetTotal, energyYOffset, null);
			} else {
				graphics.drawImage(spritesEnergy[1], xOffsetTotal, energyYOffset, null);
				xOffsetTotal += spritesEnergy[1].getWidth();
			}
		}
	}

	private void renderOther(Graphics2D graphics) {

		/**
		 * Rendering lives
		 */
		int xOffset = 20;
		for (int i = 0; i < player.getLives(); i++) {
			graphics.drawImage(icon, xOffset, livesYOffset, null);
			xOffset = xOffset + icon.getWidth() + 5;
		}

		xOffset = AlienGame.WIDTH / 2;
		for (int i = 0; i < keys; i++) {
			if (i == 0) {
				if (player.hasBlueKey())
					graphics.drawImage(spritesKeys[0], xOffset, topYOffset, null);
				else
					graphics.drawImage(spritesKeys[1], xOffset, topYOffset, null);

			} else if (i == 1) {
				if (player.hasGreenKey())
					graphics.drawImage(spritesKeys[2], xOffset, topYOffset, null);
				else
					graphics.drawImage(spritesKeys[3], xOffset, topYOffset, null);

			} else if (i == 2) {
				if (player.hasRedKey())
					graphics.drawImage(spritesKeys[4], xOffset, topYOffset, null);
				else
					graphics.drawImage(spritesKeys[5], xOffset, topYOffset, null);

			} else {
				if (player.hasRedKey())
					graphics.drawImage(spritesKeys[6], xOffset, topYOffset, null);
				else
					graphics.drawImage(spritesKeys[7], xOffset, topYOffset, null);

			}

			xOffset = xOffset + kWidth + 10;
		}

		/**
		 * Rendering coins
		 */
		Font font = new Font("Comic Note", Font.PLAIN, 40);
		graphics.drawImage(coins, AlienGame.WIDTH - coins.getWidth() - 100, topYOffset, null);
		graphics.setColor(new Color(241, 156, 183));
		graphics.setFont(font);
		graphics.drawString(String.valueOf(player.getCoins()), AlienGame.WIDTH - coins.getWidth() - 60, topYOffset + 23);
	}
}
