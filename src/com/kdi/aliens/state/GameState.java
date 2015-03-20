package com.kdi.aliens.state;

import java.awt.Graphics2D;

public abstract class GameState {

	protected GameStateManager gameStateManager;

	public GameState(GameStateManager gameStateManager) {
		this.gameStateManager = gameStateManager;
	}

	public abstract void init();

	public abstract void update();

	public abstract void render(Graphics2D graphics);

	public abstract void release();
}
