package com.kdi.aliens.entities.enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.kdi.aliens.graphics.Animation;
import com.kdi.aliens.tilemap.TileMap;
import com.kdi.aliens.util.Reference;

public class PinkBlob extends Enemy {

	private BufferedImage[] sprites;

	public PinkBlob(TileMap tileMap) {
		super(tileMap);

		moveSpeed = 0.3;
		maxSpeed = 0.3;
		fallSpeed = 1.0;
		maxFallSpeed = 20.0;

		width = 52;
		height = 28;
		cWidth = 42;
		cHeight = 28;

		health = maxHealth = 2;
		damage = 0.5;

		try {
			BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream(Reference.RESOURCE_ENEMIES + "pink_blob.png"));

			sprites = new BufferedImage[spriteSheet.getWidth() / width];
			for (int i = 0; i < sprites.length; i++) {
				sprites[i] = spriteSheet.getSubimage(i * width, 0, width, height);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(300);
		right = true;
		faceingRight = true;
	}

	private void getNextPosition() {
		if (left) {
			dx -= moveSpeed;
			if (dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		} else if (right) {
			dx += moveSpeed;
			if (dx > maxSpeed) {
				dx = maxSpeed;
			}
		}

		if (falling) {
			dy += fallSpeed;
		}
	}

	@Override
	public void update() {
		getNextPosition();
		checkTileMapCollision();
		setPosition(xTemp, yTemp);

		if (flinching) {
			long elapsedTime = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsedTime > 400) flinching = false;
		}

		if (right && dx == 0) {
			right = false;
			left = true;
			faceingRight = false;
		} else if (left && dx == 0) {
			right = true;
			left = false;
			faceingRight = true;
		}

		animation.update();
	}

	@Override
	public void render(Graphics2D graphics) {
		setMapPosition();
		super.render(graphics);
	}

}
