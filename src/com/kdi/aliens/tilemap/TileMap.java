package com.kdi.aliens.tilemap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import com.kdi.aliens.GamePanel;

public class TileMap {

	// position
	private double x;
	private double y;

	// bounds
	private int xmin;
	private int ymin;
	private int xmax;
	private int ymax;

	private double tween;

	// map
	private int[][] map;
	private int tileSize;
	private int numRows;
	private int numCols;
	private int width;
	private int height;

	// tileset
	private BufferedImage tileset;
	private int tileColumns, tileRows;
	private Tile[][] tiles;

	// drawing
	private int rowOffset;
	private int colOffset;
	private int numRowsToDraw;
	private int numColsToDraw;

	public TileMap(int tileSize) {
		this.tileSize = tileSize;
		numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
		numColsToDraw = GamePanel.WIDTH / tileSize + 2;
		tween = 0.07;
	}

	public void loadTiles(String s) {

		try {

			tileset = ImageIO.read(getClass().getResourceAsStream(s));
			tileColumns = tileset.getWidth() / tileSize;
			tileRows = tileset.getHeight() / tileSize;

			tiles = new Tile[tileRows][tileColumns];

			BufferedImage subimage;

			for (int row = 0; row < tileRows; row++) {
				for (int col = 0; col < tileColumns; col++) {
					subimage = tileset.getSubimage(col * tileSize, row * tileSize, tileSize, tileSize);

					int tileType = Tile.NORMAL;
					if (row == 1 || row == 2) {
						tileType = Tile.BLOCKED;
					} else if (row == 3) {
						tileType = Tile.DAMAGE;
					}

					tiles[row][col] = new Tile(subimage, tileType);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void loadMap(String s) {

		try {

			InputStream in = getClass().getResourceAsStream(s);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			numCols = Integer.parseInt(br.readLine());
			numRows = Integer.parseInt(br.readLine());
			map = new int[numRows][numCols];
			width = numCols * tileSize;
			height = numRows * tileSize;

			xmin = GamePanel.WIDTH - width;
			xmax = 0;
			ymin = GamePanel.HEIGHT - height;
			ymax = 0;

			String delims = "\\s+";
			for (int row = 0; row < numRows; row++) {
				String line = br.readLine();
				String[] tokens = line.split(delims);
				for (int col = 0; col < numCols; col++) {
					map[row][col] = Integer.parseInt(tokens[col]);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public int getTileSize() {
		return tileSize;
	}

	public double getx() {
		return x;
	}

	public double gety() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getType(int row, int col) {
		int rc = map[row][col];
		int r = rc / tileColumns;
		int c = rc % tileColumns;
		return tiles[r][c].getType();
	}

	public void setPosition(double x, double y) {

		this.x += (x - this.x) * tween;
		this.y += (y - this.y) * tween;

		fixBounds();

		colOffset = (int) -this.x / tileSize;
		rowOffset = (int) -this.y / tileSize;

	}

	private void fixBounds() {
		if (x < xmin) x = xmin;
		if (y < ymin) y = ymin;
		if (x > xmax) x = xmax;
		if (y > ymax) y = ymax;
	}

	public void render(Graphics2D g) {

		for (int row = rowOffset; row < rowOffset + numRowsToDraw; row++) {

			if (row >= numRows) break;

			for (int col = colOffset; col < colOffset + numColsToDraw; col++) {

				if (col >= numCols) break;

				if (map[row][col] == 0) continue;

				int rc = map[row][col];
				int r = rc / tileColumns;
				int c = rc % tileColumns;

				g.drawImage(tiles[r][c].getImage(), (int) x + col * tileSize, (int) y + row * tileSize, null);

			}
		}
	}

	public void setTween(double tween) {
		this.tween = tween;
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumCols() {
		return numCols;
	}

}
