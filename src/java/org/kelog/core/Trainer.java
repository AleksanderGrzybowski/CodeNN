package org.kelog.core;

import org.kelog.end.Config;

import java.io.File;
import java.util.logging.Logger;

public class Trainer {

    private static Logger logger = Logger.getLogger(Trainer.class.getName());

    public static void createNetwork(String directory, String filename) {
        int numberOfFiles = countTrainingFiles(directory);
        double[][] inputs = new double[numberOfFiles][Words.list.size()];
        double[][] outputs = new double[numberOfFiles][Language.values().length];

        logger.info("Creating network and saving to " + filename);

        // each file is mapped to one row of inputs matrix and one row of outputs matrix
        // first: it's histogram
        // second: predicted network response, that means: 1.0 at the correct position and 0.0's elsewhere
        int index = 0;
        for (Language lang : Language.values()) {
            //noinspection ConstantConditions
            for (File source : new File(directory + "/" + lang).listFiles()) {
                inputs[index] = Parser.histogram(source);
                outputs[index][lang.ordinal()] = 1.0; // others are 0-s

                index++;
            }
        }

        int hiddenLayerSize = (int) Math.sqrt(inputs[0].length * outputs[0].length); // formula from forum
        EncogAdapter adapter = EncogAdapter.fromTrainingData(inputs, outputs, hiddenLayerSize, Config.MAXIMUM_ERROR);
        adapter.saveToFile(filename);
    }

    private static int countTrainingFiles(String directory) {
        int count = 0;
        for (Language lang : Language.values()) {
            //noinspection ConstantConditions
            count += new File(directory + "/" + lang).listFiles().length;
        }
        return count;
    }
}
