package com.kdi.aliens.state;

import java.awt.Graphics2D;

import com.kdi.aliens.state.levels.LevelOne;
import com.kdi.aliens.util.AudioPlayer;
import com.kdi.aliens.util.Reference;

public class GameStateManager {
	private int numStates = 3;

	public static final int MENU = 0;
	public static final int OPTIONS = 1;
	public static final int LEVEL1 = 2;

	private GameState[] gameStates;
	private int currentState;

	private String currentMusic;

	public GameStateManager() {
		gameStates = new GameState[numStates];
		currentState = MENU;
		currentMusic = Reference.MUSIC_MENU;
		AudioPlayer.playMusic(currentMusic);
		loadState(currentState);
	}

	private void loadState(int state) {
		if (state == MENU) {
			gameStates[state] = new MenuState(this);
			changeMusic(Reference.MUSIC_MENU);
		}
		if (state == OPTIONS) {
			gameStates[state] = new OptionsState(this);
			changeMusic(Reference.MUSIC_MENU);
		}
		if (state == LEVEL1) {
			gameStates[state] = new LevelOne(this);
			changeMusic(Reference.MUSIC_LEVEL1);
		}
	}

	private void unloadState(int state) {
		gameStates[state] = null;
	}

	public void setState(int state) {
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

	private void changeMusic(String key) {
		if (currentMusic.equals(key)) return;
		AudioPlayer.stopMusic(currentMusic);
		currentMusic = key;
		AudioPlayer.loop(currentMusic);
	}
}
