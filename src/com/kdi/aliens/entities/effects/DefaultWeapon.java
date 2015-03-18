package com.kdi.aliens.entities.effects;

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

	public DefaultWeapon(TileMap tileMap) {
		super(tileMap);

		faceingRight = right;

		moveSpeed = 3.8;
		if (right)
			dx = moveSpeed;
		else
			dx = -moveSpeed;

		width = 30;
		height = 30;
		cWidth = 14;
		cHeight = 14;

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
	protected void render(Graphics2D graphics) {
		setMapPosition();
		super.render(graphics);
	}
}
