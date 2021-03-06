package com.kdi.aliens.entities;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.kdi.aliens.AlienGame;
import com.kdi.aliens.graphics.Animation;
import com.kdi.aliens.tilemap.Tile;
import com.kdi.aliens.tilemap.World;

public abstract class Entity {

	protected World world;
	protected int tileSize;
	protected double xMap, yMap;

	/**
	 * Position and vector
	 */
	protected double x, y; // Current position
	protected double dx, dy; // Position to move

	/**
	 * Dimensions
	 */
	protected int width, height;

	/**
	 * Collision box
	 */
	protected int cWidth, cHeight;

	/**
	 * Collision
	 */
	protected int currentRow, currentCol;
	protected double xDest, yDest;
	protected double xTemp, yTemp;
	protected boolean topLeft, topRight, bottomLeft, bottomRight; // Four corner collision
	protected boolean dmgTopLeft, dmgTopRight, dmgButtomLeft, dmgButtomRight;
	protected boolean leftCollistion, rightCollistion; // for hit sprite animation direction

	/**
	 * Animation
	 */
	protected Animation animation;
	protected int currentAction;
	protected int previousAction;
	protected boolean facingRight;

	/**
	 * Movement
	 */
	protected boolean left, right, up, down;
	protected boolean jumping;
	protected boolean falling;
	protected boolean hit;

	/**
	 * Movement Attribute
	 */
	protected double moveSpeed;
	protected double maxSpeed;
	protected double stopSpeed; // Deceleration speed
	protected double fallSpeed;
	protected double maxFallSpeed;
	protected double jumpStart;
	protected double jumpSpeed;
	protected double stopJumpSpeed;

	protected boolean remove;

	public Entity(World world) {
		this.world = world;
		tileSize = world.getTileSize();
	}

	public abstract void update();

	public abstract void render(Graphics2D graphics);

	protected void setImageDirection(Graphics2D graphics) {
		BufferedImage image = animation.getImage();

		if (facingRight)
			graphics.drawImage(image, (int) (x + xMap - image.getWidth() / 2), (int) (y + yMap - image.getHeight() / 2), null);
		else
			graphics.drawImage(image, (int) (x + xMap - image.getWidth() / 2 + image.getWidth()), (int) (y + yMap - image.getHeight() / 2),
					-image.getWidth(), image.getHeight(), null);
	}

	public boolean intersects(Entity mapObject) {
		Rectangle r1 = getRectangle();
		Rectangle r2 = mapObject.getRectangle();
		return r1.intersects(r2);
	}

	public Rectangle getRectangle() {
		return new Rectangle((int) x - cWidth, (int) y - cHeight, cWidth, cHeight);
	}

	public void checkTileMapCollision() {
		currentCol = (int) x / tileSize;
		currentRow = (int) y / tileSize;

		xDest = x + dx;
		yDest = y + dy;

		xTemp = x;
		yTemp = y;

		calculateCorners(x, yDest);

		if (dy < 0) {
			if (topLeft || topRight) {
				dy = 0;
				yTemp = currentRow * tileSize + cHeight / 2;
			} else {
				yTemp += dy;
			}
		}

		if (dy > 0) {
			if (bottomLeft || bottomRight) {
				dy = 0;
				falling = false;
				yTemp = (currentRow + 1) * tileSize - cHeight / 2;
			} else {
				yTemp += dy;
			}
		}

		calculateCorners(xDest, y);

		if (dx < 0) {
			if (topLeft || bottomLeft) {
				dx = 0;
				xTemp = currentCol * tileSize + cWidth / 2;
				leftCollistion = true;
			} else {
				xTemp += dx;
			}
		}

		if (dx > 0) {
			if (topRight || bottomRight) {
				dx = 0;
				xTemp = (currentCol + 1) * tileSize - cWidth / 2;
				rightCollistion = true;
			} else {
				xTemp += dx;
			}
		}

		if (!falling) {
			calculateCorners(x, yDest + 1);
			if (!bottomLeft && !bottomRight) falling = true;
		}

		if (dmgTopLeft || dmgTopRight || dmgButtomLeft || dmgButtomRight) hit = true;
	}

	public void calculateCorners(double x, double y) {
		int leftTile = (int) (x - cWidth / 2) / tileSize;
		int rightTile = (int) (x + cWidth / 2 - 1) / tileSize;
		int topTile = (int) (y - cHeight / 2) / tileSize;
		int bottomTile = (int) (y + cHeight / 2 - 1) / tileSize;

		if (topTile < 0 || bottomTile >= world.getNumRows() || leftTile < 0 || rightTile >= world.getNumCols()) {
			topLeft = topRight = bottomLeft = bottomRight = false;
			return;
		}

		int tl = world.getType(topTile, leftTile);
		int tr = world.getType(topTile, rightTile);
		int bl = world.getType(bottomTile, leftTile);
		int br = world.getType(bottomTile, rightTile);

		topLeft = tl == Tile.SOLID;
		topRight = tr == Tile.SOLID;
		bottomLeft = bl == Tile.SOLID;
		bottomRight = br == Tile.SOLID;

		dmgTopLeft = tl == Tile.DAMAGE;
		dmgTopRight = tr == Tile.DAMAGE;
		dmgButtomLeft = bl == Tile.DAMAGE;
		dmgButtomRight = br == Tile.DAMAGE;
	}

	public int getX() {
		return (int) x;
	}

	public int getY() {
		return (int) y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getcWidth() {
		return cWidth;
	}

	public int getcHeight() {
		return cHeight;
	}

	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public void setMapPosition() {
		xMap = world.getx();
		yMap = world.gety();
	}

	public boolean notOnScreen() {
		return x + xMap + width < 0 || x + xMap - width > AlienGame.WIDTH || y + yMap + height < 0 || y + yMap - height > AlienGame.HEIGHT;
	}

	public boolean shouldRemove() {
		return remove;
	}

	public void setRemove(boolean remove) {
		this.remove = remove;
	}
}
