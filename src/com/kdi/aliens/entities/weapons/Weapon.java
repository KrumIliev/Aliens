package com.kdi.aliens.entities.weapons;

import java.awt.image.BufferedImage;

import com.kdi.aliens.entities.Entity;
import com.kdi.aliens.tilemap.World;

public abstract class Weapon extends Entity {

	protected boolean hit;
	protected boolean remove;

	protected BufferedImage[] projectileSprites;
	protected BufferedImage[] hitSprites;

	protected int xOffset = 40;
	protected int yOffset = 20;

	protected int energyCost;
	protected int damage;

	public Weapon(World world, boolean right, int moveSpeed, int energyCost, int damage) {
		super(world);
		facingRight = right;
		this.energyCost = energyCost;
		this.damage = damage;
		this.moveSpeed = moveSpeed;

		if (right) {
			dx = moveSpeed;
		} else {
			dx = -moveSpeed;
			xOffset = -xOffset;
		}
	}

	public Weapon(World world, boolean right, double dx, double dy, int energyCost, int damage) {
		super(world);
		facingRight = right;
		this.energyCost = energyCost;
		this.damage = damage;

		this.dx = dx;
		this.dy = dy;

		if (right) {
			this.dx = dx;
		} else {
			this.dx = -dx;
			xOffset = -xOffset;
		}
	}

	protected void setDimensions(int width, int height, int cWidth, int cHeight) {
		this.width = width;
		this.height = height;
		this.cWidth = cWidth;
		this.cHeight = cHeight;
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
