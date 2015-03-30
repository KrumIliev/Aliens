package com.kdi.aliens.tilemap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import com.kdi.aliens.AlienGame;
import com.kdi.aliens.items.Item;
import com.kdi.aliens.state.levels.LevelObjects;

public class World {

	// position
	private double x;
	private double y;

	// bounds
	private int xmin;
	private int ymin;
	private int xmax;
	private int ymax;

	// map
	private int[][] map;
	private int tileSize;
	private int numRows;
	private int numCols;
	private int width;
	private int height;

	// tileset
	private BufferedImage tileset;
	private int tileColumns;
	private HashMap<Integer, Tile> tiles;

	// drawing
	private int rowOffset;
	private int colOffset;
	private int numRowsToDraw;
	private int numColsToDraw;

	public World(String name) {
		LevelObjects.clear();
		tiles = new HashMap<Integer, Tile>();

		TMXLoader.load(this, name);

		numRowsToDraw = AlienGame.HEIGHT / tileSize + 2;
		numColsToDraw = AlienGame.WIDTH / tileSize + 2;
	}

	public void setPosition(double x, double y) {

		this.x += (x - this.x) * 0.03f;
		this.y += (y - this.y) * 0.03f;

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

	public void render(Graphics2D graphics) {

		for (int row = rowOffset; row < rowOffset + numRowsToDraw; row++) {

			if (row >= numRows) break;

			for (int col = colOffset; col < colOffset + numColsToDraw; col++) {

				if (col >= numCols) break;
				if (map[row][col] == 0) continue;
				graphics.drawImage(tiles.get(map[row][col]).getImage(), (int) x + col * tileSize, (int) y + row * tileSize, null);
			}
		}

		for (Item item : LevelObjects.items) {
			item.setMapPosition((int) getx(), (int) gety());
			item.render(graphics);
		}
	}

	public int getTileSize() {
		return tileSize;
	}

	public void setTileSize(int size) {
		tileSize = size;
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

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setMap(int[][] map) {
		this.map = map;
	}

	public void setMapValue(int row, int col, int value) {
		map[row][col] = value;
	}

	public int getType(int row, int col) {
		return tiles.get(map[row][col]).getType();
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumCols() {
		return numCols;
	}

	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}

	public void setNumCols(int numCols) {
		this.numCols = numCols;
	}

	public void setXmin(int xmin) {
		this.xmin = xmin;
	}

	public void setYmin(int ymin) {
		this.ymin = ymin;
	}

	public void setXmax(int xmax) {
		this.xmax = xmax;
	}

	public void setYmax(int ymax) {
		this.ymax = ymax;
	}

	public BufferedImage getTileset() {
		return tileset;
	}

	public void setTileset(BufferedImage tileset) {
		this.tileset = tileset;
	}

	public int getTileColumns() {
		return tileColumns;
	}

	public void setTileColumns(int tileColumns) {
		this.tileColumns = tileColumns;
	}

	public HashMap<Integer, Tile> getTiles() {
		return tiles;
	}

	public void setTile(int tileCode, Tile tile) {
		tiles.put(tileCode, tile);
	}
}
