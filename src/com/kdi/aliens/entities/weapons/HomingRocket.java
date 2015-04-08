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

	public static final int SPEED = 5;
	public static final int SPEED_STOP = 0;

	public static final int HOMING_DISTANCE = 200;

	private static final int WIDTH = 30;
	private static final int HEIGHT = 30;
	private static final int COLLISION_WIDTH = 20;
	private static final int COLLISION_HEIGHT = 20;
	private static final int EXPLOSION_WIDTH = 30;
	private static final int EXPLOSION_HEIGHT = 30;

	private static final int ENERGY_COST = 0;
	private static final int DAMAGE = 1;

	public HomingRocket(World world, boolean right) {
		super(world, right, ENERGY_COST, DAMAGE);
		setDimensions(WIDTH, HEIGHT, COLLISION_WIDTH, COLLISION_HEIGHT, EXPLOSION_WIDTH, EXPLOSION_HEIGHT);
		setMovement(SPEED, SPEED_STOP);

		try {

			projectileSprites = new BufferedImage[1];
			projectileSprites[0] = ImageIO.read(getClass().getResource(Reference.RESOURCE_WEAPONS + "def.png"));
			BufferedImage explosionSheet = ImageIO.read(getClass().getResource(Reference.RESOURCE_WEAPONS + "default_weapon_explosion.png"));

			hitSprites = new BufferedImage[explosionSheet.getWidth() / explosionWidth];
			for (int i = 0; i < hitSprites.length; i++)
				hitSprites[i] = explosionSheet.getSubimage(i * explosionWidth, 0, explosionWidth, explosionHeight);

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
		if (hit && animation.hasPlayedOnce()) remove = true;

		findEnemy();
	}

	private void findEnemy() {
		if (hit) return;

		Enemy enemy = closestEnemy();

		/**
		 * Enemy is on the same X axis and up
		 */
		if (x == enemy.getX() && y - enemy.getY() > 0) {
			if (y - HOMING_DISTANCE < enemy.getY()) {
				dy = -SPEED;
				dx = SPEED_STOP;
			}
		}

		/**
		 * Enemy is on the same X axis and down
		 */
		else if (x == enemy.getX() && y - enemy.getY() < 0) {
			if (y + HOMING_DISTANCE > enemy.getY()) {
				dy = SPEED;
				dx = SPEED_STOP;
			}
		}

		/**
		 * Enemy is on the same Y axis and left
		 */
		else if (y == enemy.getY() && x - enemy.getX() > 0) {
			if (x - HOMING_DISTANCE < enemy.getX()) {
				dy = SPEED_STOP;
				dx = -SPEED;
			}
		}

		/**
		 * Enemy is on the same Y axis and right
		 */
		else if (y == enemy.getY() && x - enemy.getX() < 0) {
			if (x + HOMING_DISTANCE > enemy.getX()) {
				dy = SPEED_STOP;
				dx = SPEED;
			}
		}

		/**
		 * Enemy is on the left and top
		 */
		else if (x - enemy.getX() > 0 && y - enemy.getY() > 0) {
			if (x - HOMING_DISTANCE < enemy.getX() && y - HOMING_DISTANCE < enemy.getY()) {
				if (SPEED > x - enemy.getX()) {
					dx = -(x - enemy.getX());
				} else {
					dx = -SPEED;
				}
				if (SPEED > y - enemy.getY()) {
					dy = -(y - enemy.getY());
				} else {
					dy = -SPEED;
				}
			}
		}

		/**
		 * Enemy is on the left and down
		 */
		else if (x - enemy.getX() > 0 && y - enemy.getY() < 0) {
			if (x - HOMING_DISTANCE < enemy.getX() && y + HOMING_DISTANCE > enemy.getY()) {
				if (SPEED > x - enemy.getX()) {
					dx = -(x - enemy.getX());
				} else {
					dx = -SPEED;
				}
				if (SPEED > enemy.getY() - y) {
					dy = enemy.getY() - y;
				} else {
					dy = SPEED;
				}
			}
		}

		/**
		 * Enemy is on the right and top
		 */
		else if (x - enemy.getX() < 0 && y - enemy.getY() > 0) {
			if (x + HOMING_DISTANCE > enemy.getX() && y - HOMING_DISTANCE < enemy.getY()) {
				if (SPEED > enemy.getX() - x) {
					dx = enemy.getX() - x;
				} else {
					dx = SPEED;
				}
				if (SPEED > y - enemy.getY()) {
					dy = -(y - enemy.getY());
				} else {
					dy = -SPEED;
				}
			}
		}

		/**
		 * Enemy is on the right and down
		 */
		else if (x - enemy.getX() < 0 && y - enemy.getY() < 0) {
			if (x + HOMING_DISTANCE > enemy.getX() && y + HOMING_DISTANCE > enemy.getY()) {
				if (SPEED > enemy.getX() - x) {
					dx = enemy.getX() - x;
				} else {
					dx = SPEED;
				}
				if (SPEED > enemy.getY() - y) {
					dy = enemy.getY() - y;
				} else {
					dy = SPEED;
				}
			}
		}
	}

	private Enemy closestEnemy() {
		HashMap<Point, Enemy> enemies = new HashMap<Point, Enemy>();

		for (Enemy enemy : LevelObjects.enemies)
			enemies.put(new Point(enemy.getX(), enemy.getY()), enemy);

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
