package com.kdi.aliens.entities.enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.kdi.aliens.AlienGame;
import com.kdi.aliens.entities.Player;
import com.kdi.aliens.graphics.Animation;
import com.kdi.aliens.tilemap.TileMap;
import com.kdi.aliens.util.Reference;

public class Fly extends Enemy {

	private BufferedImage[] sprites;

	private int steps = 0;

	public Fly(TileMap tileMap, Player player) {
		super(tileMap, player);

		moveSpeed = 1;
		maxSpeed = 1.5;

		width = 60;
		height = 32;
		cWidth = 40;
		cHeight = 25;

		health = maxHealth = 5;
		damage = 1;

		try {
			BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream(Reference.RESOURCE_ENEMIES + "fly.png"));

			sprites = new BufferedImage[spriteSheet.getWidth() / width];
			for (int i = 0; i < sprites.length; i++) {
				sprites[i] = spriteSheet.getSubimage(i * width, 0, width, height);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(150);
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
		setPosition(xTemp, yTemp);

		if (right && dx == 0 || steps > 60) {
			right = false;
			left = true;
			facingRight = false;
		} else if (left && dx == 0 || steps < -60) {
			right = true;
			left = false;
			facingRight = true;
		}

		if (right) steps++;
		if (left) steps--;

		animation.update();
	}

	@Override
	public void render(Graphics2D graphics) {
		setMapPosition();
		super.render(graphics);
	}

}
