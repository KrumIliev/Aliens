package com.kdi.aliens.entities.weapons;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.kdi.aliens.entities.Entity;
import com.kdi.aliens.graphics.Animation;
import com.kdi.aliens.tilemap.TileMap;

public class DefaultWeapon extends Entity {

	private boolean hit;
	private boolean remove;

	private BufferedImage[] sprites;
	private BufferedImage[] hitSprites;

	private int eWidth, eHeight;

	public int xOffset = 40;
	public int yOffset = 20;

	private int energyCost = 200;
	private int damage = 1;

	public DefaultWeapon(TileMap tileMap, boolean right) {
		super(tileMap);

		faceingRight = right;

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

		eWidth = 70;
		eHeight = 70;

		try {

			BufferedImage shootSheet = ImageIO.read(getClass().getResource("/sprites/weapons/shoot.png"));
			BufferedImage explosionSheet = ImageIO.read(getClass().getResource("/sprites/weapons/explosion.png"));

			sprites = new BufferedImage[shootSheet.getWidth() / width];
			for (int i = 0; i < sprites.length; i++) {
				sprites[i] = shootSheet.getSubimage(i * width, 0, width, height);
			}

			hitSprites = new BufferedImage[explosionSheet.getWidth() / eWidth];
			for (int i = 0; i < hitSprites.length; i++) {
				hitSprites[i] = explosionSheet.getSubimage(i * eWidth, 0, eWidth, eHeight);
			}

			animation = new Animation();
			animation.setFrames(sprites);
			animation.setDelay(70);

		} catch (Exception e) {
			e.printStackTrace();
		}
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
		super.render(graphics);
	}

	public int getEnergyCost() {
		return energyCost;
	}

	public int getDamage() {
		return damage;
	}
}
