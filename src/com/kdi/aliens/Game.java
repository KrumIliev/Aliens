package com.kdi.aliens;

import javax.swing.JFrame;

public class Game {

	public static void main(String[] args) {
		System.setProperty("sun.java2d.opengl", "True");
		System.setProperty("sun.java2d.d3d", "True");
		System.setProperty("sun.java2d.ddforcevram", "True");
		System.setProperty("sun.java2d.transaccel", "True");

		JFrame window = new JFrame(GamePanel.GAME_NAME);
		window.setContentPane(new GamePanel());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setVisible(true);
		window.createBufferStrategy(2);
	}

}
