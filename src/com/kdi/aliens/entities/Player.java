package com.kdi.aliens.entities;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.kdi.aliens.entities.enemies.Enemy;
import com.kdi.aliens.entities.weapons.DefaultWeapon;
import com.kdi.aliens.graphics.Animation;
import com.kdi.aliens.input.KeyInput;
import com.kdi.aliens.items.Coin;
import com.kdi.aliens.items.Health;
import com.kdi.aliens.items.Item;
import com.kdi.aliens.items.Life;
import com.kdi.aliens.tilemap.TileMap;
import com.kdi.aliens.util.AudioPlayer;
import com.kdi.aliens.util.Reference;

public class Player extends Entity {

	// player stuff
	private double health;
	private double maxHealth;
	private int energy;
	private int maxEnergy;
	private int lives;
	private boolean knockback;
	private boolean dead;
	private boolean flinching;
	private long flinchTimer;
	private int coins = 0;
	private int eWidth; // Attacking width

	/**
	 * Keys
	 */
	private boolean greenKey, blueKey, yellowKey, redKey;

	// default weapon
	private boolean firing;
	private ArrayList<DefaultWeapon> weapons;

	/**
	 * Animations
	 */
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = { 7, 1, 1, 4, 5 };

	/**
	 * Animation actions
	 */
	private static final int WALKING = 0;
	private static final int FALLING = 1;
	private static final int JUMPING = 2;
	private static final int IDLE = 3;
	private static final int FIRE = 4;

	/**
	 * Sounds
	 */
	private String jumpSoundKey = "jump";
	private String laserSoundKey = "laser";

	public Player(TileMap tileMap) {
		super(tileMap);

		width = 70;
		eWidth = 158;
		height = 94;
		cWidth = 50;
		cHeight = 80;

		moveSpeed = 4;
		maxSpeed = 8;
		stopSpeed = 0.8; // slide effect
		fallSpeed = 1.0;
		maxFallSpeed = 20.0;
		jumpStart = -30.0;
		stopJumpSpeed = 6.0;

		facingRight = true;

		lives = 3;
		health = maxHealth = 5;
		energy = maxEnergy = 1000;

		weapons = new ArrayList<DefaultWeapon>();

		// load sprites
		try {

			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream(Reference.RESOURCE_PLAYER + "pink.png"));

			sprites = new ArrayList<BufferedImage[]>();
			for (int i = 0; i < 5; i++) {

				BufferedImage[] bi = new BufferedImage[numFrames[i]];

				for (int j = 0; j < numFrames[i]; j++) {

					if (i != 4) {
						bi[j] = spritesheet.getSubimage(j * width, i * height, width, height);
					} else {
						bi[j] = spritesheet.getSubimage(j * eWidth, i * height, eWidth, height);
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

		AudioPlayer.loadSound(Reference.RESOURCE_SOUNDS + "jump.mp3", jumpSoundKey);
		AudioPlayer.loadSound(Reference.RESOURCE_SOUNDS + "laser.mp3", laserSoundKey);
	}

	@Override
	public void update() {
		/**
		 * Handle user input
		 */
		handleInput();

		/**
		 * Update position
		 */
		getNextPosition();
		checkTileMapCollision();
		setPosition(xTemp, yTemp);

		if (currentAction == FIRE) if (animation.hasPlayedOnce()) firing = false;

		energy += 1; // TODO remove after energy pickup system is ready
		if (energy > maxEnergy) energy = maxEnergy;
		if (firing && currentAction != FIRE) {
			DefaultWeapon weapon = new DefaultWeapon(tileMap, facingRight);
			if (energy > weapon.getEnergyCost()) {
				setAnimation(FIRE, 30, eWidth);
				weapon.setPosition(x + weapon.xOffset, y + weapon.yOffset);
				weapons.add(weapon);
				energy -= weapon.getEnergyCost();
				AudioPlayer.playSound(laserSoundKey);
			}
		}

		for (int i = 0; i < weapons.size(); i++) {
			weapons.get(i).update();
			if (weapons.get(i).shouldRemove()) {
				weapons.remove(i);
				i--;
			}
		}

		if (flinching) {
			long elapsedTime = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsedTime > 1000) flinching = false;
		}

		// set animation
		if (firing) {} else if (dy > 0) {
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
			if (right) facingRight = true;
			if (left) facingRight = false;
		}

		//TODO remove edit player respawn position
		if (y > tileMap.getHeight()) updateLives();

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

		for (DefaultWeapon weapon : weapons)
			weapon.render(graphics);

		// draw player
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed / 100 % 2 == 0) return;
		}

		setImageDirection(graphics);
	}

