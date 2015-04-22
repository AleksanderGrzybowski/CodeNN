package org.kelog.end;

import org.kelog.core.EncogAdapter;
import org.kelog.core.Language;
import org.kelog.core.Parser;

import java.io.File;
import java.util.EnumMap;
import java.util.logging.Logger;

public class Client {

	EncogAdapter adapter = new EncogAdapter();

	private static Logger logger = Logger.getLogger(Client.class.getName());

	public Client() {
		logger.info("Starting client");

		if (!(new File(Config.NETWORK_FILENAME).exists())) {
			logger.warning("File with network does not exist!");
			throw new RuntimeException();
		}

		adapter.restoreFromFile(Config.NETWORK_FILENAME);
	}

	private static EnumMap<Language, Double> arrayToMap(double[] resp) {
		EnumMap<Language, Double> map = new EnumMap<>(Language.class);
		for (Language lang : Language.values()) {
			map.put(lang, resp[lang.ordinal()]);
		}
		return map;
	}

	public EnumMap<Language, Double> match(String snippet) {
		double[] question = Parser.histogram(snippet);
		double[] answer = adapter.ask(question);
		return arrayToMap(answer);
	}
}
