package com.kdi.aliens.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.kdi.aliens.state.OptionsState;

public class Storage {

	private static final String OPTIONS = "./options.kdi";

	public static String[] getOptions() {
		String[] options = new String[OptionsState.OPTIONS.length - 2];

		try {
			BufferedReader file = new BufferedReader(new FileReader(OPTIONS));

			for (int i = 0; i < OptionsState.OPTIONS.length - 2; i++) {
				String line = file.readLine();
				String[] value = line.split(":");
				options[i] = value[1];
			}

			file.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return options;
	}

	public static void saveOptions(String[] options) {
		try {
			FileWriter file = new FileWriter(OPTIONS);
			for (int i = 0; i < options.length; i++) {
				file.write(OptionsState.OPTIONS[i] + ":" + options[i]);
				file.write(System.getProperty("line.separator"));
			}

			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
