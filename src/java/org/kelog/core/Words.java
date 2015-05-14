package org.kelog.core;

import org.kelog.end.Config;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Logger;

public class Words {
	public static List<String> words;

	private static Logger logger = Logger.getLogger(Words.class.getName());

	static {
		logger.info("Loading list of words");
		try {
			words = Files.readAllLines(new File(Config.WORDS_FILENAME).toPath(), Charset.defaultCharset());
			logger.info("List of words loaded, count: " + words.size());
		} catch (IOException e) {
			logger.warning("Error creating words list " + e);
			throw new RuntimeException();
		}
	}
}
