package org.kelog.core;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.logging.Logger;

public class Parser {
	private static Logger logger = Logger.getLogger(Parser.class.getName());

	public static double[] histogram(String snippet) {
		double[] histogram = parse(snippet);

		histogram = Utils.normalizedHistogram(histogram);
		return histogram;
	}

	public static double[] histogram(File file) {
		String snippet = readFromFile(file);
		return histogram(snippet);
	}

	private static String readFromFile(File file) {
		try {
			logger.info("Reading file " + file.getAbsolutePath());
			String content = "";

			for (String line : Files.readAllLines(file.toPath(), Charset.defaultCharset())) {
				content += line + "\n";
			}
			return content;
		} catch (IOException e) {
			logger.warning("Failed to read from file " + file.getAbsolutePath() + " " + e);
			throw new RuntimeException(e);
		}
	}

	private static double[] parse(String snippet) {
		int index = 0;
		int length = snippet.length();
		double[] histogram = new double[Words.words.size()];

		try {
			outer:
			while (index < length) {
				for (String guess : Words.words) {
					if (snippet.startsWith(guess, index)) {
						index += guess.length();
						histogram[Words.words.indexOf(guess)] += 1;
						continue outer;
					}
				}
				index++;
			}
		} catch (IndexOutOfBoundsException ignored) {
		}

		return histogram;
	}
}
