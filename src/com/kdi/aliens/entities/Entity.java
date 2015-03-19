package com.kdi.aliens.entities;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.kdi.aliens.GamePanel;
import com.kdi.aliens.graphics.Animation;
import com.kdi.aliens.tilemap.Tile;
import com.kdi.aliens.tilemap.TileMap;

public abstract class Entity {

	protected TileMap tileMap;
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

	/**
	 * Animation
	 */
	protected Animation animation;
	protected int currentAction;
	protected int previousAction;
	protected boolean faceingRight;

	/**
	 * Movement
	 */
	protected boolean left, right, up, down;
	protected boolean jumping;
	protected boolean falling;

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

	public Entity(TileMap tileMap) {
		this.tileMap = tileMap;
		tileSize = tileMap.getTileSize();
	}

	public abstract void update();

	public abstract void render(Graphics2D graphics);

	protected void setImageDirection(Graphics2D graphics) {
		BufferedImage image = animation.getImage();

		if (faceingRight) {
			graphics.drawImage(image, (int) (x + xMap - image.getWidth() / 2), (int) (y + yMap - image.getHeight() / 2), null);
		} else {
			graphics.drawImage(image, (int) (x + xMap - image.getWidth() / 2 + image.getWidth()), (int) (y + yMap - image.getHeight() / 2),
					-image.getWidth(), image.getHeight(), null);
		}
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
			} else {
				xTemp += dx;
			}
		}

		if (dx > 0) {
			if (topRight || bottomRight) {
				dx = 0;
				xTemp = (currentCol + 1) * tileSize - cWidth / 2;
			} else {
				xTemp += dx;
			}
		}

		if (!falling) {
			calculateCorners(x, yDest + 1);
			if (!bottomLeft && !bottomRight) falling = true;
		}
	}

	public void calculateCorners(double x, double y) {
		int leftTile = (int) (x - cWidth / 2) / tileSize;
		int rightTile = (int) (x + cWidth / 2 - 1) / tileSize;
		int topTile = (int) (y - cHeight / 2) / tileSize;
		int bottomTile = (int) (y + cHeight / 2 - 1) / tileSize;

		if (topTile < 0 || bottomTile >= tileMap.getNumRows() || leftTile < 0 || rightTile >= tileMap.getNumCols()) {
			topLeft = topRight = bottomLeft = bottomRight = false;
			return;
		}

		int tl = tileMap.getType(topTile, leftTile);
		int tr = tileMap.getType(topTile, rightTile);
		int bl = tileMap.getType(bottomTile, leftTile);
		int br = tileMap.getType(bottomTile, rightTile);

		topLeft = tl == Tile.BLOCKED;
		topRight = tr == Tile.BLOCKED;
		bottomLeft = bl == Tile.BLOCKED;
		bottomRight = br == Tile.BLOCKED;

		//TODO add damage surface
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
		xMap = tileMap.getx();
		yMap = tileMap.gety();
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}

	public boolean notOnScreen() {
		return x + xMap + width < 0 || x + xMap - width > GamePanel.WIDTH || y + yMap + height < 0 || y + yMap - height > GamePanel.HEIGHT;
	}
}
