package com.kdi.aliens.items;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.kdi.aliens.graphics.Animation;
import com.kdi.aliens.util.Reference;

public class Health extends Item {

	public Health(int x, int y) {
		super(x, y, 35, 30, 35, 30);

		try {
			BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream(Reference.RESOURCE_ITEMS + "items.png"));

			sprites = new BufferedImage[spriteSheet.getWidth() / width];
			for (int i = 0; i < sprites.length; i++) {
				sprites[i] = spriteSheet.getSubimage(i * width, 30, width, height);
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
