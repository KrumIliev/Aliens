package com.kdi.aliens.items;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.kdi.aliens.entities.Player;
import com.kdi.aliens.graphics.Animation;
import com.kdi.aliens.util.AudioPlayer;

public abstract class Item {

	/**
	 * Screen position
	 */
	protected int x, y;

	/**
	 * Map position
	 */
	protected int xMap, yMap;

	/**
	 * Sprite size
	 */
	protected int width, height;

	/**
	 * Collision box
	 */
	protected int cWidth, cHeight;

	protected Animation animation;

	protected BufferedImage[] sprites;

	protected boolean remove;

	protected AudioPlayer sound;
	private boolean hasSound = false;

	public Item(int x, int y, int width, int height, int cWidth, int cHeight) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.cWidth = cWidth;
		this.cHeight = cHeight;
	}

	public abstract void update();

	public abstract void render(Graphics2D graphics);

	public void setMapPosition(int x, int y) {
		xMap = x;
		yMap = y;
	}

	public boolean intersects(Player player) {
		Rectangle rItem = new Rectangle(x - cWidth, y - cHeight, cWidth, cHeight);
		Rectangle rPlayer = player.getRectangle();
		return rItem.intersects(rPlayer);
	}

	protected void setSound(AudioPlayer sound) {
		this.sound = sound;
		hasSound = true;
	}

	public boolean shouldRemove() {
		return remove;
	}

	public void setRemove() {
		if (hasSound) sound.play();
		remove = true;
	}

}
