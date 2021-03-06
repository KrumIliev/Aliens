package com.kdi.aliens.entities.weapons;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.kdi.aliens.graphics.Animation;
import com.kdi.aliens.tilemap.World;
import com.kdi.aliens.util.Reference;

public class Blaster extends Weapon {

	public static final int X_SPEED = 8;
	public static final int Y_SPEED = 0;

	private static final int WIDTH = 30;
	private static final int HEIGHT = 30;
	private static final int COLLISION_WIDTH = 20;
	private static final int COLLISION_HEIGHT = 20;
	private static final int EXPLOSION_WIDTH = 30;
	private static final int EXPLOSION_HEIGHT = 30;

	private static final int ENERGY_COST = 0;
	private static final int DAMAGE = 1;

	public Blaster(World world, boolean right) {
		super(world, right, ENERGY_COST, DAMAGE);
		setDimensions(WIDTH, HEIGHT, COLLISION_WIDTH, COLLISION_HEIGHT, EXPLOSION_WIDTH, EXPLOSION_HEIGHT);
		setMovement(X_SPEED, Y_SPEED);

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

		if (dx == 0 && !hit) setHit();

		animation.update();
		if (hit && animation.hasPlayedOnce()) remove = true;
	}

	@Override
	public void render(Graphics2D graphics) {
		setMapPosition();
		setImageDirection(graphics);
	}

}
