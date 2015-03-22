package com.kdi.aliens.items;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.kdi.aliens.graphics.Animation;
import com.kdi.aliens.util.Reference;

public class Life extends Item {

	private BufferedImage[] sprites;

	public Life(int x, int y) {
		super(x, y, 30, 30, 30, 30);

		try {
			BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream(Reference.RESOURCE_ITEMS + "items.png"));

			sprites = new BufferedImage[spriteSheet.getWidth() / width];
			for (int i = 0; i < sprites.length; i++) {
				sprites[i] = spriteSheet.getSubimage(i * width, 60, width, height);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(200);
	}

	@Override
	public void playSound() {}

}
