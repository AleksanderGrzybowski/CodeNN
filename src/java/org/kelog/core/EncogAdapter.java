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
import org.kelog.end.Config;
import org.kelog.exceptions.EpochNumberExceeded;

import java.io.File;
import java.util.logging.Logger;


public class EncogAdapter {

	private BasicNetwork network;

	private static Logger logger = Logger.getLogger(EncogAdapter.class.getName());

	private EncogAdapter() {
	}

	public static EncogAdapter fromTrainingData(double[][] inputs, double[][] outputs, int hiddenLayerSize, double maximumError) {
		EncogAdapter adapter = new EncogAdapter();
		if (inputs.length != outputs.length) {
			throw new AssertionError("Numbers of input/output vectors don't match!");
		}
		logger.info("Training network, " + inputs.length + " vector(s)");

		MLDataSet trainingSet = new BasicMLDataSet(inputs, outputs);

		adapter.network = new BasicNetwork();
		adapter.network.addLayer(new BasicLayer(null, true, inputs[0].length));
		adapter.network.addLayer(new BasicLayer(new ActivationSigmoid(), true, hiddenLayerSize));
		adapter.network.addLayer(new BasicLayer(new ActivationSigmoid(), false, outputs[0].length));
		adapter.network.getStructure().finalizeStructure();
		adapter.network.reset();

		final ResilientPropagation train = new ResilientPropagation(adapter.network, trainingSet);

		int epoch = 1;
		do {
			train.iteration();
			logger.info("Epoch #" + epoch + " Error:" + train.getError());

			epoch++;
			if (epoch > Config.MAXIMUM_EPOCHS) {
				throw new EpochNumberExceeded();
			}
		} while (train.getError() > maximumError);
		logger.info("Epoch #" + epoch + " -> finished.");
		train.finishTraining();

		Encog.getInstance().shutdown(); // is this needed

		return adapter;
	}

	public double[] ask(double[] input) {
		return network.compute(new BasicMLData(input)).getData();
	}

	public void saveToFile(String filename) {
		logger.info("Saving network to file " + filename);
		EncogDirectoryPersistence.saveObject(new File(filename), network);
	}

	public static EncogAdapter fromFile(String filename) {
		logger.info("Restoring network from " + filename);
		EncogAdapter adapter = new EncogAdapter();
		adapter.network = (BasicNetwork) EncogDirectoryPersistence.loadObject(new File(filename));
		return adapter;
	}
}
