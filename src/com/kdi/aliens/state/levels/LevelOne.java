package com.kdi.aliens.state.levels;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.kdi.aliens.AlienGame;
import com.kdi.aliens.effects.Effect;
import com.kdi.aliens.entities.Player;
import com.kdi.aliens.entities.enemies.Enemy;
import com.kdi.aliens.graphics.Background;
import com.kdi.aliens.graphics.HUD;
import com.kdi.aliens.input.KeyInput;
import com.kdi.aliens.items.Item;
import com.kdi.aliens.state.GameState;
import com.kdi.aliens.state.GameStateManager;
import com.kdi.aliens.tilemap.World;
import com.kdi.aliens.util.AudioPlayer;
import com.kdi.aliens.util.ContentManager;
import com.kdi.aliens.util.Reference;

public class LevelOne extends GameState {

	private World world;
	private Background background;
	private Player player;
	private HUD hud;

	private String soundExplosionKey = "explosion";

	public LevelOne(GameStateManager gameStateManager) {
		super(gameStateManager);
		init();
	}

	@Override
	public void init() {

		world = new World("world1_1.tmx");

		background = new Background(ContentManager.getImage(Reference.CM_BACKGROUND_LEVEL_1), 0.5);

		player = new Player(world);
		player.setPosition(200, 0);
		world.setPosition(200, 0);

		hud = new HUD(player);

		for (Enemy enemy : LevelObjects.enemies) {
			enemy.setPlayer(player);
		}

		AudioPlayer.loadSound(Reference.RESOURCE_SOUNDS + "explosion.mp3", soundExplosionKey);
	}

	@Override
	public void update() {
		hanleInput();
		player.update();
		world.setPosition(AlienGame.WIDTH / 2 - player.getX(), AlienGame.HEIGHT / 2 - player.getY());
		background.setXPosition(world.getx());

		for (int i = 0; i < LevelObjects.items.size(); i++) {
			Item item = LevelObjects.items.get(i);
			item.update();
			if (item.shouldRemove()) {
				LevelObjects.items.remove(i);
				i--;
			}
		}

		for (int i = 0; i < LevelObjects.enemies.size(); i++) {
			Enemy enemy = LevelObjects.enemies.get(i);
			enemy.update();
			if (enemy.isDead()) {
				LevelObjects.enemies.remove(i);
				i--;
				LevelObjects.effects.add(new Effect(enemy.getX(), enemy.getY(), 118, 118, "explosion.png", 70));
				AudioPlayer.playSound(soundExplosionKey);
			}
		}

		for (int i = 0; i < LevelObjects.effects.size(); i++) {
			LevelObjects.effects.get(i).update();
			if (LevelObjects.effects.get(i).shouldRemove()) LevelObjects.effects.remove(i);
		}

		if (player.isDead()) gameStateManager.setState(GameStateManager.MENU);
	}

	@Override
	public void render(Graphics2D graphics) {
		background.render(graphics);
		world.render(graphics);
		player.render(graphics);

		for (Enemy enemy : LevelObjects.enemies)
			enemy.render(graphics);

		for (Effect effect : LevelObjects.effects) {
			effect.setMapPosition((int) world.getx(), (int) world.gety());
			effect.render(graphics);
		}

		hud.render(graphics);
	}

	@Override
	public void hanleInput() {
		if (KeyInput.keys[KeyEvent.VK_ESCAPE]) gameStateManager.setState(GameStateManager.MENU);
	}
}
