package com.kdi.aliens.tilemap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import com.kdi.aliens.AlienGame;
import com.kdi.aliens.entities.enemies.Enemy;
import com.kdi.aliens.items.Item;
import com.kdi.aliens.util.Reference;

public class TileMap {

	private static final String XML_TILEINFO = "tileset";
	private static final String XML_TILESIZE = "tilewidth";
	private static final String XML_TILESET = "image";
	private static final String XML_TILESET_IMAGE = "source";

	private static final String XML_LAYER = "layer";
	private static final String XML_LAYER_GID = "gid";

	private static final String XML_LAYER_NAME = "name";
	private static final String XML_LAYER_SOLID = "Solid"; //TODO change
	private static final String XML_LAYER_DAMAGE = "damage";
	private static final String XML_LAYER_LIQUID = "liquid";
	private static final String XML_LAYER_DECOR = "decor";
	private static final String XML_LAYER_RED_LOCK = "red"; //TODO change and add all
	private static final String XML_LAYER_RED_KEY = "redkey"; //TODO change and add all

	private static final String XML_OBJECT = "objectgroup";
	private static final String XML_OBJECT_COIN = "coins"; // TODO add all
	private static final String XML_OBJECT_ENEMY_PINK_BLOB = "pink_blob";
	private static final String XML_OBJECT_ENEMY_BAT = "bat"; //TODO add all enemies

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
	private int tileColumns, tileRows;
	private HashMap<Integer, Tile> tileMap;

	// drawing
	private int rowOffset;
	private int colOffset;
	private int numRowsToDraw;
	private int numColsToDraw;

	private ArrayList<Enemy> enemies;
	private ArrayList<Item> items;

	public TileMap(int tileSize) {
		this.tileSize = tileSize;
		numRowsToDraw = AlienGame.HEIGHT / tileSize + 2;
		numColsToDraw = AlienGame.WIDTH / tileSize + 2;
	}

