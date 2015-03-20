package com.kdi.aliens.entities.enemies;

import java.awt.Graphics2D;

import com.kdi.aliens.entities.Entity;
import com.kdi.aliens.entities.Player;
import com.kdi.aliens.tilemap.TileMap;

public abstract class Enemy extends Entity {

	protected double health;
	protected int maxHealth;
	protected boolean dead;
	protected double damage;

	protected boolean flinching;
	protected long flinchTimer;
	
	protected Player player;
	protected boolean active;

	public Enemy(TileMap tileMap, Player player) {
		super(tileMap);
		this.player = player;
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
}
