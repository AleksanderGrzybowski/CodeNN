package org.kelog.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Logger;

import static org.kelog.core.CommentRemover.removeComments;

public class Parser {
	private static Logger logger = Logger.getLogger(Parser.class.getName());

	public static double[] histogram(String snippet) {
		double[] histogram = parse(snippet);

		histogram = Utils.normalizedHistogram(histogram);
		return histogram;
	}

	public static double[] histogram(File file) {
		String snippet = readFromFile(file);
        snippet = removeComments(snippet);
        return histogram(snippet);
	}

	private static String readFromFile(File file) {
		try {
			logger.info("Reading file " + file.getAbsolutePath());
			return new String(Files.readAllBytes(file.toPath()));
		} catch (IOException e) {
			logger.warning("Failed to read from file " + file.getAbsolutePath() + " " + e);
			throw new InternalError();
		}
	}

	private static double[] parse(String snippet) {
		int index = 0;
		int length = snippet.length();
		double[] histogram = new double[Words.list.size()];

		try {
			outer:
			while (index < length) {
				for (String guess : Words.list) {
					if (snippet.startsWith(guess, index)) {
						index += guess.length();
						histogram[Words.list.indexOf(guess)] += 1;
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
