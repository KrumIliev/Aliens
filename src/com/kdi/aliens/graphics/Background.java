package com.kdi.aliens.graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.kdi.aliens.GamePanel;

public class Background {

	private BufferedImage image;

	private double x, y;
	private double dx, dy;

	private double moveScale;

	public Background(BufferedImage image, double moveScale) {
		this.image = image;
		this.moveScale = moveScale;
	}

	public void setXPosition(double x) {
		this.x = (x * moveScale) % GamePanel.WIDTH;
	}

	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public void update() {
		x += dx;
		y += dy;
	}

	public void render(Graphics2D graphics) {
		graphics.drawImage(image, (int) x, (int) y, null);
		if (x < 0) graphics.drawImage(image, (int) x + GamePanel.WIDTH, (int) y, null);
		if (x > 0) graphics.drawImage(image, (int) x - GamePanel.WIDTH, (int) y, null);
	}

}
