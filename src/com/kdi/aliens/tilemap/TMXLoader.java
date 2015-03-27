package com.kdi.aliens.tilemap;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import com.kdi.aliens.AlienGame;
import com.kdi.aliens.entities.enemies.Enemy;
import com.kdi.aliens.entities.enemies.Fly;
import com.kdi.aliens.entities.enemies.PinkBlob;
import com.kdi.aliens.items.Coin;
import com.kdi.aliens.items.Item;
import com.kdi.aliens.util.Reference;

public class TMXLoader {

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
	private static final String XML_OBJECT_NAME = "name";
	private static final String XML_OBJECT_COIN = "coins"; // TODO add all
	private static final String XML_OBJECT_ENEMY_PINK_BLOB = "pink_blob";
	private static final String XML_OBJECT_ENEMY_BAT = "bat"; //TODO add all enemies

	public static void load(TileMap tileMap, String tmxFile) {
		try {

			File file = new File("./res/levels/" + tmxFile);

			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(file);
			Element root = doc.getRootElement();

			setTileInfo(tileMap, root);
			setMapSize(tileMap, root);
			initMapArray(tileMap, root);
			initMapObjects(tileMap, root);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void setMapSize(TileMap tileMap, Element root) {
		tileMap.setNumCols(Integer.valueOf(root.getAttributeValue("width")));
		tileMap.setNumRows(Integer.valueOf(root.getAttributeValue("height")));
		tileMap.setMap(new int[tileMap.getNumRows()][tileMap.getNumCols()]);
		tileMap.setWidth(tileMap.getNumCols() * tileMap.getTileSize());
		tileMap.setHeight(tileMap.getNumRows() * tileMap.getTileSize());

		tileMap.setXmin(AlienGame.WIDTH - tileMap.getWidth());
		tileMap.setXmax(0);
		tileMap.setYmin(AlienGame.HEIGHT - tileMap.getHeight());
		tileMap.setYmax(0);
	}

	private static void setTileInfo(TileMap tileMap, Element root) throws IOException {
		Element tileInfo = root.getChild(XML_TILEINFO);
		tileMap.setTileSize(Integer.valueOf(tileInfo.getAttribute(XML_TILESIZE).getValue()));
		Element tileSetImage = tileInfo.getChild(XML_TILESET);
		String tileSetImageName = tileSetImage.getAttribute(XML_TILESET_IMAGE).getValue();
		tileMap.setTileset(ImageIO.read(TMXLoader.class.getResourceAsStream(Reference.RESOURCE_TILES + tileSetImageName)));
		tileMap.setTileColumns(tileMap.getTileset().getWidth() / tileMap.getTileSize());
	}

	private static void initMapArray(TileMap tileMap, Element root) {

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
		for (int row = 0; row < tileMap.getNumRows(); row++) {
			for (int col = 0; col < tileMap.getNumCols(); col++) {

				solid = Integer.valueOf(layerSolid.get(counter).getAttributeValue(XML_LAYER_GID));
				damage = Integer.valueOf(layerDamage.get(counter).getAttributeValue(XML_LAYER_GID));
				liquid = Integer.valueOf(layerLiquid.get(counter).getAttributeValue(XML_LAYER_GID));
				decor = Integer.valueOf(layerDecor.get(counter).getAttributeValue(XML_LAYER_GID));
				redLock = Integer.valueOf(laterRedLock.get(counter).getAttributeValue(XML_LAYER_GID));
				redKey = Integer.valueOf(layerRedKey.get(counter).getAttributeValue(XML_LAYER_GID));

				if (solid != 0) {
					tileCode = solid;
					addToTilesArray(tileMap, tileCode, Tile.SOLID);
				} else if (damage != 0) {
					tileCode = damage;
					addToTilesArray(tileMap, tileCode, Tile.DAMAGE);
				} else if (liquid != 0) {
					tileCode = liquid;
					addToTilesArray(tileMap, tileCode, Tile.LIQUID);
				} else if (decor != 0) {
					tileCode = decor;
					addToTilesArray(tileMap, tileCode, Tile.DECOR);
				} else if (redLock != 0) {
					tileCode = redLock;
					addToTilesArray(tileMap, tileCode, Tile.LOCK_RED);
				} else if (redKey != 0) {
					tileCode = redKey;
					addToTilesArray(tileMap, tileCode, Tile.LOCK_RED);
				} else {
					tileCode = 0;
					addToTilesArray(tileMap, tileCode, Tile.LOCK_RED);
				}

				tileMap.setMapValue(row, col, tileCode);
				counter++;
			}
		}
	}

	private static void addToTilesArray(TileMap tileMap, int tileCode, int type) {
		if (tileMap.getTiles().containsKey(tileCode)) return;
		int tileCol, tileRow;
		int temp;
		if (tileCode < tileMap.getTileColumns()) {
			tileRow = 0;
			tileCol = tileCode - 1;
		} else {
			temp = tileCode / tileMap.getTileColumns();
			if ((temp * tileMap.getTileColumns()) == 0) {
				tileRow = temp - 1;
				tileCol = tileMap.getTileColumns() - 1;
			} else if ((temp * tileMap.getTileColumns()) < tileCode) {
				tileRow = temp;
				tileCol = (tileCode % tileMap.getTileColumns()) - 1;
			} else {
				tileRow = temp - 1;
				tileCol = tileMap.getTileColumns() - 1;
			}
		}

		if (tileCol < 0) tileCol = 0;
		if (tileRow < 0) tileRow = 0;

		BufferedImage subimage = tileMap.getTileset().getSubimage(tileCol * tileMap.getTileSize(), tileRow * tileMap.getTileSize(),
				tileMap.getTileSize(), tileMap.getTileSize());
		tileMap.setTile(tileCode, new Tile(subimage, type));
	}

	private static void initMapObjects(TileMap tileMap, Element root) {
		List<Element> objects = root.getChildren(XML_OBJECT);

		for (Element e : objects) {
			String objName = e.getAttributeValue(XML_OBJECT_NAME);
			if (objName.equalsIgnoreCase(XML_OBJECT_COIN)) loadItem(tileMap, e.getChildren("object"), Item.TYPE_COIN);
			if (objName.equalsIgnoreCase(XML_OBJECT_ENEMY_PINK_BLOB)) loadEnemy(tileMap, e.getChildren("object"), Enemy.TYPE_PINK_BLOB);
			if (objName.equalsIgnoreCase(XML_OBJECT_ENEMY_BAT)) loadEnemy(tileMap, e.getChildren("object"), Enemy.TYPE_BAT);
		}
	}

	private static void loadItem(TileMap tileMap, List<Element> items, int type) {
		for (Element item : items) {
			int x = Integer.valueOf(item.getAttributeValue("x"));
			int y = Integer.valueOf(item.getAttributeValue("y"));
			if (type == Item.TYPE_COIN) tileMap.addItem(new Coin(x, y));
			//TODO add more items
		}
	}

	private static void loadEnemy(TileMap tileMap, List<Element> enemies, int type) {
		for (Element enemy : enemies) {
			int x = Integer.valueOf(enemy.getAttributeValue("x"));
			int y = Integer.valueOf(enemy.getAttributeValue("y"));
			if (type == Enemy.TYPE_PINK_BLOB) tileMap.addEnemy(new PinkBlob(tileMap, x, y));
			if (type == Enemy.TYPE_BAT) tileMap.addEnemy(new Fly(tileMap, x, y));
			//TODO add more enemies
		}
	}

}
