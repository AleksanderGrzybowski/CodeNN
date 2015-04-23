package org.kelog.core;

import org.kelog.exceptions.EpochNumberExceeded;

import java.io.File;
import java.util.logging.Logger;

public class Trainer {

	private static Logger logger = Logger.getLogger(Trainer.class.getName());

	public static void createNetwork(String dirname, String filename) throws EpochNumberExceeded {
		int filesCount = countTrainingFiles(dirname);
		double[][] inputs = new double[filesCount][Words.words.size()];
		double[][] outputs = new double[filesCount][Language.values().length];

		logger.info("Creating network and saving to " + filename);

		int cur = 0;
		for (Language lang : Language.values()) {
			//noinspection ConstantConditions
			for (File source : new File(dirname + "/" + lang).listFiles()) {
				inputs[cur] = Parser.histogram(source);
				outputs[cur][lang.ordinal()] = 1.0; // others are 0-s

				cur++;
			}
		}

		EncogAdapter ea = new EncogAdapter();
		int hiddenLayerSize = (int) Math.sqrt(inputs[0].length * outputs[0].length); // formula from forum
		ea.train(inputs, outputs, hiddenLayerSize, 0.00001);
		ea.saveToFile(filename);
	}

	private static int countTrainingFiles(String dirname) {
		int count = 0;
		for (Language lang : Language.values()) {
			//noinspection ConstantConditions
			count += new File(dirname + "/" + lang).listFiles().length;
		}
		return count;
	}
}
