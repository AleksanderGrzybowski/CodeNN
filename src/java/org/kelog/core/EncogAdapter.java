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
		EncogAdapter ea = new EncogAdapter();
		if (inputs.length != outputs.length) {
			throw new AssertionError("Numbers of input/output vectors don't match!");
		}
		logger.info("Training network, " + inputs.length + " vector(s)");

		MLDataSet trainingSet = new BasicMLDataSet(inputs, outputs);

		ea.network = new BasicNetwork();
		ea.network.addLayer(new BasicLayer(null, true, inputs[0].length));
		ea.network.addLayer(new BasicLayer(new ActivationSigmoid(), true, hiddenLayerSize));
		ea.network.addLayer(new BasicLayer(new ActivationSigmoid(), false, outputs[0].length));
		ea.network.getStructure().finalizeStructure();
		ea.network.reset();

		final ResilientPropagation train = new ResilientPropagation(ea.network, trainingSet);

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

		return ea;
	}

	public double[] ask(double[] input) {
		return network.compute(new BasicMLData(input)).getData();
	}

	public void saveToFile(String fname) {
		logger.info("Saving network to file " + fname);
		EncogDirectoryPersistence.saveObject(new File(fname), network);
	}

	public static EncogAdapter fromFile(String fname) {
		logger.info("Restoring network from " + fname);
		EncogAdapter ea = new EncogAdapter();
		ea.network = (BasicNetwork) EncogDirectoryPersistence.loadObject(new File(fname));
		return ea;
	}
}
