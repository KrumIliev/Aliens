package com.kdi.aliens.entities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.kdi.aliens.graphics.Animation;
import com.kdi.aliens.tilemap.TileMap;

public class Player extends Entity {

	// player stuff
	private int health;
	private int maxHealth;
	private int bullets;
	private int maxBullets;
	private boolean dead;
	private boolean flinching;
	private long flinchTimer;

	// fireball
	private boolean firing;
	private int bulletCost;
	private int bulletDamage;
	//private ArrayList<Fireball> fireBalls;

	// animations
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = { 7, 1, 1, 4, 1 };

	// animation actions
	private static final int WALKING = 0;
	private static final int FALLING = 1;
	private static final int JUMPING = 2;
	private static final int IDLE = 3;
	private static final int FIRE = 4;

	public Player(TileMap tileMap) {
		super(tileMap);

		width = 70;
		height = 94;
		cWidth = 50;
		cHeight = 80;

		moveSpeed = 4;
		maxSpeed = 8;
		stopSpeed = 0.4;
		fallSpeed = 2.0;
		maxFallSpeed = 20.0;
		jumpStart = -40.0;
		stopJumpSpeed = 4.0;

		faceingRight = true;

		health = maxHealth = 5;
		bullets = maxBullets = 2500;

		bulletCost = 200;
		bulletDamage = 5;
		//fireBalls = new ArrayList<Fireball>();

		// load sprites
		try {

			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/sprites/player/pink.png"));

			sprites = new ArrayList<BufferedImage[]>();
			for (int i = 0; i < 5; i++) {

				BufferedImage[] bi = new BufferedImage[numFrames[i]];

				for (int j = 0; j < numFrames[i]; j++) {

					if (i != 6) {
						bi[j] = spritesheet.getSubimage(j * width, i * height, width, height);
					} else {
						bi[j] = spritesheet.getSubimage(j * 90, i * height, 90, height);
					}
				}

				sprites.add(bi);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		animation = new Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(400);
	}

	@Override
	public void update() {
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xTemp, yTemp);

		if (currentAction == FIRE) {
			if (animation.hasPlayedOnce()) firing = false;
		}

		// set animation
		if (firing) {
			setAnimation(FIRE, -1, 90);
		} else if (dy > 0) {
			setAnimation(FALLING, -1, 70);
		} else if (dy < 0) {
			setAnimation(JUMPING, -1, 70);
		} else if (left || right) {
			setAnimation(WALKING, 40, 70);
		} else {
			setAnimation(IDLE, 400, 70);
		}

		animation.update();

		// set direction
		if (currentAction != FIRE) {
			if (right) faceingRight = true;
			if (left) faceingRight = false;
		}
	}

	private void setAnimation(int action, int delay, int width) {
		if (currentAction != action) {
			currentAction = action;
			animation.setFrames(sprites.get(action));
			animation.setDelay(delay);
			this.width = width;
		}
	}

	@Override
	public void render(Graphics2D graphics) {

		setMapPosition();

		//for (Fireball fireball : fireBalls) {
		//	fireball.draw(graphics);
		//}

		// draw player
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed / 100 % 2 == 0) {
				return;
			}
		}

		super.render(graphics);
	}

	private void getNextPosition() {

		// movement
		if (left) {
			dx -= moveSpeed;
			if (dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		} else if (right) {
			dx += moveSpeed;
			if (dx > maxSpeed) {
				dx = maxSpeed;
			}
		} else {
			if (dx > 0) {
				dx -= stopSpeed;
				if (dx < 0) {
					dx = 0;
				}
			} else if (dx < 0) {
				dx += stopSpeed;
				if (dx > 0) {
					dx = 0;
				}
			}
		}

		// cannot move while attacking, except in air
		if (currentAction == FIRE && !(jumping || falling)) {
			dx = 0;
		}

		// jumping
		if (jumping && !falling) {	
			dy = jumpStart;
			falling = true;
		}

		// falling
		if (falling) {
			// TODO change gravity
			dy += fallSpeed;

			if (dy > 0) jumping = false;
			if (dy < 0 && !jumping) dy += stopJumpSpeed;

			if (dy > maxFallSpeed) dy = maxFallSpeed;

		}

	}

	public int getHealth() {
		return health;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public int getBullets() {
		return bullets;
	}

	public int getMaxBullets() {
		return maxBullets;
	}

	public void setFiring() {
		firing = true;
	}

}