	public void checkAttack(ArrayList<Enemy> enemies) {
		for (Enemy enemy : enemies) {

			for (DefaultWeapon weapon : weapons) {
				if (weapon.intersects(enemy)) {
					enemy.hit(weapon.getDamage());
					weapon.setHit();
					break;
				}
			}

			if (intersects(enemy)) hit(enemy.getDamage());
		}
	}

	public void checkItems(ArrayList<Item> items) {
		for (Item item : items)
			if (item.intersects(this)) {
				if (item instanceof Coin) coins++;

				if (item instanceof Health) {
					health++;
					if (health > maxHealth) health = maxHealth;
				}
				if (item instanceof Life) lives++;

				item.setRemove();
			}
	}

	private void getNextPosition() {

		/**
		 * Knockback
		 */
		if (knockback) {
			dy += fallSpeed * 2;
			if (!falling) knockback = false;
			return;
		}

		/**
		 * Movement
		 */
		if (left) {
			dx -= moveSpeed;
			if (dx < -maxSpeed) dx = -maxSpeed;

		} else if (right) {
			dx += moveSpeed;
			if (dx > maxSpeed) dx = maxSpeed;

		} else {
			if (dx > 0) {
				dx -= stopSpeed;
				if (dx < 0) dx = 0;

			} else if (dx < 0) {
				dx += stopSpeed;
				if (dx > 0) dx = 0;
			}
		}

		/**
		 * Cannot move while attacking, except in air
		 */
		if (currentAction == FIRE && !(jumping || falling)) dx = 0;

		/**
		 * Jumping
		 */
		if (jumping && !falling) {
			dy = jumpStart;
			falling = true;
		}

		/**
		 * Falling
		 */
		if (falling) {
			// TODO implement lower gravity
			dy += fallSpeed;
			if (dy > 0) jumping = false;
			if (dy < 0 && !jumping) dy += stopJumpSpeed;
			if (dy > maxFallSpeed) dy = maxFallSpeed;
		}
	}

	public double getHealth() {
		return health;
	}

	public double getMaxHealth() {
		return maxHealth;
	}

	public int getEnergy() {
		return energy;
	}

	public int getMaxEnergy() {
		return maxEnergy;
	}

	public void setFiring() {
		firing = true;
	}

	public int getLives() {
		return lives;
	}

	public int getCoins() {
		return coins;
	}

	public boolean isDead() {
		return dead;
	}

	public void hit(double damage) {
		if (flinching) return;

		health -= damage;
		if (health <= 0) updateLives();

		/**
		 * Flinching
		 */
		flinching = true;
		flinchTimer = System.nanoTime();

		/**
		 * Knockback
		 */
		if (facingRight)
			dx = -5;
		else
			dx = 5;
		dy = -20;
		knockback = true;
		falling = true;
		jumping = false;
	}

	private void updateLives() {
		health = maxHealth;
		setPosition(200, 850); // TODO change 
		lives--;
		if (lives < 0) lives = 0;
		if (lives == 0) dead = true;
	}

	private void handleInput() {
		if (KeyInput.keys[KeyEvent.VK_LEFT])
			left = true;
		else
			left = false;

		if (KeyInput.keys[KeyEvent.VK_RIGHT])
			right = true;
		else
			right = false;

		if (KeyInput.keys[KeyEvent.VK_UP])
			up = true;
		else
			up = false;

		if (KeyInput.keys[KeyEvent.VK_DOWN])
			down = true;
		else
			down = false;

		if (KeyInput.keys[KeyEvent.VK_SPACE]) {
			jumping = true;
			AudioPlayer.playSound(jumpSoundKey);
		} else {
			jumping = false;
		}

		if (KeyInput.keys[KeyEvent.VK_Q]) firing = true;
	}

	public boolean hasGreenKey() {
		return greenKey;
	}

	public void setGreenKey(boolean greenKey) {
		this.greenKey = greenKey;
	}

	public boolean hasBlueKey() {
		return blueKey;
	}

	public void setBlueKey(boolean blueKey) {
		this.blueKey = blueKey;
	}

	public boolean hasYellowKey() {
		return yellowKey;
	}

	public void setYellowKey(boolean yellowKey) {
		this.yellowKey = yellowKey;
	}

	public boolean hasRedKey() {
		return redKey;
	}

	public void setRedKey(boolean redKey) {
		this.redKey = redKey;
	}
}
