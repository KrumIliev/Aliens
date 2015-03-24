package com.kdi.aliens.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

import com.kdi.aliens.GamePanel;

public class ContentManager {

	private static HashMap<String, BufferedImage> images;

	public static void loadImages() {
		images = new HashMap<String, BufferedImage>();

		addImage(Reference.CM_BACKGROUND_MENU_1, Reference.RESOURCE_BACKGROUNDS + "menu_background.png", GamePanel.WIDTH, GamePanel.HEIGHT);
		addImage(Reference.CM_BACKGROUND_MENU_2, Reference.RESOURCE_BACKGROUNDS + "menu_background_1.png", GamePanel.WIDTH, GamePanel.HEIGHT);
		addImage(Reference.CM_BACKGROUND_LEVEL_1, Reference.RESOURCE_BACKGROUNDS + "level1_background.png", GamePanel.WIDTH, GamePanel.HEIGHT);

		addImage(Reference.CM_TILES_LEVEL_1, Reference.RESOURCE_TILES + "grass.png");

		addImage(Reference.CM_HUD, Reference.RESOURCE_HUD + "hud.png");
		addImage(Reference.CM_ITEMS, Reference.RESOURCE_ITEMS + "items.png");
		addImage(Reference.CM_PLAYER, Reference.RESOURCE_PLAYER + "pink.png");
	}

	private static void addImage(String key, String path) {
		try {
			BufferedImage image = ImageIO.read(ContentManager.class.getResourceAsStream(path));
			images.put(key, image);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void addImage(String key, String path, int width, int height) {
		try {
			BufferedImage original = ImageIO.read(ContentManager.class.getResourceAsStream(path));
			BufferedImage resized = resizeImage(original, width, height);
			images.put(key, resized);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void loadAudio() {
		AudioPlayer.init();

		AudioPlayer.loadMusic(Reference.RESOURCE_MUSIC + "menu.mp3", Reference.MUSIC_MENU);
		AudioPlayer.loadMusic(Reference.RESOURCE_MUSIC + "level1.mp3", Reference.MUSIC_LEVEL1);
	}

	public static void loadFonts() {
		Fonts.addFont(new Fonts(Reference.RESOURCE_FONTS + "ComicNoteSmooth.ttf"));
	}

	public static BufferedImage getImage(String key) {
		return images.get(key);
	}

	public static BufferedImage resizeImage(BufferedImage original, int width, int height) {
		return Scalr.resize(original, Scalr.Method.BALANCED, width, height, Scalr.OP_ANTIALIAS);
	}

}
