package com.kdi.aliens.entities.weapons;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.kdi.aliens.entities.enemies.Enemy;
import com.kdi.aliens.graphics.Animation;
import com.kdi.aliens.state.levels.LevelObjects;
import com.kdi.aliens.tilemap.World;
import com.kdi.aliens.util.Reference;

public class Balista extends Weapon {

	public static final int SPEED = 8;

	private static final int WIDTH = 32;
	private static final int HEIGHT = 32;
	private static final int COLLISION_WIDTH = 25;
	private static final int COLLISION_HEIGHT = 25;
	private static final int EXPLOSION_WIDTH = 100;
	private static final int EXPLOSION_HEIGHT = 100;
	private static final int EXPLOSION_RADIUS = 150;

	private static final int ENERGY_COST = 0;
	private static final int DAMAGE = 10;

	private boolean getNextPosition = true;

	private BufferedImage[] horizontalHitSprites;

	public Balista(World world, boolean right) {
		super(world, right, ENERGY_COST, DAMAGE);
		setDimensions(WIDTH, HEIGHT, COLLISION_WIDTH, COLLISION_HEIGHT, EXPLOSION_WIDTH, EXPLOSION_HEIGHT);
		setMovement(SPEED, 0);

		fallSpeed = 1.0;
		maxFallSpeed = 20.0;
		jumpStart = -25.0;
		stopJumpSpeed = 6.0;
		jumping = true;

		try {

			BufferedImage spriteSheet = ImageIO.read(getClass().getResource(Reference.RESOURCE_WEAPONS + "balista.png"));
			projectileSprites = new BufferedImage[spriteSheet.getWidth() / width];
			for (int i = 0; i < projectileSprites.length; i++)
				projectileSprites[i] = spriteSheet.getSubimage(i * width, 0, width, height);

			BufferedImage explosionSheet = ImageIO.read(getClass().getResource(Reference.RESOURCE_WEAPONS + "balista_expl.png"));
			hitSprites = new BufferedImage[explosionSheet.getWidth() / explosionWidth];
			horizontalHitSprites = new BufferedImage[explosionSheet.getWidth() / explosionWidth];

			for (int i = 0; i < hitSprites.length; i++)
				hitSprites[i] = explosionSheet.getSubimage(i * explosionWidth, 0, explosionWidth, explosionHeight);

			for (int i = 0; i < horizontalHitSprites.length; i++)
				horizontalHitSprites[i] = explosionSheet.getSubimage(i * explosionWidth, explosionHeight, explosionWidth, explosionHeight);

		} catch (Exception e) {
			e.printStackTrace();
		}

		animation = new Animation();
		animation.setFrames(projectileSprites);
		animation.setDelay(70);
	}

	@Override
	public void update() {
		if (getNextPosition) getNextPosition();
		checkTileMapCollision();
		setPosition(xTemp, yTemp);

		if ((dx == 0 || bottomLeft || bottomRight) && !hit) setHit();

		animation.update();
		if (hit && animation.hasPlayedOnce()) remove = true;

	}

	@Override
	public void setHit() {
		if (hit) return;

		hit = true;
		getNextPosition = false;
		if (rightCollistion) {
			facingRight = !facingRight;
			animation.setFrames(horizontalHitSprites);
			x = x - 22;
		} else if (leftCollistion) {
			facingRight = !facingRight;
			animation.setFrames(horizontalHitSprites);
			x = x + 22;
		} else {
			animation.setFrames(hitSprites);
			y = y - 22;
		}
		animation.setDelay(70);
		dx = 0;
		dy = 0;

		for (Enemy enemy : LevelObjects.enemies)
			if (enemy.getX() > x - EXPLOSION_RADIUS && enemy.getX() < x + EXPLOSION_RADIUS && enemy.getY() > y - EXPLOSION_RADIUS
					&& enemy.getY() < y + EXPLOSION_RADIUS) enemy.hit(DAMAGE);
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
