package com.kdi.aliens.state.levels;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.kdi.aliens.GamePanel;
import com.kdi.aliens.entities.Player;
import com.kdi.aliens.graphics.Background;
import com.kdi.aliens.state.GameState;
import com.kdi.aliens.state.GameStateManager;
import com.kdi.aliens.tilemap.TileMap;

public class LevelOne extends GameState {

	private TileMap tileMap;
	private Background background;
	private Player player;

	public LevelOne(GameStateManager gameStateManager) {
		super(gameStateManager);
		init();
	}

	@Override
	public void init() {
		tileMap = new TileMap(70);
		tileMap.loadTiles("/tiles/grass.png");
		tileMap.loadMap("/levels/level1.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);

		background = new Background("/backgrounds/level1_background.png", 0.5);

		player = new Player(tileMap);
		player.setPosition(200, 600);
	}

	@Override
	public void update() {
		player.update();
		tileMap.setPosition(GamePanel.WIDTH / 2 - player.getX(), GamePanel.HEIGHT / 2 - player.getY());
		background.setXPosition(tileMap.getx());
	}

	@Override
	public void render(Graphics2D graphics) {
		background.render(graphics);
		tileMap.render(graphics);
		player.render(graphics);
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
