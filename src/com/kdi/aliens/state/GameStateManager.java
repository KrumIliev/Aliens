package com.kdi.aliens.state;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class GameStateManager {

	public static final int MENU = 0;
	public static final int LEVEL1 = 1;

	private ArrayList<GameState> gameStates;
	private int currentState;

	public GameStateManager() {
		currentState = MENU;
		gameStates = new ArrayList<GameState>();
		gameStates.add(new MenuState(this));
	}

	public void setState(int state) {
		currentState = state;
		gameStates.get(currentState).init();
	}

	public void update() {
		gameStates.get(currentState).update();
	}

	public void render(Graphics2D graphics) {
		gameStates.get(currentState).render(graphics);
	}

	public void keyPressed(int key) {
		gameStates.get(currentState).keyPressed(key);
	}

	public void keyReleased(int key) {
		gameStates.get(currentState).keyReleased(key);
	}

}
