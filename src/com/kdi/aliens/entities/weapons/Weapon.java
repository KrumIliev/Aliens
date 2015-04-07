package com.kdi.aliens.entities.weapons;

import java.awt.image.BufferedImage;

import com.kdi.aliens.entities.Entity;
import com.kdi.aliens.tilemap.World;

public abstract class Weapon extends Entity {

	protected boolean hit;
	protected boolean remove;

	protected int explosionWidth;
	protected int explosionHeight;

	protected BufferedImage[] projectileSprites;
	protected BufferedImage[] hitSprites;

	protected int xOffset = 40;
	protected int yOffset = 20;

	protected int energyCost;
	protected int damage;

	public Weapon(World world, boolean right, int energyCost, int damage) {
		super(world);
		facingRight = right;
		this.energyCost = energyCost;
		this.damage = damage;
	}

	protected void setDimensions(int width, int height, int cWidth, int cHeight, int explosionWidth, int explosionHeight) {
		this.width = width;
		this.height = height;
		this.cWidth = cWidth;
		this.cHeight = cHeight;
		this.explosionWidth = explosionWidth;
		this.explosionHeight = explosionHeight;
	}

	protected void setMovement(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;

		if (facingRight) {
			this.dx = dx;
		} else {
			this.dx = -dx;
			xOffset = -xOffset;
		}
	}

	public void setHit() {
		if (hit) return;
		hit = true;
		animation.setFrames(hitSprites);
		animation.setDelay(70);
		dx = 0;
		dy = 0;
	}

	public int getEnergyCost() {
		return energyCost;
	}

	public int getDamage() {
		return damage;
	}

	public boolean shouldRemove() {
		return remove;
	}

	public int getXOffset() {
		return xOffset;
	}

	public int getYOffset() {
		return yOffset;
	}

}
