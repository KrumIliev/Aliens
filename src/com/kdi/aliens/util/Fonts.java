package com.kdi.aliens.util;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Fonts {

	private static ArrayList<Fonts> fontList = new ArrayList<Fonts>();
	private String fontPath;

	public Fonts(String fontPath) {
		this.fontPath = fontPath;
		registerFont();
	}

	private void registerFont() {
		try {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(fontPath)));
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}
	}

	public static void addFont(Fonts font) {
		fontList.add(font);
	}
}
