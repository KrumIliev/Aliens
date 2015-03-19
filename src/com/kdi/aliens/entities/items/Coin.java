package com.kdi.aliens.entities.items;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.kdi.aliens.entities.Entity;
import com.kdi.aliens.graphics.Animation;
import com.kdi.aliens.tilemap.TileMap;
import com.kdi.aliens.util.Reference;

public class Coin extends Entity {

	private BufferedImage[] sprites;

	public Coin(TileMap tileMap, double x, double y) {
		super(tileMap);

		width = 30;
		height = 30;

		cWidth = 28;
		cHeight = 28;

		this.x = x;
		this.y = y;

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
	}

	@Override
	public void update() {
		animation.update();
	}

	@Override
	public void render(Graphics2D graphics) {
		graphics.drawImage(animation.getImage(), (int) x, (int) y, null);
	}

}
