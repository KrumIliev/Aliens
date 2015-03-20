package com.kdi.aliens.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyInput implements KeyListener {

	public static boolean[] keys = new boolean[256];

	@Override
	public void keyPressed(KeyEvent e) {
		KeyInput.keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		KeyInput.keys[e.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {}

}
