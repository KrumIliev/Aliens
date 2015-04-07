package com.kdi.aliens.entities.weapons;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.kdi.aliens.graphics.Animation;
import com.kdi.aliens.tilemap.World;
import com.kdi.aliens.util.Reference;

public class Balista extends Weapon {

	public static final int SPEED = 8;

	private static final int WIDTH = 30;
	private static final int HEIGHT = 30;
	private static final int COLLISION_WIDTH = 20;
	private static final int COLLISION_HEIGHT = 20;

	private static final int ENERGY_COST = 0;
	private static final int DAMAGE = 1;

	public Balista(World world, boolean right) {
		super(world, right, ENERGY_COST, DAMAGE);
		setDimensions(WIDTH, HEIGHT, COLLISION_WIDTH, COLLISION_HEIGHT);
		setMovement(SPEED, 0);

		fallSpeed = 1.0;
		maxFallSpeed = 20.0;
		jumpStart = -25.0;
		stopJumpSpeed = 6.0;
		jumping = true;

		try {

			projectileSprites = new BufferedImage[1];
			projectileSprites[0] = ImageIO.read(getClass().getResource(Reference.RESOURCE_WEAPONS + "def.png"));
			BufferedImage explosionSheet = ImageIO.read(getClass().getResource(Reference.RESOURCE_WEAPONS + "default_weapon_explosion.png"));

			hitSprites = new BufferedImage[explosionSheet.getWidth() / width];
			for (int i = 0; i < hitSprites.length; i++) {
				hitSprites[i] = explosionSheet.getSubimage(i * width, 0, width, height);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		animation = new Animation();
		animation.setFrames(projectileSprites);
		animation.setDelay(-1);
	}

	@Override
	public void update() {
		getNextPosition();
		checkTileMapCollision();
		setPosition(xTemp, yTemp);

		if ((dx == 0 || bottomLeft || bottomRight) && !hit) setHit();

		animation.update();
		if (hit && animation.hasPlayedOnce()) {
			remove = true;
		}
	}

	@Override
	public void render(Graphics2D graphics) {
		setMapPosition();
		setImageDirection(graphics);
	}

	private void getNextPosition() {
		if (jumping && !falling) {
			dy = jumpStart;
			falling = true;
		}

		if (falling) {
			dy += fallSpeed;
			if (dy > 0) jumping = false;
			if (dy < 0 && !jumping) dy += stopJumpSpeed;
			if (dy > maxFallSpeed) dy = maxFallSpeed;
		}
	}
}
