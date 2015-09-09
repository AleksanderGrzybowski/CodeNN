package org.kelog.core;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.persist.EncogDirectoryPersistence;
import org.kelog.exceptions.EpochNumberExceeded;

import java.io.File;
import java.util.Scanner;
import java.util.logging.Logger;

public class Trainer {

    private static Logger logger = Logger.getLogger(Trainer.class.getName());
    private Parser parser;
    private Words words;
    private int maximumEpochs;
    private double maximumError;

    @Inject
    public Trainer(Words words, Parser parser, @Named("maximum-epochs") int maximumEpochs,
                   @Named("maximum-error") double maximumError) {
        this.words = words;
        this.parser = parser;
        this.maximumEpochs = maximumEpochs;
        this.maximumError = maximumError;
    }

    public void createNetwork(String directory, String filename) {
        int numberOfFiles = countTrainingFiles(directory);
        double[][] inputs = new double[numberOfFiles][words.list.size()];
        double[][] outputs = new double[numberOfFiles][Language.values().length];

        logger.info("Creating network and saving to " + filename);

        // each file is mapped to one row of inputs matrix and one row of outputs matrix
        // first: it's histogram
        // second: predicted network response, that means: 1.0 at the correct position and 0.0's elsewhere
        int index = 0;
        for (Language lang : Language.values()) {
            //noinspection ConstantConditions
            for (File source : new File(directory + "/" + lang).listFiles()) {
                inputs[index] = parser.histogram(source);
                outputs[index][lang.ordinal()] = 1.0; // others are 0-s

                index++;
            }
        }

        int hiddenLayerSize = (int) Math.sqrt(inputs[0].length * outputs[0].length); // formula from forum
        BasicNetwork network = doTraining(inputs, outputs, hiddenLayerSize, maximumError);

        logger.info("Saving network to file " + filename);
        EncogDirectoryPersistence.saveObject(new File(filename), network);
    }

    private static int countTrainingFiles(String directory) {
        int count = 0;
        for (Language lang : Language.values()) {
            //noinspection ConstantConditions
            count += new File(directory + "/" + lang).listFiles().length;
        }
        return count;
    }

    public BasicNetwork doTraining(double[][] inputs, double[][] outputs, int hiddenLayerSize, double maximumError) {
//        EncogAdapter adapter = new EncogAdapter();
        if (inputs.length != outputs.length) {
            throw new AssertionError("Numbers of input/output vectors don't match!");
        }
        logger.info("Training network, " + inputs.length + " vector(s)");

        MLDataSet trainingSet = new BasicMLDataSet(inputs, outputs);

        BasicNetwork network = new BasicNetwork();
        network.addLayer(new BasicLayer(null, true, inputs[0].length));
        network.addLayer(new BasicLayer(new ActivationSigmoid(), true, hiddenLayerSize));
        network.addLayer(new BasicLayer(new ActivationSigmoid(), false, outputs[0].length));
        network.getStructure().finalizeStructure();
        network.reset();

        final ResilientPropagation train = new ResilientPropagation(network, trainingSet);

        int epoch = 1;
        do {
            train.iteration();
            logger.info("Epoch #" + epoch + " Error:" + train.getError());

            epoch++;
            if (epoch > maximumEpochs) {
                throw new EpochNumberExceeded();
            }
        } while (train.getError() > maximumError);
        logger.info("Epoch #" + epoch + " -> finished.");
        train.finishTraining();

        return network;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Give path with training data: ");
        String trainingDir = scanner.nextLine();

        System.out.print("Give path for network blob: ");
        String networkFilename = scanner.nextLine();

        Injector injector = Guice.createInjector(new MainModule());
        Trainer trainer = injector.getInstance(Trainer.class);

        trainer.createNetwork(trainingDir, networkFilename);
    }
}
