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

		Utils.normalizeHistogram(histogram);
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
		StringBuilder sb = new StringBuilder(snippet);
		double[] histogram = new double[Words.words.size()];

		outer:
		while (sb.length() != 0) {
			for (String guess : Words.words) {
				if (sb.toString().startsWith(guess)) {
					sb.delete(0, guess.length());
					histogram[Words.words.indexOf(guess)] += 1;
					continue outer;
				}
			}
			sb.deleteCharAt(0);
		}
		return histogram;
	}
}