	public void loadWorld(String name) {

		tileMap = new HashMap<Integer, Tile>();

		try {

			File file = new File("./res/levels/" + name);

			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(file);
			Element root = doc.getRootElement();

			/**
			 * Map size
			 */
			numCols = Integer.valueOf(root.getAttributeValue("width"));
			numRows = Integer.valueOf(root.getAttributeValue("height"));
			map = new int[numRows][numCols];
			width = numCols * tileSize;
			height = numRows * tileSize;

			xmin = AlienGame.WIDTH - width;
			xmax = 0;
			ymin = AlienGame.HEIGHT - height;
			ymax = 0;

			/**
			 * Tile size
			 */
			Element tileInfo = root.getChild(XML_TILEINFO);
			tileSize = Integer.valueOf(tileInfo.getAttribute(XML_TILESIZE).getValue());

			/**
			 * Tile set image name
			 */
			Element tileSetImage = tileInfo.getChild(XML_TILESET);
			initTileSetImage(tileSetImage.getAttribute(XML_TILESET_IMAGE).getValue());

			/**
			 * Map
			 */
			initMapArray(root);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initTileSetImage(String name) {
		try {
			tileset = ImageIO.read(getClass().getResourceAsStream(Reference.RESOURCE_TILES + name));
			tileColumns = tileset.getWidth() / tileSize;
			tileRows = tileset.getHeight() / tileSize;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Extracts the map from XML. All layers have the same length
	 */
	private void initMapArray(Element root) {

		List<Element> layers = root.getChildren(XML_LAYER);
		List<Element> layerSolid = null;
		List<Element> layerDamage = null;
		List<Element> layerLiquid = null;
		List<Element> layerDecor = null;
		List<Element> laterRedLock = null;
		List<Element> layerRedKey = null;

		for (int i = 0; i < layers.size(); i++) {

			Element layer = layers.get(i);
			String layerName = layer.getAttribute(XML_LAYER_NAME).getValue();

			if (layerName.equalsIgnoreCase(XML_LAYER_SOLID)) layerSolid = layer.getChild("data").getChildren("tile");
			if (layerName.equalsIgnoreCase(XML_LAYER_DAMAGE)) layerDamage = layer.getChild("data").getChildren("tile");
			if (layerName.equalsIgnoreCase(XML_LAYER_LIQUID)) layerLiquid = layer.getChild("data").getChildren("tile");
			if (layerName.equalsIgnoreCase(XML_LAYER_DECOR)) layerDecor = layer.getChild("data").getChildren("tile");
			if (layerName.equalsIgnoreCase(XML_LAYER_RED_LOCK)) laterRedLock = layer.getChild("data").getChildren("tile");
			if (layerName.equalsIgnoreCase(XML_LAYER_RED_KEY)) layerRedKey = layer.getChild("data").getChildren("tile");
		}

		int counter = 0;
		int solid, damage, liquid, decor, redLock, redKey;
		int tileCode;
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numCols; col++) {

				solid = Integer.valueOf(layerSolid.get(counter).getAttributeValue(XML_LAYER_GID));
				damage = Integer.valueOf(layerDamage.get(counter).getAttributeValue(XML_LAYER_GID));
				liquid = Integer.valueOf(layerLiquid.get(counter).getAttributeValue(XML_LAYER_GID));
				decor = Integer.valueOf(layerDecor.get(counter).getAttributeValue(XML_LAYER_GID));
				redLock = Integer.valueOf(laterRedLock.get(counter).getAttributeValue(XML_LAYER_GID));
				redKey = Integer.valueOf(layerRedKey.get(counter).getAttributeValue(XML_LAYER_GID));

				if (solid != 0) {
					tileCode = solid;
					addToTilesArray(tileCode, Tile.SOLID);
				} else if (damage != 0) {
					tileCode = damage;
					addToTilesArray(tileCode, Tile.DAMAGE);
				} else if (liquid != 0) {
					tileCode = liquid;
					addToTilesArray(tileCode, Tile.LIQUID);
				} else if (decor != 0) {
					tileCode = decor;
					addToTilesArray(tileCode, Tile.DECOR);
				} else if (redLock != 0) {
					tileCode = redLock;
					addToTilesArray(tileCode, Tile.LOCK_RED);
				} else if (redKey != 0) {
					tileCode = redKey;
					addToTilesArray(tileCode, Tile.LOCK_RED);
				} else {
					tileCode = 0;
					addToTilesArray(tileCode, Tile.LOCK_RED);
				}

				map[row][col] = tileCode;
				counter++;
			}
		}
	}

	private void addToTilesArray(int tileCode, int type) {
		if (tileMap.containsKey(tileCode)) return;
		int tileCol, tileRow;
		int temp;
		if (tileCode < tileColumns) {
			tileRow = 0;
			tileCol = tileCode - 1;
		} else {
			temp = tileCode / tileColumns;
			if ((temp * tileColumns) == 0) {
				tileRow = temp - 1;
				tileCol = tileColumns - 1;
			} else if ((temp * tileColumns) < tileCode) {
				tileRow = temp;
				tileCol = (tileCode % tileColumns) - 1;
			} else {
				tileRow = temp - 1;
				tileCol = tileColumns - 1;
			}
		}

		if (tileCol < 0) tileCol = 0;
		if (tileRow < 0) tileRow = 0;

		BufferedImage subimage = tileset.getSubimage(tileCol * tileSize, tileRow * tileSize, tileSize, tileSize);
		tileMap.put(tileCode, new Tile(subimage, type));
	}

	private void initMapObjects(Document doc) {
		// TODO
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
		return tileMap.get(map[row][col]).getType();
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

	public void render(Graphics2D g) {

		for (int row = rowOffset; row < rowOffset + numRowsToDraw; row++) {

			if (row >= numRows) break;

			for (int col = colOffset; col < colOffset + numColsToDraw; col++) {

				if (col >= numCols) break;
				if (map[row][col] == 0) continue;
				g.drawImage(tileMap.get(map[row][col]).getImage(), (int) x + col * tileSize, (int) y + row * tileSize, null);
			}
		}
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumCols() {
		return numCols;
	}

}
