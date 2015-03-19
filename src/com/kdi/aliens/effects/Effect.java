package com.kdi.aliens.effects;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.kdi.aliens.graphics.Animation;
import com.kdi.aliens.util.Reference;

public class Effect {

	protected int x, y;
	protected int xMap, yMap;

	protected int width, height;

	protected Animation animation;
	protected BufferedImage[] sprites;

	protected boolean remove;

	public Effect(int x, int y, int width, int height, String file, int animationDelay) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		try {
			BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream(Reference.RESOURCE_EFFECTS + file));

			sprites = new BufferedImage[spriteSheet.getWidth() / width];
			for (int i = 0; i < sprites.length; i++) {
				sprites[i] = spriteSheet.getSubimage(i * width, 0, width, height);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(animationDelay);
	}

	public void setMapPosition(int x, int y) {
		xMap = x;
		yMap = y;
	}

	public void update() {
		animation.update();
		if (animation.hasPlayedOnce()) remove = true;
	}

	public void render(Graphics2D graphics) {
		graphics.drawImage(animation.getImage(), x + xMap - width / 2, y + yMap - height / 2, null);
	}

	public boolean shouldRemove() {
		return remove;
	}
}
