package org.kelog.core;

import com.google.common.io.Files;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.kelog.exceptions.EpochNumberExceeded;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
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

    public BasicNetwork createNetwork(String zipFilename) throws IOException {
        String trdataDir = unzipTrainingData(zipFilename);

        int numberOfFiles = countTrainingFiles(trdataDir);
        double[][] inputs = new double[numberOfFiles][words.list.size()];
        double[][] outputs = new double[numberOfFiles][Language.values().length];


        // each file is mapped to one row of inputs matrix and one row of outputs matrix
        // first: it's histogram
        // second: predicted network response, that means: 1.0 at the correct position and 0.0's elsewhere
        int index = 0;
        for (Language lang : Language.values()) {
            //noinspection ConstantConditions
            for (File source : new File(trdataDir + File.separator + lang).listFiles()) {
                logger.log(Level.INFO, "Going through " + source.getName());
                inputs[index] = parser.histogram(source);
                outputs[index][lang.ordinal()] = 1.0; // others are 0-s

                index++;
            }
        }

        int hiddenLayerSize = (int) Math.sqrt(inputs[0].length * outputs[0].length); // formula from forum

        return doTraining(inputs, outputs, hiddenLayerSize, maximumError);
    }

    private String unzipTrainingData(String zipFilename) throws IOException {
        try {
            ZipFile zipFile = new ZipFile(zipFilename);
            File tempDir = Files.createTempDir();

            String destinationPath = tempDir.getAbsolutePath();
            zipFile.extractAll(destinationPath);
            return destinationPath;
        } catch (ZipException e) {
            throw new IOException(e); // not so important
        }
    }

    private static int countTrainingFiles(String directory) {
        int count = 0;
        for (Language lang : Language.values()) {
            //noinspection ConstantConditions
            count += new File(directory + File.separator + lang).listFiles().length;
        }
        return count;
    }

    public BasicNetwork doTraining(double[][] inputs, double[][] outputs, int hiddenLayerSize, double maximumError) {
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
}
