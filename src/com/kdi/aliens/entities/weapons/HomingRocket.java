package com.kdi.aliens.entities.weapons;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Set;

import javax.imageio.ImageIO;

import com.kdi.aliens.AlienGame;
import com.kdi.aliens.entities.enemies.Enemy;
import com.kdi.aliens.graphics.Animation;
import com.kdi.aliens.state.levels.LevelObjects;
import com.kdi.aliens.tilemap.World;
import com.kdi.aliens.util.Reference;

public class HomingRocket extends Weapon {

	public static final int SPEED = 8;
	public static final int SPEED_STOP = 0;

	public static final int HOMING_DISTANCE = 100;

	private static final int WIDTH = 30;
	private static final int HEIGHT = 30;
	private static final int COLLISION_WIDTH = 20;
	private static final int COLLISION_HEIGHT = 20;

	private static final int ENERGY_COST = 400;
	private static final int DAMAGE = 5;

	public HomingRocket(World world, boolean right) {
		super(world, right, ENERGY_COST, DAMAGE);
		setDimensions(WIDTH, HEIGHT, COLLISION_WIDTH, COLLISION_HEIGHT);
		setMovement(SPEED, SPEED_STOP);

		try {

			projectileSprites = new BufferedImage[1];
			projectileSprites[0] = ImageIO.read(getClass().getResource(Reference.RESOURCE_WEAPONS + "def.png"));
			BufferedImage explosionSheet = ImageIO.read(getClass().getResource(Reference.RESOURCE_WEAPONS + "default_weapon_explosion.png"));

			hitSprites = new BufferedImage[explosionSheet.getWidth() / width];
			for (int i = 0; i < hitSprites.length; i++) {
				hitSprites[i] = explosionSheet.getSubimage(i * width, 0, width, height);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		animation = new Animation();
		animation.setFrames(projectileSprites);
		animation.setDelay(-1);
	}

	@Override
	public void update() {
		checkTileMapCollision();
		setPosition(xTemp, yTemp);

		if (dx == 0 && !hit) setHit();

		animation.update();
		if (hit && animation.hasPlayedOnce()) {
			remove = true;
		}

		findEnemy();
	}

	// TODO
	private void findEnemy() {
		Enemy enemy = closestEnemy();
		System.out.println("Weapon location " + x + " / " + y);
		System.out.println("Enemy location " + enemy.getX() + " / " + enemy.getY());

		if (y - enemy.getY() > 0) {
			if (y - HOMING_DISTANCE < enemy.getY()) dy = -SPEED;
		} else if (y - enemy.getY() < 0) {
			if (y + HOMING_DISTANCE > enemy.getY()) dy = SPEED;
		} else {
			if (dx != SPEED_STOP) dy = SPEED_STOP;
		}

		if (x - enemy.getX() > 0) {
			if (x - HOMING_DISTANCE < enemy.getX()) dx = -SPEED;
		} else if (x - enemy.getX() < 0) {
			if (x + HOMING_DISTANCE > enemy.getX()) dx = SPEED;
		} else {
			if (dy != SPEED_STOP) dx = SPEED_STOP;

		}
	}

	private Enemy closestEnemy() {
		HashMap<Point, Enemy> enemies = new HashMap<Point, Enemy>();

		for (Enemy enemy : LevelObjects.enemies) {
			enemies.put(new Point(enemy.getX(), enemy.getY()), enemy);
		}

		Set<Point> locations = enemies.keySet();

		Point weaponLocation = new Point(getX(), getY());
		Point nearestLocation = null;

		double distance = AlienGame.WIDTH * AlienGame.HEIGHT;
		double temp;

		for (Point point : locations) {
			temp = weaponLocation.distance(point);
			if (temp < distance) {
				nearestLocation = point;
				distance = temp;
			}
		}

		return enemies.get(nearestLocation);
	}

	@Override
	public void render(Graphics2D graphics) {
		setMapPosition();
		setImageDirection(graphics);
	}

}
