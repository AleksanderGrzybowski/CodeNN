package org.kelog.core;

import org.kelog.end.Config;
import org.kelog.exceptions.WordsListNotFoundException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

import static java.util.stream.Collectors.toList;

public class Words {
	public static List<String> list;

	private static Logger logger = Logger.getLogger(Words.class.getName());

	static {
		logger.info("Loading list of words");
		try {
			list = Files.readAllLines(Paths.get(Config.WORDS_FILENAME))
					.stream()
					.filter(word -> !word.startsWith("#"))
					.collect(toList());
			logger.info("List of words loaded, count: " + list.size());
		} catch (IOException e) {
			logger.warning("Error creating words list " + e);
			throw new WordsListNotFoundException();
		}
	}

}
