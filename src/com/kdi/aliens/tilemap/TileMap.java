package com.kdi.aliens.tilemap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.kdi.aliens.AlienGame;
import com.kdi.aliens.entities.enemies.Enemy;
import com.kdi.aliens.items.Item;
import com.kdi.aliens.util.Reference;

public class TileMap {

	private static final String XML_TILEINFO = "tileset";
	private static final String XML_TILESIZE = "tilewidth";
	private static final String XML_TILESET = "image";
	private static final String XML_TILESET_IMAGE = "source";
	private static final String XML_TILESET_WIDTH = "width";
	private static final String XML_TILESET_HEIGHT = "height";

	private static final String XML_LAYER = "layer";
	private static final String XML_LAYER_DATA = "data";
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
	private Tile[][] tiles;

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

	public void loadTiles(String name) {

		try {

			tileset = ImageIO.read(getClass().getResourceAsStream(Reference.RESOURCE_TILES + name));
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

	public void loadLevel(String name) {

		try {
			InputStream in = getClass().getResourceAsStream(Reference.RESOURCE_LEVELS + name);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			numCols = Integer.parseInt(br.readLine());
			numRows = Integer.parseInt(br.readLine());
			map = new int[numRows][numCols];
			width = numCols * tileSize;
			height = numRows * tileSize;

			xmin = AlienGame.WIDTH - width;
			xmax = 0;
			ymin = AlienGame.HEIGHT - height;
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

	public void loadWorld(String name) {

	}

	private void extractWorld(String xmlFilePath) {
		try {

			File fXmlFile = new File(xmlFilePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			/**
			 * Tile size
			 */
			Element tileInfo = (Element) doc.getElementsByTagName(XML_TILEINFO).item(0);
			tileSize = Integer.valueOf(tileInfo.getAttribute(XML_TILESIZE));

			/**
			 * Tile set image name
			 */
			Element tileSetImage = (Element) doc.getElementsByTagName(XML_TILESET).item(0);
			initTileSetImage(tileSetImage.getAttribute(XML_TILESET_IMAGE));

			/**
			 * Map
			 */
			width = Integer.valueOf(tileSetImage.getAttribute(XML_TILESET_WIDTH));
			height = Integer.valueOf(tileSetImage.getAttribute(XML_TILESET_HEIGHT));
			initMapArray(doc);

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
	private void initMapArray(Document doc) {
		numCols = width / tileSize;
		numRows = height / tileSize;
		map = new int[numRows][numCols];

		xmin = AlienGame.WIDTH - width;
		xmax = 0;
		ymin = AlienGame.HEIGHT - height;
		ymax = 0;

		NodeList layers = doc.getElementsByTagName(XML_LAYER);
		Node layerSolid, layerDamage, layerLiquid, layerDecor, laterRedLock, layerRedKey;

		for (int i = 0; i < layers.getLength(); i++) {

			Node layer = layers.item(i);

			Element element = (Element) layer;
			String layerName = element.getAttribute(XML_LAYER_NAME);

			if (layerName.equalsIgnoreCase(XML_LAYER_SOLID)) layerSolid = layers.item(i);
			if (layerName.equalsIgnoreCase(XML_LAYER_DAMAGE)) layerDamage = layers.item(i);
			if (layerName.equalsIgnoreCase(XML_LAYER_LIQUID)) layerLiquid = layers.item(i);
			if (layerName.equalsIgnoreCase(XML_LAYER_DECOR)) layerDecor = layers.item(i);
			if (layerName.equalsIgnoreCase(XML_LAYER_RED_LOCK)) laterRedLock = layers.item(i);
			if (layerName.equalsIgnoreCase(XML_LAYER_RED_KEY)) layerRedKey = layers.item(i);
		}

		//TODO Add layer information to array
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
		int rc = map[row][col];
		int r = rc / tileColumns;
		int c = rc % tileColumns;
		return tiles[r][c].getType();
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

				int rc = map[row][col];
				int r = rc / tileColumns;
				int c = rc % tileColumns;

				g.drawImage(tiles[r][c].getImage(), (int) x + col * tileSize, (int) y + row * tileSize, null);

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
