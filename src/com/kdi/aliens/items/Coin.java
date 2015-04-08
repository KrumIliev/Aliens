package com.kdi.aliens.items;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.kdi.aliens.graphics.Animation;
import com.kdi.aliens.util.AudioPlayer;
import com.kdi.aliens.util.Reference;

public class Coin extends Item {

	private BufferedImage[] sprites;
	private String soundKey = "coin";

	public Coin(int x, int y) {
		super(x, y, 30, 30, 30, 30);

		try {
			BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream(Reference.RESOURCE_ITEMS + "items.png"));

			sprites = new BufferedImage[spriteSheet.getWidth() / width];
			for (int i = 0; i < sprites.length; i++)
				sprites[i] = spriteSheet.getSubimage(i * width, 0, width, height);

		} catch (Exception e) {
			e.printStackTrace();
		}

		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(200);
		AudioPlayer.loadSound(Reference.RESOURCE_SOUNDS + "coin.mp3", soundKey);
		hasSound = true;
	}

	@Override
	public void playSound() {
		AudioPlayer.playSound(soundKey);

	}
}
