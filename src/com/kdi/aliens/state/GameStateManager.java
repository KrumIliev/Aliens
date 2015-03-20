package com.kdi.aliens.state;

import java.awt.Graphics2D;

import com.kdi.aliens.state.levels.LevelOne;

public class GameStateManager {
	private int numStates = 3;

	public static final int MENU = 0;
	public static final int OPTIONS = 1;
	public static final int LEVEL1 = 2;

	private GameState[] gameStates;
	private int currentState;

	public GameStateManager() {
		gameStates = new GameState[numStates];
		currentState = MENU;
		loadState(currentState);
	}

	private void loadState(int state) {
		if (state == MENU) gameStates[state] = new MenuState(this);
		if (state == OPTIONS) gameStates[state] = new OptionsState(this);
		if (state == LEVEL1) gameStates[state] = new LevelOne(this);
	}

	private void unloadState(int state) {
		gameStates[state] = null;
	}

	public void setState(int state) {
		gameStates[currentState].release();
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
	}

	public void update() {
		gameStates[currentState].update();
	}

	public void render(Graphics2D graphics) {
		gameStates[currentState].render(graphics);
	}
}
