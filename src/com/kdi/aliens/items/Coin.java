package com.kdi.aliens.items;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.kdi.aliens.graphics.Animation;
import com.kdi.aliens.util.AudioPlayer;
import com.kdi.aliens.util.Reference;

public class Coin extends Item {

	private BufferedImage[] sprites;

	public Coin(int x, int y) {
		super(x, y, 30, 30, 28, 28);

		try {
			BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream(Reference.RESOURCE_ITEMS + "coin.png"));

			sprites = new BufferedImage[spriteSheet.getWidth() / width];
			for (int i = 0; i < sprites.length; i++) {
				sprites[i] = spriteSheet.getSubimage(i * width, 0, width, height);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(200);
		setSound(new AudioPlayer(Reference.RESOURCE_SOUNDS + "coin.mp3"));
	}

	@Override
	public void update() {
		animation.update();
	}

	@Override
	public void render(Graphics2D graphics) {
		graphics.drawImage(animation.getImage(), x + xMap - width / 2, y + yMap - height / 2, null);
	}
}
