package org.kelog.core;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Logger;

public class Words {
	public static List<String> words;
	private static Logger logger = Logger.getLogger(Words.class.getName());

	private static final String WORDS_FILE = "/home/kelog/Kodzenie/EncogAdapter/trdata/words.txt";
	static {
		logger.info("Loading list of words");
		try {
			words = Files.readAllLines(new File(WORDS_FILE).toPath(), Charset.defaultCharset());
			logger.info("List of words loaded, count: " + words.size());
		} catch (IOException e) {
			logger.warning("Error creating words list " + e);
		}
	}
}
