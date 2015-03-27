package com.kdi.aliens.entities.weapons;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.kdi.aliens.entities.Entity;
import com.kdi.aliens.graphics.Animation;
import com.kdi.aliens.tilemap.World;
import com.kdi.aliens.util.Reference;

public class DefaultWeapon extends Entity {

	private boolean hit;
	private boolean remove;

	private BufferedImage[] sprites;
	private BufferedImage[] hitSprites;

	public int xOffset = 40;
	public int yOffset = 20;

	private int energyCost = 200;
	private int damage = 1;

	public DefaultWeapon(World world, boolean right) {
		super(world);

		facingRight = right;

		moveSpeed = 8;
		if (right) {
			dx = moveSpeed;
		} else {
			dx = -moveSpeed;
			xOffset = -xOffset;
		}

		width = 30;
		height = 30;
		cWidth = 20;
		cHeight = 20;

		try {

			sprites = new BufferedImage[1];
			sprites[0] = ImageIO.read(getClass().getResource(Reference.RESOURCE_WEAPONS + "def.png"));
			BufferedImage explosionSheet = ImageIO.read(getClass().getResource(Reference.RESOURCE_WEAPONS + "default_weapon_explosion.png"));

			hitSprites = new BufferedImage[explosionSheet.getWidth() / width];
			for (int i = 0; i < hitSprites.length; i++) {
				hitSprites[i] = explosionSheet.getSubimage(i * width, 0, width, height);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(-1);
	}

	@Override
	public void update() {
		checkTileMapCollision();
		setPosition(xTemp, yTemp);

		if (dx == 0 && !hit) setHit();

		animation.update();
		if (hit && animation.hasPlayedOnce()) {
			remove = true;
		}
	}

	public void setHit() {
		if (hit) return;
		hit = true;
		animation.setFrames(hitSprites);
		animation.setDelay(70);
		dx = 0;
	}

	public boolean shouldRemove() {
		return remove;
	}

	@Override
	public void render(Graphics2D graphics) {
		setMapPosition();
		setImageDirection(graphics);
	}

	public int getEnergyCost() {
		return energyCost;
	}

	public int getDamage() {
		return damage;
	}
}
