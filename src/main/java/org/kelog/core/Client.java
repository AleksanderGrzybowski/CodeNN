package org.kelog.core;

import java.util.EnumMap;

public class Client {

	private EncogAdapter adapter;
	private Parser parser;

	public Client(EncogAdapter adapter, Parser parser) {
		this.parser = parser;
		this.adapter = adapter;
	}

	private static EnumMap<Language, Double> arrayToMap(double[] networkResponse) {
		EnumMap<Language, Double> map = new EnumMap<>(Language.class);

		for (Language lang : Language.values()) {
			map.put(lang, networkResponse[lang.ordinal()]);
		}

		return map;
	}

	public EnumMap<Language, Double> match(String snippet) {
		double[] question = parser.histogram(snippet);
		double[] answer = adapter.ask(question);

		return arrayToMap(answer);
	}
}
