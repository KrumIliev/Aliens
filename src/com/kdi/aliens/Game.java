package com.kdi.aliens;

import javax.swing.JFrame;

public class Game {

	public static void main(String[] args) {
		JFrame window = new JFrame(GamePanel.GAME_NAME);
		window.setContentPane(new GamePanel());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setVisible(true);
	}

}
