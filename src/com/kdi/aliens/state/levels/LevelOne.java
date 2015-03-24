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
import com.kdi.aliens.input.KeyInput;
import com.kdi.aliens.items.Coin;
import com.kdi.aliens.items.Health;
import com.kdi.aliens.items.Item;
import com.kdi.aliens.items.Life;
import com.kdi.aliens.state.GameState;
import com.kdi.aliens.state.GameStateManager;
import com.kdi.aliens.tilemap.TileMap;
import com.kdi.aliens.util.AudioPlayer;
import com.kdi.aliens.util.ContentManager;
import com.kdi.aliens.util.Reference;

public class LevelOne extends GameState {

	private TileMap tileMap;
	private Background background;
	private Player player;
	private HUD hud;

	private ArrayList<Item> items;
	private ArrayList<Enemy> enemies;
	private ArrayList<Effect> effects;

	private String soundExplosionKey = "explosion";

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

		background = new Background(ContentManager.getImage(Reference.CM_BACKGROUND_LEVEL_1), 0.5);

		player = new Player(tileMap);
		player.setPosition(200, 850);

		hud = new HUD(player);

		populateEnemies();
		populateItems();

		AudioPlayer.loadSound(Reference.RESOURCE_SOUNDS + "explosion.mp3", soundExplosionKey);
	}

	private void populateEnemies() {
		Point[] enemyLocations = new Point[] { new Point(600, 850), new Point(900, 450) };

		PinkBlob pinkBlob;
		for (Point location : enemyLocations) {
			pinkBlob = new PinkBlob(tileMap, player);
			pinkBlob.setPosition(location.x, location.y);
			enemies.add(pinkBlob);
		}
	}

	private void populateItems() {
		Point[] coinLocations = new Point[] { new Point(300, 600), new Point(400, 600), new Point(500, 600), new Point(600, 600) };
		Point[] heatsLocations = new Point[] { new Point(300, 550), new Point(400, 550), new Point(500, 550), new Point(600, 550) };
		Point[] lifesLocations = new Point[] { new Point(300, 650), new Point(400, 650), new Point(500, 650), new Point(600, 650) };

		Coin coin;
		for (Point location : coinLocations) {
			coin = new Coin(location.x, location.y);
			items.add(coin);
		}

		Health health;
		for (Point location : heatsLocations) {
			health = new Health(location.x, location.y);
			items.add(health);
		}

		Life life;
		for (Point location : lifesLocations) {
			life = new Life(location.x, location.y);
			items.add(life);
		}
	}

	@Override
	public void update() {
		hanleInput();
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
				effects.add(new Effect(enemy.getX(), enemy.getY(), 118, 118, "explosion.png", 70));
				AudioPlayer.playSound(soundExplosionKey);
			}
		}

		for (int i = 0; i < items.size(); i++) {
			Item item = items.get(i);
			item.update();
			if (item.shouldRemove()) {
				items.remove(i);
				i--;
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
	public void hanleInput() {
		if (KeyInput.keys[KeyEvent.VK_ESCAPE]) gameStateManager.setState(GameStateManager.MENU);
	}
}
