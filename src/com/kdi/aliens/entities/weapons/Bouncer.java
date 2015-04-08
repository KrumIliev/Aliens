package com.kdi.aliens.entities.weapons;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.kdi.aliens.graphics.Animation;
import com.kdi.aliens.tilemap.World;
import com.kdi.aliens.util.Reference;

public class Bouncer extends Weapon {

	public static final int SPEED = 8;

	private static final int WIDTH = 32;
	private static final int HEIGHT = 32;
	private static final int COLLISION_WIDTH = 25;
	private static final int COLLISION_HEIGHT = 25;
	private static final int EXPLOSION_WIDTH = 30;
	private static final int EXPLOSION_HEIGHT = 30;

	private static final int ENERGY_COST = 0;
	private static final int DAMAGE = 10;

	private final int maxBounces = 5;
	private int bounceCounter = 0;
	private int maxHeight;

	public Bouncer(World world, boolean right) {
		super(world, right, ENERGY_COST, DAMAGE);
		setDimensions(WIDTH, HEIGHT, COLLISION_WIDTH, COLLISION_HEIGHT, EXPLOSION_WIDTH, EXPLOSION_HEIGHT);
		setMovement(SPEED, SPEED);

		try {

			projectileSprites = new BufferedImage[1];
			projectileSprites[0] = ImageIO.read(getClass().getResource(Reference.RESOURCE_WEAPONS + "def.png"));
			BufferedImage explosionSheet = ImageIO.read(getClass().getResource(Reference.RESOURCE_WEAPONS + "default_weapon_explosion.png"));

			hitSprites = new BufferedImage[explosionSheet.getWidth() / explosionWidth];
			for (int i = 0; i < hitSprites.length; i++)
				hitSprites[i] = explosionSheet.getSubimage(i * explosionWidth, 0, explosionWidth, explosionHeight);

		} catch (Exception e) {
			e.printStackTrace();
		}

		animation = new Animation();
		animation.setFrames(projectileSprites);
		animation.setDelay(-1);
	}

	@Override
	public void update() {
		checkTileMapCollision();
		setPosition(xTemp, yTemp);

		if (dx == 0) {
			if (leftCollistion) dx = SPEED;
			if (rightCollistion) dx = -SPEED;
			bounceCounter++;
		} else if (dy == 0) {
			if (topCollision) dy = SPEED;
			if (bottomCollision) dy = -SPEED;
			bounceCounter++;
		}

		if (bounceCounter > maxBounces) bounceCounter = maxBounces;
		if (bounceCounter == maxBounces && !hit) setHit();

		animation.update();
		if (hit && animation.hasPlayedOnce()) remove = true;

		bottomCollision = false;
		leftCollistion = false;
		topCollision = false;
		rightCollistion = false;
	}

	@Override
	public void render(Graphics2D graphics) {
		setMapPosition();
		setImageDirection(graphics);
	}
}
