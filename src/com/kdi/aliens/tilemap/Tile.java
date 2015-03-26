package com.kdi.aliens.tilemap;

import java.awt.image.BufferedImage;

public class Tile {
	private BufferedImage image;
	private int type;

	/**
	 * Tile types
	 */
	public static final int DECOR = 0;
	public static final int SOLID = 1;
	public static final int DAMAGE = 2;
	public static final int LIQUID = 3;
	public static final int LOCK_RED = 4;

	public Tile(BufferedImage image, int type) {
		this.image = image;
		this.type = type;
	}

	public BufferedImage getImage() {
		return image;
	}

	public int getType() {
		return type;
	}
}
