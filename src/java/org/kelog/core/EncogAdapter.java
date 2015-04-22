package org.kelog.core;

import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.persist.EncogDirectoryPersistence;

import java.io.File;
import java.util.logging.Logger;


public class EncogAdapter {

	private BasicNetwork network;

	private static Logger logger = Logger.getLogger(EncogAdapter.class.getName());

	public EncogAdapter() {
	}

	public void train(double[][] inputs, double[][] outputs, int hiddenLayerSize, double maximumError) {
		if (inputs.length != outputs.length) {
			throw new AssertionError("Numbers of input vectors don't match!");
		}
		logger.info("Training network, " + inputs.length + " vector(s)");

		MLDataSet trainingSet = new BasicMLDataSet(inputs, outputs);


		network = new BasicNetwork();
		network.addLayer(new BasicLayer(null, true, inputs[0].length));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true, hiddenLayerSize));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), false, outputs[0].length));
		network.getStructure().finalizeStructure();
		network.reset();

		final ResilientPropagation train = new ResilientPropagation(network, trainingSet);

		int epoch = 1;
		do {
			train.iteration();
			if (epoch % 10 == 0) {
				logger.info("Epoch #" + epoch + " Error:" + train.getError());
			}
			epoch++;
		} while (train.getError() > maximumError);
		logger.info("Epoch #" + epoch + " -> finished.");
		train.finishTraining();

		Encog.getInstance().shutdown(); // is this needed
	}

	public double[] ask(double[] input) {
		return network.compute(new BasicMLData(input)).getData();
	}

	public void saveToFile(String fname) {
		logger.info("Saving network to file " + fname);
		EncogDirectoryPersistence.saveObject(new File(fname), network);
	}

	public void restoreFromFile(String fname) {
		logger.info("Restoring network from " + fname);
		network = (BasicNetwork) EncogDirectoryPersistence.loadObject(new File(fname));
	}
}
