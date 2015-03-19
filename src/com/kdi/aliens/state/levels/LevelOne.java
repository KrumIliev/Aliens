package com.kdi.aliens.state.levels;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.kdi.aliens.GamePanel;
import com.kdi.aliens.effects.Effect;
import com.kdi.aliens.entities.Player;
import com.kdi.aliens.entities.enemies.Enemy;
import com.kdi.aliens.entities.enemies.PinkBlob;
import com.kdi.aliens.graphics.Background;
import com.kdi.aliens.graphics.HUD;
import com.kdi.aliens.items.Coin;
import com.kdi.aliens.items.Item;
import com.kdi.aliens.state.GameState;
import com.kdi.aliens.state.GameStateManager;
import com.kdi.aliens.tilemap.TileMap;

public class LevelOne extends GameState {

	private TileMap tileMap;
	private Background background;
	private Player player;
	private HUD hud;

	private ArrayList<Item> items;
	private ArrayList<Enemy> enemies;
	private ArrayList<Effect> effects;

	public LevelOne(GameStateManager gameStateManager) {
		super(gameStateManager);
		init();
	}

	@Override
	public void init() {
		enemies = new ArrayList<Enemy>();
		effects = new ArrayList<Effect>();
		items = new ArrayList<Item>();

		tileMap = new TileMap(70);
		tileMap.loadTiles("grass.png");
		tileMap.loadLevel("level1.map");
		tileMap.setPosition(0, 0);

		background = new Background("level1_background.png", 0.5);

		player = new Player(tileMap);
		player.setPosition(200, 850);

		hud = new HUD(player);

		populateEnemies();
		populateItems();
	}

	private void populateEnemies() {
		Point[] enemyLocations = new Point[] { new Point(600, 850) };

		PinkBlob pinkBlob;
		for (Point location : enemyLocations) {
			pinkBlob = new PinkBlob(tileMap);
			pinkBlob.setPosition(location.x, location.y);
			enemies.add(pinkBlob);
		}
	}

	private void populateItems() {
		Point[] itemLocations = new Point[] { new Point(600, 600) };

		Coin coin;
		for (Point location : itemLocations) {
			coin = new Coin(location.x, location.y);
			items.add(coin);
		}
	}

	@Override
	public void update() {
		player.update();
		tileMap.setPosition(GamePanel.WIDTH / 2 - player.getX(), GamePanel.HEIGHT / 2 - player.getY());
		background.setXPosition(tileMap.getx());

		player.checkAttack(enemies);
		player.checkItems(items);

		for (int i = 0; i < enemies.size(); i++) {
			Enemy enemy = enemies.get(i);
			enemy.update();
			if (enemy.isDead()) {
				enemies.remove(i);
				i--;
				effects.add(new Effect(enemy.getX(), enemy.getY(), 70, 70, "explosion.png", 70));
			}
		}

		for (int i = 0; i < items.size(); i++) {
			Item item = items.get(i);
			item.update();
			if (item.shouldRemove()) {
				items.remove(i);
				i--;
				//TODO play sound
			}
		}

		for (int i = 0; i < effects.size(); i++) {
			effects.get(i).update();
			if (effects.get(i).shouldRemove()) effects.remove(i);
		}

		if (player.isDead()) gameStateManager.setState(GameStateManager.MENU);
	}

	@Override
	public void render(Graphics2D graphics) {
		background.render(graphics);
		tileMap.render(graphics);
		player.render(graphics);

		for (Enemy enemy : enemies)
			enemy.render(graphics);

		for (Effect effect : effects) {
			effect.setMapPosition((int) tileMap.getx(), (int) tileMap.gety());
			effect.render(graphics);
		}

		for (Item item : items) {
			item.setMapPosition((int) tileMap.getx(), (int) tileMap.gety());
			item.render(graphics);
		}

		hud.render(graphics);
	}

	@Override
	public void keyPressed(int key) {
		if (key == KeyEvent.VK_LEFT) player.setLeft(true);
		if (key == KeyEvent.VK_RIGHT) player.setRight(true);
		if (key == KeyEvent.VK_UP) player.setUp(true);
		if (key == KeyEvent.VK_DOWN) player.setDown(true);
		if (key == KeyEvent.VK_SPACE) player.setJumping(true);
		if (key == KeyEvent.VK_Q) player.setFiring();
	}

	@Override
	public void keyReleased(int key) {
		if (key == KeyEvent.VK_LEFT) player.setLeft(false);
		if (key == KeyEvent.VK_RIGHT) player.setRight(false);
		if (key == KeyEvent.VK_UP) player.setUp(false);
		if (key == KeyEvent.VK_DOWN) player.setDown(false);
		if (key == KeyEvent.VK_SPACE) player.setJumping(false);
	}

}
