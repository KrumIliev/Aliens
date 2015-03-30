package com.kdi.aliens.state.levels;

import java.util.ArrayList;

import com.kdi.aliens.effects.Effect;
import com.kdi.aliens.entities.enemies.Enemy;
import com.kdi.aliens.items.Item;

public class LevelObjects {

	public static ArrayList<Item> items = new ArrayList<Item>();
	public static ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	public static ArrayList<Effect> effects = new ArrayList<Effect>();

	public static void clear() {
		items.clear();
		enemies.clear();
		effects.clear();
	}
}
