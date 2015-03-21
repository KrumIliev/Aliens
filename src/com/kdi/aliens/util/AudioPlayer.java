package com.kdi.aliens.util;

import java.util.HashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AudioPlayer {

	private static HashMap<String, Clip> music;
	private static HashMap<String, Clip> sound;
	private static int gap;
	private static boolean muteMusic = false;
	private static boolean muteSound = false;

	public static void init() {
		music = new HashMap<String, Clip>();
		sound = new HashMap<String, Clip>();
		gap = 0;
		
		loadMusic(Reference.RESOURCE_MUSIC + "menu.mp3", Reference.MUSIC_MENU);
		loadMusic(Reference.RESOURCE_MUSIC + "level1.mp3", Reference.MUSIC_LEVEL1);
	}

	public static void loadSound(String path, String key) {
		if (sound.get(key) != null) return;
		sound.put(key, decodeClip(path));
	}

	public static void loadMusic(String path, String key) {
		if (music.get(key) != null) return;
		music.put(key, decodeClip(path));
	}

	private static Clip decodeClip(String path) {
		Clip clip = null;

		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(AudioPlayer.class.getResourceAsStream(path));
			AudioFormat baseFormat = ais.getFormat();
			AudioFormat decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
					baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
			AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais);
			clip = AudioSystem.getClip();
			clip.open(dais);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return clip;
	}

	public static void playSound(String key) {
		playSound(key, gap);
	}

	public static void playSound(String key, int gap) {
		if (muteSound) return;
		Clip clip = sound.get(key);
		play(clip, gap);
	}

	public static void playMusic(String key) {
		if (muteMusic) return;
		Clip clip = music.get(key);
		play(clip, gap);
	}

	private static void play(Clip clip, int gap) {
		if (clip == null) return;
		if (clip.isRunning()) clip.stop();
		clip.setFramePosition(gap);
		while (!clip.isRunning())
			clip.start();
	}
	
	public static void stopMusic(String key) {
		if (music.get(key) == null) return;
		if (music.get(key).isRunning()) music.get(key).stop();
	}
	
	public static void stopSound(String key) {
		if (sound.get(key) == null) return;
		if (sound.get(key).isRunning()) sound.get(key).stop();
	}
	
	public static void resumeMusic(String key) {
		if (muteMusic) return;
		if (music.get(key).isRunning()) return;
		music.get(key).start();
	}
	
	public static void resumeSound(String key) {
		if (muteSound) return;
		if (sound.get(key).isRunning()) return;
		sound.get(key).start();
	}
	
	public static void loop(String key) {
		stopMusic(key);
		if (muteMusic) return;
		music.get(key).loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public static int getMusicFrames(String key) {
		return music.get(key).getFrameLength();
	}
}
