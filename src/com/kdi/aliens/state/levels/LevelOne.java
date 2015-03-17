package com.kdi.aliens.state.levels;

import java.awt.Graphics2D;

import com.kdi.aliens.state.GameState;
import com.kdi.aliens.state.GameStateManager;
import com.kdi.aliens.tilemap.Background;
import com.kdi.aliens.tilemap.TileMap;

public class LevelOne extends GameState {

	private TileMap tileMap;
	private Background background;

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
	}

	@Override
	public void update() {
		background.setPosition(tileMap.getx(), tileMap.gety());
	}

	@Override
	public void render(Graphics2D graphics) {
		background.render(graphics);
		tileMap.render(graphics);
	}

	@Override
	public void keyPressed(int key) {}

	@Override
	public void keyReleased(int key) {}

}
