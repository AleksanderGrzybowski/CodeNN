package org.kelog.core;

import org.kelog.end.Config;
import org.kelog.exceptions.WordsListNotFoundException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Words {
	public static List<String> list;

	private static Logger logger = Logger.getLogger(Words.class.getName());

	static {
		logger.info("Loading list of words");
		try {
			list = Files.readAllLines(new File(Config.WORDS_FILENAME).toPath(), Charset.defaultCharset());
            list = stripComments(list); // no streams here...
			logger.info("List of words loaded, count: " + list.size());
		} catch (IOException e) {
			logger.warning("Error creating words list " + e);
			throw new WordsListNotFoundException();
		}
	}

    private static List<String> stripComments(List<String> list) {
        ArrayList<String> result = new ArrayList<>();
        for (String word : list) {
            if (!word.startsWith("#")) {
                result.add(word);
            }
        }
        return result;
    }
}
