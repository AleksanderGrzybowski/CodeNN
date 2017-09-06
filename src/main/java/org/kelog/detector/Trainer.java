package org.kelog.detector;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
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
import java.nio.file.Files;

@Log
@RequiredArgsConstructor
public class Trainer {
    
    private final KeywordsList keywordsList;
    private final Parser parser;
    
    private final int maximumEpochs;
    private final double maximumError;
    private final File trainingDataFile;
    
    BasicNetwork createNetwork() throws IOException {
        String trainingDataDir = unzipTrainingData();
        
        int numberOfFiles = countTrainingFiles(trainingDataDir);
        double[][] inputs = new double[numberOfFiles][keywordsList.size()];
        double[][] outputs = new double[numberOfFiles][Language.values().length];
        
        // each file is mapped to one row of inputs matrix and one row of outputs matrix
        // first: it's histogram
        // second: predicted network response, that means: 1.0 at the correct position and 0.0's elsewhere
        int index = 0;
        for (Language lang : Language.values()) {
            //noinspection ConstantConditions
            for (File source : new File(trainingDataDir + File.separator + lang).listFiles()) {
                log.info("Going through " + source.getName());
                inputs[index] = parser.histogram(source);
                outputs[index][lang.ordinal()] = 1.0; // others are 0-s
                
                index++;
            }
        }
        
        int hiddenLayerSize = (int) Math.sqrt(inputs[0].length * outputs[0].length); // formula from forum
        
        return doTraining(inputs, outputs, hiddenLayerSize, maximumError);
    }
    
    private String unzipTrainingData() throws IOException {
        try {
            ZipFile zipFile = new ZipFile(trainingDataFile);
            File tempDir = Files.createTempDirectory("codenn").toFile();
            
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
    
    /**
     * Most of this is blatantly copied from the official tutorials (encog/encog-java-examples)
     */
    private BasicNetwork doTraining(double[][] inputs, double[][] outputs, int hiddenLayerSize, double maximumError) {
        if (inputs.length != outputs.length) {
            throw new AssertionError("Numbers of input/output vectors don't match!");
        }
        log.info("Training network, " + inputs.length + " vector(s)");
        
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
            log.info("Epoch #" + epoch + " Error:" + train.getError());
            
            epoch++;
            if (epoch > maximumEpochs) {
                throw new EpochNumberExceeded();
            }
        } while (train.getError() > maximumError);
        log.info("Epoch #" + epoch + " -> finished.");
        train.finishTraining();
        
        return network;
    }
}
