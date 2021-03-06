package com.kdi.aliens.entities.enemies;

import java.awt.Graphics2D;

import com.kdi.aliens.entities.Entity;
import com.kdi.aliens.entities.Player;
import com.kdi.aliens.tilemap.World;

public abstract class Enemy extends Entity {

	public static final int TYPE_PINK_BLOB = 0;
	public static final int TYPE_BAT = 1;

	protected double health;
	protected int maxHealth;
	protected boolean dead;
	protected double damage;

	protected boolean flinching;
	protected long flinchTimer;

	protected Player player;
	protected boolean active;

	public Enemy(World world, Player player) {
		super(world);
		this.player = player;
	}

	public Enemy(World tileMap, double x, double y) {
		super(tileMap);
		setPosition(x, y);
	}

	public boolean isDead() {
		return dead;
	}

	public double getDamage() {
		return damage;
	}

	public void hit(double damage) {
		if (dead || flinching) return;
		health -= damage;
		if (health < 0) health = 0;
		if (health == 0) dead = true;
		flinching = true;
		flinchTimer = System.nanoTime();
	}

	@Override
	public void render(Graphics2D graphics) {
		setImageDirection(graphics);
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
