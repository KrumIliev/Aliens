package com.kdi.aliens.entities.enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.kdi.aliens.AlienGame;
import com.kdi.aliens.entities.Player;
import com.kdi.aliens.graphics.Animation;
import com.kdi.aliens.tilemap.TileMap;
import com.kdi.aliens.util.Reference;

public class PinkBlob extends Enemy {

	private BufferedImage[] sprites;

	public PinkBlob(TileMap tileMap, Player player) {
		super(tileMap, player);

		moveSpeed = 0.5;
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
		facingRight = true;
	}

	private void getNextPosition() {
		if (left)
			dx = -moveSpeed;
		else if (right)
			dx = moveSpeed;
		else
			dx = 0;
		if (falling) {
			dy += fallSpeed;
			if (dy > maxFallSpeed) dy = maxFallSpeed;
		}
	}

	@Override
	public void update() {

		if (!active) {
			if (Math.abs(player.getX() - x) < AlienGame.WIDTH) active = true;
			return;
		}

		if (flinching) {
			long elapsedTime = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsedTime > 400) flinching = false;
		}

		getNextPosition();
		checkTileMapCollision();
		calculateCorners(x, yDest + 1);
		if (!bottomLeft) {
			left = false;
			right = facingRight = true;
		}
		if (!bottomRight) {
			left = true;
			right = facingRight = false;
		}
		setPosition(xTemp, yTemp);

		if (right && dx == 0) {
			right = false;
			left = true;
			facingRight = false;
		} else if (left && dx == 0) {
			right = true;
			left = false;
			facingRight = true;
		}

		animation.update();
	}

	@Override
	public void render(Graphics2D graphics) {
		setMapPosition();
		super.render(graphics);
	}

}
